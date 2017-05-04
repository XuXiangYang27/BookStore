package xxy.category.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import xxy.category.service.CategoryService;

public class CategoryServlet extends BaseServlet
{
	private CategoryService service=new CategoryService();
	
	
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		
		request.setAttribute("categoryList",service.findAll());
		
		return "f:/jsps/left.jsp";
	}
}
