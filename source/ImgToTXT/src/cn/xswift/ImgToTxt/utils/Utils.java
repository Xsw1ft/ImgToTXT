package cn.xswift.ImgToTxt.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException; 

import javax.imageio.ImageIO;

import cn.xswift.ImgToTxt.I.IUtils;

public class Utils implements IUtils{

	private BufferedImage bi = null;

	/**
	 * �ֵ�
	 * */
	private String[] getDic() {
		String[] dic = {
				"&nbsp;",".",":","/","~",
				"o","+","r","=","j",
				"(","O","P","&","B",
				"0"};
		return dic;
	}

	/**
	 * ��ѯ�ֵ�
	 * */
	private String queryChar(int index) {
		String[] dic= getDic();
		return dic[index];
	}
	
	/**
	 * ��ȡͼƬ�Ŀ�͸�
	 * 
	 * */
	@Override
	public int[] getImgWidthAndHeight(File file) {

		int widthandheight[] = new int[2];

		try {
			bi=ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		widthandheight[0] = bi.getWidth();//ȡ��
		widthandheight[1] = bi.getHeight();//ȡ��

		return widthandheight;
	}

	/**
	 * ͨ��ͼƬ�Ŀ�͸�
	 * ��ȡ�������ص��RGBֵ(0xAARRGGBB)
	 * ��װ��ImgRGB
	 * */
	@Override
	public ImgRGB getRGBs(int maxX,int maxY,File file) {

		ImgRGB imgrgb = new ImgRGB();
		int[][] rgbs = new int [maxY][maxX];

		try {
			bi = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i=1 ; i<maxY-1 ; i++ ) {
			for (int j=1 ; j<maxX-1 ; j++) {
				rgbs[i][j] = bi.getRGB(j, i);
			}
		}

		imgrgb.setRGBs(rgbs);
		return imgrgb;
	}

	/**
	 * ͨ��ͼƬ��RGB
	 * ��ȡ�������ص�ĻҶ�ֵ
	 * ��װ��ImgGray
	 * */
	@Override
	public ImgGray getGray(int maxX, int maxY, File file) {

		ImgRGB rgbs = getRGBs(maxX, maxY, file);
		ImgGray gray = new ImgGray();
		int gs[][] = new int[maxY][maxX];
		/*int gsmix[][] = new int[maxY/6][maxX];*/
		int r=0 , g=0 , b=0, gr=0;

		for (int i=0 ; i<maxY ; i++ ) {
			for (int j=0 ; j<maxX ; j++) {
				// 0xAARRGGBB 
				r = rgbs.getRGBs()[i][j] & 0xff0000 >> 16;//ȡ��R
				g = rgbs.getRGBs()[i][j] & 0xff00 >> 8;//ȡ��G
				b = rgbs.getRGBs()[i][j] & 0xff;//ȡ��B
				gr = ( r*30 + g*59 + b*11) / 100; //ͨ��RGB����Ҷ�ֵ
				gs[i][j] = gr; //��������
			}
		}
		
		/*for (int i=0 ; i<maxY-6 ; i=i+6 ) {
			for (int j=0 ; j<maxX ; j++) {
				gsmix[i/6][j] = (gs[i][j]+gs[i+1][j]+gs[i+2][j]+gs[i+3][j]+gs[i+4][j]+gs[i+5][j]) /6; //��������
			}
		}*/
		gray.setGrays(gs);
		return gray;
	}

	
	/**
	 * ͨ��ͼƬ�ĻҶ�ֵ
	 * ��ȡ��Ӧ���ַ�
	 * size:1=ԭͼ,2=1/4ͼ,3=1/9ͼ
	 * ��װ��ImgChar
	 * */
	@Override
	public ImgChar getChar(int maxX, int maxY, File file, int size) {
		ImgGray gray = null;
		
		switch (size) {
		case 1:
			gray = getGray(maxX, maxY, file);
			break;
		case 2:
			gray = getGrayByOneFourth(maxX, maxY, file);
			maxX = maxX /2;
			maxY = maxY /2;
			break;
		case 3:
			gray = getGrayByOneNinth(maxX, maxY, file);
			maxX = maxX /3;
			maxY = maxY /3;
			break;
		default:
			gray = getGray(maxX, maxY, file);
			break;
		}
		String chars[][] = new String[maxY][maxX];
		int index = 0;
		for (int i=0 ; i<maxY ; i++ ) {
			for (int j=0 ; j<maxX ; j++) {
				index = gray.getGrays()[i][j] / 16;
				chars[i][j] = queryChar(index);
			}
		}
		return new ImgChar(chars); 
	}

	
	/**
	 * ͨ��ͼƬ�ĻҶ�ֵ
	 * �����ڵ��ĸ����صĻҶ�ֵȡƽ��
	 * ��ͼƬѹ����ԭ�����ķ�֮һ
	 * ��ȡ��Ӧ���ַ�
	 * ��װ��ImgChar
	 * */
	
	/*public ImgChar getCharByOneFourth(int maxX, int maxY, File file) {
		ImgGray oldgray = getGray(maxX, maxY, file);
		ImgGray newgray = getGrayByOneFourth(oldgray, maxX, maxY);
		
		
		char chars[][] = new char[(maxX-(maxX%2))/2][(maxY-(maxY%2))/2];
		
		int index = 0;
		for (int i=0 ; i<maxX ; i++ ) {
			for (int j=0 ; j<maxY ; j++) {
				index = newgray.getGrays()[i][j] / 16;
				chars[i][j] = queryChar(index);
			}
		}
		return new ImgChar(chars);
	}*/


	private ImgGray getGrayByOneFourth(int maxX,int maxY,File file) {
		ImgGray igray = getGray(maxX, maxY, file);
		int[][] oldgrays = igray.getGrays();
		int[][] newgrays = new int[maxY/2][maxX/2];
		int row=0;
		int col=0;
		if(maxY%2==1) {
			maxY = maxY - 1;
		}
		if(maxX%2==1) {
			maxX = maxX - 1;
		}
		for (int i=0;i<maxY;i+=2) {
			col=0;
			for(int j=0;j<maxX;j+=2) {
				newgrays[row][col] = ( oldgrays[i][j] + oldgrays[i][j+1] + oldgrays[i+1][j] + oldgrays[i+1][j+1] ) / 4;
				col++;
			}
			row++;
		}
		return new ImgGray(newgrays);
	}
	
	private ImgGray getGrayByOneNinth(int maxX,int maxY,File file) {
		ImgGray igray = getGray(maxX, maxY, file);
		int[][] oldgrays = igray.getGrays();
		int[][] newgrays = new int[maxY/3][maxX/3];
		int row=0;
		int col=0;
		
		maxY = maxY/3;
		maxX = maxX/3;
		
		for (int i=0;i<maxY;i=i+3) {
			col=0;
			for(int j=0;j<maxX;j=j+3) {
				newgrays[row][col] = ( oldgrays[i][j] + oldgrays[i][j+1] + oldgrays[i][j+2] +oldgrays[i+1][j] + oldgrays[i+1][j+1] + oldgrays[i+1][j+2] + oldgrays[i+2][j] +oldgrays[i+2][j+1] + oldgrays[i+2][j+2]) / 9;
				col++;
			}
			row++;
		}
		return new ImgGray(newgrays);
	}
}
