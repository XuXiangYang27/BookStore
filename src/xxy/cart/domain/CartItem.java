package xxy.cart.domain;

import java.math.BigDecimal;

import xxy.book.domain.Book;

/*
 * 购物车条目类
 */
public class CartItem 
{
	private Book book;//商品
	private int count;//数量
	
	//小计方法，但它没有对应的成员
	public double getSubtotal()
	{
		//使用BigDecimal处理浮点类型精度问题
		BigDecimal d1=new BigDecimal(book.getPrice()+"");
		BigDecimal d2=new BigDecimal(count+"");
		return d1.multiply(d2).doubleValue();
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
