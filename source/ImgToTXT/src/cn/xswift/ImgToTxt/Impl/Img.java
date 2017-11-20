package cn.xswift.ImgToTxt.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.xswift.ImgToTxt.I.IImg;
import cn.xswift.ImgToTxt.I.IUtils;
import cn.xswift.ImgToTxt.utils.ImgChar;
import cn.xswift.ImgToTxt.utils.ImgGray;
import cn.xswift.ImgToTxt.utils.Utils;

public class Img implements IImg{

	private IUtils utils;//工具包
	private int maxX;//图片宽
	private int maxY;//图片高
	private File file;//图片file
	
	public Img() {
		super();
		utils = new Utils();
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}


	public File getFile() {
		return file;
	}

	/**
	 * 通过File获取图片信息
	 * @throws Exception 
	 * */
	@Override
	public void setImage(File file) throws Exception{
		if(file.exists()) {
			this.file = file;
			int[] wah= utils.getImgWidthAndHeight(file);
			this.maxX = wah[0];
			this.maxY = wah[1];
		}
		else {
			throw new Exception();
		}
	}

	/**
	 * 将file输出到outputPath
	 * size: 1=原图 , 2 =1/4图 ,3=1/9图。
	 * 如果文件存在，则删除重建
	 * 不存在则直接建立
	 * */
	@Override
	public boolean imgToTXT(String outputPath,int size) {
		
		boolean status = false;
		ImgChar ic = utils.getChar(maxX, maxY, file , size);
		int maxX = this.maxX / size - size;
		int maxY = this.maxY / size - size;
		String txt = ic.charToString(maxX, maxY);//获取图片的字符形式
		
		//以HTML格式文件输出
		File output = new File(outputPath);
		if (output.exists()) {
			output.delete();
		}
		try {
			OutputStream os = new FileOutputStream(outputPath,true);
			os.write(txt.getBytes());
			os.close();
			status=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
