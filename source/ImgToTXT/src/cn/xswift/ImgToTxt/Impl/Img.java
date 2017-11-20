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

	private IUtils utils;//���߰�
	private int maxX;//ͼƬ��
	private int maxY;//ͼƬ��
	private File file;//ͼƬfile
	
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
	 * ͨ��File��ȡͼƬ��Ϣ
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
	 * ��file�����outputPath
	 * size: 1=ԭͼ , 2 =1/4ͼ ,3=1/9ͼ��
	 * ����ļ����ڣ���ɾ���ؽ�
	 * ��������ֱ�ӽ���
	 * */
	@Override
	public boolean imgToTXT(String outputPath,int size) {
		
		boolean status = false;
		ImgChar ic = utils.getChar(maxX, maxY, file , size);
		int maxX = this.maxX / size - size;
		int maxY = this.maxY / size - size;
		String txt = ic.charToString(maxX, maxY);//��ȡͼƬ���ַ���ʽ
		
		//��HTML��ʽ�ļ����
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
