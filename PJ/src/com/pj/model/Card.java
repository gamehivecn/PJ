package com.pj.model;

public class Card {

	/** �žŵ�����*/
	private String name;
	/** ����*/
	private int dotNum;
	/** porkNum��*/
	private int porkNum;
	/** �Ӽ����������*/
	private int inputNum;
	/**ÿ���Ƶļ���	 */
	private int levelNum;
	
	public Card(){}
	public Card(String name,int dotNum,int porkNum,int inputNum,int levelNum){
		this.name = name;
		this.dotNum = dotNum;
		this.porkNum = porkNum;
		this.inputNum = inputNum;
		this.levelNum = levelNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDotNum() {
		return dotNum;
	}
	public void setDotNum(int dotNum) {
		this.dotNum = dotNum;
	}
	public int getPorkNum() {
		return porkNum;
	}
	public void setPorkNum(int porkNum) {
		this.porkNum = porkNum;
	}
	public int getInputNum() {
		return inputNum;
	}
	public void setInputNum(int inputNum) {
		this.inputNum = inputNum;
	}
	public int getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}
}
