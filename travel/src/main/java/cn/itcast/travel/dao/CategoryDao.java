package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryDao {

    /**
     * 查询所有分类
     * @return
     */
    List<Category> findAll();

    /**
     * 根据cid查询此分类对象
     * @param cid
     * @return
     */
    Category findCategoryByCid(int cid);
}
