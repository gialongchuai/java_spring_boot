package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_group")
@Entity(name = "Group")
public class Group extends AbstractEntity<Integer> {

    @Column(name = "name")
    private String name;

//    @Column(name = "description")
//    private String description;

    @OneToOne
    private Role role;

    @OneToMany(mappedBy = "group")
    private Set<GroupHasUser> groupHasUsers = new HashSet<>();
}
