package com.pj.regulation.impl;

import com.pj.regulation.Regulation;

public class RegulationD extends Regulation {

	public RegulationD(){
		/* 组合的大小顺序，前面两位是PorkNum码，最后一位是权值 */
		/**
		 * 规则D，虎头配杂9大于对子小于至尊。也就是第二大。其实就是将其移动到第二大。
		 * 对应表的PorkNum码（7，3）
		 */
		super(new int[][] { { 6, 3, 0 }, { 7, 2, 0 },{ 16, 4, 0 }, 
				{ 8, 0, 2 }, { 16, 9, 2 }, { 10, 3, 2 }, { 11, 0, 6 },
				{ 12, 7, 7 }, { 13, 1, 9 }, { 13, 5, 9 }, { 14, 5, 11 },
				{ 14, 1, 11 }, { 13, 2, 13 }, { 13, 12, 13 }, { 14, 12, 15 },
				{ 14, 2, 15 }, { 14, 3, 16 }, { 0, 0, 17 }, { 1, 1, 18 },
				{ 2, 2, 19 }, { 3, 3, 20 }, { 4, 4, 24 }, { 5, 5, 24 },
				{ 6, 6, 24 }, { 7, 7, 24 }, { 8, 8, 27 }, { 9, 9, 27 },
				{ 10, 10, 27 }, { 11, 11, 28 }, { 12, 12, 29 }, { 13, 13, 30 },
				{ 14, 14, 31 },{ 7, 3, 32 }, { 16, 15, 33 } });
	}
}
