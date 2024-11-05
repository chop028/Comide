package com.klkk.CMD.codeKnowledge.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.board.dto.BoardContentDto;
import com.klkk.CMD.board.dto.BoardDto;
import com.klkk.CMD.codeKnowledge.dto.LangKnwlContentDto;
import com.klkk.CMD.codeKnowledge.dto.LangKnwlDto;
import com.klkk.CMD.codeKnowledge.entity.LangKnwlContent;
import com.klkk.CMD.codeKnowledge.service.LangKnwlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/LangKnwl")
public class LangKnwlController {
    private final LangKnwlService langKnwlService;

    @RequestMapping(value = "post-langKnwl.do", method = RequestMethod.POST)
    public ResultVO postLangKnwl(@RequestBody LangKnwlDto langKnwlDto) {
        ResultVO resultVO = new ResultVO(false, null);
        Long boardId = langKnwlService.saveLangKnwl(langKnwlDto);
        resultVO.setResult(boardId);
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "save-langKnwl-content.do", method = RequestMethod.POST)
    public ResultVO saveBoardContent(@RequestBody List<LangKnwlContentDto> langKnwlDtoList) {
        ResultVO resultVO = new ResultVO(false, null);
        for (LangKnwlContentDto langKnwlContentDto : langKnwlDtoList) {
            langKnwlService.saveLangKnwlContent(langKnwlContentDto);
        }
        resultVO.setResult("success");
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "get-langKnwl-list.do", method = RequestMethod.GET)
    public ResultVO getLangKnwlList() {
        ResultVO resultVO = new ResultVO(false, null);

        try {
            resultVO.setResult(langKnwlService.getLangKnwlList());
            resultVO.setSuccess(true);
        } catch (Exception e) {

        }
        return resultVO;
    }

    @RequestMapping(value = "get-langKnwl-list-category.do", method = RequestMethod.GET)
    public ResultVO getLangKnwlListBycategory(@RequestParam String category) {
        ResultVO resultVO = new ResultVO(false, null);

        try {
            resultVO.setResult(langKnwlService.getLangKnwlListByCategory(category));
            resultVO.setSuccess(true);
        } catch (Exception e) {

        }
        return resultVO;
    }

    @RequestMapping(value = "/get-langKnwl-detail.do", method = RequestMethod.GET)
    public ResultVO getPostDetail(@RequestParam Long id) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(langKnwlService.getLangKnwl(id));
            langKnwlService.updateViewCnt(id);
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
        }
        return resultVO;
    }

    @RequestMapping(value = "/get-langKnwl-content.do", method = RequestMethod.GET)
    public ResultVO getBoardContent(@RequestParam Long langKnwlId) {
        ResultVO resultVO = new ResultVO(false, null);
        try {
            resultVO.setResult(langKnwlService.getContentByLangKnwlId(langKnwlId));
            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            // 예외를 로깅하거나 적절한 방법으로 처리할 수 있습니다.
        }
        return resultVO;
    }
}
