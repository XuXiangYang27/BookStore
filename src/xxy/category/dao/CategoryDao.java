package xxy.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import xxy.category.domain.Category;

import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao 
{
	private QueryRunner qr=new TxQueryRunner();

	//查询所有分类
	public List<Category> findAll() 
	{
		try
		{
			String sql="select * from category";
			return qr.query(sql, new BeanListHandler<Category>(Category.class));
		}
		catch(SQLException e)
		{
			throw new RuntimeException();
		}
	}
}
