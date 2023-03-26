package com.example.library_management.service;


import com.example.library_management.entity.*;
import com.example.library_management.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;
    private final BooksRepository booksRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final BookService bookService;
    private final BanList banList;


    public List<User> getallUsers() {
        log.info("ayyyyyyyyyyyyyyyyyyyyyyyyo wtf");
        return userRepository.findAll();
    }

    public void deleteUser(long id) {
        User userToDelte = getUserById(id);
        userToDelte.getBooks().forEach(book -> book.getUser().clear());
        userToDelte.getBooks().clear();
        userRepository.delete(userToDelte);
    }

    public User getUserById(long id)  {
        return userRepository.
                findById(id).
                orElseThrow(
                        ()-> new RuntimeException("user not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.
                findByEmail(email).
                orElseThrow(
                        ()-> new RuntimeException("user not found"));
    }

    public void promoteUser(long id){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("user doesn't exist"));
        Role role = roleRepository.findById(1L)
                .orElseThrow(
                        ()->new RuntimeException()
                );
        var list = user.getRoles();
        list.add(role);
        user.setRoles(list);
    }

    public void addToCart(long id) {
        User currentUser = authenticationService.getCurrentUser();
        log.info(currentUser.getBooks().toString()+ "shiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiit");
        if (currentUser.getOrder() != null) throw new RuntimeException("you can't modify your cart while your order is submitted and being processed");
        log.info(currentUser.getBooks().toString());
        log.info(currentUser.getEmail()+"  hhhhhhhhhhhhhhhhhhhhhhh");
        Book bookToAdd = booksRepository.findById(id)
                .orElseThrow(
                        ()-> new RuntimeException
                                ("this book isn't available")
                );

        log.info(currentUser.getBooks().toString().toString() + currentUser.getEmail());
        if(currentUser.getBooks().stream().anyMatch(book -> book==bookToAdd)) {
             throw new RuntimeException("you can't have 2 copies of the same book");
        }
        if (bookToAdd.getQuantity() == 0) throw new RuntimeException("this book is outta stock");

        if (currentUser.getBooks().stream().count() >= 4) {
            log.info("noooooooooooooooooooooooooooooooooooooooooOOOOOOOOO");
            throw new RuntimeException("cart at full capacity");
        }
        log.info("DMC=<MGRR");
        currentUser.addBooktoCart(bookToAdd);

        userRepository.save(currentUser);
        booksRepository.save(bookToAdd);

        log.info(currentUser.getBooks().toString()+" ayoooooooooooooooooooo wtf");
    }

    public void removeBookFromCart(Long id) {
        User currentUser = authenticationService.getCurrentUser();
        Book bookToRemove = booksRepository.findById(id).orElseThrow(()-> new RuntimeException("book isn't available"));
        if (currentUser.getBooks().isEmpty()){
            throw new RuntimeException("your cart is already empty ");
        }
        currentUser.getBooks().remove(bookToRemove);
    }

    public void emptyCart() {
        User currentUser = authenticationService.getCurrentUser();
        currentUser.getBooks().removeAll(currentUser.getBooks());
    }

    public List<Book> getUserCart(Long id) {
        List<Integer> orderingList = new ArrayList<>();
        List<Book> orderDetails = new ArrayList<>();
        User target = userRepository.findById(id)
                .orElseThrow(
                        ()-> new RuntimeException("user doesn't exist")
                );
        
        return target.getBooks();
    }

    public List<Book> getOrderDetails(Long id) {
        Order userOrder = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("no order"));
        User targetUser = userOrder.getUser();
        List<Book> userCart = targetUser.getBooks();
        Collections.sort(userCart,Comparator.comparing(Book::getQuantity));

        return userCart;
    }

    public List<Book> checkMyCart() {
        User currentUser = authenticationService.getCurrentUser();
        return currentUser.getBooks();
    }

    public void deleteBookFromCart(Long id) {
        User currentUser= authenticationService.getCurrentUser();
        Book bookToRemove = bookService.getBookbyId(id);
        currentUser.getBooks().remove(bookToRemove);
    }

    public void banUser(Long id) {
        String bannedUserEmail = getUserById(id).getEmail();
        deleteUser(id);
        BannedUser bannedUser = new BannedUser();
        bannedUser.setUserEmail(bannedUserEmail);
        banList.save(bannedUser);
    }




}
