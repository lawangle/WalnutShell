package com.creaty.walnutshell.UI;

import com.creaty.walnutshell.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WaitingFragmentV4 extends Fragment {
	private ProgressBar mProgress;
	private TextView mTextView;
	private View rootView;
	
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
//		mTextView.setText(R.string.please_waiting);
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
}
