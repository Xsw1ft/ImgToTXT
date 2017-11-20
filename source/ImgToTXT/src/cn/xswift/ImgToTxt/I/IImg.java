package cn.xswift.ImgToTxt.I;

import java.io.File;

public interface IImg {
	
	public void setImage(File file) throws Exception ;
	public boolean imgToTXT(String outputPath,int size); 
	
}
