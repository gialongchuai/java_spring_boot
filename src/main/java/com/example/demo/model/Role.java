package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_role")
@Entity(name = "Role")
public class Role extends AbstractEntity<Integer> {

    @Column(name = "name")
    private String name;

//    @Column(name = "description")
//    private String description;

    @OneToMany(mappedBy = "role")
    private Set<RoleHasPermission> roleHasPermissions = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<UserHasRole> userHasRoles = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<Group> groups = new HashSet<>();
}
