/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // TODO - getAllBorrowOrders (@Mappings, URI=/orders, and method) + filter by expiry and paginate
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllBorrowOrders(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiry,
            Pageable pageable) {

        List<Order> orders = (expiry != null)
                ? orderService.findByExpiryDate(expiry, pageable)
                : orderService.findAllOrders();   // uten paginering (OK ifølge oppgaven)
        return ResponseEntity.ok(orders);
    }

    // TODO - getBorrowOrder (@Mappings, URI=/orders/{id}, and method)
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getBorrowOrder(@PathVariable Long id) throws OrderNotFoundException {
        Order order = orderService.findOrder(id);
        return ResponseEntity.ok(order);
    }

	// TODO - updateOrder (@Mappings, URI=/orders/{id}, and method)
    @PutMapping("/orders/{id}")
	public ResponseEntity<Order> updateOrder(@RequestBody Order order, @PathVariable long id) throws OrderNotFoundException {
        return new ResponseEntity<>(orderService.updateOrder(order, id), HttpStatus.OK);
    }
	// TODO - deleteBookOrder (@Mappings, URI=/orders/{id}, and method)
	@DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) throws OrderNotFoundException {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
