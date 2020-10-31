package cn.winggon;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 对象工具 深度拷贝诸如此类
 *
 * Created by winggonLee on 2020/10/31
 */
public class ObjUtils {
    private ObjUtils(){}

    /**
     * 对象字段索引缓存
     */
//    private static Map<Class, Field[]> cacheList = new HashMap<>();
    private static Map<Class, Map<String,Field>> cacheMap = new HashMap<>();

    public static <T> T deepCopy(T sourceObj) {
        return (T)deepCopy(sourceObj, sourceObj.getClass());
    }

    /**
     * 深度拷贝单个对象
     *
     * 如果被拷贝对象里含有多引用情况，拷贝出来的对象会丢失这种关联
     * 比方说sourceObj对象里面有集合listA和listB, 有一对象a同时存在于listA、listB, 众所周知对listA里的a进行修改，B里面的a也会被变化
     * 被此方法深度拷贝出来的listA和listB里的"a"将会是分别独立存在的，对listA的"a"进行修改，B里面的"a"不会被变化
     *
     * @param sourceObj 被拷贝对象
     * @param targetObjClz 拷贝到目标类类型
     */
    public static <T> T deepCopy(Object sourceObj, Class<T> targetObjClz) {

        if (sourceObj instanceof Collection) {
            return (T) ListUtils.deepClone((Collection) sourceObj);
        } else if (sourceObj instanceof Map) {
            return (T) MapUtils.deepClone((Map) sourceObj);
        }

        T target;
        try {
            target = targetObjClz.newInstance();
        } catch (InstantiationException |IllegalAccessException  e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Field> s = getFieldMapping(sourceObj.getClass());
        Map<String, Field> t = getFieldMapping(targetObjClz);
        for (Field targetField : t.values()) {

            // 源对象存在跟目标对象同样名称的字段
            Field sourceField = s.get(targetField.getName());

            if (sourceField != null) {
                try {
                    Object value = sourceField.get(sourceObj);
                    targetField.set(target, getValueByValue(value));
                } catch (IllegalAccessException e) {
                    //
                }
            }
        }
        return target;
    }

    /**
     * 判定内容是基本数据类型还是引用数据类型
     * 基本数据类型直接返回即可， 引用数据类型还需要再进一步进行深度拷贝
     */
    private static Object getValueByValue(Object value){
        if (value instanceof String
                || value instanceof Integer
                || value instanceof Boolean
                || value instanceof Long
                || value instanceof Short
                || value instanceof Double
                || value instanceof Float
                || value instanceof Character
                || value instanceof Byte) {
            return value;
        }
        return deepCopy(value, value.getClass());
    }

    /**
     * 字段名称作为索引map
     */
    public static Map<String, Field> getFieldMapping(Class clz) {
        Map<String, Field> result = cacheMap.get(clz);
        if (result == null) {
            List<Field> list = new ArrayList<>();
            collectField(clz, list);
            cacheMap.put(clz, result = MapUtils.objMapping(list, Field::getName));
        }
        return result;
    }

    /**
     * 递归搜集对象的所有字段
     */
    public static void collectField(Class clz, List<Field> result) {
        // 向上追溯父对象
        if (!(clz.getSuperclass() == Object.class)) {
            collectField(clz.getSuperclass(), result);
        }
        // 到达除Object外的最上层父对象时结束递归，开始收集字段
        for (Field field : clz.getDeclaredFields()) {
            field.setAccessible(true);
            result.add(field);
        }
    }
}
