package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    //声明UserDao
    private CategoryDao dao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        List<Category> list = new ArrayList<Category>();
        //1.查询缓存
        Jedis jedis = JedisUtils.getJedis();
        Set<Tuple> categorys = jedis.zrangeWithScores("categorys", 0, -1);
        if (categorys == null || categorys.size() == 0) {
            //2.缓存没有，查数据库，并存入缓存
            list = dao.findAll();
            for (Category category : list) {
                jedis.zadd("categorys", category.getCid(), category.getCname());
            }
        } else {
            //3.缓存有，取出，添加到list集合
            for (Tuple tuple : categorys) {
                Category c = new Category();
                c.setCid((int) tuple.getScore());
                c.setCname(tuple.getElement());
                list.add(c);
            }
        }
        //4.释放Jedis连接
        JedisUtils.close(jedis);
        //5.返回结果
        return list;
    }
}
