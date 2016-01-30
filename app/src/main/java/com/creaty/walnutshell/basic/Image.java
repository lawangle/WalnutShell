package com.creaty.walnutshell.basic;

import java.io.InputStream;
import java.io.Serializable;

public class Image implements Serializable{

	public InputStream is;
	public long size;
	public Image(InputStream is) {
		// TODO Auto-generated constructor stub
		this.is = is;
	}
	
}
