package demo.main;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;


/**
 * 快速入门使用shiro api
 *
 */
public class Quickstart {

    private static final transient Logger log = Logger.getLogger(Quickstart.class);


    public static void main(String[] args) {

    	//1.从shiro.ini加载得到securityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-main.ini");
        SecurityManager securityManager = factory.getInstance();

        //2.设置到SecurityUtils中，实现单例
        SecurityUtils.setSecurityManager(securityManager);

        //..shiro的环境搭建好了

        // 得到主题对象，一般可以理解成用户user
        Subject currentUser = SecurityUtils.getSubject();

        // 3.做一些session操作，不需要web容器，shiro api中带有Session管理
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("从session中取出someKey的值为:"+value);
        }
        boolean login = true;
        // 4.登陆用户，来完成角色和权限的认证
        if (!currentUser.isAuthenticated()) {//如果当前主题没有登陆
            //4.1.获取用户登录票据
        	UsernamePasswordToken token = new UsernamePasswordToken("root", "secret");//使用lonestarr为loginname,vespa为pwd来登陆
            //4.2.记录用户票据
        	token.setRememberMe(true);
            try {
            	//4.3.登陆用户
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("用户名不存在 -- " + token.getPrincipal());
                login = false;
            } catch (IncorrectCredentialsException ice) {
                log.info("账户密码 " + token.getPrincipal() + " 不正确！");
                login = false;
            } catch (LockedAccountException lae) {
                log.info("账户 " + token.getPrincipal() + " 被锁定  " +
                        "请联系管理员解锁！");
                login = false;
            }
            catch (AuthenticationException ae) {
            	login = false;
            }
        }
        if(!login){
        	//登陆失败，推出系统
        	System.exit(1);
        }
        //4.3.没有异常，则登陆成功
        log.info("用户 [" + currentUser.getPrincipal() + "] 登录成功！");

        //5.角色验证，验证有无schwartz角色
        if (currentUser.hasRole("schwartz")) {
            log.info("用户["+currentUser.getPrincipal()+"]拥有schwartz角色！");
        } else {
            log.info("用户["+currentUser.getPrincipal()+"]没有schwartz角色！");
        }

        //6.权限验证，验证有无lightsaber资源的weild权限
        if (currentUser.isPermitted("lightsaber:weild")) {
            log.info("用户["+currentUser.getPrincipal()+"]拥有lightsaber资源的weild权限！");
        } else {
            log.info("对不起，lightsaber资源只存在schwartz角色中。");
        }

        //6.1.多层权限验证
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("用户["+currentUser.getPrincipal()+"]拥有winnebago:drive:eagle5权限！");
        } else {
            log.info("对不起，你不被允许winnebago:drive:eagle5！");
        }

        //7.完成操作，注销
        currentUser.logout();

        System.exit(0);
    }
}
