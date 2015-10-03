package demo.spring.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	
	/**
	 * 跳转到登陆页
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest req,HttpServletResponse res){
		Subject user = SecurityUtils.getSubject();
		if(user != null && user.isAuthenticated()){
			//已经登陆
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "已经登陆");
	        return "/spring-main";
		}
		if(user !=null && user.isRemembered()){
			//记住,从cookie中登陆，但是session中的用户信息null了。
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "被记住，从cookie中登陆");
	        return "/spring-main";
		}
		
		req.setAttribute("root",req.getContextPath());
        return "/spring-login";
    }
	/**
	 * 登陆
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/loginUser")
	public String loginUser(HttpServletRequest req,HttpServletResponse res){
		String loginname = req.getParameter("loginname");
		String pwd = req.getParameter("pwd");
		String remember = req.getParameter("rememberMe");
		boolean rememberMe = false;
		if(remember != null && remember.equals("true")){
			rememberMe = true;
		}
		Subject user = null;
		try{
			user = SecurityUtils.getSubject();
			if(user.isAuthenticated() && !loginname.equals(user.getPrincipal())){
				//踢掉原来的用户
				return logout(req,res);
			}else{
				UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd,rememberMe);
				user.login(token);
				req.setAttribute("root",req.getContextPath());
				req.setAttribute("info", "正常登陆");
		        return "/spring-main";
			}
		}catch (UnknownAccountException e1) {
			//做一些异常处理
			req.setAttribute("msg", "用户名错误");
			req.setAttribute("loginname", loginname);
			req.setAttribute("pwd", pwd);
			req.setAttribute("root",req.getContextPath());
	        return "/spring-login";
		}catch (IncorrectCredentialsException e2) {
			//做一些异常处理
			req.setAttribute("msg", "密码错误");
			req.setAttribute("loginname", loginname);
			req.setAttribute("pwd", pwd);
			req.setAttribute("root",req.getContextPath());
	        return "/spring-login";
		}
		
    }
	/**
	 * 注销
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req,HttpServletResponse res){
		SecurityUtils.getSubject().logout();
		req.setAttribute("msg", "注销成功");
		return login(req, res);
	}
}
