package xxy.book.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import xxy.book.domain.Book;

import cn.itcast.jdbc.TxQueryRunner;

public class BookDao 
{
	QueryRunner qr=new TxQueryRunner();
	
	public List<Book	> findAll()
	{
		try
		{
		String sql="select * from book";
		return qr.query(sql, new BeanListHandler<Book>(Book.class));
		}
		catch(SQLException e)
		{
			throw new RuntimeException();
		}
	}

	public List<Book> findByCategory(String cid) 
	{
		try
		{
		String sql="select * from book where cid=?";
		return qr.query(sql, new BeanListHandler<Book>(Book.class),cid);
		}
		catch(SQLException e)
		{
			throw new RuntimeException();
		}
	}

	public Book load(String bid)
	{
		try
		{
			String sql="select * from book where bid=?";
			return qr.query(sql, new BeanHandler<Book>(Book.class),bid);
		}
		catch (SQLException e) 
		{
			throw new RuntimeException();
		}
	}
}
