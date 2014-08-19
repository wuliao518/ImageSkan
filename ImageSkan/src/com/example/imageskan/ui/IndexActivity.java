package com.example.imageskan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IndexActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button button=new Button(this);
		button.setText("´´½¨");
		setContentView(button);
		button.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(IndexActivity.this, CreateActivity.class);
				startActivity(intent);
			}
		});
	}
}
