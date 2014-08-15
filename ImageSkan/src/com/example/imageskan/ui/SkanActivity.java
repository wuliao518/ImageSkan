package com.example.imageskan.ui;

import java.util.ArrayList;

import com.example.imageskan.R;
import com.example.imageskan.utils.ImageLoader;
import com.example.imageskan.utils.ImageLoader.OnCallBackListener;
import com.example.imageskan.view.MyImageView;
import com.example.imageskan.view.MyImageView.OnMeasureListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

public class SkanActivity extends Activity {
	private GridView mGridView;
	private ArrayList<String> lists;
	private LayoutInflater inflate;
	private int viewWidth=0,viewHeight=0;
	private MyAdapter adapter;
	private ArrayList<String> strs=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);
		lists=(ArrayList<String>) getIntent().getExtras().get("data");
		initView();
	}
	private void initView() {
		mGridView=(GridView) findViewById(R.id.child_grid);
		inflate=LayoutInflater.from(SkanActivity.this);
		adapter=new MyAdapter();
		mGridView.setAdapter(adapter);
	}
	@Override
	public void onBackPressed() {
		Intent intent=new Intent();
		intent.putExtra("data", strs);
		setResult(200,intent);
		finish();
		super.onBackPressed();
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return lists.size();
		}
		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String path=lists.get(position);
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=inflate.inflate(R.layout.grid_child_item, null);
				viewHolder.image=(MyImageView) convertView.findViewById(R.id.child_image);
				viewHolder.check=(CheckBox) convertView.findViewById(R.id.child_checkbox);
				convertView.setTag(viewHolder);
				viewHolder.image.setOnMeasureListener(new OnMeasureListener() {			
					@Override
					public void onMeasureSize(int width, int height) {
						viewWidth=width;
						viewHeight=height;
					}
				});
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
				viewHolder.image.setImageResource(R.drawable.friends_sends_pictures_no);
			}
			viewHolder.image.setTag(path);
			Bitmap bitmap=ImageLoader.getInstance().loadImage(path, viewWidth, viewHeight, new OnCallBackListener() {
				@Override
				public void setOnCallBackListener(Bitmap bitmap, String url) {
					ImageView image=(ImageView) mGridView.findViewWithTag(url);
					if(image!=null&&bitmap!=null){
						image.setImageBitmap(bitmap);
					}
				}
			});
			if(bitmap!=null){
				viewHolder.image.setImageBitmap(bitmap);
			}else{				
				viewHolder.image.setImageResource(R.drawable.friends_sends_pictures_no);
			}
			viewHolder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {	
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						strs.add(path);
					}else{
						if(strs.contains(path))
							strs.remove(path);
					}
				}
			});
			
			return convertView;
			
		}		
	}
	private static class ViewHolder{
		public MyImageView image;
		public CheckBox check;
	}
	
	
}
