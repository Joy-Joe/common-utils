/**
 * <p>Title: CacheBuilderLocalCache.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 *
 * @author xuguofei - 13917664945
 * @date 2019年10月10日
 * @version 1.0
 */
package cn.sy.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class GuavaBuilderLocalCache {
    //TODO 需要过期时间可以动态配置

    // 通过CacheBuilder构建一个缓存实例
    public static Cache<String, Object> longCache = CacheBuilder.newBuilder().maximumSize(1000) // 设置缓存的最大容量
            .expireAfterWrite(1200, TimeUnit.SECONDS) // 设置缓存在写入xx分钟后失效
            .concurrencyLevel(Runtime.getRuntime().availableProcessors()) // 设置并发级别为cpu核心数
            .recordStats() // 开启缓存统计
            .build();

    public static String generateCacheKey(String key) {
        return "sy:cache:".concat(key);
    }

    public static <T> List<T> getListFromLongCache(String key, Callable<List<T>> callable, Class<T> clazz) throws Exception {
        String data = (String) longCache.getIfPresent(key);
        if (StringUtils.isNotBlank(data)) {
            return parseString2List(data, clazz);
        }
        List<T> list = callable.call();
        if (list != null && list.size() > 0) {
            putToLongCacheList(key, list);
        }
        return list;
    }

    public static void putCacheStr(String key, Object value) throws Exception {
        longCache.put(key, value);
    }

    public static Object getCacheStr(String key, Callable<Object> valueLoader) throws Exception {
        try {
            return longCache.get(key, valueLoader);
        } catch (InvalidCacheLoadException e) {
            return null;
        }
    }

/*    public static <T> T getCacheT(String key, Callable<Object> valueLoader, Class<T> clazz) throws Exception {
        try {
            Object data =  getCacheStr(key, valueLoader);
            if (data !=null ) {
                return JSON.parseObject(data, clazz);
            }
            return null;
        } catch (InvalidCacheLoadException e) {
            return null;
        }
    }*/

    public static <T> void putToLongCacheList(String key, List<T> list) throws Exception {
        putCacheStr(key, JSON.toJSONString(list));
    }


    public static <T> List<T> parseString2List(String json, @SuppressWarnings("rawtypes") Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(json, type);
        return list;
    }


}
