package com.creaty.walnutshell.fang;


import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;


import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.basic.Image;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;


public interface InternetInterface2 {

	final static String tag = "InternetInterface2";

	
	/**
	 * @throws ClientProtocolException 
	 * @throws IOException 
	 * @throws Exception */
	public PageDetail getPrimitiveSources ( URL url ) throws ClientProtocolException,java.net.UnknownHostException, IOException, Exception;
	
	/**
	 * @throws UnknownHostException */
	public Source getRssSource( URL url ) throws UnknownHostException;
	
	public Source getSelfSource( URL url, int seq,int strategyType);		//seq ��Դ����������ҳ����Դ�е����кţ���0��ʼ
	
	public ContentDetail getContent( URL url );
	
	public Image getImage( URL url );

}
