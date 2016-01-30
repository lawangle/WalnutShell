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
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.creaty.walnutshell.basic.StringAndImg;



public class ToolFunction {
	public static PageDetailAndHtml GetPageDetailWithoutSource(String url) throws ClientProtocolException, IOException{
		long htmlStart = 0;
		long htmlEnd = 0;
		PageDetailAndHtml returnValue = new PageDetailAndHtml();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");//�Է��İ���ͷ�����趨���Ա����˳����ȡ��ȷ��Դ��
		System.out.println("!!!!!@@@@@@@@@@@@" + url);
		HttpResponse response = null;
		try{
			response = httpClient.execute(httpGet);
		}catch(Exception e){
			System.out.println(e.toString() + "@@@@");	
		}
		HttpEntity entity = response.getEntity();
		String lastMod = "";
		if( response.getLastHeader("Last-Modified") != null)
			lastMod = response.getLastHeader("Last-Modified").toString();
		if(lastMod == null || lastMod.length() == 0){
			Date date = new Date();
			returnValue.detail.lastModifiedTime = date.getTime();
		}
		else{/////����������
			Date date = new Date();
			returnValue.detail.lastModifiedTime = date.getTime();
		}
		///��ȡHTMLԴ��
		if(entity != null){
			entity = new BufferedHttpEntity(entity);
			InputStream input = entity.getContent();
			String charset = "";
			if(url.indexOf("http://ssdut.dlut.edu.cn/") != -1)
				charset = "utf-8";
			else
				charset = GetCharset(entity.getContentType().toString(),entity,input);
			//BufferedReader br = new BufferedReader(new InputStreamReader(input,charset));
			//String s;
			htmlStart = System.currentTimeMillis();
			System.out.println("$$" + charset);
			returnValue.html = EntityUtils.toString(entity,charset).toLowerCase();
//			while((s = br.readLine()) != null){
//				//s = s.toLowerCase();			
//				returnValue.html += "\n" + s.trim();//��ѭ���л�ȡ���е�Դ��
//			}
			htmlEnd = System.currentTimeMillis();
			System.out.println("!!!!Get html cost " + String.valueOf(htmlEnd - htmlStart));
			input.close();
			//br.close();
		}
		returnValue.detail.description = "";
		//��ȡ����
		org.jsoup.nodes.Document doc = Jsoup.parse(returnValue.html);
		Elements titleList = doc.select("title");
		for(Element titleEle : titleList){
			returnValue.detail.pageName = titleEle.text();
		}
		///��ȡLOGO
		htmlStart = System.currentTimeMillis();
		returnValue.detail.logoAddress = GetPrefix(url) + "favicon.ico";
		System.out.println("@@" + returnValue.detail.logoAddress);
//		InputStream input = entity.getContent();
//		byte[] buffer = new byte[30720];
//		int byteNum = 0;
//		while(true){
//			byteNum = input.read(buffer);
//			if(byteNum != - 1 && byteNum < 30720){
//				byte[] last = new byte[byteNum];
//				for(int i = 0;i < byteNum;i++){
//					last[i] = buffer[i];
//				}				
//				returnValue.detail.logo = last;
//			}
//			else
//				break;
//		}
		htmlEnd = System.currentTimeMillis();
		System.out.println("Get ICON cost : " + String.valueOf(htmlEnd - htmlStart));	
		//input.close();		
		return returnValue;		
	}
//	public static ArrayList<String> ArticleAnalysis(String htmlCode) throws MalformedURLException{
//		ArrayList<String> wholeText = new ArrayList<String>();
//		String html = HtmlPretreatment(htmlCode);
//		//System.out.println("$$$" + html);
//		if(html.indexOf("div") == -1){
//			return new ArrayList<String>();
//		}
//		org.jsoup.nodes.Document  doc = Jsoup.parse(html);
//		int maxNum = 0;
//		String text = "";
//		Element textEle = null;
//		Elements mainEleList = doc.select("div");
//		for(Element mainEle : mainEleList){
//			int length = mainEle.text().length();
//			if(length > maxNum)
//			{
//				String totalString = mainEle.toString();
//				String textString = mainEle.text();
//				String totalStringFilter = HtmlFilter(totalString);
//				double totalLength = totalStringFilter.length();
//				double textLength = textString.length();
//				System.out.println("rate: " + String.valueOf(textLength/totalLength));
//				//System.out.println(totalStringFilter);
//				if(textLength / totalLength > 0.2)
//				{	
//					
//					//System.out.println("Before: " + maxNum);
//					textEle = mainEle;
//					text = mainEle.text();
//					maxNum = length;
//					//System.out.println("After: " + maxNum);
//					for(Element pEle : mainEle.select("p,img")){
//						if(pEle.hasAttr("real_src") && pEle.attr("real_src").length() > 0){
//							
//							System.out.println("@@" + pEle.attr("real_src"));
//							wholeText.add(pEle.attr("real_src"));
//						}
//						else if(pEle.hasAttr("src") && pEle.attr("src").length() > 0){
//							System.out.println("@@" + pEle.attr("src"));
//							wholeText.add(pEle.attr("src"));
//						}
//						else if(pEle.text().length() != 0 && pEle.text().length() != 1)
//						{
//							//System.out.println(pEle.text());
//							wholeText.add(pEle.text());
//						}
//					}
//					
//				}
//			}
//		}
//		if(wholeText.size() == 0 && textEle != null){
//			for(Element pEle : textEle.select("div")){
//				if(pEle.text().length() != 0 && pEle.text().length() != 1)
//					wholeText.add(pEle.text());
//			}
//		}
//		return wholeText;
//	}
	public static ArrayList<StringAndImg> ArticleAnalysis(String htmlCode) throws MalformedURLException{
		ArrayList<StringAndImg> wholeText = new ArrayList<StringAndImg>();
		String html = HtmlPretreatment(htmlCode);
		if(html.indexOf("div") == -1){
			return new ArrayList<StringAndImg>();
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
				String totalString = mainEle.toString();
				String textString = mainEle.text();
				String totalStringFilter = HtmlFilter(totalString);
				double totalLength = totalStringFilter.length();
				double textLength = textString.length();
//				System.out.println(textLength / totalLength);
				if(textLength / totalLength > 0.3)
				{	
					//System.out.println("Before: " + maxNum);
					textEle = mainEle;
					text = mainEle.text();
					maxNum = length;
					//System.out.println("After: " + maxNum);
					for(Element pEle : mainEle.select("p,img")){
						if(pEle.hasAttr("real_src") && pEle.attr("real_src").length() > 0){
							if(pEle.attr("real_src").indexOf(".gif") == -1)
							{
								StringAndImg temp = new StringAndImg(pEle.attr("real_src"),2);
								wholeText.add(temp);
							}
						}
						else if(pEle.hasAttr("src") && pEle.attr("src").length() > 0){
							if(pEle.attr("src").indexOf(".gif") == -1)
							{
								StringAndImg temp = new StringAndImg(pEle.attr("src"),2);
								wholeText.add(temp);
							}
						}
						else if(pEle.text().length() != 0 && pEle.text().length() != 1)
						{
							StringAndImg temp = new StringAndImg(pEle.text(),1);
							wholeText.add(temp);
						}
					}
					
				}
			}
		}
		if(wholeText.size() == 0 && textEle != null){
			for(Element pEle : textEle.select("div")){
				if(pEle.text().length() != 0 && pEle.text().length() != 1)
				{
					StringAndImg temp = new StringAndImg(pEle.text(),1);
					wholeText.add(temp);
				}
			}
		}
		return wholeText;
	}

	private static String HtmlPretreatment(String html){
		ArrayList<String> keyTag = new ArrayList<String>();
		System.out.println("html: " + html.length());
		keyTag.add("script");
		keyTag.add("textarea");
		for(String currentKey : keyTag){
			int indexStart = html.indexOf("<" + currentKey);
			int testIndex = html.indexOf("</" + currentKey + ">");
			if(indexStart == -1 || testIndex == -1)
				continue;
			while(true){
				int indexEnd = html.indexOf("</" + currentKey + ">",indexStart);
				String left = html.substring(0, indexStart);
				String right = html.substring(indexEnd + 3 + currentKey.length());
				//System.out.println("!!!!!!!" + "left: " + left.length() + "right: " + right.length() + "html: " + html.length());
				html = left + right;
				indexStart = html.indexOf("<" + currentKey, indexEnd);
				if(indexStart == -1)
					break;
			}
		}
		org.jsoup.nodes.Document doc = Jsoup.parse(html);
		Elements liNode = doc.select("li");
		for(Element link : liNode){
			link.text("");
		}
		Elements nodes = doc.select("p a");
		for (Element link : nodes) {
			  link.attr("sign", "!");
		}
		Elements links = doc.select("a");
		for (Element link : links) {
			if(!link.hasAttr("sign"))
			  link.text("");
		}
		html = doc.toString();
//		int aIndex = html.indexOf("<a");
//		while(aIndex != -1){
//			int begin = aIndex;
//			int end = html.indexOf('>', begin);
//			while(end != -1 && html.charAt(end + 1) == '<'){
//				end = html.indexOf('>', end + 1);
//			}
//			begin = end + 1;
//			end = html.indexOf("<", begin);
//			String left = html.substring(0, begin);
//			String right = html.substring(end);
//			html = left + right;
//			aIndex = html.indexOf("<a", end);
//		}
		//
		html = html.replaceAll("<a [^>]*>", "<a>");
		html = html.replaceAll("table", "div");
		html = html.replaceAll("<wbr>", "");
		html = html.replaceAll("<br/>", "</p><p>");
		html = html.replaceAll("<br />", "</p><p>");
		html = html.replaceAll("<br>", "");
		html = html.replaceAll("&nbsp;"," ");
		html = html.replaceAll("<iframe [^>]*>", "<iframe>");
		html = html.replaceAll("<span [^>]*>", "<p>");
		html = html.replaceAll("<span>", "<p>");
		html = html.replaceAll("</span>", "</p>");
		return html;
		//return HtmlFilter(html);
	}
	private static String HtmlFilter(String html){
		//html = html.replaceAll("<span [^>]*>", "<span>");
		html = html.replaceAll("<img [^>]*>", "<img>");
		html = html.replaceAll("<font [^>]*>", "");
		html = html.replaceAll("<p [^>]*>", "<p>");
		html = html.replaceAll("<td [^>]*>", "<td>");
		html = html.replaceAll("<tr [^>]*>", "<tr>");
		html = html.replaceAll("<embed [^>]*>", "<embed>");
		html = html.replaceAll("</font>", "");
		return html;
	}
	
	
	public static String GetCharset(String temp,HttpEntity entity,InputStream input){
		//��ȡ�ַ���
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
		//��ȡhtmlԴ��
		String htmlCode = "";
		System.out.println("Get html start");
		URL connect = new URL(url);
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");//�Է��İ���ͷ�����趨���Ա����˳����ȡ��ȷ��Դ��
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
				//BufferedReader br = new BufferedReader(new InputStreamReader(input,charset));
				String s;
				htmlCode = EntityUtils.toString(entity,charset).toLowerCase();
//				while((s = br.readLine()) != null){
//					s = s.toLowerCase();			
//					htmlCode += "\n" + s.trim();//��ѭ���л�ȡ���е�Դ��
//				}
				input.close();
				//br.close();
				
			}
		}catch(Exception e){
			System.out.print(e);
		}	
		System.out.println("Get html code finished!");
		return htmlCode;
	}
	
	
	public static ArrayList<String> GetXmlUrlFromUrl(String htmlCode,String url) throws MalformedURLException{
		//����ҳ���ҳ�RSS������
		ArrayList<String> xmlUrlList = new ArrayList<String>();
		org.jsoup.nodes.Document doc = Jsoup.parse(htmlCode);
		Elements rssList1 = doc.select("link[type$=application/rss+xml]");
		for(Element rssEle : rssList1){
			String temp = rssEle.attr("href");
			if(!xmlUrlList.contains(temp))
				//xmlUrlList.add(rssEle.attr("href"));
				xmlUrlList.add(HrefCheck(rssEle.attr("href"),url));
		}
//		if(xmlUrlList.size() >= 1)
//			return xmlUrlList;
		Elements rssList2 = doc.select("a[href$=.xml]");
		for(Element rssEle : rssList2){
			String temp = rssEle.attr("href");
			if(!xmlUrlList.contains(temp))
				//xmlUrlList.add(rssEle.attr("href"));
				xmlUrlList.add(HrefCheck(rssEle.attr("href"),url));
		}	
		if(xmlUrlList.size() >= 1)
			return xmlUrlList;
		Elements rssList3 = doc.select("a[href$=feed]");
		for(Element rssEle : rssList3){
			String temp = rssEle.attr("href");
			if(!xmlUrlList.contains(temp))
				//xmlUrlList.add(rssEle.attr("href"));
				xmlUrlList.add(HrefCheck(rssEle.attr("href"),url));
		}	
		
		return xmlUrlList;
	}
	public static String HrefCheck(String hrefWaitChecked,String href){
		if(hrefWaitChecked.charAt(0) == '/' || hrefWaitChecked.indexOf("http") == -1){
			System.out.println("!!!!" + hrefWaitChecked + " " + href);
			int index = href.indexOf("/",8);
			if(index != -1)
			{
				String temp = href.substring(0,index);
				System.out.println("temp: " + temp);
				temp = temp + hrefWaitChecked;
				return temp;
			}
			else{
				String temp = href + hrefWaitChecked;
				return temp;
			}
		}
		else
			return hrefWaitChecked;
	}
	public static String GetPrefix(String href){
		int startIndex = href.indexOf("/", 8);
		if(startIndex == -1){
			return href + "/";
		}
		int endIndex = href.lastIndexOf("/");
		if(startIndex == endIndex)
			return href;
		else{
			href = href.substring(0, startIndex + 1);
			return href;
		}
		
	}

	 
	public static String AssistFunction(String text){
		if(text.equals("��һҳ"))
			return "";
		if(text.equals("��һҳ"))
			return "";
		if(text.equals("���һҳ") || text.equals("βҳ") || text.equals("ĩҳ"))
			return "";
		if(text.equals("��ҳ"))
			return "";
		if(text.length() <= 1)
			return "";
		return text;
	}
	

}
