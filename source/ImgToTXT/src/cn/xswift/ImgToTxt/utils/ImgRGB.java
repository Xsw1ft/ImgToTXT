package cn.xswift.ImgToTxt.utils;

public class ImgRGB {

	private int rgbs[][];//ȫͼRGB
	
	public ImgRGB(int[][] rgbs) {
		super();
		this.rgbs = rgbs;
	}
	public ImgRGB() {
		super();
	}
	public int[][] getRGBs() {
		return rgbs;
	}
	public void setRGBs(int[][] rgbs) {
		this.rgbs = rgbs;
	}

}
