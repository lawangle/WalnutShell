package com.creaty.walnutshell.fang;


import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.Source;

import android.util.Log;

public class UserDefinedSourceThread implements Callable<ArrayList<Source>>{

	private String html;
	private String url;
	public UserDefinedSourceThread(String html,String url) {
		// TODO Auto-generated constructor stub
		this.html = html;
		this.url = url;
	}
	@Override
	public ArrayList<Source> call() throws Exception {
		// TODO Auto-generated method stub
		long timeStart = System.currentTimeMillis();
		//System.out.println("User thread start!");
		Date obj = new Date();
		long start = obj.getTime();
		if(html.indexOf("ul") != -1){
			ArrayList<Source> planList = new ArrayList<Source>();
			int seq = 1;
			Document doc = Jsoup.parse(html);
			Elements ulList = doc.select("ul");
			for(Element ulEle : ulList){
				Source tempSource = new Source();
				tempSource.page_address = this.url;
				tempSource.sourceSeq = seq;
				seq++;
				tempSource.strategyType = Source.UL_STRATEGY;
				ArrayList<Entry> entryList = new ArrayList<Entry>();
				Elements liList = ulEle.select("li");
				for(Element liEle : liList){
					Elements aList = liEle.select("a");
					if(aList.size() > 1){
						Entry temp = new Entry();
						temp.name = "";
						for(Element aEle : aList){
							String title = ToolFunction.AssistFunction(aEle.text());
							if(title.length() > 5 && title != null && title.length() > temp.name.length())
							{
								temp.name = aEle.text().trim();
								temp.href =  ToolFunction.HrefCheck(aEle.attr("href").trim(),url);
//								if(temp.href.indexOf("http://") == -1){
//									if(this.url.indexOf('/', 7) == -1)
//										temp.href = this.url + temp.href;
//									else{
//										int index = this.url.indexOf('/', 7);
//										String subHref = this.url.substring(0, index);
//										temp.href = subHref + temp.href;
//									}
//								}
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
							if(title.length() > 5 && title != null)
							{
								temp.name = aEle.text().trim();
								temp.href =  ToolFunction.HrefCheck(aEle.attr("href").trim(),url);
//								if(temp.href.indexOf("http://") == -1){
//									if(this.url.indexOf('/', 7)== -1)
//										temp.href = this.url + temp.href;
//									else{
//										int index = this.url.indexOf('/', 7);
//										String subHref = this.url.substring(0, index);
//										temp.href = subHref + temp.href;
//									}
//								}
								temp.description = "";
								entryList.add(temp);
							}				
						}
					}
				}
				if(entryList.size() > 1)
				{
					tempSource.entries = entryList;
					planList.add(tempSource);
					if(planList.size() > 20)
					{
						//System.out.println(obj.getTime() - start);
						long timeEnd = System.currentTimeMillis();
				        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
				        Log.d("UserDefined " + String.valueOf(seq), msg);
				        System.out.println(msg);
						return planList;
					}

				}
				//System.out.println("----------------------------------");
			}
			//System.out.println("Analysis end!");
			if(planList.size() > 1){
				//System.out.println("ul!");
				//System.out.println(obj.getTime() - start);
				long timeEnd = System.currentTimeMillis();
		        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
		        Log.d("UserDefined " + String.valueOf(seq), msg);
		        System.out.println(msg);
				return planList;
			}
		}
		if(html.indexOf("table") != -1){
			//换下一种策略,table策略
			ArrayList<Source> planList = new ArrayList<Source>();
			int seq = 1;
			Document doc = Jsoup.parse(html);
			Elements tableList = doc.select("table");
			for(Element tableEle : tableList){
				Source tempSource = new Source();
				tempSource.page_address = this.url;
				tempSource.sourceSeq = seq;
				seq++;
				tempSource.strategyType = Source.TABLE_STRATEGY;
				ArrayList<Entry> entryList = new ArrayList<Entry>();
				Elements trList = tableEle.select("tr");
				for(Element trEle : trList){
					Elements aList = trEle.select("a");
					if(aList.size() > 1){
						Entry temp = new Entry();
						temp.name = "";
						for(Element aEle : aList){
							String title = ToolFunction.AssistFunction(aEle.text());
							if(title.length() > 5 && title != null && title.length() > temp.name.length())
							{
								temp.name = aEle.text().trim();
								temp.href =  ToolFunction.HrefCheck(aEle.attr("href").trim(),url);
//								if(temp.href.indexOf("http://") == -1){
//									if(this.url.indexOf('/', 7)== -1)
//										temp.href = this.url + temp.href;
//									else{
//										int index = this.url.indexOf('/', 7);
//										String subHref = this.url.substring(0, index);
//										temp.href = subHref + temp.href;
//									}
//								}
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
							if(title.length() > 5 && title != null)
							{
								tempEntry.name = title.trim();
								tempEntry.href =  ToolFunction.HrefCheck(aEle.attr("href").trim(),url);
//								if(tempEntry.href.indexOf("http://") == -1){
//									if(this.url.indexOf('/', 7)== -1)
//										tempEntry.href = this.url + tempEntry.href;
//									else{
//										int index = this.url.indexOf('/', 7);
//										String subHref = this.url.substring(0, index);
//										tempEntry.href = subHref + tempEntry.href;
//									}
//								}
								tempEntry.description = "";
								entryList.add(tempEntry);
							}
						}
					}
				}
				
				if(entryList.size() > 1)
				{
					tempSource.entries = entryList;
					planList.add(tempSource);
					if(planList.size() > 20)
					{
						//System.out.println(obj.getTime() - start);
						long timeEnd = System.currentTimeMillis();
				        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
				        Log.d("UserDefined " + String.valueOf(seq), msg);
				        System.out.println(msg);
						return planList;
					}

				}
				//System.out.println("--------------------------------------");
			}
			if(planList.size() > 1){
				//System.out.println("table!");
				//System.out.println(obj.getTime() - start);
				long timeEnd = System.currentTimeMillis();
		        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
		        Log.d("UserDefined " + String.valueOf(seq), msg);
		        System.out.println(msg);
				return planList;
			}
		}
		if(html.indexOf("section") != -1){
			//换下一个策略,section策略
			ArrayList<Source> planList = new ArrayList<Source>();
			int seq = 1;
			Document doc = Jsoup.parse(html);
			Elements tableList = doc.select("section");
			for(Element tableEle : tableList){
				Source tempSource = new Source();
				tempSource.page_address = this.url;
				tempSource.sourceSeq = seq;
				seq++;
				tempSource.strategyType = Source.SECTION_STRATEGY;
				ArrayList<Entry> entryList = new ArrayList<Entry>();
				Elements aList = tableEle.select("a");
				for(Element aEle : aList){
					Entry tempEntry = new Entry();
					String title = ToolFunction.AssistFunction(aEle.text());
					if(title.length() > 5 && title != null)
					{
						tempEntry.name = title.trim();
						tempEntry.href =  ToolFunction.HrefCheck(aEle.attr("href").trim(),url);
//						if(tempEntry.href.indexOf("http://") == -1){
//							if(this.url.indexOf('/', 7)== -1)
//								tempEntry.href = this.url + tempEntry.href;
//							else{
//								int index = this.url.indexOf('/', 7);
//								String subHref = this.url.substring(0, index);
//								tempEntry.href = subHref + tempEntry.href;
//							}
//						}
						tempEntry.description = "";
						entryList.add(tempEntry);
					}

				}
				if(entryList.size() > 1)
				{
					tempSource.entries = entryList;
					planList.add(tempSource);
					if(planList.size() > 20)
					{
						//System.out.println(obj.getTime() - start);
						long timeEnd = System.currentTimeMillis();
				        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
				        Log.d("UserDefined " + String.valueOf(seq), msg);
				        System.out.println(msg);
						return planList;
					}
				}
				//System.out.println("--------------------------------------");
			}
			if(planList.size() > 1){
				//System.out.println("section!");
				//System.out.println(obj.getTime() - start);
				long timeEnd = System.currentTimeMillis();
		        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
		        Log.d("UserDefined " + String.valueOf(seq), msg);
		        System.out.println(msg);
				return planList;
			}
		}
		if(html.indexOf("div") != -1){
			//换下一个策略,div策略
			ArrayList<Source> planList = new ArrayList<Source>();
			int seq = 1;
			Document doc = Jsoup.parse(html);
			Elements tableList = doc.select("div");
			for(Element tableEle : tableList){
				Source tempSource = new Source();
				tempSource.page_address = this.url;
				tempSource.sourceSeq = seq;
				seq++;
				tempSource.strategyType = Source.SECTION_STRATEGY;
				ArrayList<Entry> entryList = new ArrayList<Entry>();
				Elements aList = tableEle.select("a");
				for(Element aEle : aList){
					Entry tempEntry = new Entry();
					String title = ToolFunction.AssistFunction(aEle.text());
					if(title.length() > 5 && title != null)
					{
						tempEntry.name = title.trim();
						tempEntry.href =  ToolFunction.HrefCheck(aEle.attr("href").trim(),url);
//						if(tempEntry.href.indexOf("http://") == -1){
//							if(this.url.indexOf('/', 7)== -1)
//								tempEntry.href = this.url + tempEntry.href;
//							else{
//								int index = this.url.indexOf('/', 7);
//								String subHref = this.url.substring(0, index);
//								tempEntry.href = subHref + tempEntry.href;
//							}
//						}
						tempEntry.description = "";
						entryList.add(tempEntry);
					}

				}
				if(entryList.size() > 1)
				{
					tempSource.entries = entryList;
					planList.add(tempSource);
					if(planList.size() > 20)
					{
						//System.out.println(obj.getTime() - start);
						long timeEnd = System.currentTimeMillis();
				        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
				        Log.d("UserDefined " + String.valueOf(seq), msg);
				        System.out.println(msg);
						return planList;
					}
				}
				//System.out.println("--------------------------------------");
			}
			if(planList.size() > 1){
				//System.out.println("div!");
				//System.out.println(obj.getTime() - start);
				long timeEnd = System.currentTimeMillis();
		        String msg =  "UserDefined " + String.valueOf(seq) + " cost: " + String.valueOf(timeStart - timeEnd);
		        Log.d("UserDefined " + String.valueOf(seq), msg);
		        System.out.println(msg);
				return planList;
			}
		}
		
		return new ArrayList<Source>();
	}
	

}
