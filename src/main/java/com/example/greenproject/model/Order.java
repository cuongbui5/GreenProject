package com.example.greenproject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_order")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "contact_id",referencedColumnName = "id")
    private Contact contact;

}
