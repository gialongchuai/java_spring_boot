package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user_has_group")
@Entity(name = "GroupHasUser")
public class GroupHasUser extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
}
