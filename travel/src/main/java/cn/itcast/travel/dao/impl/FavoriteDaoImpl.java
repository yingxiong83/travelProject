package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findById(int uid, int rid) {
        String sql = "select * from tab_favorite where uid=? and rid=?";
        try {
            return template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid, rid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public int getFavoriteCount(int rid) {
        String sql = "select count(*) from tab_favorite where rid=?";
        return template.queryForObject(sql, Integer.class, rid);
    }

    @Override
    public void add(int uid, Date date, int rid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql, rid, date, uid);
    }

    @Override
    public List<Favorite> findByUid(int uid) {
        String sql = "select * from tab_favorite where uid=?";
        try {
            return template.query(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid);
        } catch (DataAccessException e) {
            return null;
        }
    }
}
