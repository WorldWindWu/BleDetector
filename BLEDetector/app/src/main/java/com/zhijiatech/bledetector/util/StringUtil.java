package com.zhijiatech.bledetector.util;

public class StringUtil
{
    private static final int BUILD_VERSION_JELLYBEAN = 17;

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isStringEmpty(String str)
    {
        if (null == str || "".equals(str))
        {
            return true;
        }
        return false;
    }
}
