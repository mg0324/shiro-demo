package demo.spring.web;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;


public class JdbcRealm implements Realm{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		String username = (String)token.getPrincipal();  //得到用户名  
        String password = new String((char[])token.getCredentials()); //得到密码

        Map<String,Object> res = null;
        try{
        	res = jdbcTemplate.queryForMap("select password from shiro_user where username='"+username+"'");
        }catch(Exception e){
        	throw new UnknownAccountException(); //如果用户名错误 
        }
        String pwd = null;
        if(res!=null){
        	pwd = res.get("password").toString();
        }
        if(pwd==null){
        	throw new UnknownAccountException(); //如果用户名错误  
        }else{
        	if(pwd.equals(password)){
        		//如果身份认证验证成功，返回一个AuthenticationInfo实现；  
                return new SimpleAuthenticationInfo(username, password, getName());
        	}else{
        		throw new IncorrectCredentialsException(); //如果密码错误  
        	}
        }
          
	}

	@Override
	public String getName() {
		return "jdbcRealm";  
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		//仅支持UsernamePasswordToken类型的Token  
        return token instanceof UsernamePasswordToken;
	}

	

	

}
