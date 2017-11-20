package cn.xswift.ImgToTxt.example;

import java.io.File;

import cn.xswift.ImgToTxt.Impl.Img;

public class Example {
	public static void main(String[] args) {
		File file = new File("E:\\a.jpg");
		Img i = new Img();
		try {
			i.setImage(file);
			i.imgToTXT("E:\\1.html",1);
		} catch (Exception e) {
			System.out.println("Img input error!");
		}
	}
}
