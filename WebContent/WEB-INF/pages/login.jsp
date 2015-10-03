<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%> 
<title>shiro-simple-demo</title>
<h2>登陆页：</h2>
<form action="${root}/user?action=loginUser" method="post">
	用户名：<input type="text" name="loginname" value="${loginname }"/><br/><br/>
	密　码：<input type="text" name="pwd" value="${pwd }"/><br/><br/>
	记住我 <input type="checkbox" name="rememberMe" value="true"/><br/><br/>
	<button type="submit">登陆</button> <button type="resets">重置</button>
	<span style="color:red;">${msg }</span>
</form>