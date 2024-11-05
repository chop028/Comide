package com.klkk.CMD.board.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "board_content")
public class BoardContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column(name = "code_id")
    private Long codeId;
    @Column(name = "board_id")
    private Long boardId;

}