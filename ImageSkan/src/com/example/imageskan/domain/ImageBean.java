package com.example.imageskan.domain;

public class ImageBean {
	//第一张图片路径
	private String firstImagePath;
	//文件夹路径
	private String folderPath;
	//图片数量
	private int count;
	public String getFirstImagePath() {
		return firstImagePath;
	}
	public void setFirstImagePath(String firstImagePath) {
		this.firstImagePath = firstImagePath;
	}
	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
