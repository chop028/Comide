package com.klkk.CMD.board.dto;

import com.klkk.CMD.board.Entity.BoardContent;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class BoardContentDto {
    private Long id;
    private String category;
    private String content;
    private Long codeId;
    private Long boardId;

    public BoardContent toEntity(){
        BoardContent boardContent = BoardContent.builder()
                .id(id)
                .category(category)
                .content(content)
                .codeId(codeId)
                .boardId(boardId)
                .build();
        return boardContent;
    }
}