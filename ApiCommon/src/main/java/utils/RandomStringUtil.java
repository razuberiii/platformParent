package utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomStringUtil {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 获取随机字符串
     * @param sampleString 样本字符串
     * @param length 生成的随机字符串长度
     * @return 返回随机字符串
     */
    public static String randomString(String sampleString, int length) {
        if (sampleString == null || sampleString.length() == 0) {
            return "";
        }
        if (length < 1) {
            length = 1;
        }
        final StringBuilder sb = new StringBuilder(length);
        int baseLength = sampleString.length();
        while (sb.length() < length) {
            //此处用ThreadLocalRandom 不用Random的感兴趣的同学可以看看这俩的区别
            //主要区别在于多线程高并发环境下 ThreadLocalRandom比Random生成随机数的速度快
            int number = ThreadLocalRandom.current().nextInt(baseLength);
            sb.append(sampleString.charAt(number));
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        System.out.println(randomString(CHARS,6));
    }
}
