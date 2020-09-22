package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {

    /**
     * 分页查询
     * @param cid   分类
     * @param currentPage   当前页码
     * @param pageSize  页面容量
     * @param rname   搜索线路
     * @param queryMethod   查询方式，true代表普通查询，false代表按收藏次数从大到小查询
     * @return  返回PageBean对象
     */
    PageBean<Route> pagingQuery(int cid, int currentPage, int pageSize, String rname, Boolean queryMethod,int bottomPrice,int topprice);

    /**
     * 查询带有卖家信息及线路的组合照片的route信息
     * @param rid
     * @return
     */
    Route fingDetailedRoute(int rid);
}
