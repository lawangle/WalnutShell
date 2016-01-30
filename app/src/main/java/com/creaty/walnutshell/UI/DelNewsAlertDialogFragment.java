package com.creaty.walnutshell.UI;

import com.creaty.walnutshell.R;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.NewsTableMetaData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

public class DelNewsAlertDialogFragment extends DialogFragment {
	public static final String ARG_ALERT_NEWS_NAME = "news_name";
	public static final String ARG_ALERT_NEWS_ID = "news_id";
	private OnDelNewsConfirmListener mListener;
	public interface OnDelNewsConfirmListener {
		public void onDelNewsConfirm(Uri uri);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (OnDelNewsConfirmListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mListener = null;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final String newsname = getArguments().getString(
				ARG_ALERT_NEWS_NAME);
		final int newsId = getArguments().getInt(ARG_ALERT_NEWS_ID);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.alert)
				.setMessage("您真的要删除报刊 " + newsname + " 吗？\n该报刊包含的所有源将被删除")
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								mListener.onDelNewsConfirm(Uri
										.withAppendedPath(
												NewsTableMetaData.CONTENT_URI,
												String.valueOf( newsId ) ));
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
		return builder.create();
	}
}