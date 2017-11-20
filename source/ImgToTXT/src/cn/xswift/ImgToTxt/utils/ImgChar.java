package cn.xswift.ImgToTxt.utils;

public class ImgChar {
	
	private String chars[][];

	public String[][] getChars() {
		return chars;
	}

	public ImgChar(String[][] chars) {
		super();
		this.chars = chars;
	}

	public ImgChar() {
		super();
	}

	public void setChars(String[][] chars) {
		this.chars = chars;
	}
	
	@SuppressWarnings("null")
	public String charToString(int maxX,int maxY) {
		
		StringBuffer txt = new StringBuffer();
		txt.append("<!DOCTYPE html>");
		txt.append("<html><head>");
		txt.append("<meta charset=\"utf-8\">");
		txt.append("</head><body>");
		
		if(chars.length!=0) {
			for (int i = 0; i < maxY; i++) {
				for (int j = 0; j < maxX; j++) {
					txt.append(chars[i][j]);
				}
				txt.append("<br/>");
			}
		}
		
		txt.append("</body>");
		return txt.toString();
	}
}
