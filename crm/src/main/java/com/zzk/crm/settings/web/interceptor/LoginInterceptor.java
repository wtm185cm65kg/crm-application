package com.zzk.crm.settings.web.interceptor;

import com.zzk.crm.contants.CodeConstant;
import com.zzk.crm.settings.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 登陆验证写在preHandle()中,因为是在登录之前进行拦截而非登录之后(此时已经可以绕进后台了)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //验证用户是否登陆过
        /**拦截器中不能像控制器里一样直接将HttpSession作为内置对象直接放入拦截器方法参数中
         * 在拦截器中应通过HttpServletRequest对象获取HttpSession对象*/
        User user = (User)request.getSession().getAttribute(CodeConstant.SESSION_USER);
        if (user==null){//说明想绕过登陆界面登录 或 session域中的数据达到超时时长被自动清除
            response.sendRedirect(request.getContextPath()); /**重定向时必须要获取项目名称，再跳转到相应页面*/
            return false;
        }else if(user.getLoginAct()==null||user.getLoginPwd()==null
                ||"".equals(user.getLoginAct())||"".equals(user.getLoginPwd())) {//说明有人想伪造一个user(可能通过反射机制)绕过拦截器
            return false;
        }//进一步防范措施是连接数据库查询该user是否合法，但效率也随之降低
        return true;
    }
}
