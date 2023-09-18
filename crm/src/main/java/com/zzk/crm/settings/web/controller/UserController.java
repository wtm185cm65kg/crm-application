package com.zzk.crm.settings.web.controller;

import com.zzk.crm.commons.pojo.ReturnJson;
import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建Controller的原则：处理完请求后返回的响应信息，该响应信息返回的资源页面对应一个Controller
 * '/'对应的jsp在WEB-INF下,其要跳转的html为"settings/qx/user/login.jsp"，
 * 不与该jsp在同一WEB-INF下，而是在WEB-INF/pages/settings/user下，因此应该新建一个与IndexController不同的Controller
 *
 * 也就是说：一个资源目录对应一个Controller
 *
 * Q:起名为UserController还是LoginController？
 * A:UserController，因为最终的资源在user包下，最好起资源目录名而不是具体资源名
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 跳转请求
     * @RequestMapping的url要和controller方法最终处理的资源页面保持一致(资源名采用方法名)
     */
    //@RequestMapping("/WEB-INF/pages/settings/qx/user/toLogin.do")
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        //转发到登陆页面,通过内部资源解析器对该字符串拼接成/WEB-INF/pages/settings/qx/user/login.jsp
        return "settings/qx/user/login";
    }

    /**
     * 由于登陆页面有两种情况：登陆成功->跳转页面(发送同步请求)
     *                      登陆失败->不跳转页面，呆在原界面(发送异步请求)
     * 这种响应结果可能刷新整个页面也可能刷新局部页面,要使用异步请求,使用@ResponseBody表示使用ajax请求
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        //调用Service层方法查询用户
        User user = userService.queryUserByLoginActAndPwd(map);

        //创建自写的类来封装code、message等信息(不用map的原因：效率低)
        ReturnJson returnJson = new ReturnJson();

        //根据查询结果生成响应信息
        if (user==null){//user为null说明用户名或密码错误导致select没有查出结果
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("用户名或密码错误");
        }else{//user不为null说明获得账户信息，要进一步判断账号是否合法
            //判断账号是否过期
            String nowTime = DateUtil.formatDateTime19(new Date());
            if (nowTime.compareTo(user.getExpireTime())>0){
                //当前nowTime大于expireTime说明账户已经过期了
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("账户已经过期！");
            }else if ("0".equals(user.getLockState())){//账号没有过期，则判断账号是否被锁定
                //当lockState==0说明当前账户被锁定
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("账户被锁定！");
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())) {//账号没有过期、锁定，则判断账号是否在安全ip下登录
                //当前ip地址被包含在allowIps时说明没有在安全环境下登录
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("没有在安全环境下登录！");
            }else {//运行到这里说明以上判断条件均不成立，可以成功登录
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);

                //将service获取的user放入session域,以便将处理好的数据信息传到视图层(jsp)
                session.setAttribute(Constants.SESSION_USER,user);

                /**如果选中了十天免登录则要生成Cookie以记住密码*/
                if ("true".equals(isRemPwd)){
                    //不能直接传入loginAct,而要传入user.getLoginAct().    因为一开始的loginAct未必正确，要获取登陆成功后的
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    //设置cookie有效时长为10天
                    c1.setMaxAge(10*24*60*60);
                    c2.setMaxAge(10*24*60*60);
                    //将Cookie响应给Browser
                    response.addCookie(c1);
                    response.addCookie(c2);
                }else {
                    /**把没有过期的Cookie删除
                     * 当cookie还在有效期，但本次用户登录不希望下次登陆使用cookie，将十天免登录选项取消
                     * 则应做到下次登陆时不再使用cookie自动填入，也就是说要删除原来存储的cookie*/
                    //设置超时时长为0代表删除，再次提交覆盖原Cookie(其实还要保证Cookie路径，但这里都采用默认路径，因此也不用写了)
                    Cookie c1 = new Cookie("loginAct", "sb");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "sb");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnJson;
    }

    /**
     * 选择这个请求放在UserController下而不是WorkbenchIndexController下：
     *      因为最终退出的结果希望返回到登陆首页，也就是user包下的login.jsp
     */
    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        //清除Cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        //销毁session，要使用invalidate()而不是removeAttribute("")
        /*session.removeAttribute("loginAct");
        session.removeAttribute("loginPwd");*/
        session.invalidate();
        /**重定向到首页
         * 如果是请求转发则地址栏仍显示 '/settings/qx/user/logout.do' ,用户每次刷新都会发送请求并重新执行一次
         * 这样每刷新一次就会向服务器发送请求进行清除Cookie、销毁Session的操作，极大浪费资源
         *
         * 不能直接重定向到 '/settings/qx/user/toLogin.do'，因为其为内部隐藏资源，无法直接访问
         * 而是先重定向到根路径下，再由其请求转发到 '/settings/qx/user/toLogin.do'*/
        //return "redirect:/settings/qx/user/toLogin.do";
        return "redirect:/";    /**SpringMVC底层看到这个重定向路径会执行
                                   response.sendRedirect(request.getContextPath()+"/")*/
    }
}
