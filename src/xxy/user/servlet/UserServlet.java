package xxy.user.servlet;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.Session;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Commons;

import xxy.cart.domain.Cart;
import xxy.user.domain.User;
import xxy.user.service.UserException;
import xxy.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;
/*
 * User表述层
 */
public class UserServlet extends BaseServlet 
{
	private UserService service=new UserService();
	
	/**
	 * 激活用户
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String active(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		System.out.println("您激活了");
		String code=request.getParameter("code");
		try 
		{
			service.active(code);
			request.setAttribute("msg", "恭喜，激活成功！请马上登陆！");
			
		} 
		catch (UserException e)//激活失败
		{
			String msg=e.getMessage();
			request.setAttribute("msg", msg);
		}
		return "f:/jsps/msg.jsp";
	}
	/**
	 * 注册功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//1、封装表单数据到form对象中
		User form=CommonUtils.toBean(request.getParameterMap(), User.class);
		//2、补全uid和code
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
		/*
		 * 3、输入校验
		 * 		创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
		 */
		Map<String,  String> error=new HashMap<String, String>();
		//验证username
		String username=form.getUsername();
		if(username==null || username.trim().isEmpty())
			error.put("username", "用户名不能为空");
		else if(username.length()<3 || username.length()>10)
			error.put("username", "用户名长度必须为3~10之间");
		
		//验证password
		String password=form.getPassword();
		if(password==null || password.trim().isEmpty())
			error.put("password", "密码不能为空");
		else if(password.length()<3 || password.length()>10)
			error.put("password", "密码长度必须为3~10之间");
		
		//验证邮箱格式
		String email=form.getEmail();
		if(email==null || email.trim().isEmpty())
			error.put("email", "邮箱不能为空");
		else if(!email.matches("\\w+@\\w+\\.\\w+"))
			error.put("email", "邮箱格式错误");
		
		//判断是否存在错误信息
		if(error.size()>0)
		{
			//1、保存错误信息
			//2、保存表单数据
			//3、转发到regist.jsp
			request.setAttribute("error", error);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		//4、调用service完成注册
		try
		{
			service.regist(form);
		} catch (UserException e) 
		{
			/*
			 * 1、保存异常信息
			 * 2、保存form
			 * 3、转发到regist.jsp
			 */
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 发邮件,已准备配置文件
		 */
		//获取配置文件内容
		Properties props=new Properties();
		props.load(this.getClass().getClassLoader().
				getResourceAsStream("email_template.properties"));
		String host=props.getProperty("host");
		String uname=props.getProperty("uname");
		String pwd=props.getProperty("pwd");
		String from=props.getProperty("from");
		String to=form.getEmail();
		String subject=props.getProperty("subject");
		String content=props.getProperty("content");
		
		/*
		 * MessageFormat.format(String 参数1,Obeject...参数2)
		 * 参数1：模板  
		 * 		包含了点位符的字符串就是模板
		 * 		点位符：{0}、{1}、{2}
		 * 参数2：替换物
		 * 	
		 */
		//替换第一个占位符为code
		content=MessageFormat.format(content, form.getCode());//替换{0}
			
		javax.mail.Session session=MailUtils.createSession(host, uname, pwd);
		Mail mail=new Mail(from, to, subject, content);
		try 
		{
			MailUtils.send(session, mail);//发邮件
		} 
		catch (MessagingException e) 
		{
			
			e.printStackTrace();
		}
		
		/*
		 * 执行到这里，说明添加用户成功
		 * 1、保存成功信息
		 * 2、转发到msg.jsp
		 */
		request.setAttribute("msg", "恭喜,注册成功，请马上到邮箱激活");
		return "f:/jsps/msg.jsp";	
	}
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @return
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
	{
		User user=CommonUtils.toBean(request.getParameterMap(), User.class);
		try
		{
			user=service.login(user);
		}
		catch (UserException e)//输出错误信息
		{
			String msg=e.getMessage();
			request.setAttribute("msg", msg);
			request.setAttribute("form", user);
			return  "f:jsps/user/login.jsp";
		}
		System.out.println("登录成功");
		HttpSession session=request.getSession();

		
		
		//添加用户到session
		session.setAttribute("session_user", user);
		
		//给用户添加一个购物车
		session.setAttribute("cart", new Cart());
		session.setMaxInactiveInterval(60*60);
		return "r:/index.jsp";
	}
	/**
	 * 退出功能
	 * @param request
	 * @param response
	 * @return
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("quit");
		request.getSession().invalidate();
		return "r:/index.jsp";
	}
	
	
}
