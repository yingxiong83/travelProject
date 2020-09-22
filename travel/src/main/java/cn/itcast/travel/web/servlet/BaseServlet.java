package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpResponse;

public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取请求uri路径
        String uri = req.getRequestURI();
        //2.截取方法名
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        try {
            //3.获取方法对象
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
//            method.setAccessible(true);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将obj转成json字符串，并写回前端
     * @param response
     * @param obj
     * @throws IOException
     */
    public void writeValue(HttpServletResponse response,Object obj) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getWriter(), obj);
    }

}
