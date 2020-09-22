package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 增加用户
     * @param user
     */
    void insert(User user);

    /**
     * 根据激活码查询用户
     * @param code
     * @return
     */
    User findUserByCode(String code);

    /**
     * 修改用户激活状态
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User findUserByUsernameAndPassword(String username, String password);
}
