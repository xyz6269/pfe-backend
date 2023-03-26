package com.example.library_management.controller;

import com.example.library_management.DTO.AuthenticationRequest;
import com.example.library_management.DTO.AuthenticationResponse;
import com.example.library_management.DTO.BooksDto;
import com.example.library_management.DTO.RegisterRequest;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.User;
import com.example.library_management.repository.UserRepository;
import com.example.library_management.service.AuthenticationService;
import com.example.library_management.service.BookService;
import com.example.library_management.service.OrderService;
import com.example.library_management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lib/admin")
@RequiredArgsConstructor
@CrossOrigin()
@Slf4j
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;
    private final BookService bookService;
    @GetMapping("/allusers")
    public List<User> getEveryUsers() {
        log.info("testesttest");
        return userService.getallUsers();
    }
    @GetMapping("/finduser/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public User getUsersById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/deleteuser/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/banuser/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void banUserById(@PathVariable("id") long id) {
        userService.banUser(id);
    }

    @PostMapping("/add/book")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@RequestBody BooksDto request) {
        bookService.addBook(request);
    }

    @GetMapping("/checkcart/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> checkUserCart(@PathVariable("id") Long id) {
        return userService.getOrderDetails(id);
    }

    @DeleteMapping("/delete/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("id") long id) {
        bookService.removeBook(id);
    }

    @GetMapping("/get/book/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public void getBook(@PathVariable("id") long id) {
        bookService.getBookbyId(id);
    }

    @GetMapping("/getbyisbn/book/{isbn}")
    @ResponseStatus(HttpStatus.FOUND)
    public void getBookbyisbn(@PathVariable("isbn") String isbn) {
        bookService.getBookbyIsbn(isbn);
    }

    @DeleteMapping("/reject/Order/{id}")
    public ResponseEntity<String> rejectOrder(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.rejectOrder(id));
    }
    @PostMapping("/validateorder/{id}")
    public String validateOrder(@PathVariable("id") Long id) {
        return orderService.validateOrder(id);
    }
    @PostMapping("/refreshing")
    public void refreshOrders() {
        orderService.refresh();
    }
}