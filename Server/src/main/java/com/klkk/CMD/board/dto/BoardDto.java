package com.klkk.CMD.board.dto;

import com.klkk.CMD.board.Entity.Board;
import lombok.*;

// DTO : 데이터 전달 목적
// 데이터를 캡슐화한 데이터 전달 객체

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString   // 객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메서드
@NoArgsConstructor  // 인자 없이 객체 생성 가능
public class BoardDto {
    private Long id;
    private String category;
    private String title;
    private String content;
    private String created_date;
    private String modified_date;
    private Long userId;
    private Long groupId;
    private int viewCnt;
    private int likes;

    public Board toEntity() {
        Board board = Board.builder()
                .id(id)
                .category(category)
                .title(title)
                .content(content)
                .created_date(created_date)
                .modified_date(modified_date)
                .userId(userId)
                .groupId(groupId)
                .viewCnt(viewCnt)
                .likes(likes)
                .build();
        return board;
    }
}