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
		String username = (String)token.getPrincipal();  //�õ��û���  
        String password = new String((char[])token.getCredentials()); //�õ�����

        Map<String,Object> res = null;
        try{
        	res = jdbcTemplate.queryForMap("select password from shiro_user where username='"+username+"'");
        }catch(Exception e){
        	throw new UnknownAccountException(); //����û������� 
        }
        String pwd = null;
        if(res!=null){
        	pwd = res.get("password").toString();
        }
        if(pwd==null){
        	throw new UnknownAccountException(); //����û�������  
        }else{
        	if(pwd.equals(password)){
        		//��������֤��֤�ɹ�������һ��AuthenticationInfoʵ�֣�  
                return new SimpleAuthenticationInfo(username, password, getName());
        	}else{
        		throw new IncorrectCredentialsException(); //����������  
        	}
        }
          
	}

	@Override
	public String getName() {
		return "jdbcRealm";  
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		//��֧��UsernamePasswordToken���͵�Token  
        return token instanceof UsernamePasswordToken;
	}

	

	

}
