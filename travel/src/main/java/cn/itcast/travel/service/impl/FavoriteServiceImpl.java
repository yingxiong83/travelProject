package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao = new RouteDaoImpl();

    @Override
    public boolean isFavorite(int uid, int rid) {
        //1.查询数据库，是否有记录
        Favorite favorite = favoriteDao.findById(uid, rid);
        if (favorite == null) {
            return false;
        }
        return true;
    }

    @Override
    public void addFavorite(int uid, int rid) {
        favoriteDao.add(uid, new Date(), rid);
    }

    @Override
    public List<Route> findMyFavorite(int uid) {
        List<Route> routes = new ArrayList<>();

        //1.查询tab_favorite表，获取该用户所有的收藏记录
        List<Favorite> favorites = favoriteDao.findByUid(uid);
        //2.遍历favorites,查询tab_route表，获取所有收藏线路的具体信息
        if (favorites == null) {
            return null;
        }
        for (Favorite favorite : favorites) {
            Route route = routeDao.findRouteByRid(favorite.getRid());
            routes.add(route);
        }
        return routes;
    }

}
