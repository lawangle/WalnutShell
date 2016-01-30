package com.creaty.walnutshell.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ContentDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Entry basicInfor;		//��Ŀ������Ϣ
	public String author;		//����/��Դ
	public ArrayList<StringAndImg>	content;			//���ģ��ֶ����ȡ��
	
	//ͼƬλ�ú�ͼƬ���ӣ�λ�þ������������0�����1֮����ͼƬ��ͼƬλ��Ϊ1��
	public Map<Integer,String> images;		
	
	//����޸�ʱ�䣬������ʱ���ҳ���ȡ��û��Ϊ-1�����޸�ʱ��Ϊϵͳ��ǰʱ�䣩
	public long modified_date;
	public ContentDetail() {
		// TODO Auto-generated constructor stub
		this.author = "";
		content = new ArrayList<StringAndImg>();
		basicInfor = new Entry();
	}
	@Override
	public String toString() {
		return "ContentDetail [basicInfor=" + basicInfor + ", author=" + author
				+ ", content=" + content + ", images=" + images
				+ ", modified_date=" + modified_date + "]";
	}	
	
	
}
