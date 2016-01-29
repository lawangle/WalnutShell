package com.creaty.walnutshell.UI;

import com.creaty.walnutshell.R;
import com.creaty.walnutshell.basic.ContentDetail;
import com.creaty.walnutshell.content_provider.ProviderUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class ArticleFragment extends Fragment {

	public static String ARG_CONTENT_DETAIL = "content_detail";
	private ContentDetail cd = null;
	private ViewHolder articleView;

	private class ViewHolder {
		ScrollView scrollView;
		TextView titleView;
		TextView secTitleView;
		TextView contentView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView =  inflater.inflate(
				R.layout.fragment_article, container, false);
		articleView = new ViewHolder();
		articleView.titleView = (TextView) rootView
				.findViewById(R.id.article_titleview);
		articleView.secTitleView = (TextView) rootView
				.findViewById(R.id.article_sec_titleview);
		articleView.contentView = (TextView) rootView
				.findViewById(R.id.article_contentview);
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		cd = (ContentDetail) getArguments().getSerializable(ARG_CONTENT_DETAIL);
		articleView.titleView.setText(cd.basicInfor.name);
		if( cd.author != null && ! cd.author.isEmpty() ){
			articleView.secTitleView.setText(cd.author);
		}else{
			articleView.secTitleView.setText(cd.basicInfor.description);
		}
		articleView.contentView.setText(ProviderUtils.listToString(cd.content));
	}

}
