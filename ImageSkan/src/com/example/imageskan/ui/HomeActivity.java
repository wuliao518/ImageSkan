package com.example.imageskan.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imageskan.R;
import com.example.imageskan.utils.ImageLoader;
import com.example.imageskan.utils.ImageLoader.OnCallBackListener;
import com.example.imageskan.view.MyImageView;
import com.example.imageskan.view.MyImageView.OnMeasureListener;

public class HomeActivity extends Activity implements OnClickListener{
	private View view;
	private ListView mListView;
	private MyAdapter adapter;
	private ArrayList<String> strs;
	private LayoutInflater inflate;
	private int width=0,height=0;
	private Handler handle=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter=new MyAdapter();
			mListView.setAdapter(adapter);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		initView();
	}
	private void initView() {
		width=getWindowManager().getDefaultDisplay().getWidth();
		view=findViewById(R.id.view_add);
		mListView=(ListView) findViewById(R.id.lv_photo);
		inflate=LayoutInflater.from(this);
		view.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_add:
			Intent intent=new Intent(HomeActivity.this,MainActivity.class);
			startActivityForResult(intent, 0);
			break;

		default:
			break;
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		strs=(ArrayList<String>) data.getExtras().get("data");
		for(String path:strs){
			try {
				ExifInterface exif= new ExifInterface(path);
				//String dateTime=exif.getAttribute("DateTime");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Toast.makeText(getApplicationContext(), "ÄúÑ¡ÔñÁË"+strs.size()+"¸ö", 0).show();
		handle.sendEmptyMessage(0);
		
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return strs.size();
		}

		@Override
		public Object getItem(int position) {
			return strs.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String path=strs.get(position);
			ViewHolder viewHolder;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=inflate.inflate(R.layout.item_image, null);
				viewHolder.tv=(TextView) convertView.findViewById(R.id.tv_title);
				viewHolder.iv=(MyImageView) convertView.findViewById(R.id.iv_photo);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			LayoutParams params=viewHolder.iv.getLayoutParams();
			params.height=height;
			params.height=getHeight(path);
			viewHolder.tv.setText(path);
			viewHolder.iv.setLayoutParams(params);
			Bitmap bitmap=ImageLoader.getInstance().loadImage(path, width, height, new OnCallBackListener() {		
				@Override
				public void setOnCallBackListener(Bitmap bitmap, String url) {
					ImageView image=(ImageView) mListView.findViewWithTag(url);
					if(image!=null&&bitmap!=null){
						image.setImageBitmap(bitmap);
					}
				}
			});
			if(bitmap!=null){
				viewHolder.iv.setImageBitmap(bitmap);
			}else{
				//viewHolder.iv.set
			}
			return convertView;
		}
	}
	private static class ViewHolder{
		public TextView tv;
		public MyImageView iv;
	}
	
	private int getHeight(String path){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path, options);
		int imageWidth=options.outWidth;
		int imageHeight=options.outHeight;
		if(imageWidth>width){
			float scale=(float)imageWidth/(float)width;
			return (int) (imageHeight/scale);
		}
		return imageHeight;
		
		
		
		
		
	} 
}
