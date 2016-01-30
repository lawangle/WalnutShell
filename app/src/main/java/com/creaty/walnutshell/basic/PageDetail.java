package com.creaty.walnutshell.basic;

import java.io.Serializable;
import java.util.ArrayList;

/**��ҳҳ�������Ϣ���������ȫ��Դ
 * 
 */
public class PageDetail implements Serializable{
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	public String pageName;
	public String description;
	public long lastModifiedTime;	//����޸�ʱ�䣬��Դ��ϵͳ��ǰʱ��
	public String logoAddress;			//logoͼƬ����
	
	public ArrayList<Source> ourSource;		//��ɸѡԴ
	public ArrayList<Source> rssSource;		//RSSԴ
	@Override
	public String toString() {
		return "PageDetail [pageName=" + pageName + ", description="
				+ description + ", lastModifiedTime=" + lastModifiedTime + "]";
	}
	
}
