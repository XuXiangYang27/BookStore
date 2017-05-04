package xxy.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import xxy.order.dao.OrderDao;
import xxy.order.domain.Order;

public class OrderService 
{
	private OrderDao dao=new OrderDao();
	
	/*
	 * 添加订单
	 * 需要处理事务
	 */
	public void add(Order order)
	{
		try
		{
			//开启事物
			JdbcUtils.beginTransaction();
			
			dao.addOrder(order);//插入订单
			dao.addOrderItemList(order.getOrderItemList());//插入订单条目
			
			//提交事物
			JdbcUtils.commitTransaction();
		}
		catch (Exception e) 
		{
			System.out.println("回滚");
			//回滚事物
			try
			{
				JdbcUtils.rollbackTransaction();
			}
			catch(SQLException e1)
			{
				throw new RuntimeException();
			}
		}
	}
	/**
	 * 查询我的订单
	 * @param uid
	 * @return
	 */
	public List<Order> myOrders(String uid) 
	{
		
		return dao.findByUid(uid);
	}

	/**
	 * 加载指定订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid) 
	{
		
		return dao.load(oid);
	}
	/**
	 * 确认收货
	 * @throws OrderException
	 */
	public void confirm(String oid) throws OrderException
	{
		int state=dao.getStateByOid(oid);
		
		if(state!=3)//先验证订单状态是否合法
			throw new OrderException("订单状态错误，不能确认收货");
		else//修改订单状态 ，表示确认收货成功
		{
			dao.updateState(oid,4);
		}
	}
}
