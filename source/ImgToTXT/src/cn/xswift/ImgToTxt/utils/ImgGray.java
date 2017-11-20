package cn.xswift.ImgToTxt.utils;

public class ImgGray {
	
	private int grays[][];

	public ImgGray(int[][] grays) {
		super();
		this.grays = grays;
	}

	public ImgGray() {
		super();
	}

	public int[][] getGrays() {
		return grays;
	}

	public void setGrays(int[][] grays) {
		this.grays = grays;
	}
	
}
