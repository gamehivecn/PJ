package com.pj.model;

public class TzCard {

	/** porkNum��*/
	private int porkNum;
	/** �Ӽ����������*/
	private int inputNum;

	
	public TzCard(){}
	public TzCard(int porkNum,int inputNum){
		this.porkNum = porkNum;
		this.inputNum = inputNum;
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

}
