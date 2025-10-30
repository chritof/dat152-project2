/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Map;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){

        List<User> users = userService.findAllUsers();

        if(users.isEmpty())

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or (authentication.details != null && authentication.details.userid == #id)")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserNotFoundException, OrderNotFoundException{

        User user = userService.findUser(id);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    // TODO - createUser (@Mappings, URI=/users, and method)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user){
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }
    // TODO - updateUser (@Mappings, URI, and method)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) throws UserNotFoundException {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    // TODO - deleteUser (@Mappings, URI, and method)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    // TODO - getUserOrders (@Mappings, URI=/users/{id}/orders, and method)
    @PreAuthorize("hasAuthority('ADMIN') or (authentication.details != null && authentication.details.userid == #userid)")
    @GetMapping("/users/{id}/orders")
    public ResponseEntity<Object> getUserOrders(@PathVariable("id") Long userid) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserOrders(userid), HttpStatus.OK);
    }
    // TODO - getUserOrder (@Mappings, URI=/users/{uid}/orders/{oid}, and method)
    @PreAuthorize("hasAuthority('ADMIN') or (authentication.details != null && authentication.details.userid == #userid)")
    @GetMapping("/users/{uid}/orders/{oid}")
    public ResponseEntity<Order> getUserOrder(@PathVariable("uid") Long userid, @PathVariable("oid") Long oid) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserOrder(userid, oid), HttpStatus.OK);
    }
    // TODO - deleteUserOrder (@Mappings, URI, and method)
    @PreAuthorize("hasAuthority('ADMIN') or (authentication.details != null && authentication.details.userid == #userid)")
    @DeleteMapping("/users/{userid}/orders/{oid}")
    public ResponseEntity<Void> deleteUserOrder(@PathVariable long userid, @PathVariable long oid) throws UserNotFoundException {
        userService.deleteOrderForUser(userid, oid);
        return ResponseEntity.ok().build();
    }
    // TODO - createUserOrder (@Mappings, URI, and method) + HATEOAS links
    @PreAuthorize("hasAuthority('ADMIN') or (authentication.details != null && authentication.details.userid == #userid)")
    @PostMapping("/users/{userid}/orders")
    public ResponseEntity<Object> createUserOrder(
            @RequestBody Order order,
            @PathVariable long userid)
            throws UserNotFoundException, OrderNotFoundException {

        var user = userService.createOrdersForUser(userid, order);

        for (Order o : user.getOrders()) {
            o.add(linkTo(methodOn(OrderController.class).getBorrowOrder(o.getId())).withSelfRel());
            o.add(linkTo(methodOn(OrderController.class).deleteOrder(o.getId())).withRel("delete"));
            o.add(linkTo(methodOn(OrderController.class).updateOrder(o, o.getId())).withRel("update"));
        }

        return new ResponseEntity<>(user.getOrders(), HttpStatus.CREATED);

    }

}

