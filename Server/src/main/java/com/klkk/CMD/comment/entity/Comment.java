package com.klkk.CMD.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @Column(name = "created_time")
    private String createdTime;

    private int likes;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public Comment(Long id, String content, String createdTime, int likes, Long postId, Long userId) {
        this.id = id;
        this.content = content;
        this.createdTime = createdTime;
        this.likes = likes;
        this.postId = postId;
        this.userId = userId;
    }
}