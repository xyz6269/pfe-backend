package com.example.library_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "timer")
@Entity
public class OrderTimer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date pickUpDuration;
    private Date returnDate;
    @OneToOne
    @JsonIgnore
    private Order order;


}
