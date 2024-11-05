package com.klkk.CMD.board.dto;

import com.klkk.CMD.board.Entity.Board;
import com.klkk.CMD.board.Entity.HashTag;
import com.klkk.CMD.group.entity.Group;
import com.klkk.CMD.group.entity.GroupLang;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CoopPostDto {
    Board board;
    List<HashTag> hashTags;
    Group groupInfo;
    List<GroupLang> groupLangList;
    String userName;
    String userImg;
}