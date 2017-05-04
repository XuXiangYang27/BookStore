package xxy.order.servlet;


import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xxy.cart.domain.Cart;
import xxy.cart.domain.CartItem;
import xxy.order.domain.Order;
import xxy.order.domain.OrderItem;
import xxy.order.service.OrderException;
import xxy.order.service.OrderService;
import xxy.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet 
{
	
	
	private OrderService service=new OrderService();
	
	/**
	 * 确认收货
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		LinkedList<E>
		String msg;
		try 
		{
			System.out.println((String)request.getParameter("oid"));
			service.confirm((String)request.getParameter("oid"));
			msg="确认收货成功！";	
		} 
		catch (OrderException e)
		{
			msg=e.getMessage();	
		}
		request.setAttribute("msg", msg);
		return "f:/jsps/msg.jsp";
	}
	/**
	 * 加载订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String oid=request.getParameter("oid");
		System.out.println(oid);
		Order order=service.load(oid);
		request.setAttribute("order", order);
		System.out.println(order.toString());
		return "f:/jsps/order/desc.jsp";
	}
	
	
	/**
	 * 我的订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		
		User user=(User) request.getSession().getAttribute("session_user");
		List<Order> orderList=service.myOrders(user.getUid());
		
		for (Order order : orderList) {
			System.out.println(order.toString());;
		}
		request.setAttribute("orderList", orderList);
		return "f:/jsps/order/list.jsp";
	}
	/**
	 * 添加订单
	 * 把session中的车用来生成Order对象
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//1、得到cart
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		//2、把cart封装成order对象
		Order order=new Order();
		
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new java.util.Date());
		order.setTotal(cart.getTotal());
		order.setState(1);
		User user=(User) request.getSession().getAttribute("session_user");
		order.setOwner(user);
		System.out.println(user.getUid());
		order.setAddress(null);
		
		//创建条目集合
		List<OrderItem> items=new ArrayList<OrderItem>();
		for(CartItem item:cart.getCartItem())
		{
			OrderItem oi=new OrderItem();
			
			oi.setBook(item.getBook());
			oi.setCount(item.getCount());
			oi.setIid(CommonUtils.uuid());
			oi.setOrder(order);
			oi.setSubtotal(item.getSubtotal());
			
			items.add(oi);
		}//把订单条目添加到订单中
		order.setOrderItemList(items);
		
		//清空购物车
		cart.clear();
		
		//3、调用service添加订单
		service.add(order);
		
		request.setAttribute("order", order);
		return "/jsps/order/desc.jsp";
	}
}
