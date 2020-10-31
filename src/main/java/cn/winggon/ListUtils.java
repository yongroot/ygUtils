package cn.winggon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by winggonLee on 2020/10/30
 */
public class ListUtils {
    private ListUtils(){}

    /**
     * 集合元素转换到目标类型
     */
    public static <T> List<T> cloneTo(Collection<?> list, Class<T> clazz) {
        if (isEmpty(list) || clazz == null) {
            return new ArrayList<>(0);
        }
        List<T> result = new ArrayList<>(size(list));
        for (Object o : list) {
            result.add(ObjUtils.deepCopy(o, clazz));
        }
        return result;
    }

    /**
     * 审核拷贝集合内所有元素
     */
    public static <T> List<T> deepClone(Collection<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<T> result = new ArrayList<>(size(list));
        for (T t : list) {
            result.add(ObjUtils.deepCopy(t));
        }
        return result;
    }

    public static int size(Collection<?> list) {
        return list == null ? 0 : list.size();
    }

    public static boolean isEmpty(Collection<?> list) {
        return size(list) == 0;
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return size(list) > 0;
    }
}
