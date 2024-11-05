package com.klkk.CMD.comment.dto;

import com.klkk.CMD.comment.entity.Comment;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String createdTime;
    private int likes;
    private Long postId;
    private Long userId;

    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .content(content)
                .createdTime(createdTime)
                .likes(likes)
                .postId(postId)
                .userId(userId)
                .build();
    }

    @Builder
    public CommentDto(Long id, String content, String createdTime, int likes, Long postId, Long userId) {
        this.id = id;
        this.content = content;
        this.createdTime = createdTime;
        this.likes = likes;
        this.postId = postId;
        this.userId = userId;
    }
}