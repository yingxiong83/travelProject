package cn.itcast.travel.dao;


import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根据分类信息及搜索框查询记录数
     * @param cid
     * @param rname
     * @param bottomPrice
     * @param topprice
     * @return
     */
    int findTotaolCount(int cid, String rname, int bottomPrice, int topprice);

    /**
     * 条件查询，分类标识，开始索引，查询条数,搜索框,查询方法,价格区间
     * @param cid
     * @param start
     * @param pageSize
     * @param rname
     * @param queryMethod   true：无需指定顺序，false：按count降序查询
     * @param bottomPrice
     * @param topprice
     * @return
     */
    List<Route> findRouteByPage(int cid, int start, int pageSize, String rname, Boolean queryMethod,int bottomPrice,int topPrice);

    /**
     * 根据线路id，rid查询到线路，并返回
     * @param rid
     * @return
     */
    Route findRouteByRid(int rid);

    /**
     * 更新线路的收藏次数
     * @param rid
     * @param count
     */
    void updateCount(int rid, int count);


}
