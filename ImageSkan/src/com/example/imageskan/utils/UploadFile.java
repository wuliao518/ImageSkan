package com.example.imageskan.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.imageskan.config.Config;

/**
 * @author wuliao
 * 文件上传类
 * @version 1.0
 *
 */
public class UploadFile {
	public static UploadFile mUploadFile;
	private ExecutorService mThreadPool;
	private OnUploadListener listener;
	private static final String CHARSET = "utf-8";
	private Handler handle=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				JSONObject mjsonoJsonObject=new JSONObject((String) msg.obj);
				int code=mjsonoJsonObject.getInt("status");
				String message=mjsonoJsonObject.getString("message");
				listener.callBack(message);
				System.out.println("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		
	};
	private UploadFile(){
		mThreadPool=Executors.newFixedThreadPool(1);
	}
	public synchronized static UploadFile getInstances(){
		if(mUploadFile==null){
			mUploadFile=new UploadFile();
		}
		return mUploadFile;
	}
	public void uploadFile(final File file,OnUploadListener listener){
		if(mThreadPool==null){
			mThreadPool=Executors.newFixedThreadPool(1);
		}
		this.listener=listener;
		mThreadPool.execute(new Thread(){
			@Override
			public void run() {
				super.run();
				int res=0;
				String result = null;
				String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
				String PREFIX = "--", LINE_END = "\r\n";
				String CONTENT_TYPE = "multipart/form-data"; // 内容类型
				try {
					URL url = new URL(Config.UploadURI);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(Config.TIME_OUT);
					conn.setConnectTimeout(Config.TIME_OUT);
					conn.setDoInput(true); // 允许输入流
					conn.setDoOutput(true); // 允许输出流
					conn.setUseCaches(false); // 不允许使用缓存
					conn.setRequestMethod("POST"); // 请求方式
					conn.setRequestProperty("Charset", CHARSET); // 设置编码
					conn.setRequestProperty("connection", "keep-alive");
					conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);
					if (file != null) {
						/**
						 * 当文件不为空时执行上传
						 */
						DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
						StringBuffer sb = new StringBuffer();
						sb.append(PREFIX);
						sb.append(BOUNDARY);
						sb.append(LINE_END);
						/**
						 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
						 * filename是文件的名字，包含后缀名
						 */

						sb.append("Content-Disposition: form-data; name=\"upfile\"; filename=\""
								+ file.getName() + "\"" + LINE_END);
						sb.append("Content-Type: application/octet-stream; charset="
								+ CHARSET + LINE_END);
						sb.append(LINE_END);
						dos.write(sb.toString().getBytes());
						InputStream is = new FileInputStream(file);
						byte[] bytes = new byte[1024];
						int len = 0;
						while ((len = is.read(bytes)) != -1) {
							dos.write(bytes, 0, len);
						}
						is.close();
						dos.write(LINE_END.getBytes());
						byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
								.getBytes();
						
						dos.write(end_data);
						dos.flush();
						/**
						 * 获取响应码 200=成功 当响应成功，获取响应的流
						 */
						 res = conn.getResponseCode();
						if (res == 200) {

							InputStream input = conn.getInputStream();
							StringBuffer sb1 = new StringBuffer();
							int ss;
							while ((ss = input.read()) != -1) {
								sb1.append((char) ss);
							}
							result = sb1.toString();
							Message msg=new Message();
							msg.obj=result;
							handle.sendMessage(msg);
						} else {
							Log.e("UploadFile", "request error");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	public void cancle(){
		if(mThreadPool!=null){
			mThreadPool.shutdown();
			mThreadPool=null;
		}
	}
	public interface OnUploadListener{
		public void callBack(String str);
	}
	
}
