/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
	
	public Author saveAuthor(Author author) {
		if(author == null) return null;
		
		return authorRepository.save(author);
	}
	
	public Author updateAuthor(long id, Author author) throws AuthorNotFoundException {
		Author a = authorRepository.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with id = "+id+" not found!"));
		
		a.setBooks(author.getBooks());
		a.setFirstname(author.getFirstname());
		a.setLastname(author.getLastname());
		
		return authorRepository.save(a);
	}
	
	public Author findById(long id) throws AuthorNotFoundException {
		
		Author author = authorRepository.findById(id)
				.orElseThrow(()-> new AuthorNotFoundException("Author with the id: "+id+ "not found!"));
		
		return author;
	}
	
	public List<Author> findAllAuthors() {
		return (List<Author>) authorRepository.findAll();
	}
	
	public Author findAllAuthorById(long id) throws AuthorNotFoundException {
		Author authors =  authorRepository.findById(id)
				.orElseThrow(()-> new AuthorNotFoundException("Author with the id: "+id+ "not found!"));
		
		return authors;
	}
	
}
