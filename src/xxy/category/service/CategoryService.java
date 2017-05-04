package xxy.category.service;

import java.util.List;

import xxy.category.dao.CategoryDao;
import xxy.category.domain.Category;

public class CategoryService
{
	private CategoryDao dao=new CategoryDao();
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() 
	{
		
		return dao.findAll();
	}
	
	
}
