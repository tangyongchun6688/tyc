package com.ultrapower.ci.common.utils;

import java.util.Random;

/**
 * @time 2018-03-15
 * @author tangyongchun
 * @description 生成六位随机数
 *
 */
public class RandomSix {

	public static String randomSix() {
		String rvcNameArr[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "g", "k", "l", "m", "n", "o", "p", "q",
				"r", "s", "t", "u", "v", "w", "x", "y", "z" };

		Random r = new Random();
		StringBuffer randomSix = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			int nextInt = r.nextInt(rvcNameArr.length);
			randomSix.append(rvcNameArr[nextInt]);
		}

		return randomSix.toString();
	}
}
