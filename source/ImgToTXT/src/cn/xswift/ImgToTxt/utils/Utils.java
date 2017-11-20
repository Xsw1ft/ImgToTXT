package cn.xswift.ImgToTxt.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException; 

import javax.imageio.ImageIO;

import cn.xswift.ImgToTxt.I.IUtils;

public class Utils implements IUtils{

	private BufferedImage bi = null;

	/**
	 * 字典
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
	 * 查询字典
	 * */
	private String queryChar(int index) {
		String[] dic= getDic();
		return dic[index];
	}
	
	/**
	 * 获取图片的宽和高
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

		widthandheight[0] = bi.getWidth();//取宽
		widthandheight[1] = bi.getHeight();//取高

		return widthandheight;
	}

	/**
	 * 通过图片的宽和高
	 * 获取各个像素点的RGB值(0xAARRGGBB)
	 * 封装到ImgRGB
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
	 * 通过图片的RGB
	 * 获取各个像素点的灰度值
	 * 封装到ImgGray
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
				r = rgbs.getRGBs()[i][j] & 0xff0000 >> 16;//取出R
				g = rgbs.getRGBs()[i][j] & 0xff00 >> 8;//取出G
				b = rgbs.getRGBs()[i][j] & 0xff;//取出B
				gr = ( r*30 + g*59 + b*11) / 100; //通过RGB计算灰度值
				gs[i][j] = gr; //存入数组
			}
		}
		
		/*for (int i=0 ; i<maxY-6 ; i=i+6 ) {
			for (int j=0 ; j<maxX ; j++) {
				gsmix[i/6][j] = (gs[i][j]+gs[i+1][j]+gs[i+2][j]+gs[i+3][j]+gs[i+4][j]+gs[i+5][j]) /6; //存入数组
			}
		}*/
		gray.setGrays(gs);
		return gray;
	}

	
	/**
	 * 通过图片的灰度值
	 * 获取对应的字符
	 * size:1=原图,2=1/4图,3=1/9图
	 * 封装到ImgChar
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
	 * 通过图片的灰度值
	 * 对相邻的四个像素的灰度值取平均
	 * 将图片压缩成原来的四分之一
	 * 获取对应的字符
	 * 封装到ImgChar
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
