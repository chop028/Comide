package com.klkk.CMD.group.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.klkk.CMD.group.GroupMemberRole;
import com.klkk.CMD.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "group_member")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "group_id")
    private Long groupId;

    @Enumerated(EnumType.STRING)
    private GroupMemberRole role;

    public String getRoleKey() {
        return this.role.getKey();
    }
}