package com.klkk.CMD.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "lang")
public class Language {
    @Id
    @Column(name = "LANG_ID")
    private String id;
    private String name;

    @Builder
    public Language(String id, String name) {
        Assert.hasText(name, "name must not be empty");

        this.id = id;
        this.name = name;
    }
}
