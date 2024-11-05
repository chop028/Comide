package com.klkk.CMD.group.dto;

import com.klkk.CMD.group.GroupMemberRole;
import com.klkk.CMD.group.entity.Group;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MyGroupListDto {
    Group groupInfo;
    GroupMemberRole role;
}
