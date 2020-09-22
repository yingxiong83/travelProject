package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //声明UserService业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 用户注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //创建后端返回给前端数据的对象
        ResultInfo info = new ResultInfo();
        //获取请求验证码
        String check = request.getParameter("check");
        //取出session中的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //销毁验证码
        session.removeAttribute("CHECKCODE_SERVER");

        //1.校验验证码
        if (!check.equalsIgnoreCase(checkcode_server)) {
            //验证码验证不通过
            info.setFlag(false);
            info.setErrorMsg("验证码输入有误");
        } else {
            //2.注册用户
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            boolean flag = service.regist(user);

            //3.判断用户是否注册成功
            if (!flag) {
                //用户已存在
                info.setFlag(false);
                info.setErrorMsg("用户名已存在");
            } else {
                //注册成功
                info.setFlag(true);
            }
        }

        //4.返回给前端数据
        /*response.setContentType("application/json;charset=utf-8");
        //创建Jackson解析器
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.getWriter().write(json);*/
        writeValue(response, info);
    }

    /**
     * 用户激活
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //2.激活
            boolean flag = service.active(code);
            //3.判断标记
            if (flag) {
                //激活成功
                response.sendRedirect(request.getContextPath() + "/login.html");
            } else {
                //激活失败
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("激活失败，请联系管理员！");
            }
        }
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = new ResultInfo();
        /*
            校验验证码
         */
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server != null && checkcode_server.equalsIgnoreCase(check)) {
            //校验通过
            //1.获取请求参数，封装User对象
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //2.登录
            User u = service.login(user);
            //3.判断
            if (u == null) {
                //用户名或密码有误
                info.setFlag(false);
                info.setErrorMsg("用户名或密码有误");
            } else if (u != null && !"Y".equals(u.getStatus())) {
                //用户未激活
                info.setFlag(false);
                info.setErrorMsg("用户未进行邮箱激活");
            } else {
                //登录成功
                info.setFlag(true);
                session.setAttribute("user", u);

                /*
                    登录成功，设置cookie
                 */
                String remember = request.getParameter("remember");
                if ("on".equals(remember)) {
                    Cookie cookie_username = new Cookie("username", u.getUsername());
                    Cookie cookie_password = new Cookie("password", u.getPassword());
                    cookie_username.setMaxAge(7 * 24 * 60 * 60);
                    cookie_password.setMaxAge(7 * 24 * 60 * 60);
                    cookie_username.setPath("/travel");
                    cookie_password.setPath("/travel");
                    response.addCookie(cookie_username);
                    response.addCookie(cookie_password);
                } else {
                    Cookie[] cookies = request.getCookies();
                    for (Cookie cookie : cookies) {
                        System.out.println("删除cookie执行了");
                        cookie.setMaxAge(0);
                        cookie.setPath("/travel");
                        response.addCookie(cookie);
                    }
                }
            }
        } else {
            //验证码输入有误
            info.setFlag(false);
            info.setErrorMsg("验证码输入有误");
        }

        //4.回写数据
        /*response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getWriter(), info);*/
        writeValue(response, info);
    }

    /**
     * 在网站首页动态展示用户名
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void showUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        /*ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);*/
        writeValue(response, user);
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/index.html");
    }
}
