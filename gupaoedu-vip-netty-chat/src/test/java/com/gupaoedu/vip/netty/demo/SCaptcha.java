package com.gupaoedu.vip.netty.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;


public class SCaptcha {

	private int width = 120;

	private int height = 40;

	private int codeCount = 6;

	private int lineCount = 50;

	private String code = null;
	
	

	private BufferedImage buffImg = null;

	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };


	private Random random = new Random();

	public SCaptcha() {
		this.createCode();
	}


	public SCaptcha(int width, int height) {
		this.width = width;
		this.height = height;
		this.createCode();
	}


	public SCaptcha(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		this.createCode();
	}

	public void createCode() {
		int codeX = 0;
		int fontHeight = 0;
		fontHeight = height - 5;
		codeX = width / (codeCount + 3);


		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();


		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);


		ImgFontByte imgFont = new ImgFontByte();
		Font font = imgFont.getFont(fontHeight);		
		g.setFont(font);


		for (int i = 0; i < lineCount; i++) {
			int xs = getRandomNumber(width);
			int ys = getRandomNumber(height);
			int xe = xs + getRandomNumber(width / 8);
			int ye = ys + getRandomNumber(height / 8);
			g.setColor(getRandomColor());
			g.drawLine(xs, ys, xe, ye);
		}

		StringBuffer randomCode = new StringBuffer();

		for (int i = 0; i < codeCount; i++) {
			String strRand = String.valueOf(codeSequence[random
					.nextInt(codeSequence.length)]);

			g.setColor(getRandomColor());

			g.drawString(strRand, (i + 1) * codeX,
					getRandomNumber(height / 2) + 25);
			randomCode.append(strRand);
		}
		code = randomCode.toString();
	}


	private Color getRandomColor() {
		int r = getRandomNumber(255);
		int g = getRandomNumber(255);
		int b = getRandomNumber(255);
		return new Color(r, g, b);
	}


	private int getRandomNumber(int number) {
		return random.nextInt(number);
	}

	public void write(String path) throws IOException {
		OutputStream sos = new FileOutputStream(path);
		this.write(sos);
	}

	public void write(OutputStream sos) throws IOException {
		ImageIO.write(buffImg, "png", sos);
		sos.close();
	}

	public BufferedImage getBuffImg() {
		return buffImg;
	}

	public String getCode() {
		return code;
	}

	
}