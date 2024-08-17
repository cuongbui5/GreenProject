package com.example.greenproject.model;

import com.example.greenproject.utils.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})
        })
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = Constants.MESSAGE_EMPTY)
    @Email(message = "Email should be valid")
    private String email;
    private String username;
    private String password;

    @ManyToMany
    @JoinTable(name = "_user_role",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id")
    )
    private Set<Role> roles=new HashSet<>();
}
