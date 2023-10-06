/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.service.AuthorService;


/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@GetMapping("/authors")
	public ResponseEntity<Object> getAllAuthors() {
		return new ResponseEntity<>(authorService.findAllAuthors(), HttpStatus.OK);
	}
	
	@GetMapping("/authors/{id}")
	public ResponseEntity<Object> getAuthorById(@PathVariable("id") long id) {
		Author author;
		try {
			author = authorService.findById(id);
		} catch(AuthorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(author, HttpStatus.OK);
	}
	
	@GetMapping("/authors/{id}/books")
	public ResponseEntity<Object> getAuthorsBooks(@PathVariable("id") long id) {
		Author author;
		try {
			author = authorService.findById(id);
		} catch(AuthorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(author.getBooks(), HttpStatus.OK);
	}
	
	@PostMapping("/authors")
	public ResponseEntity<Object> createAuthor(@RequestBody Author author) {
		Author created = authorService.saveAuthor(author);
		
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}
	
	@PutMapping("/authors/{id}")
	public ResponseEntity<Object> updateAuthors(@RequestBody Author author, @PathVariable("id") int id) {
		Author uauthor;
		try {
			uauthor = authorService.updateAuthor(id, author);
		} catch(AuthorNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(uauthor, HttpStatus.OK);
	}
	

}
