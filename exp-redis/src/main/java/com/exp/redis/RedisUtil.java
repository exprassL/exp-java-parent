package com.exp.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作封装
 */
//@Component
public final class RedisUtil {
    
    // FIXME pipeline操作未实现
    
    private static RedisTemplate<String, Object> redisTemplate;
    
    private static StringRedisTemplate stringRedisTemplate;
    
    /**
     * @param redisTemplate
     * @param stringRedisTemplate
     */
//    @Autowired
    public static void init(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
        RedisUtil.stringRedisTemplate = stringRedisTemplate;
    }
    
    /**
     * 模糊匹配key
     *
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
    
    /**
     * 指定缓存失效时间，按秒计
     *
     * @param key     键
     * @param timeout 时间(秒)
     * @return
     * @throws Exception 指定失效时间为负时
     */
    public static Boolean expire(String key, long timeout) throws Exception {
        if (timeout > 0) {
            return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
        
        throw RedisException.produce("失效时间：%d 不为正数", timeout);
    }
    
    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
    
    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
    
    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     * @return 删除的数量
     */
    public static Long delete(String... key) {
        if (key == null || key.length == 0) {
            return 0L;
        }
        if (key.length == 1) {
            return Boolean.TRUE.equals(redisTemplate.delete(key[0])) ? 1L : 0L;
        } else {
            return redisTemplate.delete(Arrays.asList(key));
        }
    }
    
    /**
     * 缓存Object
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 获取String
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }
    
    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 普通缓存放入并设置时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static void set(String key, Object value, long timeout) {
        if (timeout > 0) {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }
    
    /**
     * 递增
     *
     * @param key   键
     * @param delta 增加量
     * @return
     * @throws Exception 递增因子为负时
     */
    public static Long increment(String key, long delta) throws Exception {
        if (delta > 0) {
            return redisTemplate.opsForValue().increment(key, delta);
        }
        throw RedisException.produce("递增因子：%d 不为正数", delta);
    }
    
    /**
     * 递减
     *
     * @param key   键
     * @param delta 减少量
     * @return
     * @throws Exception 递减因子为负时
     */
    public static Long decrement(String key, long delta) throws Exception {
        if (delta > 0) {
            return redisTemplate.opsForValue().increment(key, -delta);
        }
        throw RedisException.produce("递减因子：%d 不为正数", delta);
    }
    
    /**
     * HashGet
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return 值
     */
    public static Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
    
    /**
     * 获取key对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> hashGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
    
    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static void hashSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }
    
    /**
     * HashSet 并设置时间
     *
     * @param key     键
     * @param map     对应多个键值
     * @param timeout 时间(秒)
     * @return true成功 false失败
     */
    public static void hashSet(String key, Map<String, Object> map, long timeout) {
        hashSet(key, map);
        try {
            expire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @return true 成功 false失败
     */
    public static void hashSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
    
    /**
     * 向一张hash表中放入数据，如果不存在将创建；并设置（覆盖）过期时间
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @param timeout 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static void hashSet(String key, String hashKey, Object value, long timeout) {
        hashSet(key, hashKey, value);
        try {
            expire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除hash表中的值
     *
     * @param key     键 不能为null
     * @param hashKay 项 可以使多个 不能为null
     */
    public static Long hashDelete(String key, Object... hashKay) {
        return redisTemplate.opsForHash().delete(key, hashKay);
    }
    
    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return true 存在 false不存在
     */
    public static Boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }
    
    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   增加量
     * @return 增加后的值
     */
    public Double hashIncrement(String key, String hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }
    
    /**
     * hash递减
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   减少量
     * @return 减小后的值
     */
    public Double hashDecrement(String key, String hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }
    
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public static List<Object> listGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }
    
    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }
    
    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     * @return push之后的长度
     */
    public static Long listSet(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }
    
    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static Long listSet(String key, List<Object> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }
    
    /**
     * 将值放入缓存，并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return
     */
    public static Long listSet(String key, Object value, long timeout) {
        Long count = listSet(key, value);
        try {
            expire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    /**
     * 将list放入缓存，并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return
     */
    public static Long listSet(String key, List<Object> value, long timeout) {
        Long count = listSet(key, value);
        try {
            expire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引，从0开始；负数表示倒数，从-1开始
     * @return
     */
    public static Object listIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }
    
    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static void listIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }
    
    /**
     * 移除N个值为value的元素
     *
     * @param key   键
     * @param count 移除多少个，>0 从头到尾匹配，<0 从尾到头匹配，=0 匹配所有
     * @param value 值
     * @return 移除的个数
     */
    public static Long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }
    
    /**
     * 移除第一个值为value的元素
     *
     * @param key
     * @param value
     * @return
     */
    public static Long listRemoveFirst(String key, Object value) {
        return listRemove(key, 1, value);
    }
    
    //===============================list=================================
    
    /**
     * 移除第一个置为value的元素
     *
     * @param key
     * @param value
     * @return
     */
    public static Long lRemoveLast(String key, Object value) {
        return listRemove(key, -1, value);
    }
    
    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> setGet(String key) {
        redisTemplate.watch("");
        return redisTemplate.opsForSet().members(key);
    }
    
    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static Boolean setHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }
    
    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static Long setSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }
    
    /**
     * 将set数据放入缓存，并设置过期时间
     *
     * @param key     键
     * @param timeout 时间(秒)
     * @param values  值 可以是多个
     * @return 成功个数
     */
    public static long setSet(String key, long timeout, Object... values) {
        Long count = setSet(key, values);
        try {
            expire(key, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static Long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }
    
    /**
     * 移除值为value的元素
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }
    
    
    /**
     * 使用Redis的消息队列
     *
     * @param channel
     * @param message 消息内容
     */
    public static void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}