package com.example.greenproject.security;

import lombok.Getter;
import lombok.*;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private List<String> authorities;


    List<GrantedAuthority> getAllAuthorities() {
        if (authorities == null) return new ArrayList<>();
        return authorities.stream().map(s -> (GrantedAuthority) () -> s).toList();
    }


}
