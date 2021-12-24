package cn.sy.cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author joy joe
 * @date 2021/12/5 下午2:26
 * @DESC
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    @SuppressWarnings("rawtypes")
    Class clazz;

    @SuppressWarnings("rawtypes")
    public ParameterizedTypeImpl(Class clz) {
        clazz = clz;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{clazz};
    }

    @Override
    public Type getRawType() {
        return List.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
