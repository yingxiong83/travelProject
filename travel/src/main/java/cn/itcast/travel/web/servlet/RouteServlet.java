package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    //声明业务层对象
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 完成分页查询
     *      cid,rname,currentPage
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pagingQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //声明queryMethod,ture代表普通分页查询，false代表收藏列表展示
        Boolean queryMethod;

        /*
            1.接收请求参数
         */
        String cidstr = request.getParameter("cid");
        String currentPagestr = request.getParameter("currentPage");
        String pageSizestr = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        String bottomPricestr = request.getParameter("bottomPrice");
        String topPricestr = request.getParameter("topPrice");
        //处理参数rname
        if (rname == null || rname.length() == 0 || "null".equals(rname)) {
            rname = "";
        } /*else {
            //tomcat7及之前版本采用iso-8859-1字符集解码
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }*/

        /*
            2.处理请求参数
         */
        int cid;
        if (cidstr == null || "null".equals(cidstr) || cidstr.length() == 0) {
            cid = 0;
        } else {
            cid = Integer.parseInt(cidstr);
        }
        int currentPage;
        if (currentPagestr == null || currentPagestr.length() == 0) {
            currentPage = 1;
        } else {
            currentPage = Integer.parseInt(currentPagestr);
        }
        int pageSize;
        if (pageSizestr == null || pageSizestr.length() == 0) {
            pageSize = 5;
            queryMethod = true;
        } else {
            pageSize = Integer.parseInt(pageSizestr);
            queryMethod = false;
        }
        int bottomPrice;
        if (bottomPricestr == null || bottomPricestr.length() == 0) {
            bottomPrice = 0;
        } else {
            bottomPrice = Integer.parseInt(bottomPricestr);
        }
        int topPrice;
        if (topPricestr == null || topPricestr.length() == 0) {
            topPrice = 0;
        } else {
            topPrice = Integer.parseInt(topPricestr);
        }
        /*
            3.调用业务层方法，完成查询，返回PageBean对象
         */
        PageBean<Route> pb = routeService.pagingQuery(cid, currentPage, pageSize, rname,queryMethod,bottomPrice,topPrice);
        /*
            4.将PageBean对象序列化为json返回
         */
        writeValue(response, pb);
    }


    /**
     * 展示路线详情
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取请求参数
        String ridstr = request.getParameter("rid");
        //2.处理请求参数
        int rid = Integer.parseInt(ridstr);
        //3.调用业务层方法，完成查询详细信息，并返回route对象
        Route route = routeService.fingDetailedRoute(rid);
        //4.将route转成json字符串，返回
        writeValue(response, route);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
            声明响应状态:
                NOTLOGIN:未登录
                COLLECTED：已收藏
                NOTCOLLECTED：未收藏
         */
        String statusCode;
        //1.获取请求参数rid并处理
        String ridstr = request.getParameter("rid");
        int rid = Integer.parseInt(ridstr);
        //2.判断用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //用户未登录
            statusCode = "NOTLOGIN";
        } else {
            //用户已登录,调用业务层方法查询用户是否收藏该路线
            boolean flag = favoriteService.isFavorite(user.getUid(), rid);
            statusCode = flag ? "COLLECTED" : "NOTCOLLECTED";
        }
        //3.回写数据
        response.getWriter().write(statusCode);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取请求参数,rid
        String ridstr = request.getParameter("rid");
        int rid = Integer.parseInt(ridstr);
        //2.获取已登录用户的uid
        int uid = ((User) request.getSession().getAttribute("user")).getUid();
        //3.添加此线路到用户的收藏中
        favoriteService.addFavorite(uid, rid);
        //4.重定向至route_detail.html?rid=ridstr
        response.sendRedirect(request.getContextPath() + "/route_detail.html?rid=" + ridstr);
    }

    public void showMyFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取正在登录用户的uid
        int uid = ((User) request.getSession().getAttribute("user")).getUid();
        //2.查询用户收藏的所有线路
        List<Route> routes = favoriteService.findMyFavorite(uid);
        //3.返回
        writeValue(response, routes);
    }



}
