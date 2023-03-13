package com.example.library_management.service;


import com.example.library_management.entity.Book;
import com.example.library_management.entity.Role;
import com.example.library_management.entity.User;
import com.example.library_management.repository.BooksRepository;
import com.example.library_management.repository.RoleRepository;
import com.example.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final BooksRepository booksRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;

    public List<User> getallUsers() {
        log.info("ayyyyyyyyyyyyyyyyyyyyyyyyo wtf");
        return userRepository.findAll();
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
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
        Role role = roleRepository.findByName("Admin")
                .orElseThrow(
                        ()->new RuntimeException()
                );
        var list = user.getRoles();
        list.add(role);
        user.setRoles(list);
    }
    public void addToCart(long id) {
        log.info("hiiiiiiiiiiiiiiiiiiiiiiiiii looooooooooooooooooooooooool");
        User currentUser = authenticationService.getCurrentUser();
        log.info("  joeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        if (currentUser.getOrder() != null) throw new RuntimeException("you can't modify your cart while your order is submitted and being processed");
        log.info(currentUser.getCart().toString());
        log.info(currentUser.getEmail()+"  hhhhhhhhhhhhhhhhhhhhhhh");
        Book bookToAdd = booksRepository.findById(id)
                .orElseThrow(
                        ()-> new RuntimeException
                                ("this book isn't available")
                );

         if(currentUser.checkIfExistBook(bookToAdd)) {
             throw new RuntimeException("you can't have 2 copies of the same book");
         }
        if (bookToAdd.getQuantity() == 0) throw new RuntimeException("this book is outta stock");
        if (currentUser.getCart().stream().count() >= 4) {
            log.info("noooooooooooooooooooooooooooooooooooooooooOOOOOOOOO");
            throw new RuntimeException("cart at full capacity");
        }
        else {
            log.info("DMC=<MGRR");
            currentUser.getCart().add(bookToAdd);
        }
        log.info(currentUser.getCart().toString()+" ayoooooooooooooooooooo wtf");
    }

    public List<Book> getUserCart(Long id) {
        User target = userRepository.findById(id)
                .orElseThrow(
                        ()-> new RuntimeException("user doesn't exist")
                );
        return target.getCart();
    }

    public List<Book> checkMyCart() {
        User currentUser = authenticationService.getCurrentUser();
        return currentUser.getCart();
    }




}
