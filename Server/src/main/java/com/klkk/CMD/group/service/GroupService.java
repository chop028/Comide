package com.klkk.CMD.group.service;

import com.klkk.CMD.chat.dto.ChatRoomDto;
import com.klkk.CMD.chat.service.chat.ChatService;
import com.klkk.CMD.group.GroupMemberRole;
import com.klkk.CMD.group.dto.*;
import com.klkk.CMD.group.entity.*;
import com.klkk.CMD.group.repository.*;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableAsync
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTaskRepository groupTaskRepository;
    private final GroupRequestRepository groupRequestRepository;

    private final GroupLangRepository groupLangRepository;

    private final ChatService chatService;
    private final UserRepository userRepository;

    public Long getGroupLeaderId(Long groupId) {
        GroupMember leader = groupMemberRepository.findByGroupIdAndRole(groupId, GroupMemberRole.LEADER)
                .orElseThrow(() -> new RuntimeException("그룹 리더가 존재하지 않습니다."));
        return leader.getUser().getId();
    }

    public List<GroupMember> findGroupMemByUserId(Long userId) {
        return groupMemberRepository.findByUserId(userId);
    }

    public Optional<Group> getByGroupId(Long groupId) {
        return groupRepository.findById(groupId);
    }

    public List<GroupTask> findTaskByGroupId(Long groupId) {
        return groupTaskRepository.getListByGroupId(groupId);
    }

    public List<GroupMember> findGroupMemByUserIdAndRole(Long userId) {
        List<GroupMember> groupMembers = groupMemberRepository.findByUserId(userId);
        return groupMembers.stream()
                .filter(member -> member.getRole() == GroupMemberRole.LEADER)
                .collect(Collectors.toList());
    }

    public boolean checkGroupRequestExists(Long userId, Long groupId) {
        return groupRequestRepository.findByUserIdAndGroupId(userId, groupId).isPresent();
    }

    public List<GroupMember> getGroupMemberById(Long groupId) { return groupMemberRepository.findGroupMemberByGroupId(groupId); }

    public List<GroupRequest> findRequestByGroupId(Long groupId) {
        return groupRequestRepository.findByGroupId(groupId);
    }

    @Transactional
    public Long saveGroup(GroupDto groupDto) {
        // 그룹 저장
        Group savedGroup = groupRepository.save(groupDto.toEntity());

        // 채팅방 생성
        ChatRoomDto chatRoomDto = chatService.createChatRoom(savedGroup.getName(), savedGroup.getHeadCnt());
        chatService.increaseUserCount(chatRoomDto.getRoomId());

        // 생성된 채팅방의 ID를 그룹에 설정
        savedGroup.setChatRoomId(chatRoomDto.getRoomId());
        groupRepository.save(savedGroup);

        // 그룹 생성자 정보를 chat_users 테이블에 등록
        chatService.addUser(chatRoomDto.getRoomId(), groupDto.getUserId(), findUserNameById(groupDto.getUserId()));

        return savedGroup.getId();
    }

    public List<GroupLang> getGroupLangListByGroupId(Long groupId) {
        return groupLangRepository.getByGroupId(groupId);
    }

    public String findUserNameById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getName();
    }

    @Transactional
    public Long saveGroupTask(GroupTaskDto groupTaskDto) {
        return groupTaskRepository.save(groupTaskDto.toEntity()).getId();
    }

    @Transactional
    public Long saveGroupMember(GroupMemberDto groupMemberDto) {
        User user = userRepository.findById(groupMemberDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        GroupMember groupMember = groupMemberDto.toEntity(user);
        return groupMemberRepository.save(groupMember).getId();
    }

    @Transactional
    public Long saveGroupRequest(GroupRequestDto groupRequestDto) {
        return groupRequestRepository.save(groupRequestDto.toEntity()).getId();
    }

    @Transactional
    public void deleteGroupRequestById(Long groupRequestId) {
        groupRequestRepository.deleteById(groupRequestId);
    }

    @Transactional
    public void updateGroupTask(GroupTaskDto groupTaskDto){
        GroupTask groupTask = groupTaskRepository.getById(groupTaskDto.getId());
        groupTask.setTaskName(groupTaskDto.getTaskName());
        groupTask.setOverall(groupTaskDto.getOverall());
        groupTask.setCrtProgress(groupTaskDto.getCrtProgress());
        groupTaskRepository.save(groupTask);
    }

    @Transactional
    public void saveGroupLang(List<GroupLangDto> groupLangDtoList) {
        for(GroupLangDto groupLangDto : groupLangDtoList) {
            groupLangRepository.save(groupLangDto.toEntity());
        }
    }
}
