package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {

    /**
     * 注册方法，成功返回true，失败返回false
     * @return
     * @param user
     */
    boolean regist(User user);

    /**
     * 保存用户
     * @param user
     */
    void save(User user);

    /**
     * 激活
     * @param code 激活码
     * @return
     */
    boolean active(String code);

    /**
     * 登录
     * @param user
     * @return
     */
    User login(User user);
}
