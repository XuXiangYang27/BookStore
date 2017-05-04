package xxy.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import xxy.user.domain.User;

import cn.itcast.jdbc.TxQueryRunner;

public class UserDao 
{
	/*
	 * User持久层
	 */
	private QueryRunner qr=new TxQueryRunner();
	//按username查询
	public User findByUsername(String username)
	{
		try 
		{
			String sql="select * from tb_user where username=?";
			return qr.query(sql,new BeanHandler<User>(User.class),username);
		} catch (SQLException e) 
		{
			throw new RuntimeException();
		}
	}
	//按email查询
	public User findByEmail(String email)
	{
		try 
		{
			String sql="select * from tb_user where email=?";
			return qr.query(sql,new BeanHandler<User>(User.class),email);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	//插入user
	public void add(User	 user)
	{
		try 
		{
			String sql="insert into tb_user values(?,?,?,?,?,?)";
			Object[] params={user.getUid(),user.getUsername(),
					user.getPassword(),user.getEmail(),user.getCode(),user.isState()};
			qr.update(sql, params);
		} catch (SQLException e)
		{
			throw new RuntimeException();
		}
	}
	//按code查询
	public User findByCode(String code)
	{
		String sql="select * from tb_user where code=?";
		
		
		try {
			return qr.query(sql, new BeanHandler<User>(User.class), code );
		} catch (SQLException e)
		{
			throw new RuntimeException();
		}
	}
	
	//根据code的跟新state的值
	public boolean updateByCode(String	code)
	{
		String sql="update tb_user set state='1' where code=?";
		
		try {
			qr.update(sql, code);
		} catch (SQLException e) 
		{
			throw new RuntimeException();
		}
		return true;
	}
}
