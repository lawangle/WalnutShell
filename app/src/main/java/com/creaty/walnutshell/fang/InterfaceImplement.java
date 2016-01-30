package com.creaty.walnutshell.fang;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.Image;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;

import android.util.Log;

public class InterfaceImplement implements InternetInterface2 {

	@Override
	public PageDetail getPrimitiveSources(URL url) throws ClientProtocolException,UnknownHostException {
		// TODO Auto-generated method stub	
		//System.out.println("Get all source start!");
		PageDetail returnValue = null;
		try {
			ArrayList<Source> rssSource = new ArrayList<Source>();
			ArrayList<Source> ourSource = new ArrayList<Source>();
			long htmlTimeStart = System.currentTimeMillis();
			String urlTemp = url.toString();
			if(urlTemp.indexOf(" ") != -1)
				urlTemp = urlTemp.replaceAll(" ", "");
			PageDetailAndHtml value = ToolFunction.GetPageDetailWithoutSource(urlTemp);
			long htmlTimeEnd = System.currentTimeMillis();
			String msg =  "Get htmlCode cost: " + String.valueOf(htmlTimeEnd - htmlTimeStart);
			Log.d("htmlTime", msg);
		    returnValue = value.detail;
			String htmlCode = value.html;
			long xmlStart = System.currentTimeMillis();
			ArrayList<String>xmlUrlList = ToolFunction.GetXmlUrlFromUrl(htmlCode,urlTemp);		
			long xmlEnd = System.currentTimeMillis();
			msg =  "Get xml url cost: " + String.valueOf(xmlEnd - xmlStart);
			Log.d("get xml url", msg);
			int seq = 1;
			int temp = 5;
			int index = 0;
			if(xmlUrlList.size() < 3)
			{
				while(true)
				{
					ArrayList<FutureTask<Source>> rssGroup = new ArrayList<FutureTask<Source>>();		
					if(index + temp - 1 <= xmlUrlList.size() - 1)
					{
						for(int i = index;i < index + temp;i++)
						{
							RssThread rt = new RssThread(xmlUrlList.get(i),seq);
							FutureTask<Source> task = new FutureTask<Source>(rt);
							rssGroup.add(task);
							new Thread(task,"rssThread" + seq).start();
							seq++;
						}
						for(FutureTask<Source> i3 : rssGroup){
							rssSource.add(i3.get());
						}
						//System.out.println("Thread Group" + (index + 1) + "->" +(index + temp) + "Finished!");
						index += temp;
					}
					else{
						for(int i = index;i < xmlUrlList.size();i++){
							RssThread rt = new RssThread(xmlUrlList.get(i),seq);
							FutureTask<Source> task = new FutureTask<Source>(rt);
							rssGroup.add(task);
							new Thread(task,"rssThread" + seq).start();
							seq++;
						}
						for(FutureTask<Source> i3 : rssGroup){
							rssSource.add(i3.get());
						}
						//System.out.println("Thread Group" + (index + 1) + "->" +(xmlUrlList.size()) + "Finished!");
						break;
					}
					for(FutureTask<Source> i3 : rssGroup){
						rssSource.add(i3.get());
					}
				}
			}
			else{
				while(true)
				{
					ArrayList<FutureTask<Source>> rssGroup = new ArrayList<FutureTask<Source>>();		
					if(index + temp - 1 <= xmlUrlList.size() - 1)
					{
						for(int i = index;i < index + temp;i++)
						{
							SimpleRssThread rt = new SimpleRssThread(xmlUrlList.get(i),seq);
							FutureTask<Source> task = new FutureTask<Source>(rt);
							rssGroup.add(task);
							new Thread(task,"rssThread" + seq).start();
							seq++;
						}
						for(FutureTask<Source> i3 : rssGroup){
							rssSource.add(i3.get());
						}
						//System.out.println("Thread Group" + (index + 1) + "->" +(index + temp) + "Finished!");
						index += temp;
					}
					else{
						for(int i = index;i < xmlUrlList.size();i++){
							SimpleRssThread rt = new SimpleRssThread(xmlUrlList.get(i),seq);
							FutureTask<Source> task = new FutureTask<Source>(rt);
							rssGroup.add(task);
							new Thread(task,"rssThread" + seq).start();
							seq++;
						}
						for(FutureTask<Source> i3 : rssGroup){
							rssSource.add(i3.get());
						}
						//System.out.println("Thread Group" + (index + 1) + "->" +(xmlUrlList.size()) + "Finished!");
						break;
					}
					for(FutureTask<Source> i3 : rssGroup){
						rssSource.add(i3.get());
					}
				}
//				int rssSeq = 1;
//				for(String xml:xmlUrlList){
//					Source a = new Source();
//					a.name = "RssԴ" + String.valueOf(rssSeq);
//					a.page_address = xml;
//					rssSource.add(a);
//				}
			}
			seq = 1;
			UserDefinedSourceThread udst = new UserDefinedSourceThread(value.html,urlTemp);
			FutureTask<ArrayList<Source>> task = new FutureTask<ArrayList<Source>>(udst);
			new Thread(task,"ourThread").start();			
			returnValue.rssSource = rssSource;
			returnValue.ourSource = task.get();			
		}catch(UnknownHostException e){
			throw e;
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Get all source end!");
		return returnValue;
	}
	
	@Override
	public Source getRssSource(URL url) throws UnknownHostException {
		// TODO Auto-generated method stub
		String urlTemp = url.toString();
		if(urlTemp.indexOf(" ") != -1)
			urlTemp = urlTemp.replaceAll(" ", "");
		RssThread rt = new RssThread(urlTemp,-1);
		FutureTask<Source> task = new FutureTask<Source>(rt);
		new Thread(task,"rssThread").start();
		Source returnValue = null;
		try {
			returnValue = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValue;		
	}

	@Override
	public Source getSelfSource(URL url, int seq,int strategyType) {
		// TODO Auto-generated method stub
		String html = "";
		String urlTemp = url.toString();
		if(urlTemp.indexOf(" ") != -1)
			urlTemp = urlTemp.replaceAll(" ", "");
		try {
			html = ToolFunction.GetHtmlCode(urlTemp);
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Source returnValue = new Source();
		returnValue.page_address = urlTemp;
		Date dateObj = new Date();
		returnValue.modified_date = dateObj.getTime();
		if(strategyType == Source.UL_STRATEGY){			
			ArrayList<Entry> entryList = new ArrayList<Entry>();
			returnValue.strategyType = Source.UL_STRATEGY;
			int localSeq = 1;
			Document doc = Jsoup.parse(html);
			Elements ulList = doc.select("ul");
			for(Element ulEle : ulList){
				if(seq != localSeq)
				{
					localSeq++;
					continue;
				}
				else{		
					Elements liList = ulEle.select("li");
					for(Element liEle : liList){
						Elements aList = liEle.select("a");
						if(aList.size() > 1){
							Entry temp = new Entry();
							temp.name = "";
							for(Element aEle : aList){
								String title = ToolFunction.AssistFunction(aEle.text());
								if(title.length() != 0 && title != null && title.length() > temp.name.length())
								{
									temp.name = aEle.text().trim();
									temp.href =  aEle.attr("href").trim();
									if(temp.href.indexOf("http://") == -1){
										if(urlTemp.indexOf('/', 7)== -1)
											temp.href = urlTemp + temp.href;
										else{
											int index = urlTemp.indexOf('/', 7);
											String subHref = urlTemp.substring(0, index);
											temp.href = subHref + temp.href;
										}
									}
									temp.description = "";
								}				
							}
							if(temp.name.length() != 0)
								entryList.add(temp);
						}
						else{
							for(Element aEle : aList){
								Entry temp = new Entry();
								String title = ToolFunction.AssistFunction(aEle.text());
								if(title.length() != 0 && title != null)
								{
									temp.name = aEle.text().trim();
									temp.href =  aEle.attr("href").trim();
									if(temp.href.indexOf("http://") == -1){
										if(urlTemp.indexOf('/', 7)== -1)
											temp.href = urlTemp + temp.href;
										else{
											int index = urlTemp.indexOf('/', 7);
											String subHref = urlTemp.substring(0, index);
											temp.href = subHref + temp.href;
										}
									}
									temp.description = "";
									entryList.add(temp);
								}				
							}
						}
					}
					returnValue.entries = entryList;
					return returnValue;
				}
			}
		}
		else if(strategyType == Source.TABLE_STRATEGY){
			int localSeq = 1;
			ArrayList<Entry> entryList = new ArrayList<Entry>();
			returnValue.strategyType = Source.TABLE_STRATEGY;
			Document doc = Jsoup.parse(html);
			Elements tableList = doc.select("table");
			for(Element tableEle : tableList){
				if(localSeq != seq){
					localSeq++;
					continue;
				}
				else{
					Elements trList = tableEle.select("tr");
					for(Element trEle : trList){
						Elements aList = trEle.select("a");
						if(aList.size() > 1){
							Entry temp = new Entry();
							temp.name = "";
							for(Element aEle : aList){
								String title = ToolFunction.AssistFunction(aEle.text());
								if(title.length() != 0 && title != null && title.length() > temp.name.length())
								{
									temp.name = aEle.text().trim();
									temp.href =  aEle.attr("href").trim();
									if(temp.href.indexOf("http://") == -1){
										if(urlTemp.indexOf('/', 7)== -1)
											temp.href = urlTemp + temp.href;
										else{
											int index = urlTemp.indexOf('/', 7);
											String subHref = urlTemp.substring(0, index);
											temp.href = subHref + temp.href;
										}
									}
									temp.description = "";
								}				
							}
							if(temp.name.length() != 0)
								entryList.add(temp);
						}
						else{
							for(Element aEle : aList){
								Entry tempEntry = new Entry();
								String title = ToolFunction.AssistFunction(aEle.text());
								if(title.length() != 0 && title != null)
								{
									tempEntry.name = title.trim();
									tempEntry.href = aEle.attr("href").trim();
									if(tempEntry.href.indexOf("http://") == -1){
										if(urlTemp.indexOf('/', 7)== -1)
											tempEntry.href = urlTemp + tempEntry.href;
										else{
											int index = urlTemp.indexOf('/', 7);
											String subHref = urlTemp.substring(0, index);
											tempEntry.href = subHref + tempEntry.href;
										}
									}
									tempEntry.description = "";
									entryList.add(tempEntry);
								}
							}
						}
					}
					returnValue.entries = entryList;
					return returnValue;
				}
			}
		}
		else if(strategyType == Source.SECTION_STRATEGY){
			int localSeq = 1;
			ArrayList<Entry> entryList = new ArrayList<Entry>();
			returnValue.strategyType = Source.SECTION_STRATEGY;
			Document doc = Jsoup.parse(html);
			Elements tableList = doc.select("section");
			for(Element tableEle : tableList){
				if(localSeq != seq){
					localSeq++;
					continue;
				}
				else{
					 Elements aList = tableEle.select("a");
					for(Element aEle : aList){
						Entry temp = new Entry();
						String title = ToolFunction.AssistFunction(aEle.text());
						if(title.length() != 0 && title != null)
						{
							temp.name = aEle.text().trim();
							temp.href =  aEle.attr("href").trim();
							if(temp.href.indexOf("http://") == -1){
								temp.href = urlTemp + temp.href;
							}
							temp.description = "";
							entryList.add(temp);
						}
					}
					returnValue.entries = entryList;
					return returnValue;
				}
			}
			
		}
		else if(strategyType == Source.DIV_STRATEGY){
			int localSeq = 1;
			ArrayList<Entry> entryList = new ArrayList<Entry>();
			returnValue.strategyType = Source.DIV_STRATEGY;
			Document doc = Jsoup.parse(html);
			Elements tableList = doc.select("div");
			for(Element tableEle : tableList){
				if(localSeq != seq){
					localSeq++;
					continue;
				}
				else{
					 Elements aList = tableEle.select("a");
					for(Element aEle : aList){
						Entry temp = new Entry();
						String title = ToolFunction.AssistFunction(aEle.text());
						if(title.length() != 0 && title != null)
						{
							temp.name = aEle.text().trim();
							temp.href =  aEle.attr("href").trim();
							if(temp.href.indexOf("http://") == -1){
								temp.href = urlTemp + temp.href;
							}
							temp.description = "";
							entryList.add(temp);
						}
					}
					returnValue.entries = entryList;
					return returnValue;
				}
			}
		}
		return null;
	}

	@Override
	public ContentDetail getContent(URL url) {
		// TODO Auto-generated method stub
		
		ContentDetail returnValue = new ContentDetail();
		Date dateObj = new Date();
		returnValue.modified_date = dateObj.getTime();
		try {
			String urlTemp = url.toString();
			if(urlTemp.indexOf(" ") != -1)
				urlTemp = urlTemp.replaceAll(" ", "");
			String htmlCode = ToolFunction.GetHtmlCode(urlTemp);
			returnValue.content = ToolFunction.ArticleAnalysis(htmlCode);
			if(returnValue.content.size() > 1)
				returnValue.basicInfor.description = returnValue.content.get(0).GetText() + returnValue.content.get(1).GetText() + "...";///��ʱ������
			Document doc = Jsoup.parse(htmlCode);
			Elements titleList = doc.select("title");
			for(Element titleEle : titleList){
				returnValue.basicInfor.name = titleEle.text();
			}
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValue;
	}
	
	@Override
	public Image getImage(URL url) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		String urlString = url.toString();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(urlString);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");//�Է��İ���ͷ�����趨���Ա����˳����ȡ��ȷ��Դ��
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream input = entity.getContent();
		Image returnValue = new Image(input);
		returnValue.size = entity.getContentLength();
		return returnValue;
	}

}
