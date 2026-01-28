package com.example.demo.dto.request;

import com.example.demo.model.AbstractEntity;
import com.example.demo.model.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {

    private String apartmentNumber;

    private String floor;

    private String building;

    private String streetNumber;

    private String street;

    private String city;

    private String country;

    private Integer addressType; // gán kiểu wrapper ko truyền là null kẻo int = 0

}