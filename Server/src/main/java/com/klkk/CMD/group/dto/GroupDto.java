package com.klkk.CMD.group.dto;

import com.klkk.CMD.chat.dto.ChatRoomDto;
import com.klkk.CMD.group.entity.Group;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroupDto {
    private Long id;
    private String name;
    private int headCnt;
    private String startDate;
    private String endDate;
    private String introComment;
    private Long userId;
    private String chatRoomId; // 채팅방 ID를 직접 저장

    // Entity로의 변환 메서드
    public Group toEntity() {
        return Group.builder()
                .id(id)
                .name(name)
                .headCnt(headCnt)
                .startDate(startDate)
                .endDate(endDate)
                .introComment(introComment)
                .chatRoomId(chatRoomId)
                .build();
    }
}