package com.example.greenproject.model;

import com.example.greenproject.dto.res.UserDtoLazy;
import com.example.greenproject.model.enums.UserType;
import com.example.greenproject.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;
    private String imgUrl;
    private Integer points=0;
    private String phoneNumber;
    private String fullName;

    @OneToOne(mappedBy = "user")
    private ResetPasswordToken resetPasswordToken;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "_user_role",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Role> roles=new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "_user_voucher",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id",
                    referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Voucher> vouchers=new HashSet<>();

    @PrePersist
    @PreUpdate
    public void trimData() {
        this.username = this.username.trim();
        this.email = this.email.trim();


    }

    public UserDtoLazy mapToUserDtoLazy(){
        UserDtoLazy userDtoLazy = new UserDtoLazy();
        userDtoLazy.setId(id);
        userDtoLazy.setUsername(username);
        userDtoLazy.setImageUrl(imgUrl);
        return userDtoLazy;
    }
}
