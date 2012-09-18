package com.matburt.mobileorg.Gui.Capture;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.matburt.mobileorg.R;
import com.matburt.mobileorg.Gui.ViewFragment;
import com.matburt.mobileorg.OrgData.OrgNode;
import com.matburt.mobileorg.OrgData.OrgNodePayload;

public class PayloadFragment extends ViewFragment {
	private static final String PAYLOAD = "payload";
	
	private RelativeLayout payloadView;
	private EditText payloadEdit;
	private OrgNodePayload payload;
	
	private ImageButton editButton;
	private ImageButton cancelButton;
	private ImageButton saveButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.payloadView = (RelativeLayout) inflater.inflate(
				R.layout.edit_payload, container, false);
		this.webView = (WebView) payloadView
				.findViewById(R.id.edit_payload_webview);
		this.webView.setBackgroundColor(0x00000000);
		this.webView.setWebViewClient(new InternalWebViewClient());
		this.webView.setOnClickListener(editListener);

		this.payloadEdit = (EditText) payloadView
				.findViewById(R.id.edit_payload_edittext);
		
		this.editButton = (ImageButton) payloadView
				.findViewById(R.id.edit_payload_edit);
		editButton.setOnClickListener(editListener);
		
		this.cancelButton = (ImageButton) payloadView
				.findViewById(R.id.edit_payload_cancel);
		cancelButton.setOnClickListener(cancelListener);
		
		this.saveButton = (ImageButton) payloadView
				.findViewById(R.id.edit_payload_save);
		saveButton.setOnClickListener(saveListener);
		
		return payloadView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		EditActivity editActivity = (EditActivity) getActivity();
		
		OrgNode node = editActivity.getOrgNode();
		this.payload = node.getOrgNodePayload();
		
		if(savedInstanceState != null)
			restoreInstanceState(savedInstanceState);
		else
			switchToView();
		
		if(editActivity.isNodeModifiable() == false)
			setUnmodifiable();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if(this.payloadEdit.getVisibility() == View.VISIBLE)
			outState.putString(PAYLOAD, this.payloadEdit.getText().toString());
	}
	
	public void restoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			String payloadString = savedInstanceState.getString(PAYLOAD);
			
			if(payloadString != null)
				switchToEdit(payloadString);
			else
				switchToView();
		}
	}
	
	public void setUnmodifiable() {
		this.editButton.setVisibility(View.GONE);
	}

	public void setPayload(String payload) {
		this.payload.set(payload);
	}
	
	public String getPayload() {
		return this.payload.get();
	}
	
	private void switchToEdit(String payloadString) {
		webView.setVisibility(View.GONE);
		editButton.setVisibility(View.GONE);

		if(payloadString != null)
			payloadEdit.setText(payloadString);
		payloadEdit.setVisibility(View.VISIBLE);
		cancelButton.setVisibility(View.VISIBLE);
		saveButton.setVisibility(View.VISIBLE);
	}
	
	private void switchToEdit() {
		switchToEdit(this.payload.get());
	}
	
	private void switchToView() {
		payloadEdit.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		saveButton.setVisibility(View.GONE);
		
		display(this.payload.getCleanedPayload());
		webView.setVisibility(View.VISIBLE);
		editButton.setVisibility(View.VISIBLE);
	}
	
	private OnClickListener editListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switchToEdit();
			payloadEdit.requestFocus();
		}
	};
	
	private OnClickListener saveListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setPayload(payloadEdit.getText().toString());
			switchToView();
			webView.requestFocus();
		}
	};
	
	private OnClickListener cancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switchToView();
			webView.requestFocus();
		}
	};
}
