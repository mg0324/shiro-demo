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
	 * ��ת����½ҳ
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest req,HttpServletResponse res){
		Subject user = SecurityUtils.getSubject();
		if(user != null && user.isAuthenticated()){
			//�Ѿ���½
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "�Ѿ���½");
	        return "/spring-main";
		}
		if(user !=null && user.isRemembered()){
			//��ס,��cookie�е�½������session�е��û���Ϣnull�ˡ�
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "����ס����cookie�е�½");
	        return "/spring-main";
		}
		
		req.setAttribute("root",req.getContextPath());
        return "/spring-login";
    }
	/**
	 * ��½
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
				//�ߵ�ԭ�����û�
				return logout(req,res);
			}else{
				UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd,rememberMe);
				user.login(token);
				req.setAttribute("root",req.getContextPath());
				req.setAttribute("info", "������½");
		        return "/spring-main";
			}
		}catch (UnknownAccountException e1) {
			//��һЩ�쳣����
			req.setAttribute("msg", "�û�������");
			req.setAttribute("loginname", loginname);
			req.setAttribute("pwd", pwd);
			req.setAttribute("root",req.getContextPath());
	        return "/spring-login";
		}catch (IncorrectCredentialsException e2) {
			//��һЩ�쳣����
			req.setAttribute("msg", "�������");
			req.setAttribute("loginname", loginname);
			req.setAttribute("pwd", pwd);
			req.setAttribute("root",req.getContextPath());
	        return "/spring-login";
		}
		
    }
	/**
	 * ע��
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req,HttpServletResponse res){
		SecurityUtils.getSubject().logout();
		req.setAttribute("msg", "ע���ɹ�");
		return login(req, res);
	}
}
