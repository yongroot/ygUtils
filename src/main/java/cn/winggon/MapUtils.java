package cn.winggon;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by winggonLee on 2020/10/30
 */
public class MapUtils {
    private MapUtils() {}

    /**
     * 深度拷贝map
     */
    public static <K, V> Map<K, V> deepClone(Map<K, V> sourceMap) {
        if (isEmpty(sourceMap)) {
            return new HashMap<>(0);
        }
        Map<K, V> result = new HashMap<>(guessSize(sourceMap));
        sourceMap.forEach(((k, v) -> result.put(k, ObjUtils.deepCopy(v))));
        return result;
    }

//    /**
//     * 对map类型集合进行索引构建
//     *
//     * @param list
//     * @param keyMapper Map构建的key行为
//     */
//    public static <V, K, k>
//    Map<K, Map<k, V>> mapMapping(Collection<Map<k, V>> list, Function<Map<? super k, V>, ? extends K> keyMapper) {
//        if (ListUtils.isEmpty(list)) {
//            return new HashMap<>(0);
//        }
//        Map<K, Map<k, V>> result = new HashMap<>(guessSize(list));
//        for (Map<k, V> map : list) {
//            if (map != null) {
//                K key = keyMapper.apply(map);
//                result.put(key, map);
//            }
//        }
//        return result;
//    }

    public static <T, K>
    Map<K, T> objMapping(Collection<T> list, Function<T, K> keyMpper) {
        if (ListUtils.isEmpty(list)) {
            return new HashMap<>(0);
        }
        Map<K, T> result = new HashMap<>(guessSize(list));
        for (T t : list) {
            if (t != null) {
                result.put(keyMpper.apply(t), t);
            }
        }
        return result;
    }

    /**
     * 根据来源估计HashMap的初始化大小值
     */
    public static int guessSize(Object source) {
        int flag;
        if (source instanceof Collection) {
            flag = ((Collection) source).size();
        } else if (source instanceof Map) {
            flag = ((Map) source).size();
        } else if (source instanceof Integer) {
            flag = (int) source;
        } else {
            return 16;
        }
        return (int) (flag / .75f) + 1;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
