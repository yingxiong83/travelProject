package cn.itcast.travel.service;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteService {

    /**
     * 判断某用户是否收藏该条线路
     * @param uid
     * @param rid
     * @return
     */
    boolean isFavorite(int uid, int rid);

    /**
     * 添加收藏
     * @param uid
     * @param rid
     */
    void addFavorite(int uid, int rid);

    /**
     * 查询我的收藏
     * @param uid
     * @return
     */
    List<Route> findMyFavorite(int uid);
}
