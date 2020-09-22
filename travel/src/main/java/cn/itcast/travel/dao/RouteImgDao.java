package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    /**
     * 根据线路的id查询出详细的图片列表
     * @param rid
     * @return
     */
    List<RouteImg> findAllByRid(int rid);
}
