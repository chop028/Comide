package com.klkk.CMD.codeKnowledge.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "lang_knowledge")
public class LangKnwl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = true)
    private String category;

    @Column(length = 100, nullable = false)
    private String title;

    @Column
    private String created_date;

    private int likes;

    @Column(name = "view_count")
    private int viewCnt;
}
