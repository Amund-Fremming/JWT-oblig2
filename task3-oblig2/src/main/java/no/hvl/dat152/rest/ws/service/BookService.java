/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	// Denne må endres, men får DB feil om man lagrer direkte
	public Book saveBook(Book book) {
		
		Book toSave = findById(book.getId());
		if(toSave != null)
			return null;
		
		
		return bookRepository.save(book);		
	}
	
	public Book updateBook(Book book, String isbn) 
			throws BookNotFoundException, UpdateBookFailedException {
		
		Book returnbook = findByISBN(isbn);
		
		returnbook.setAuthors(book.getAuthors());
		returnbook.setTitle(book.getTitle());
		returnbook.setIsbn(book.getIsbn());
		
		try {
			returnbook = bookRepository.save(returnbook);
		}catch(Exception e) {		// we can also collect the exception object and pass it to our custom exception
			throw new UpdateBookFailedException("Update of book id = "+ returnbook.getId() + " failed!");
		}
		
		return returnbook;
	}
	
	public List<Book> findAll(){
		
		return (List<Book>) bookRepository.findAll();
		
	}
	
	public List<Book> findAllPaginate(Pageable page){
		
		Page<Book> books = bookRepository.findAll(page);
		
		return books.getContent();
	}
	
	public Book findById(long id) throws BookNotFoundException{
		
		Book book = findBookById(id);
		
		return book;
	}
	
	public Book findByISBN(String isbn) throws BookNotFoundException {
		
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
		
		return book;
	}
	
	public Set<Author> findAuthorsByBookISBN(String isbn) throws BookNotFoundException{
		
		Book books = findByISBN(isbn);
		Set<Author> authorsForBook = books.getAuthors();
				
		return authorsForBook;
		
	}
	
	public void deleteByISBN(String isbn) throws BookNotFoundException {
		Book book = findByISBN(isbn);
		
		bookRepository.delete(book);
	}
	
	private Book findBookById(long id) throws BookNotFoundException {
		
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException("Book with id = "+id+" not found!"));
		
		return book;
	}
}
