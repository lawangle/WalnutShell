package com.creaty.walnutshell.fang;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;

import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.basic.Image;
import com.creaty.walnutshell.basic.PageDetail;
import com.creaty.walnutshell.basic.Source;

public interface InternetInterface3 {
final static String tag = "InternetInterface3";
	/**��ȡ��ҳԴ��
	 * @return ָ��Ŀ����ַ��Դ��*/
	public String getHttpContent(URL url) throws ClientProtocolException, UnknownHostException, IOException, Exception;
	/**��ȡԭʼԴ
	 * @throws ClientProtocolException 
	 * @throws IOException 
	 * @throws Exception */
	public PageDetail getPrimitiveSources(String code);
	
	/**��ȡRSSԴ
	 * @throws UnknownHostException */
	public Source getRssSource(String code);
	
	/**��ȡ��ɸѡԴ*/
	public Source getSelfSource(String code, int seq, int strategyType);		//seq ��Դ����������ҳ����Դ�е����кţ���0��ʼ
	
	/**��ȡ����*/
	public ContentDetail getContent(String code);
	
}
