package com.klkk.CMD.friendShip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendDto {
    private Long id;
    private String name;
    private String picture;
    private String comment;
}