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
	 * ��ת����½ҳ
	 * @param req
	 * @param res
	 * @return
	 */
	public String login(HttpServletRequest req,HttpServletResponse res){
		Subject user = SecurityUtils.getSubject();
		if(user != null && user.isAuthenticated()){
			//�Ѿ���½
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "�Ѿ���½");
	        return "/WEB-INF/pages/main.jsp";
		}
		if(user !=null && user.isRemembered()){
			//��ס,��cookie�е�½������session�е��û���Ϣnull�ˡ�
			req.setAttribute("root",req.getContextPath());
			req.setAttribute("info", "����ס����cookie�е�½");
	        return "/WEB-INF/pages/main.jsp";
		}
		
		req.setAttribute("root",req.getContextPath());
        return "/WEB-INF/pages/login.jsp";
    }
	/**
	 * ��½
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
				//�ߵ�ԭ�����û�
				return logout(req,res);
			}else{
				UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd,rememberMe);
				user.login(token);
				req.setAttribute("root",req.getContextPath());
				req.setAttribute("info", "������½");
		        return "/WEB-INF/pages/main.jsp";
			}
		}catch (Exception e) {
			//��һЩ�쳣����
			req.setAttribute("msg", "��½ʧ��");
			req.setAttribute("loginname", loginname);
			req.setAttribute("pwd", pwd);
			req.setAttribute("root",req.getContextPath());
	        return "/WEB-INF/pages/login.jsp";
		}
		
    }
	/**
	 * ע��
	 * @param req
	 * @param res
	 * @return
	 */
	public String logout(HttpServletRequest req,HttpServletResponse res){
		SecurityUtils.getSubject().logout();
		req.setAttribute("msg", "ע���ɹ�");
		return login(req, res);
	}

}
