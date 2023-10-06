package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public User saveUser(User user) {
		
		user = userRepo.save(user);
		
		return user;
	}
	
	public void deleteUser(Long id) throws UserNotFoundException {
		userRepo.deleteById(id);
	}
	
	public User updateUser(User user, Long id) throws UserNotFoundException {
		User userToUpdate = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: "+id+" not found"));
		userToUpdate.setFirstname(user.getFirstname());
		userToUpdate.setLastname(user.getLastname());
		userToUpdate.setOrders(user.getOrders());
		userRepo.save(userToUpdate);
		
		return userToUpdate;
	}
	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepo.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long id) throws UserNotFoundException {
		
		User user = userRepo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+id+" not found"));
		
		return user;
	}
	
	public Set<Order> findOrdersForUser(Long id) throws UserNotFoundException{
		
		User user = findUser(id);
		
		return user.getOrders();
	}
	
	public User createOrdersForUser(Long id, Order order) throws UserNotFoundException{
		User user = userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User with id: "+id+" not found"));
		Set<Order> orders = user.getOrders();
		orders.add(order);
		
		user.setOrders(orders);
		return user;
	}
}
