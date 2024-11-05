package com.klkk.CMD.fileSystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long ownerId;

    private Integer version = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageType type;



    public enum LanguageType {
        JAVA,
        HTML
    }

    public boolean isHtml() {
        return this.type == LanguageType.HTML;
    }

    public boolean isJava() {
        return this.type == LanguageType.JAVA;
    }
}
