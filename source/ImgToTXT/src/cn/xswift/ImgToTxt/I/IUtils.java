package cn.xswift.ImgToTxt.I;

import java.io.File;
import java.util.Map;

import cn.xswift.ImgToTxt.utils.ImgChar;
import cn.xswift.ImgToTxt.utils.ImgGray;
import cn.xswift.ImgToTxt.utils.ImgRGB;

public interface IUtils {
	
	public int[] getImgWidthAndHeight(File file);//��ȡͼ��Ŀ�ߣ�Returns��0=��1=�ߡ�
	public ImgRGB getRGBs(int maxx,int maxy,File file);
	public ImgGray getGray(int maxx,int maxy,File file);
	public ImgChar getChar(int maxx,int maxy,File file,int size);
}
