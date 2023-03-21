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
import java.time.LocalDateTime;
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
        if (currentUser.getOrder() != null) {
            throw new RuntimeException("you already submitted an ordered that is still being processed");
        }
        log.info(String.valueOf(currentUser));
        log.info("testetestest");
        log.info(currentUser.getCart().toString());
        if (currentUser.getCart().isEmpty()) {
            throw new RuntimeException("you're cart is empty");
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderOwner(currentUser.getEmail());
        order.setUser(currentUser);


        Notifications newNoti = Notifications.builder()
                .text("your order has been submited")
                .user(currentUser)
                .build();
        currentUser.getNotifications().add(newNoti);
        log.info(order.toString());

        orderRepository.save(order);
    }

    public List<Book> orderDetails(Long id) {
        Order targetOrder = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("this user hasn't submitted an order yet"));
        User orderUser = targetOrder.getUser();

        return userService.getUserCart(orderUser.getId());
    }

    public String rejectOrder(Long id) {
        log.info("elon MID");
        Order orderToReject = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("no order"));
        User orderUser = orderToReject.getUser();
        orderRepository.delete(orderToReject);
        orderUser.getCart().clear();
        Notifications rejectionNoti = Notifications.builder()
                .user(orderUser)
                .text("your order was unfortunately rejected by the administration try again alternatively")
                .build();
        notificationRepository.save(rejectionNoti);
        orderUser.getNotifications().add(rejectionNoti);
        return "your order has been terminated by the system";
    }

    public String validateOrder(Long id) {
        Order userOrder = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("no order"));
        userOrder.setCreatedAt(LocalDateTime.now());
        userOrder.setIsOrderValid(true);
        orderRepository.save(userOrder);
        pickUpOrder(userOrder.getId());

        return "your order has been validated come pick it up before";
    }

    public boolean checkIsValide(Order order) {
        return order.getIsOrderValid();
    }

    public String pickUpOrder(Long orderId) {
        Order targetOrder = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("no order"));
        if (!checkIsValide(targetOrder)) {
            throw new RuntimeException("this order hasn't been validated");
        }

        LocalDateTime no = targetOrder.getCreatedAt();
        //long time = 24*3600;ong time = 24*3600;
        //long diff = (long) (LocalDateTime.now() - targetOrder.getCreatedAt().plusSeconds(time));
        long time =  3600;
        LocalDateTime end = no.plusSeconds(time);

        if (no.getDayOfMonth() >= end.getDayOfMonth()
                && no.getHour() >= end.getHour()
                && no.getMinute() >= end.getMinute()) {
            orderRepository.delete(targetOrder);
            return "your too late to pickup your order";
        }else return "come pick up you're order";

    }
}