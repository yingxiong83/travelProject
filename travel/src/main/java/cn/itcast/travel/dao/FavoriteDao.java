package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

import java.util.Date;
import java.util.List;

public interface FavoriteDao {

    /**
     * 查询tab_favorite表,根据uid和rid
     * @param uid
     * @param rid
     * @return
     */
    Favorite findById(int uid, int rid);

    /**
     * 获取收藏次数
     * @param rid
     * @return
     */
    int getFavoriteCount(int rid);

    /**
     * 添加一条记录
     * @param uid
     * @param date
     * @param rid
     */
    void add(int uid, Date date, int rid);

    /**
     * 根据uid获取所有收藏数据
     * @param uid
     * @return
     */
    List<Favorite> findByUid(int uid);
}
