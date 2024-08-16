package com.example.greenproject.model;

import jakarta.persistence.*;


import lombok.*;

@Entity
@Table(name = "_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
