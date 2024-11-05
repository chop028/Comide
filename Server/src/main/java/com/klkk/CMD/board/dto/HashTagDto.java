package com.klkk.CMD.board.dto;

import com.klkk.CMD.board.Entity.HashTag;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString   // 객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메서드
@NoArgsConstructor  // 인자 없이 객체 생성 가능
public class HashTagDto {
    private Long id;
    private String content;
    private Long boardId;

    public HashTag toEntity() {
        HashTag hashTag = HashTag.builder()
                .id(id)
                .content(content)
                .boardId(boardId)
                .build();
        return hashTag;
    }
}