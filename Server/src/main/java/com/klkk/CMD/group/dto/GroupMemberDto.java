package com.klkk.CMD.group.dto;

import com.klkk.CMD.group.GroupMemberRole;
import com.klkk.CMD.group.entity.GroupMember;
import com.klkk.CMD.security.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupMemberDto {
    private Long id;
    private Long userId;
    private Long groupId;
    private String role;

    public GroupMember toEntity(User user) {
        return GroupMember.builder()
                .id(id)
                .user(user)
                .groupId(groupId)
                .role(GroupMemberRole.valueOf(role))
                .build();
    }
}

