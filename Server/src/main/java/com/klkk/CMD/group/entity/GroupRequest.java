package com.klkk.CMD.group.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "group_request")
public class GroupRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "group_id")
    private Long groupId;
    private Boolean opt1;
    private Boolean opt2;
    private Boolean opt3;
    private String opt4;
    private String techStack;
    private String comment;
    private Boolean permit;

}