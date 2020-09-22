package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    @Override
    public boolean regist(User user) {
        //1.判断用户名是否存在
        User u = dao.findUserByUsername(user.getUsername());
        if (u != null) {
            return false;
        }
        //用户不存在，可以注册
        //设置激活状态
        user.setStatus("N");
        /*
            获取激活码，设置激活码
         */
        String code = UuidUtil.getUuid();
        user.setCode(code);
        this.save(user);
        //发送邮件验证
        String content = "<a href='http://localhost/travel/user/active?code=" + code + "'>点我激活</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");
        return true;
    }

    @Override
    public void save(User user) {
        dao.insert(user);
    }

    @Override
    public boolean active(String code) {
        //1.查询此激活码对应得用户是否存在
        User user = dao.findUserByCode(code);
        if (user != null) {
            //用户存在，设置激活状态位true
            dao.updateStatus(user);
            return true;
        } else {
            //用户不存在
            return false;
        }
    }

    @Override
    public User login(User user) {
        return dao.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
