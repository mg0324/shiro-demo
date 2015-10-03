package demo.simple.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.framework.servlet.FrameworkServlet;
@WebServlet("/user")
public class UserServlet extends FrameworkServlet{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 跳转到登陆页
	 * @param req
	 * @param res
	 * @return
	 */
	public String login(HttpServletRequest req,HttpServletResponse res){
		Subject user = SecurityUtils.getSubject();
		if(user != null && user.isAuthenticated()){
			//已经登陆
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "已经登陆");
	        return "/WEB-INF/pages/main.jsp";
		}
		if(user !=null && user.isRemembered()){
			//记住,从cookie中登陆，但是session中的用户信息null了。
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "被记住，从cookie中登陆");
	        return "/WEB-INF/pages/main.jsp";
		}
		
		req.setAttribute("root",req.getContextPath());
        return "/WEB-INF/pages/login.jsp";
    }
	/**
	 * 登陆
	 * @param req
	 * @param res
	 * @return
	 */
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
		        return "/WEB-INF/pages/main.jsp";
			}
		}catch (Exception e) {
			//做一些异常处理
			req.setAttribute("msg", "登陆失败");
			req.setAttribute("loginname", loginname);
			req.setAttribute("pwd", pwd);
			req.setAttribute("root",req.getContextPath());
	        return "/WEB-INF/pages/login.jsp";
		}
		
    }
	/**
	 * 注销
	 * @param req
	 * @param res
	 * @return
	 */
	public String logout(HttpServletRequest req,HttpServletResponse res){
		SecurityUtils.getSubject().logout();
		req.setAttribute("msg", "注销成功");
		return login(req, res);
	}

}
