package xxy.cart.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xxy.book.service.BookService;
import xxy.book.domain.Book;
import xxy.cart.domain.Cart;
import xxy.cart.domain.CartItem;
import cn.itcast.servlet.BaseServlet;

public class CartServlet extends BaseServlet
{
	
	/**
	 * 添加购物条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		//1、得到车
		Cart cart=(Cart)request.getSession().getAttribute("cart");
		//2、得到条目
		String bid =request.getParameter("bid");
		Book book=new BookService().load(bid);
		int count=Integer.parseInt(request.getParameter("count"));
		CartItem cartItem=new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		//3、把条目添加到车中
		cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 清空购物条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String clear(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 删除购物条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		//1、得到车
		Cart cart=(Cart)request.getSession().getAttribute("cart");
		//2、得到bid
		String bid=request.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
}
