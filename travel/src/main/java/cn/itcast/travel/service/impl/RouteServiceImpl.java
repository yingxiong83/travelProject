package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.*;
import cn.itcast.travel.dao.impl.*;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import java.util.ArrayList;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private CategoryDao categoryDao = new CategoryDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pagingQuery(int cid, int currentPage, int pageSize, String rname, Boolean queryMethod,int bottomPrice,int topPrice) {
        PageBean<Route> pb = new PageBean<>();
        List<Route> list = new ArrayList<>();
        //1.获取cid对应的记录条数
        int totaolCount = routeDao.findTotaolCount(cid,rname,bottomPrice,topPrice);
        //2.获取页面路径信息
        list = routeDao.findRouteByPage(cid, (currentPage - 1) * pageSize, pageSize, rname,queryMethod,bottomPrice,topPrice);
        //Collections.sort(list, (o1, o2) -> o2.getCount() - o1.getCount());
        //3.计算总页数
        int totalPage = (int) Math.ceil((double) totaolCount / (double) pageSize);
        /*
            4.设置pb的值
         */
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setTotalCount(totaolCount);
        pb.setTotalPage(totalPage);
        pb.setList(list);
        //5.返回pb对象
        return pb;
    }

    @Override
    public Route fingDetailedRoute(int rid) {
        //1.查询tab_route表，获取基本信息
        Route route = routeDao.findRouteByRid(rid);
        //2.查询tab_route_img表，获取路线的图片组合，并添加到route里
        List<RouteImg> list = routeImgDao.findAllByRid(rid);
        route.setRouteImgList(list);
        //3.查询tab_seller表，获取线路的卖家详情，并添加到route里
        Seller seller = sellerDao.findSellerBySid(route.getSid());
        route.setSeller(seller);
        //4.查询tab_category表，获取线路分类，并添加到route里
        Category category = categoryDao.findCategoryByCid(route.getCid());
        route.setCategory(category);
        //4.查询tab_favorite表，获取线路收藏次数，并添加到route中，并更新到tab_route表中
        int count = favoriteDao.getFavoriteCount(rid);
        route.setCount(count);
        routeDao.updateCount(rid,count);
        return route;
    }
}
