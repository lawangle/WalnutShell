package com.creaty.walnutshell.UI;

import com.creaty.walnutshell.R;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {

	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		getActivity().setTitle(getResources().getString(R.string.menu_about));
		return rootView;
	}

}
