package xxy.book.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import xxy.book.dao.BookDao;
import xxy.book.domain.Book;

public class BookService
{
	BookDao dao=new BookDao();
	
	public List<Book	> findAll()
	{
		return dao.findAll();
	}

	public List<Book> findByCategory(String cid) {
		
		return dao.findByCategory(cid);
	}
	
	public Book load(String bid)
	{
		return dao.load(bid);
	}
}
