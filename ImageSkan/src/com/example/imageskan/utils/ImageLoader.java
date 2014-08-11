package com.example.imageskan.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.graphics.BitmapFactory;

public class ImageLoader {
	private static LruCache<String, Bitmap> mMermoryCachre;
	private ExecutorService mEService;
	private static ImageLoader mLoader;
	private ImageLoader(){};
	public static ImageLoader getInstance(){
		synchronized (ImageLoader.class) {
			if(mLoader==null){
				mLoader=new ImageLoader();
			}
			int maxSize=(int) Runtime.getRuntime().maxMemory()/4;
			mMermoryCachre=new LruCache<String, Bitmap>(maxSize){
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes()*value.getHeight();
				}
				
			};
			return	mLoader;
			
		}
	}
	public void getThreadPool(){
		mEService=Executors.newFixedThreadPool(2);
	}
	public Bitmap loadImage(final String path,final int width,final int height){
		Bitmap bitmap=getFormCache(path);
		if(bitmap==null){
			mEService.execute(new Thread(){
				@Override
				public void run() {
					Bitmap bitmap=getThumbImage(path, width, height);
					Message msg=new Message();
					msg.obj=bitmap;
					
					
				}
				
			});
		}
		return null;
	}
	private Bitmap getFormCache(String path) {
		return mMermoryCachre.get(path);
	}
	public void cancleTask(){
		if(mEService!=null){
			mEService.shutdown();
		}
	}
	
	private Bitmap getThumbImage(String path,int width,int height){
		int scale=1;
		BitmapFactory.Options option=new BitmapFactory.Options();
		option.inJustDecodeBounds=true;
		Bitmap bitmap=BitmapFactory.decodeFile(path, option);
		option.inSampleSize=getScale(option,width,height);
		option.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(path, option);
		
	}
	private int getScale(Options options, int viewWidth, int viewHeight) {
		int inSampleSize = 1;  
        if(viewWidth == 0 || viewWidth == 0){  
            return inSampleSize;  
        }  
        int bitmapWidth = options.outWidth;  
        int bitmapHeight = options.outHeight;  
        if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){  
            int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);  
            int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);  
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;  
        }  
        return inSampleSize;  
    }  

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
