package cn.winggon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 对象工具 深度拷贝诸如此类
 * <p>
 * Created by winggonLee on 2020/10/31
 */
public class ObjUtils {
    private ObjUtils() {
    }

    /**
     * 拷贝自身
     */
    public static <T> T deepClone(T sourceObj) {
        return deepClone(sourceObj, (Class<T>) sourceObj.getClass());
    }

    /**
     * 拷贝成目标类
     */
    public static <T> T deepClone(Object sourceObj, Class<T> targetObjClz) {
        T t = null;
        try {
            t = targetObjClz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        deepCopy(sourceObj, t);
        return t;
    }

    /**
     * 深度拷贝对象
     * <p>
     * 如果被拷贝对象里含有多引用情况，拷贝出来的对象会丢失这种关联
     * 比方说sourceObj对象里面有集合listA和listB, 有一对象a同时存在于listA、listB, 众所周知对listA里的a进行修改，B里面的a也会被变化
     * 被此方法深度拷贝出来的listA和listB里的"a"将会是分别独立存在的，对listA的"a"进行修改，B里面的"a"不会被变化
     *
     * @param source 被拷贝对象
     * @param target 拷贝到对象
     */
    public static void deepCopy(Object source, Object target) {
        if (target instanceof Collection) {
            ((Collection) target).addAll(ListUtils.deepClone((Collection) source));
        } else if (target instanceof Map) {
            ((Map) target).putAll(MapUtils.deepClone((Map) source));
        } else if (target instanceof StringBuilder) {
            ((StringBuilder) target).append(source);
        } else if (target != null) {
            copy(source, target, ObjUtils::checkTypeClone);
        }
    }

    /**
     * 把sourceObj数据浅克隆到targetObj
     */
    public static void shallowCopy(Object sourceObj, Object targetObj) {
        if (targetObj instanceof Collection) {
            ((Collection) targetObj).addAll((Collection) sourceObj);
        } else if (targetObj instanceof Map) {
            ((Map) targetObj).putAll((Map) sourceObj);
        } else if (targetObj instanceof StringBuilder) {
            ((StringBuilder) targetObj).append(sourceObj);
        } else if (targetObj != null) {
            copy(sourceObj, targetObj, v -> v);
        }
    }

    /**
     * 复制对象
     *
     * @param fun 值传播方式
     */
    private static void copy(Object sourceObj, Object targetObj, Function<Object, Object> fun) {
        Map<String, Field> a = getFieldMapping(sourceObj.getClass());
        Map<String, Field> b = getFieldMapping(targetObj.getClass());
        Map<String, Field> targetF;
        Map<String, Field> sourceF;
        if (a.size() < b.size()) {
            targetF = a;
            sourceF = b;
        } else {
            targetF = b;
            sourceF = a;
        }
        for (Field targetField : targetF.values()) {

            // 源对象存在跟目标对象同样名称的字段
            Field sourceField = sourceF.get(targetField.getName());

            if (sourceField != null) {
                try {
                    Object value = sourceField.get(sourceObj);
                    targetField.set(targetObj, fun.apply(value)); // 深/浅 克隆关键在于内容的钻取判断
                } catch (IllegalAccessException e) {
                    //
                }
            }
        }
    }

    /**
     * 判定内容是基本数据类型还是引用数据类型
     * 基本数据类型直接返回即可， 引用数据类型还需要再进一步进行深度拷贝
     */
    private static Object checkTypeClone(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof String
                || val instanceof Integer
                || val instanceof Boolean
                || val instanceof Long
                || val instanceof Short
                || val instanceof Double
                || val instanceof Float
                || val instanceof Character
                || val instanceof Byte) {
            return val;
        }
        return deepClone(val);
    }

    /**
     * 对象字段索引缓存
     */
    private static final Map<Class<?>, Map<String, Field>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 字段名称作为索引map
     */
    private static Map<String, Field> getFieldMapping(Class<?> clz) {
        return cacheMap.computeIfAbsent(clz,
                k -> {
                    List<Field> list = new ArrayList<>();
                    collectField(clz, list);
                    return MapUtils.objMapping(list, Field::getName);
                });
    }

    /**
     * 递归搜集对象的所有字段
     */
    private static void collectField(Class clz, List<Field> result) {
        // 向上追溯父对象
        if (!(clz.getSuperclass() == Object.class)) {
            collectField(clz.getSuperclass(), result);
        }
        // 到达除Object外的最上层父对象时结束递归，开始收集字段
        for (Field field : clz.getDeclaredFields()) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            result.add(field);
        }
    }
}