package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_token")
@Entity(name = "Token")
public class Token extends AbstractEntity<Long> {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;
}
