package com.creaty.walnutshell.basic;

import android.R.bool;

public class StringAndImg {
	public static final int TYPE_TEXT =1;
	public static final int TYPE_URL=2;
	public static final int TYPE_OTHER=3;
	public static final String identifier = "#$(F)$#";
	public String content;
	public int type; 
	public StringAndImg() {
		// TODO Auto-generated constructor stub
		type = 3;
		content = "";
	}
	public StringAndImg(String content,int sign){
		if(sign == 1){
			this.content = content;
			type = TYPE_TEXT;
		}
		else if(sign == 2){
			this.content = content;
			type = TYPE_URL;
		}
		else{
			this.content = "";
			type = TYPE_OTHER;
		}
	}
	public String GetText(){
		if( type == TYPE_TEXT ){
			return content;
		}else{
			return "";
		}
	}
	public StringAndImg( String s ) {
		int index = s.indexOf(identifier);
		if( index != -1 ){
			type = Integer.parseInt(s.substring(0, index));
			content = s.substring(identifier.length()+1);
		}
		
	}
	//这个函数很重要！不要轻易改
	@Override
	public String toString() {
		return type+identifier+content;
	}

}
