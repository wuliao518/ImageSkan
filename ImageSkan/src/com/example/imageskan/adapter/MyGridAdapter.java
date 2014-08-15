package com.example.imageskan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imageskan.R;
import com.example.imageskan.domain.ImageBean;
import com.example.imageskan.utils.ImageLoader;
import com.example.imageskan.utils.ImageLoader.OnCallBackListener;
import com.example.imageskan.view.MyImageView;
import com.example.imageskan.view.MyImageView.OnMeasureListener;

public class MyGridAdapter extends BaseAdapter {
	private List<ImageBean> beans;
	private GridView mGridView;
	int width=0,height=0;
	LayoutInflater inflater;
	public MyGridAdapter(Context context,List<ImageBean> beans,GridView gridView) {
		this.beans = beans;
		this.mGridView=gridView;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageBean bean=beans.get(position);
		String path=bean.getTopImagePath();
		final ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_gridview, null);
			viewHolder.image=(MyImageView) convertView.findViewById(R.id.group_image);
			viewHolder.tv1=(TextView) convertView.findViewById(R.id.group_count);
			viewHolder.tv2=(TextView) convertView.findViewById(R.id.group_title);
			viewHolder.image.setOnMeasureListener(new OnMeasureListener() {           
				@Override  
                public void onMeasureSize(int width, int height) {  
					MyGridAdapter.this.width=width; 
					MyGridAdapter.this.height=height;
                }  
            });  
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
			viewHolder.image.setImageResource(R.drawable.friends_sends_pictures_no);
		}	
		
		viewHolder.image.setTag(path);

		Bitmap bitmap=ImageLoader.getInstance().loadImage(path, width,height, new OnCallBackListener() {

			@Override
			public void setOnCallBackListener(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					mImageView.setImageBitmap(bitmap);
				}
			}
		});
		if(bitmap!=null){
			viewHolder.image.setImageBitmap(bitmap);
		}else{
			viewHolder.image.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.tv1.setText(bean.getImageCounts()+"");
		viewHolder.tv2.setText(bean.getFolderName());
		return convertView;
	}
	public static class ViewHolder{
		public MyImageView image;
		public TextView tv1,tv2;
	}
}
