package com.klkk.CMD.friendShip.dto;

import com.klkk.CMD.friendShip.dto.enums.FriendShipStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WaitingFriendListDto {
    private Long friendShipId;
    private String friendEmail;
    private String friendName;
    private FriendShipStatus status;
    private String picture;
}
