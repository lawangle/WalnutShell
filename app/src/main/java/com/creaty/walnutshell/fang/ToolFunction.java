package com.creaty.walnutshell.fang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.dom4j.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ToolFunction {
	public static PageDetailAndHtml GetPageDetailWithoutSource(String url) throws ClientProtocolException, IOException{
	
		PageDetailAndHtml returnValue = new PageDetailAndHtml();
		HttpClient httpClient = new DefaultHttpClient();
		//请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
		//读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");//对发的包的头进行设定，以便可以顺利获取正确的源码
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String lastMod = "";
		if( response.getLastHeader("Last-Modified") != null)
			lastMod = response.getLastHeader("Last-Modified").toString();
		if(lastMod == null || lastMod.length() == 0){
			Date date = new Date();
			returnValue.detail.lastModifiedTime = date.getTime();
		}
		else{/////先暂且这样
			Date date = new Date();
			returnValue.detail.lastModifiedTime = date.getTime();
		}
		///获取HTML源码
		if(entity != null){
			entity = new BufferedHttpEntity(entity);
			InputStream input = entity.getContent();
			String charset = "";
			if(url.indexOf("http://ssdut.dlut.edu.cn/") != -1)
				charset = "utf-8";
			else
				charset = GetCharset(entity.getContentType().toString(),entity,input);
			BufferedReader br = new BufferedReader(new InputStreamReader(input,charset));
			String s;
			while((s = br.readLine()) != null){
				//s = s.toLowerCase();			
				returnValue.html += "\n" + s.trim();//在循环中获取所有的源码
			}
			input.close();
			br.close();
		}
		returnValue.detail.description = "";
		//获取标题
		org.jsoup.nodes.Document doc = Jsoup.parse(returnValue.html);
		org.jsoup.select.Elements titleList = doc.select("title");
		for(Element titleEle : titleList){
			returnValue.detail.pageName = titleEle.text();
		}
		///获取LOGO
		InputStream input = entity.getContent();
		byte[] buffer = new byte[30720];
		int byteNum = 0;
		while(true){
			byteNum = input.read(buffer);
			if(byteNum != - 1 && byteNum < 30720){
				byte[] last = new byte[byteNum];
				for(int i = 0;i < byteNum;i++){
					last[i] = buffer[i];
				}				
				returnValue.detail.logo = last;
			}
			else
				break;
		}
		input.close();		
		return returnValue;		
	}
	public static ArrayList<String> ArticleAnalysis(String htmlCode) throws MalformedURLException{
		ArrayList<String> wholeText = new ArrayList<String>();
		String html = HtmlPretreatment(htmlCode);
		if(html.indexOf("div") == -1){
			return new ArrayList<String>();
		}
		org.jsoup.nodes.Document  doc = Jsoup.parse(html);
		int maxNum = 0;
		String text = "";
		Element textEle = null;
		Elements mainEleList = doc.select("div");
		for(Element mainEle : mainEleList){
			int length = mainEle.text().length();
			if(length > maxNum)
			{
				String temp = mainEle.text();
				double totalLength = mainEle.toString().length();
				double textLength = mainEle.text().length();
				if(textLength / totalLength > 0.3)
				{	
					textEle = mainEle;
					text = mainEle.text();
					maxNum = length;
					for(Element pEle : mainEle.select("p")){
						if(pEle.text().length() != 0 && pEle.text().length() != 1)
						{
							wholeText.add(pEle.text());
						}
					}
				}
			}
		}
		if(wholeText.size() == 0 && textEle != null){
			for(Element pEle : textEle.select("div")){
				if(pEle.text().length() != 0 && pEle.text().length() != 1)
					wholeText.add(pEle.text());
			}
		}
		return wholeText;
	}
	private static String HtmlPretreatment(String html){
		ArrayList<String> keyTag = new ArrayList<String>();
		keyTag.add("script");
		keyTag.add("a");
		for(String currentKey : keyTag){
			while(true){
				int indexStart = html.indexOf("<" + currentKey);
				if(indexStart == -1)
					break;
				int indexEnd = html.indexOf("</" + currentKey + ">",indexStart);
				String left = html.substring(0, indexStart);
				String right = html.substring(indexEnd + 3 + currentKey.length());
				html = left + right;
			}
		}
		html = html.replaceAll("table", "div");
		html = html.replaceAll("<span [^>]*>", "<span>");
		html = html.replaceAll("wbr", "p");
		html = html.replaceAll("br", "p");
		html = html.replaceAll("<p [^>]*>", "<p>");
		html = html.replaceAll("<td [^>]*>", "<td>");
		html = html.replaceAll("<tr [^>]*>", "<tr>");
		html = html.replaceAll("&nbsp;","");
		return html;
	}
	
	
	public static String GetCharset(String temp,HttpEntity entity,InputStream input){
		//获取字符集
		int start = temp.indexOf("charset=") + 8;
		try{		
				if(start == 7)
				{
					BufferedReader read = new BufferedReader(new InputStreamReader(input,"utf-8"));
					String s;
					while((s = read.readLine()) != null){
						if(s.indexOf("charset=") != -1){
							int i = s.indexOf("charset=");
							int i2 = s.indexOf('"', i + 8);
							input.reset();
							return s.substring(i + 8, i2);
						}
					}					
				}				
		}catch(Exception e){			
			System.out.print(e);
		}
		return temp.substring(start);
	}
	
	public static String GetHtmlCode(String url) throws MalformedURLException{
		//获取html源码
		String htmlCode = "";
		System.out.println("Get html start");
		URL connect = new URL(url);
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");//对发的包的头进行设定，以便可以顺利获取正确的源码
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				entity = new BufferedHttpEntity(entity);
				InputStream input = entity.getContent();
				String charset = "";
				if(url.indexOf("http://ssdut.dlut.edu.cn/") != -1)
					charset = "utf-8";
				else
					charset = GetCharset(entity.getContentType().toString(),entity,input);
				BufferedReader br = new BufferedReader(new InputStreamReader(input,charset));
				String s;
				while((s = br.readLine()) != null){
					s = s.toLowerCase();			
					htmlCode += "\n" + s.trim();//在循环中获取所有的源码
				}
				input.close();
				br.close();
				
			}
		}catch(Exception e){
			System.out.print(e);
		}	
		System.out.println("Get html code finished!");
		return htmlCode;
	}
	
	
	public static ArrayList<String>  GetXmlUrlFromUrl(String htmlCode) throws MalformedURLException{
		//从网页中找出RSS的连接
		ArrayList<String> xmlUrlList = new ArrayList<String>();
		org.jsoup.nodes.Document doc = Jsoup.parse(htmlCode);
		Elements rssList1 = doc.select("link[type$=application/rss+xml]");
		for(Element rssEle : rssList1){
			String temp = rssEle.attr("href");
			if(!xmlUrlList.contains(temp))
				xmlUrlList.add(rssEle.attr("href"));
		}
		if(xmlUrlList.size() >= 1)
			return xmlUrlList;
		Elements rssList2 = doc.select("a[href$=.xml]");
		for(Element rssEle : rssList2){
			String temp = rssEle.attr("href");
			if(!xmlUrlList.contains(temp))
				xmlUrlList.add(rssEle.attr("href"));
		}	
		if(xmlUrlList.size() >= 1)
			return xmlUrlList;
		Elements rssList3 = doc.select("a[href$=feed]");
		for(Element rssEle : rssList3){
			String temp = rssEle.attr("href");
			if(!xmlUrlList.contains(temp))
				xmlUrlList.add(rssEle.attr("href"));
		}	
		
		return xmlUrlList;
	}
	public static String AssistFunction(String text){
		if(text.equals("下一页"))
			return "";
		if(text.equals("上一页"))
			return "";
		if(text.equals("最后一页") || text.equals("尾页") || text.equals("末页"))
			return "";
		if(text.equals("首页"))
			return "";
		if(text.length() <= 1)
			return "";
		return text;
	}
	

}
