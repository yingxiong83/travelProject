package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotaolCount(int cid, String rname, int bottomPrice, int topPrice) {
        /*String sql = "select count(*) from tab_route where cid=?";
        return template.queryForObject(sql, Integer.class, cid);*/
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List list = new ArrayList();
        if (cid != 0) {
            sb.append(" and cid=? ");
            list.add(cid);
        }
        if (rname.length() != 0) {
            sb.append(" and rname like ? ");
            list.add("%" + rname + "%");
        }
        if (bottomPrice != 0) {
            sb.append(" and price>=? ");
            list.add(bottomPrice);
        }
        if (topPrice != 0) {
            sb.append(" and price<=? ");
            list.add(topPrice);
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, list.toArray());
    }

    @Override
    public List<Route> findRouteByPage(int cid, int start, int pageSize, String rname, Boolean queryMethod,int bottomPrice,int topPrice) {
        /*String sql = "select * from tab_route where cid=? limit ?,?";
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cid, start, pageSize);*/
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List list = new ArrayList();
        if (cid != 0) {
            sb.append(" and cid=? ");
            list.add(cid);
        }
        if (bottomPrice != 0) {
            sb.append(" and price>= ? ");
            list.add(bottomPrice);
        }
        if (topPrice != 0) {
            sb.append(" and price<= ? ");
            list.add(topPrice);
        }
        if (rname.length() != 0 ) {
            sb.append(" and rname like ? ");
            list.add("%" + rname + "%");
        }
        //如果queryMethod为false，追加排序查询条件，按收藏数降序查询
        if (!queryMethod) {
            sb.append(" order by count DESC ");
        }
        sb.append(" limit ?,? ");
        list.add(start);
        list.add(pageSize);
        sql = sb.toString();
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), list.toArray());
    }

    @Override
    public Route findRouteByRid(int rid) {
        String sql = "select * from tab_route where rid=?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
    }

    @Override
    public void updateCount(int rid, int count) {
        String sql = "update tab_route set count=? where rid=?";
        template.update(sql, count, rid);
    }

}
