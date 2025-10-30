/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Order saveOrder(Order order) {
		
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findOrder(Long id) throws OrderNotFoundException {
		
		Order order = orderRepository.findById(id)
				.orElseThrow(()-> new OrderNotFoundException("Order with id: "+id+" not found in the order list!"));
		
		return order;
	}
	
	// TODO public void deleteOrder(Long id)
	public void deleteOrder(Long id) {
		if (orderRepository.existsById(id)) {
			orderRepository.deleteById(id);
		}
	}
	// TODO public List<Order> findAllOrders()
	public List<Order> findAllOrders() {
		return orderRepository.findAll();
	}
	// TODO public List<Order> findByExpiryDate(LocalDate expiry, Pageable page)
	//skal dette være findOrdersByExpiryDate slik det står i oppgaveteksten eller
	//findByExpiryDate slik det står i TODO?
	public List<Order> findByExpiryDate(LocalDate expiry, Pageable pageable) {
		return orderRepository.findByExpiryBefore(expiry, pageable).getContent();
	}
	// TODO public Order updateOrder(Order order, Long id)
	public Order updateOrder(Order order, long id) throws OrderNotFoundException {
		Order order1 = findOrder(id);
		order1.setIsbn(order.getIsbn());
		order1.setExpiry(order.getExpiry());
		orderRepository.save(order1);
		return order1;
	}

	public List<Order> findAllOrders(Pageable pageable) {
		return orderRepository.findAll(pageable).getContent();
	}
}
