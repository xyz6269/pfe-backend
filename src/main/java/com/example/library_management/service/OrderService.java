package com.example.library_management.service;

import com.example.library_management.entity.*;
import com.example.library_management.repository.NotificationRepository;
import com.example.library_management.repository.OrderRepository;
import com.example.library_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public void createOrder() {
        log.info("tf now");
        User currentUser = authenticationService.getCurrentUser();
        if(currentUser.getOrder() != null) {
            throw new RuntimeException("you already submitted an ordered that is still being processed");
        }
        log.info(String.valueOf(currentUser));
        log.info("testetestest");
        if (currentUser.getCart() == null){
            throw new RuntimeException("you're cart is empty");
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderOwner(currentUser.getEmail());
        order.setUser(currentUser);


        Notifications newNoti = Notifications.builder()
                .text("your order has been sublited")
                .user(currentUser)
                .build();

        log.info(order.toString());
        orderRepository.save(order);
    }

    public List<Book> orderDetails(Long id) {
        Order targetOrder = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("this user hasn't submitted an order yet"));
        User orderUser = targetOrder.getUser();

        return userService.getUserCart(orderUser.getId());
    }

    public String rejectOrder(Long id) {
        log.info("elon MID");
        Order orderToReject = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("no order"));
        User orderUser = orderToReject.getUser();
        orderRepository.delete(orderToReject);
        orderUser.getCart().clear();
        Notifications rejectionNoti = Notifications.builder()
                .user(orderUser)
                .text("your order was unfortunately rejected by the administration try again alternatively")
                .build();
        notificationRepository.save(rejectionNoti);
        return "your order has been terminated by the system";
    }

    public String validateOrder(Long id) {
        Order userOrder =orderRepository.findById(id).orElseThrow(()-> new RuntimeException("no order"));
        OrderTimer orderTimer = OrderTimer.builder()
                .order(userOrder)
                .pickUpDuration(new Date(System.currentTimeMillis()+ 1000*60*60*24))
                .build();

        return "your order has been validated come pick it up before";
    }

/*
    public void deleteOrder(Cart order) {
        orderRepository.delete(orderRepository.findById(order.getId()).orElseThrow(()-> new RuntimeException("NO order with this id: "+order.getId())));
    }

    public Cart getOrderbyId(long id) {
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("NO order with this id: "+id));
    }

    public List<Cart> getAllOrders() {
        return orderRepository.findAll();
    }

    public String ValditeOrder(long id) {
        orderRepository.findById(id);

        return "this order has been validated by the administration " +
                "please head to library on site to claim it";
    }

    public String RejectOrder(long id) {
        deleteOrder(orderRepository.findById(id).orElseThrow(()-> new RuntimeException()));

        return "this order has been unfortunately rejected by the system";
    }

    public List<Book> getOrder(String orderNumber) {
       return orderRepository
               .findByOrderNumber(orderNumber)
               .orElseThrow(RuntimeException::new)
               .getOrderedBooksList();
    }

    public Book maptoOrder(BooksDto orderedItemsDto) {
        Book orderedBooks = Book.builder()
                .id(orderedItemsDto.getId())
                .isbn(orderedItemsDto.getIsbn())
                .price(orderedItemsDto.getPrice())
        .build();
        return orderedBooks;
    }

 */

}
