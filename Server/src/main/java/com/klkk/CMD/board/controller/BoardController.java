package com.klkk.CMD.board.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.board.dto.BoardContentDto;
import com.klkk.CMD.board.dto.BoardDto;
import com.klkk.CMD.board.dto.HashTagDto;
import com.klkk.CMD.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @RequestMapping(value = "get-board-list.do", method = RequestMethod.GET)
    @GetMapping("get-board-list.do")
    public ResultVO getBoardList() {
        ResultVO resultVO = new ResultVO(false, null);

        try {
            resultVO.setResult(boardService.getBoardList());
            resultVO.setSuccess(true);
        } catch (Exception e) {

        }
        return resultVO;
    }

    @RequestMapping(value = "/get-board-list-category.do", method = RequestMethod.GET)
    public ResultVO getBoardListByCategory(@RequestParam String cate) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(boardService.getBoardListByCategory(cate));
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }

    @RequestMapping(value = "/get-board-list-user.do", method = RequestMethod.GET)
    public ResultVO getBoardListByUserId(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(boardService.getBoardListByUserId(userId));
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }

    @RequestMapping(value = "/get-post-detail.do", method = RequestMethod.GET)
    public ResultVO getPostDetail(@RequestParam int id) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(boardService.getPostById(id));
            boardService.updateViewCount(id);
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
        }
        return resultVO;
    }

    @RequestMapping(value = "post-board.do", method = RequestMethod.POST)
    @PostMapping("post-board.do")
    public ResultVO postToBoard(@RequestBody BoardDto boardDto) {
        ResultVO resultVO = new ResultVO(false, null);
        Long boardId = boardService.savePost(boardDto);
        resultVO.setResult(boardId);
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "save-board-content.do", method = RequestMethod.POST)
    public ResultVO saveBoardContent(@RequestBody List<BoardContentDto> boardContentList) {
        ResultVO resultVO = new ResultVO(false, null);
        for (BoardContentDto boardContentDto : boardContentList) {
            boardService.saveBoardContent(boardContentDto);
        }
        resultVO.setResult("success");
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "save-hashTag.do", method = RequestMethod.POST)
    public ResultVO saveHashTag(@RequestBody List<HashTagDto> hashTagList) {
        ResultVO resultVO = new ResultVO(false, null);
        for (HashTagDto hashTagDto : hashTagList) {
            boardService.saveHashTag(hashTagDto);
        }
        resultVO.setResult("success");
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "/get-board-content.do", method = RequestMethod.GET)
    public ResultVO getBoardContent(@RequestParam Long boardId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(boardService.getBoardContentByBoardId(boardId));
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }

    @RequestMapping(value = "/get-hashTag.do", method = RequestMethod.GET)
    public ResultVO getHashTag(@RequestParam Long boardId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(boardService.getHashTagByBoardId(boardId));
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }

    @RequestMapping(value = "get-main-coop.do", method = RequestMethod.GET)
    public ResultVO getMainpageCoop() {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(boardService.getMainCoopList());
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }

    @DeleteMapping("/delete-board-content")
    public ResultVO deleteBoard(@RequestParam Long boardId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            boardService.deleteBoardAndContents(boardId); // 게시글 삭제
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
        }
        return resultVO;
    }
}