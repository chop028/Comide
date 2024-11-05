package com.klkk.CMD.comment.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.comment.dto.CommentDto;
import com.klkk.CMD.comment.entity.Comment;
import com.klkk.CMD.comment.service.CommentService;
import com.klkk.CMD.security.dto.CustomOAuth2User;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/get-cmt-list-post.do")
    public ResultVO getCommentListByPostId(@RequestParam Long postId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            // 댓글 리스트 가져오기
            List<Comment> commentList = commentService.getCommentByPostId(postId);
            List<Map<String, Object>> commentData = new ArrayList<>(commentList.size());

            // 각 댓글에 대해 작성자의 정보를 추가
            for (Comment comment : commentList) {
                // 댓글 작성자의 ID로 유저 정보 가져오기
                User user = customOAuth2UserService.findUserById(comment.getUserId());

                // attributes를 빈 HashMap 또는 필요한 정보를 추출하여 전달
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("googleId", user.getGoogleId());
                attributes.put("name", user.getName());
                attributes.put("email", user.getEmail());
                attributes.put("picture", user.getPicture());
                attributes.put("comment", user.getComment());

                CustomOAuth2User customOAuth2User = new CustomOAuth2User(user, attributes);

                // 댓글 정보와 사용자 정보를 합친 맵 구성
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("cmtData", comment);

                // 사용자 정보 매핑 (CustomOAuth2User로부터 필요한 정보 추출)
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", customOAuth2User.getGoogleId());  // Google ID
                userData.put("email", customOAuth2User.getEmail());
                userData.put("name", customOAuth2User.getName());
                userData.put("picture", customOAuth2User.getPicture());
                userData.put("comment", customOAuth2User.getComment());

                // 댓글과 사용자 정보를 리스트에 추가
                resultMap.put("userData", userData);
                commentData.add(resultMap);
            }

            // 성공 시 ResultVO 구성
            resultVO.setSuccess(true);
            resultVO.setResult(commentData);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            e.printStackTrace(); // 예외를 로깅
        }
        return resultVO;
    }

    @GetMapping("/get-cmt-list-user.do")
    public ResultVO getCommentListByUserId(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            List<Comment> commentList = commentService.getCommentByUserId(userId);
            resultVO.setSuccess(true);
            resultVO.setResult(commentList);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            e.printStackTrace(); // 예외를 로깅
        }
        return resultVO;
    }

    @PostMapping("/post-cmt.do")
    public ResultVO postToBoard(@RequestBody CommentDto commentDto) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            commentService.saveComment(commentDto);
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            e.printStackTrace(); // 예외를 로깅
        }
        return resultVO;
    }

    @DeleteMapping("/delete-comment")
    public ResultVO deleteComment(@RequestParam Long commentId) {
        ResultVO resultVO = new ResultVO(false, null);
        commentService.deleteComment(commentId);
        resultVO.setSuccess(true);
        return resultVO;
    }
}