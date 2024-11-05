package com.klkk.CMD.codeEditer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "code")
@Builder
@AllArgsConstructor
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column(name = "lang_id")
    private String langId;
    @Column(name = "user_id")
    private Long userId;
    
    private Integer version = 1;
}

