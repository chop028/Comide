package com.klkk.CMD.board.service;

import com.klkk.CMD.board.Entity.Board;
import com.klkk.CMD.board.Entity.BoardContent;
import com.klkk.CMD.board.Entity.HashTag;
import com.klkk.CMD.board.dto.BoardContentDto;
import com.klkk.CMD.board.dto.BoardDto;
import com.klkk.CMD.board.dto.CoopPostDto;
import com.klkk.CMD.board.dto.HashTagDto;
import com.klkk.CMD.board.repository.BoardContentRepository;
import com.klkk.CMD.board.repository.BoardRepository;
import com.klkk.CMD.board.repository.HashTagRepository;
import com.klkk.CMD.group.service.GroupService;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.service.CustomOAuth2UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@EnableAsync
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardContentRepository boardContentRepository;
    private final HashTagRepository hashTagRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final GroupService groupService;

    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    public List<HashTag> getHashTagByBoardId(Long boardId) {
        return hashTagRepository.findByBoardId(boardId);
    }

    public List<BoardContent> getBoardContentByBoardId(Long groupId) {
        return boardContentRepository.findByBoardId(groupId);
    }

    public List<Board> getBoardListByCategory(String category) {
        return boardRepository.findByCategory(category);
    }

    public List<Board> getBoardListByUserId(Long userId) {
        return boardRepository.findByUserId(userId);
    }

    public Optional<Board> getPostById(int id) {
        return boardRepository.findById(id);
    }

    public void updateViewCount(int id) {
        Board board = boardRepository.findById(id).get();
        board.setViewCnt(board.getViewCnt() + 1);
        boardRepository.save(board);
    }

    public List<CoopPostDto> getMainCoopList() {
        List<Board> boardList = getBoardListByCategory("협업모집");

        // 리스트를 역순으로 정렬합니다.
        Collections.reverse(boardList);

        // 앞의 6개의 요소를 가져옵니다.
        List<Board> mainCoopList;
        if (boardList.size() > 6) {
            mainCoopList = boardList.subList(0, 6);
        } else {
            mainCoopList = boardList;
        }
        ArrayList<CoopPostDto> coopPostDtoArrayList = new ArrayList<>();
        for (Board board : mainCoopList) {
            CoopPostDto coopPostDto = new CoopPostDto();
            coopPostDto.setBoard(board);
            coopPostDto.setHashTags(getHashTagByBoardId(board.getId()));
            coopPostDto.setGroupInfo(groupService.getByGroupId(board.getGroupId()).get());
            User userInfo = customOAuth2UserService.findUserById(board.getUserId());
            coopPostDto.setUserName(userInfo.getName());
            coopPostDto.setUserImg(userInfo.getPicture());
            coopPostDto.setGroupLangList(groupService.getGroupLangListByGroupId(board.getGroupId()));
            coopPostDtoArrayList.add(coopPostDto);
        }
        return coopPostDtoArrayList;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public void saveBoardContent(BoardContentDto boardContentDto) {
        boardContentRepository.save(boardContentDto.toEntity());
    }

    @Transactional
    public void saveHashTag(HashTagDto hashTagDto) {
        hashTagRepository.save(hashTagDto.toEntity());
    }

    @Transactional
    public void deleteBoardAndContents(Long boardId) {
        // 1. 보드 ID에 해당하는 보드 컨텐츠 삭제
        boardContentRepository.deleteByBoardId(boardId);
        // 2. 보드 삭제
        boardRepository.deleteById(Math.toIntExact(boardId));
    }
}