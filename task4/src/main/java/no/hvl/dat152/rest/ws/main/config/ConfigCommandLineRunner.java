package no.hvl.dat152.rest.ws.main.config;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
import no.hvl.dat152.rest.ws.repository.BookRepository;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import no.hvl.dat152.rest.ws.service.AuthorService;

@Component
class ConfigCommandLineRunner implements CommandLineRunner  {

  private static final Logger log = LoggerFactory.getLogger(ConfigCommandLineRunner.class);

  @Autowired
  AuthorService authorService;
  
  @Autowired
  BookRepository bookRepository;
  
  @Autowired
  AuthorRepository authorRepository;
  
  @Autowired
  UserRepository userRepository;
  
  @Autowired
  RoleRepository roleRepository;
  
  @Override
  public void run(String... args) throws Exception {
	  
	  authorRepository.saveAll(createDefaultAuthors());
	  bookRepository.saveAll(creatDefaultBooks());
	  roleRepository.saveAll(createDefaultRoles());
	  userRepository.saveAll(createDefaultUsersPlusOrders());
   
  }
  
	private Iterable<Author> createDefaultAuthors() {
		
		List<Author> authors = new ArrayList<Author>();
		
		authors.add(new Author("Shari", "Pfleeger"));
		authors.add(new Author("Perry", "Lea"));
		authors.add(new Author("James", "Kurose"));
		authors.add(new Author("Keith", "Ross"));
		authors.add(new Author("Martin", "Kleppmann"));
		
		return authors;
	}
	
	private Iterable<Book> creatDefaultBooks() throws AuthorNotFoundException {
		
		Author author1 = authorService.findById(1L);
		Author author2 = authorService.findById(2L);
		
		Set<Author> authors = new HashSet<Author>();
		Book book1 = new Book();
		book1.setIsbn("abcde1234");
		book1.setTitle("Software Engineering");
		authors.add(author1);
		book1.setAuthors(authors);
		
		Set<Author> authors2 = new HashSet<Author>();
		Book book2 = new Book();
		book2.setIsbn("ghijk1234");
		book2.setTitle("Computer Network");
		authors2.add(author1);
		authors2.add(author2);
		book2.setAuthors(authors2);
		
		Set<Author> authors3 = new HashSet<Author>();
		Book book3 = new Book();
		book3.setIsbn("qabfde1230");
		book3.setTitle("Real-Time Operating System");
		authors3.add(author1);
		book3.setAuthors(authors3);
		
		List<Book> books =  new ArrayList<Book>();		
		books.add(book1);
		books.add(book2);
		books.add(book3);
		
		return books;
	}
	
	private Iterable<User> createDefaultUsersPlusOrders(){
		
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		
		List<User> users = new ArrayList<>();
		
		// user1 (roles = USER)
		String robertPwd = pwdEncoder.encode("isaac_pwd");
		User user1 = new User("robert@email.com",robertPwd,"Robert", "Isaac");
		// orders
		Order order1 = new Order("ghijk1234", LocalDate.now().plusWeeks(2));		
		user1.addOrder(order1);
		// add roles
		Role role1 = roleRepository.findById(2).get();
		user1.addRole(role1);
		System.out.println("Role = "+role1.getName());
		
		
		//user2 (roles = USER)
		String kristinPwd = pwdEncoder.encode("kristin_pwd");
		User user2 = new User("kristin@email.com",kristinPwd,"Kristin", "Solberg");
		// add order
		Order order2_1 = new Order("abcde1234", LocalDate.now().plusWeeks(3));
		user2.addOrder(order2_1);
		// add roles
		Role role = roleRepository.findById(2).get();
		user2.addRole(role);
		
		//user 3  (roles = ADMIN, USER)
		String tosinPwd = pwdEncoder.encode("tosin_pwd");
		User user3 = new User("tosin@email.com", tosinPwd, "Tosin", "Oyetoyan");
		// add roles
		Role role31 = roleRepository.findById(1).get();
		Role role32 = roleRepository.findById(2).get();
		user3.addRole(role31);
		user3.addRole(role32);
		
		users.add(user1);
		users.add(user2);
		users.add(user3);
		
		return users;
		
	}
	
	// we take the roles we have configured in our property file (application.properties)
	@Value("#{'${user.resource.roles}'.split(',')}")
	private List<String> userRoles;
	
	private Iterable<Role> createDefaultRoles(){
		List<Role> roles = new ArrayList<>();
		for(String role : userRoles) {
			roles.add(new Role(role));
		}
		
		return roles;
	}

}
