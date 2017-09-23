package org.zagelnews.dtos;


public class ColorCodeDto extends ZagelnewsDto{
	private int colorKey;
	private int bluePart;
	private String colorAdesc;
	private int colorCode;
	private String colorEdesc;
	private int greenPart;
	private int redPart;

	public ColorCodeDto() {
	}

	public int getColorKey() {
		return this.colorKey;
	}

	public void setColorKey(int colorKey) {
		this.colorKey = colorKey;
	}

	public int getBluePart() {
		return this.bluePart;
	}

	public void setBluePart(int bluePart) {
		this.bluePart = bluePart;
	}

	public String getColorAdesc() {
		return this.colorAdesc;
	}

	public void setColorAdesc(String colorAdesc) {
		this.colorAdesc = colorAdesc;
	}

	public int getColorCode() {
		return this.colorCode;
	}

	public void setColorCode(int colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorEdesc() {
		return this.colorEdesc;
	}

	public void setColorEdesc(String colorEdesc) {
		this.colorEdesc = colorEdesc;
	}

	public int getGreenPart() {
		return this.greenPart;
	}

	public void setGreenPart(int greenPart) {
		this.greenPart = greenPart;
	}

	public int getRedPart() {
		return this.redPart;
	}

	public void setRedPart(int redPart) {
		this.redPart = redPart;
	}

}