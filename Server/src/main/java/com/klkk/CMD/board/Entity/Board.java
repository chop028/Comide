package com.klkk.CMD.board.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "board")  // 이거 보고 테이블 생성
public class Board {

    @Id // PK Field
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // PK의 생성 규칙
    private Long id;

    @Column(length = 10, nullable = true)
    private String category;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private String created_date;

    @Column
    private String modified_date;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "user_id")
    private Long userId;

    private int likes;
    @Column(name = "view_count")
    private int viewCnt;
}