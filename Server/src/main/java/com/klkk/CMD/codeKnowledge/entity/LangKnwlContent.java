package com.klkk.CMD.codeKnowledge.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "langknwl_content")
public class LangKnwlContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column(name = "code_id")
    private Long codeId;
    @Column(name = "lang_knowledge_id")
    private Long langKnwlId;
}
