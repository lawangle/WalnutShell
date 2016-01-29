package com.creaty.walnutshell.fang;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.creaty.walnutshell.basic.Entry;
import com.creaty.walnutshell.basic.Source;


public class RssThread implements Callable<Source> {

	private String url;
	private int seq;//RSSԴ����Ŷ��壿
	public RssThread(String url,int seq) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.seq = seq;
	}
	@Override
	public Source call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("RSS "  + this.seq + " start!");
		Source returnValue = new Source();
		returnValue.page_address = this.url;
		returnValue.type = returnValue.RSS_SOURCE;
		returnValue.sourceSeq = this.seq;
		Date date = new Date();
		returnValue.modified_date = date.getTime();//��ʱ������
		ArrayList<Entry> entryList = new ArrayList<Entry>();
		SAXReader reader = new SAXReader();
		org.dom4j.Document doc = null;
		try{
			doc= reader.read(url);
		}catch(Exception e){
			System.out.println("No Page!");
			return new Source();
		}
		List list = doc.selectNodes("/rss/channel");
		Iterator iter = list.iterator();
		while(iter.hasNext()){
	        	org.dom4j.Element ownerElement = (org.dom4j.Element)iter.next();
	        	returnValue.description = GetText(ownerElement.elementText("description"));
	        	try
	        	{
	        		returnValue.page_address = ownerElement.elementText("link");
	        	}catch(	NullPointerException e){
	        		returnValue.page_address = "";
	        	}
	        	returnValue.name = ownerElement.elementText("title"); 	
		}
        List itemList = doc.selectNodes("/rss/channel/item");
        Iterator tempIter = itemList.iterator();
        while(tempIter.hasNext()){
        	org.dom4j.Element ownerElement = (org.dom4j.Element)tempIter.next();
	       	Entry temp = new Entry();
	       	temp.description = GetText(ownerElement.elementText("description")).trim();
	       	temp.description = temp.description.replaceAll("\n{2,}", "");       
	       	try
	       	{
	       		temp.href = ownerElement.elementText("link").trim();
	       	}catch(	NullPointerException e){
	       		temp.href = "";
	       	}
	       	temp.name = ownerElement.elementText("title").trim();
	       	entryList.add(temp);
        }
        returnValue.entries = entryList;
        System.out.println("RSS "  + this.seq + " end!");
		return returnValue;
	}
	private String GetText(String html) {
		html = html.replaceAll("<a.*>", "");
		html = html.replaceAll("<[^>]*>", "");
		html = html.replaceAll("&nbsp;", "");
		return html;
	}


}
