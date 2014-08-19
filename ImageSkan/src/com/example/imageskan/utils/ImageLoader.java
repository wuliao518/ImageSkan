package com.example.imageskan.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.graphics.BitmapFactory;

public class ImageLoader {
	private LruCache<String, Bitmap> mMermoryCachre;
	private ExecutorService mEService;
	private static ImageLoader mLoader;
	private ImageLoader(){
		int maxSize=(int) Runtime.getRuntime().maxMemory()/4;
		mMermoryCachre=new LruCache<String, Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}		
		};
		getThreadPool();	
	};
	public static ImageLoader getInstance(){
		synchronized (ImageLoader.class) {
			if(mLoader==null){
				mLoader=new ImageLoader();
			}
			return	mLoader;		
		}
	}
	private void getThreadPool(){
		mEService=Executors.newFixedThreadPool(2);
	}
	public Bitmap loadImage(final String path,final int width,final int height,final OnCallBackListener listener){
		Bitmap bitmap=getFormCache(path);
		final Handler handle=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Bitmap bitmap=(Bitmap) msg.obj;
				if(listener!=null){					
					listener.setOnCallBackListener(bitmap,path);
				}
				super.handleMessage(msg);
			}
		};
		if(bitmap==null){	
			mEService.execute(new Thread(){
				@Override
				public void run() {
					Bitmap bitmap=getThumbImage(path, width, height);
					Message msg=new Message();
					msg.obj=bitmap;
					handle.sendMessage(msg);
					addtoCache(path,bitmap);
				}
			});
			return null;
		}else{
			return bitmap;
		}

	}
	private Bitmap getFormCache(String path) {
		return mMermoryCachre.get(path);
	}
	public void cancleTask(){
		if(mEService!=null){
			mEService.shutdown();
		}
	}
	private void addtoCache(String path,Bitmap bitmap){
		if(bitmap!=null){			
			mMermoryCachre.put(path, bitmap);
		}
	}
	private Bitmap getThumbImage(String path,int width,int height){
		BitmapFactory.Options option=new BitmapFactory.Options();
		option.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path, option);
		option.inSampleSize=getScale(option,width,height);
		option.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(path, option);
		
	}
	private int getScale(Options options, int viewWidth, int viewHeight) {
		int inSampleSize = 1;  
        if(viewWidth == 0){  
            return inSampleSize;  
        }  
        int bitmapWidth = options.outWidth;

        int bitmapHeight = options.outHeight;  
        if(bitmapWidth > viewWidth){  
            int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);  
            int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);  
            inSampleSize = widthScale< heightScale ? widthScale : heightScale;  
        }
        return inSampleSize;  
    }  

	public interface OnCallBackListener{
		public void setOnCallBackListener(Bitmap bitmap,String url);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
