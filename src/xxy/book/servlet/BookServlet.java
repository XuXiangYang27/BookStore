package xxy.book.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xxy.book.domain.Book;
import xxy.book.service.BookService;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet
{
	private BookService service=new BookService();
	
	//查询所有
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		request.setAttribute("bookList", service.findAll());
		
		return "f:/jsps/book/list.jsp";
	}
	//按分类查询
	public String findByCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String cid=request.getParameter("cid");
		System.out.println(cid);
		request.setAttribute("bookList", service.findByCategory(cid));
		
		return "f:/jsps/book/list.jsp";
	}
	//显示细节
	public String detail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//1、获得bid
		String bid=(String)request.getParameter("bid");
		Book book=service.load(bid);
		request.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
}
