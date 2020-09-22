package cn.itcast.travel.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *Jedis工具类
 */
public class JedisUtils {
    private static JedisPool jedisPool;

    /**
     * 初始化Jedis连接池
     */
    static {
        try {
            //1.加载配置文件
            Properties pro = new Properties();
            InputStream is = JedisUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
            pro.load(is);
            //2.配置Jedis连接池
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
            config.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));
            //3.初始化连接池
            jedisPool = new JedisPool(config, pro.getProperty("host"), Integer.parseInt(pro.getProperty("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis连接
     * @return
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 释放Jedis连接
     * @param jedis
     */
    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
