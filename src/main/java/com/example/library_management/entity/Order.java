package com.example.library_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String orderNumber;
    private String orderOwner;
    private LocalDateTime createdAt;
    @OneToOne
    @JsonIgnore
    private User user;
    @OneToOne()
    @JsonIgnore
    private OrderTimer timer;
    @JsonIgnore
    private Boolean isOrderValid;



}