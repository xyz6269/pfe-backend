package com.example.library_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "history")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;
    private enum Status {
        REJECTED,
        GRANTED
    }
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> order;
}
