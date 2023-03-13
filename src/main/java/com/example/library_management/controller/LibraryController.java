package com.example.library_management.controller;


import com.example.library_management.DTO.*;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.Role;
import com.example.library_management.entity.User;
import com.example.library_management.repository.RoleRepository;
import com.example.library_management.repository.UserRepository;
import com.example.library_management.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lib")
@RequiredArgsConstructor
@Slf4j
public class LibraryController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final BookService bookService;
    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @GetMapping("/admin")
    public List<User> getEveryUsers() {
        log.info("testesttest");
        return userService.getallUsers();
    }
    @GetMapping("/admin/find/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public User getUsersById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }
    @DeleteMapping("/admin/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void getDeleteById(@PathVariable("id") long id) {
         userService.deleteUser(id);
    }
    @PostMapping("/user/add/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addToCart(@PathVariable("id") long id) {
        userService.addToCart(id);
    }

    @PostMapping("/admin/add/book")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@RequestBody BooksDto request) {
        bookService.addBook(request);
    }

    @GetMapping("/admin/cart/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> checkUserCart(@PathVariable("id") Long id) {
        return userService.getUserCart(id);
    }
    @GetMapping("/user/cart/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBYUsers(@PathVariable("id") Long id) {
        return userService.getUserCart(id);
    }


    @PostMapping("/user/order")
    @ResponseStatus(HttpStatus.CREATED)
    public String submitOrder(){
        orderService.createOrder();
        return "you're order has been submited";
    }
    @DeleteMapping("/admin/delete/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("id") long id) {
        bookService.removeBook(id);
    }
    @GetMapping("/admin/get/book/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public void getBook(@PathVariable("id") long id) {
        bookService.getBookbyId(id);
    }
    @GetMapping("/admin/getbyisbn/book/{isbn}")
    @ResponseStatus(HttpStatus.FOUND)
    public void getBookbyisbn(@PathVariable("isbn") String isbn) {
        bookService.getBookbyIsbn(isbn);
    }

    @PostMapping("/admin/reject/Order/{id}")
    public ResponseEntity<String> rejectOrder(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.rejectOrder(id));
    }



/*
    @GetMapping("/admin/order/{num}")
    public ResponseEntity<List<Book>> GetEOrderbyNum(@PathVariable("num") String ordernum) {
        return new ResponseEntity<>(orderService.getOrder(ordernum),HttpStatus.OK);
    }

    @DeleteMapping("/admin/reject/{id}")
    public ResponseEntity<Cart> rejectOrder(@PathVariable("id") long id) {
        orderService.RejectOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Cart> deleteOrder(@PathVariable("id") long id) {
        orderService.deleteOrder(orderService.getOrderbyId(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/admin/validate/{id}")
    public ResponseEntity<String> validation(@PathVariable("id") long id) {
        orderService.ValditeOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



 */
}
