package com.creaty.walnutshell.fang;


import java.io.IOException;
import java.io.InputStream;
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
	
	/**��ȡԭʼԴ
	 * @throws ClientProtocolException 
	 * @throws IOException 
	 * @throws Exception */
	public PageDetail getPrimitiveSources(URL url) throws ClientProtocolException,UnknownHostException, IOException, Exception;
	
	/**��ȡRSSԴ
	 * @throws UnknownHostException */
	public Source getRssSource(URL url) throws UnknownHostException;
	
	/**��ȡ��ɸѡԴ*/
	public Source getSelfSource(URL url, int seq, int strategyType);		//seq ��Դ����������ҳ����Դ�е����кţ���0��ʼ
	
	/**��ȡ����*/
	public ContentDetail getContent(URL url);
	
	/**��ȡͼƬ InputStream Ӧ�ñ�����*/
	public Image getImage(URL url) throws ClientProtocolException, IOException;

}
