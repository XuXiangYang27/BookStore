package xxy.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * 购物车类
 */
public class Cart 
{
	private Map<String, CartItem> map=new LinkedHashMap<String, CartItem>();
	
	/**
	 * 返回合计
	 * @return
	 */
	public double getTotal()
	{
		BigDecimal tatol=new BigDecimal("0");
		
		for (CartItem item : map.values())
		{
			BigDecimal b1=new BigDecimal(item.getSubtotal()+"");
			tatol=tatol.add(b1);
		}
		return tatol.doubleValue();
	}
	
	/**
	 * 添加条目到购物车中
	 * @param cartItem
	 */
	public void add(CartItem cartItem)
	{
		if(map.containsKey(cartItem.getBook().getBid()))
		{
			CartItem item=map.get(cartItem.getBook().getBid());
			//新条目的 count等于新的count+老的count
			item.setCount(item.getCount()+cartItem.getCount());
			map.put(item.getBook().getBid(), item);
		}
		else
		{
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	
	/**
	 * 清空所有条目
	 */
	public void clear()
	{
		map.clear();
	}
	
	
	/**
	 * 删除指定条目
	 * @param bid
	 */
	public void delete(String bid)
	{
		map.remove(bid);
	}
	
	/**
	 * 获取所有条目
	 * @return
	 */
	public Collection<CartItem> getCartItem()
	{
		return map.values();
	}
}
