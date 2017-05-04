package xxy.user.service;

import xxy.user.dao.UserDao;
import xxy.user.domain.User;
/*
 * User 业务层
 */
public class UserService

{
	private UserDao dao=new UserDao();
	/**
	 * 
	 * @param 表单信息封装成的User对象
	 * @throws UserException 
	 */
	public void regist(User from) throws UserException
	{
		//判断用户名是否存在
		User user=dao.findByUsername(from.getUsername());
		
		if(user!=null)
			throw new UserException("用户名已存在");
		
		//判断邮箱是否被注册
		user=dao.findByEmail(from.getEmail());

		if(user!=null)
		{
			System.out.println("eamil:"+user.getEmail());
			throw new UserException("邮箱已被注册");
		}
		
		//添加这个User到数据库
		dao.add(from);
	}
	/**
	 * 激活用户
	 * @param code
	 * @throws UserException 
	 */
	public void active(String code) throws UserException 
	{
		
		User user=dao.findByCode(code);
		
		//判断激活码是否为空
		if(code==null || code.trim().isEmpty())
			throw new UserException("您请求了错误的激活地址！");
		//判断激活码是否存在
		if(user==null)
			throw new UserException("激活码不存在！");
		//判断用户是否已经激活过了
		if(user.isState()==true)
			throw new UserException("您已经激活过了！");
		
		//激活用户
		dao.updateByCode(code);
			
		
			
		
	}
	public User login(User user) throws UserException 
	{
		String username=user.getUsername();
		User userDB;
		
		//判断用户名是否存在
		userDB=dao.findByUsername(username);
		if(userDB==null)
			throw new UserException("用户名不存在!");
		
		//判断密码是否正确
		String password=user.getPassword();
		if(!password.equals(userDB.getPassword()))
			throw new UserException("密码错误！");
		
		//判断用户是否激活
		if(userDB.isState()==false)
			throw new UserException("账户尚未激活");
		
		return userDB;
	}
}
