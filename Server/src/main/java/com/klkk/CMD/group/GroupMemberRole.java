package com.klkk.CMD.group;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupMemberRole {
    LEADER("ROLE_LEADER", "그룹장"),
    MEMBER("ROLE_MEMBER", "그룹원");

    private final String key;
    private final String title;
}