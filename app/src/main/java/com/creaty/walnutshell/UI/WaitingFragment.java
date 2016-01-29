package com.creaty.walnutshell.UI;

import java.net.URL;

import com.creaty.walnutshell.R;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WaitingFragment extends Fragment {
	private ProgressBar mProgress;
	private TextView mTextView;
	private View rootView;
	
	public static String ARG_PREPTASK = "preptask";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_waiting, container,
				false);
		//getActivity().setTitle(getResources().getString(R.string.menu_about));
		
		mProgress = (ProgressBar)rootView.findViewById(R.id.progressBar_waiting);
//		mTextView = (TextView) rootView.findViewById(R.id.waiting_textView);
       
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
	
	
}
