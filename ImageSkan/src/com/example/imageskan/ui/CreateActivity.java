package com.example.imageskan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.imageskan.R;

public class CreateActivity extends Activity {
	private EditText mEditText;
	private Button mButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_activity);
		initView();
	}
	private void initView() {
		mEditText=(EditText) findViewById(R.id.et_name);
		mButton=(Button) findViewById(R.id.bt_create);
		mButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				String name=mEditText.getText().toString().trim();
				Intent intent=new Intent(CreateActivity.this,HomeActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
				finish();
			}
		});
	}
}
