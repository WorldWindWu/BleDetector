package com.zhijiatech.bledetector.util;

public class DescriptionUtil {
	private static final int PM_LEVEL_1 = 12;
	private static final int PM_LEVEL_2 = 25;
	private static final int PM_LEVEL_3 = 55;
	private static final int PM_LEVEL_4 = 150;
	private static final int PM_LEVEL_5 = 250;

	private static final String PM_DESCRIPTION_1 = "好";
	private static final String PM_DESCRIPTION_2 = "中等";
	private static final String PM_DESCRIPTION_3 = "敏感人群有害";
	private static final String PM_DESCRIPTION_4 = "不健康";
	private static final String PM_DESCRIPTION_5 = "非常不健康";
	private static final String PM_DESCRIPTION_6 = "有毒害";

	private static final int CO2_LEVEL_1 = 1000;
	private static final int CO2_LEVEL_2 = 1500;

	private static final String CO2_DESCRIPTION_1 = "空气清新";
	private static final String CO2_DESCRIPTION_2 = "中度缺氧";
	private static final String CO2_DESCRIPTION_3 = "重度缺氧";

	public static String despPm25(int value) {
		if (value <= PM_LEVEL_1) {
			return PM_DESCRIPTION_1;
		}else if (value <= PM_LEVEL_2) {
			return PM_DESCRIPTION_2;
		}else if (value <= PM_LEVEL_3) {
			return PM_DESCRIPTION_3;
		}else if (value <= PM_LEVEL_4) {
			return PM_DESCRIPTION_4;
		}else if (value <= PM_LEVEL_4) {
			return PM_DESCRIPTION_5;
		}else{
			return PM_DESCRIPTION_6;
		}
	}

	public static String despCO2(int value) {
		if (value <= CO2_LEVEL_1) {
			return CO2_DESCRIPTION_1;
		}else if (value <= CO2_LEVEL_2) {
			return CO2_DESCRIPTION_2;
		}else{
			return CO2_DESCRIPTION_3;
		}
	}
}
