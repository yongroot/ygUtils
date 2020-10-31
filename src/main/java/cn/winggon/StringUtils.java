package cn.winggon;

import java.util.Collection;

/**
 * Created by winggonLee on 2020/10/30
 */
public class StringUtils {
    private StringUtils() {
    }

    /**
     * 拼接String
     * 适用于集合的元素长度都相等的情况
     */
    public static String join(CharSequence delimiter, Collection<? extends CharSequence> list) {
        if (list == null || list.size() == 0 || delimiter == null) {
            return "";
        }
        StringBuilder sb = null;
        for (CharSequence cs : list) {
            if (sb == null) {
                // 把第一个元素作为等长拼接字符串的初始化容量参考
                int capacity = (cs.length() + delimiter.length()) * list.size();
                sb = new StringBuilder(capacity);
            }
            sb.append(cs).append(delimiter);
        }
        return sb.substring(0, sb.length() - delimiter.length() - 1);
    }

    /**
     * 拼接String
     * 适用于集合的元素长度都相等的情况
     */
    public static String join(CharSequence delimiter, CharSequence... array) {
        if (array == null || array.length == 0 || delimiter == null) {
            return "";
        } else if (array.length == 1) {
            return String.valueOf(array[0]);
        }
        // 取中位元素作为参考长度估算等长拼接StringBuilder的初始化容量
        int capacity = (array[(array.length / 2) + 1].length() + delimiter.length()) * array.length - delimiter.length();
        StringBuilder sb = new StringBuilder(capacity);

        int i = 0;
        do {
            sb.append(array[i++]);
            if (i < array.length) {
                sb.append(delimiter);
            }
        } while (i < array.length);
        return sb.toString();
    }
}
