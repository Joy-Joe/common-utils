package cn.sy.cache;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis缓存实现
 */
@Slf4j
@Component
public class SyRedisUtils {

    @Autowired
    private JedisPool jedisPool;

    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            log.info("get key:{},value:{}", key, value);
            if (clazz.newInstance() instanceof String) {
                return (T) value;
            }
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            log.error("redis_get key:{},error msg:{},error:{}", key, e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public <T> void set(String key, T value, Integer timeout) {
        Jedis jedis = null;
        String data = "";
        try {
            jedis = jedisPool.getResource();
            if (value instanceof String) {
                data = (String) value;
            } else {
                data = JSON.toJSONString(value);
            }
            jedis.setex(key, timeout, data);
            log.info("redis_set key:{},cost:{},data:{}", key, timeout, data);
        } catch (Exception e) {
            log.error("redis_set key:{},error msg:{},error:{}", key, e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 储存缓存值
     *
     * @param key
     * @param value
     */
    public <T> void set(String key, T value) {
        Jedis jedis = null;
        String data = "";
        try {
            jedis = jedisPool.getResource();
            if (value instanceof String) {
                data = (String) value;
            } else {
                data = JSON.toJSONString(value);
            }
            jedis.set(key, data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void remove(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            log.error("redis_remove_error,key:{}", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public void expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error("redis_expire_error,key:{}", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long incr(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long l1 = jedis.incr(key);
            jedis.expire(key, seconds);
            return l1;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
