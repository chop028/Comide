package com.klkk.CMD.comment.service;

import com.klkk.CMD.board.Entity.Board;
import com.klkk.CMD.board.repository.BoardRepository;
import com.klkk.CMD.comment.dto.CommentDto;
import com.klkk.CMD.comment.entity.Comment;
import com.klkk.CMD.comment.repository.CommentRepository;
import com.klkk.CMD.notification.service.NotificationService;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableAsync
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public List<Comment> getCommentByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Transactional
    public Long saveComment(CommentDto commentDto) {
        Comment savedComment = commentRepository.save(commentDto.toEntity());

        // 2. 댓글 작성자의 이름 조회
        Long commenterId = savedComment.getUserId();
        Optional<User> optionalUser = userRepository.findById(commenterId);

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID: " + commenterId);
        }

        String commenterName = optionalUser.get().getName();

        // 3. postId로 게시물(Board) 조회해서 게시물 소유자(userId) 가져오기
        Long postId = savedComment.getPostId();
        Optional<Board> optionalBoard = boardRepository.findById(postId);

        if (optionalBoard.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 게시물 ID: " + postId);
        }

        Long postOwnerId = optionalBoard.get().getUserId();

        // 4. 댓글 알림 전송
        notificationService.createCommentNotification(
                commenterName + "님이 회원님의 게시글에 댓글을 남겼습니다.", postOwnerId
        );

        return savedComment.getId();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
