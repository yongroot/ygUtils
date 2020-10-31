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
     * @param sourceObj 被拷贝对象
     * @param targetObj 拷贝到目标类类型
     */
    public static <T> T deepCopy(Object sourceObj, Class<T> targetObj) {
        T t;
        try {
            t = targetObj.newInstance();
        } catch (InstantiationException |IllegalAccessException  e) {
            e.printStackTrace();
            return null;
        }
        Map<String, Field> sourceObjFieldMapping = getFieldMapping(sourceObj.getClass());
        Map<String, Field> targetObjFieldMapping = getFieldMapping(targetObj);
        for (Field targetField : targetObjFieldMapping.values()) {
            Field sourceField = sourceObjFieldMapping.get(targetField.getName());
            if (sourceField != null) {
                try {
                    Object value = sourceField.get(sourceObj);
                    targetField.set(t, getValueByValue(value));
                } catch (IllegalAccessException e) {
                    //
                }
            }
        }
        return t;
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
                || value instanceof Byte
                || value instanceof Character
                || value instanceof Short
                || value instanceof Float
                || value instanceof Double) {
            return value;
        } else if (value instanceof Collection) {
            return ListUtils.deepClone((Collection) value);
        } else if (value instanceof Map) {
            return MapUtils.deepClone((Map) value);
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
        if (!(clz instanceof Object)) {
            collectField(clz.getSuperclass(), result);
        }
        for (Field field : clz.getDeclaredFields()) {
            field.setAccessible(true);
            result.add(field);
        }
    }
}
