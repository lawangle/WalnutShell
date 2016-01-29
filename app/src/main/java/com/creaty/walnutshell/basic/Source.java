package com.creaty.walnutshell.basic;

import java.io.Serializable;
import java.util.ArrayList;

public class Source implements Serializable {
	
	/**
	 * 
	 */
	public Source() {
		// TODO Auto-generated constructor stub
		page_address = "";
		name = "";
		description = "";
		modified_date = -1;
	}
	private static final long serialVersionUID = 1L;
	
	public static int RSS_SOURCE = 0;		//����
	public static int SELF_SOURCE = 1;
	public static int UL_STRATEGY = 100;
	public static int TABLE_STRATEGY = 101;
	public static int SECTION_STRATEGY = 102;
	public static int DIV_STRATEGY = 103;
	public static int OTHER_STRATEGY = 104;
	public String page_address, name, description;		//Դ������ҳ��ַ�����֣�û����Ϊ""����������û����Ϊ""��
	public int sourceSeq;   										//��Դ����������ҳ����Դ�е����кţ���0��ʼ
	public int type;												//Դ�����ͣ���ɸѡԴ����RSSԴ
	public int strategyType;
	public long modified_date;			//����޸�ʱ�䣬��Դ��ϵͳ��ǰʱ��
	public ArrayList<Entry> entries;		//Դ����Ŀ���ϣ�pair�� ����/����
	@Override
	public String toString() {
		return "Source [page_address=" + page_address + ", name=" + name
				+ ", description=" + description + ", sourceSeq=" + sourceSeq
				+ ", type=" + type + ", strategyType=" + strategyType
				+ ", modified_date=" + modified_date + ", entries=" + entries
				+ "]";
	}
}
