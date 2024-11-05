package com.klkk.CMD.group.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.chat.service.chat.ChatService;
import com.klkk.CMD.fileSystem.entity.Project;
import com.klkk.CMD.fileSystem.entity.ProjectGroup;
import com.klkk.CMD.fileSystem.service.ProjectGroupService;
import com.klkk.CMD.fileSystem.service.ProjectService;
import com.klkk.CMD.group.GroupMemberRole;
import com.klkk.CMD.group.dto.*;
import com.klkk.CMD.group.entity.Group;
import com.klkk.CMD.group.entity.GroupMember;
import com.klkk.CMD.group.entity.GroupRequest;
import com.klkk.CMD.group.entity.GroupTask;
import com.klkk.CMD.group.repository.GroupRepository;
import com.klkk.CMD.group.service.GroupService;
import com.klkk.CMD.notification.service.NotificationService;
import com.klkk.CMD.security.entity.User;
import com.klkk.CMD.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final ProjectService projectService;
    private final ProjectGroupService projectGroupService;

    @RequestMapping(value = "get-group-code-list", method = RequestMethod.GET)
    public ResultVO getGroupCodeList(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        ArrayList<Project> projectArrayList = new ArrayList<>();
        List<GroupMember> enteredGroup = groupService.findGroupMemByUserId(userId);

        for(GroupMember groupMember : enteredGroup) {
            List<ProjectGroup> projectGroups = projectGroupService.getProjectGroupByGroupId(groupMember.getGroupId());
            List<Project> projects = projectService.getProjectByGroupId(projectGroups);
            for (Project project : projects) {
                projectArrayList.add(project);
            }
        }
        resultVO.setSuccess(true);
        resultVO.setResult(projectArrayList);
        return resultVO;
    }

    @RequestMapping(value = "get-group-list.do", method = RequestMethod.GET)
    public ResultVO getGroupList(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<MyGroupListDto> myGroupList = new ArrayList<>();

        try {
            List<GroupMember> enteredGroup = groupService.findGroupMemByUserId(userId);

            for (GroupMember groupMember : enteredGroup) {
                MyGroupListDto myGroupListDto = new MyGroupListDto();
                Optional<Group> group = groupService.getByGroupId(groupMember.getGroupId());
                myGroupListDto.setGroupInfo(group.get());
                myGroupListDto.setRole(groupMember.getRole());
                myGroupList.add(myGroupListDto);
            }

            resultVO.setSuccess(true);
            resultVO.setResult(myGroupList);
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return resultVO;
    }

    @RequestMapping(value = "get-group-detail.do", method = RequestMethod.GET)
    public ResultVO getGroupData(@RequestParam Long groupId) {
        ResultVO resultVO = new ResultVO(false, null);
        Optional<Group> group = groupService.getByGroupId(groupId);
        resultVO.setSuccess(true);
        resultVO.setResult(group.get());
        return resultVO;
    }

    @RequestMapping(value = "get-leader-group-list.do", method = RequestMethod.GET)
    public ResultVO getLeaderGroupList(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<Group> myGroupList = new ArrayList<>();

        try {
            List<GroupMember> enteredGroup = groupService.findGroupMemByUserIdAndRole(userId);

            for (GroupMember groupMember : enteredGroup) {
                Optional<Group> group = groupService.getByGroupId(groupMember.getGroupId());
                group.ifPresent(myGroupList::add);
            }

            resultVO.setSuccess(true);
            resultVO.setResult(myGroupList);
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return resultVO;
    }

    @RequestMapping(value = "get-group-task-list.do", method = RequestMethod.GET)
    public ResultVO getTaskList(@RequestParam Long groupId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<GroupTask> taskList = groupService.findTaskByGroupId(groupId);
        resultVO.setSuccess(true);
        resultVO.setResult(taskList);
        return resultVO;
    }

    @RequestMapping(value = "create-group.do", method = RequestMethod.POST)
    @PostMapping("create-group.do")
    public ResultVO postToGroupWithChatRoom(@RequestBody GroupDto groupDto) {
        ResultVO resultVO = new ResultVO(false, null);

        // 유저 정보 조회
        Long userId = groupDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String userName = user.getName(); // 조회된 유저의 이름

        // 그룹 생성 및 채팅방 생성
        Long groupId = groupService.saveGroup(groupDto);

        // 그룹 멤버 추가
        GroupMemberDto groupMemberDto = new GroupMemberDto();
        groupMemberDto.setGroupId(groupId);
        groupMemberDto.setUserId(userId);
        groupMemberDto.setRole(GroupMemberRole.LEADER.toString());
        groupService.saveGroupMember(groupMemberDto);

        resultVO.setSuccess(true);
        resultVO.setResult(groupId);

        return resultVO;
    }

    @RequestMapping(value = "save-group-task.do", method = RequestMethod.POST)
    @PostMapping("save-group-task.do")
    public ResultVO saveGroupTask(@RequestBody List<GroupTaskDto> groupTaskList) {
        ResultVO resultVO = new ResultVO(false, null);
        for (GroupTaskDto groupTask : groupTaskList) {
            groupService.saveGroupTask(groupTask);
        }
        resultVO.setResult("success");
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "update-group-task.do", method = RequestMethod.POST)
    @PostMapping("update-group-task.do")
    public ResultVO updateGroupTask(@RequestBody List<GroupTaskDto> groupTaskList) {
        ResultVO resultVO = new ResultVO(false, null);
        for (GroupTaskDto groupTask : groupTaskList) {
            groupService.updateGroupTask(groupTask);
        }
        resultVO.setResult("success");
        resultVO.setSuccess(true);
        return resultVO;
    }

    @PostMapping("save-group-request.do")
    public ResultVO saveGroupRequest(@RequestBody GroupRequestDto groupRequestDto) {
        ResultVO resultVO = new ResultVO(false, null);
        if (groupService.checkGroupRequestExists(groupRequestDto.getUserId(), groupRequestDto.getGroupId())) {
            resultVO.setSuccess(false);
            return resultVO;
        }
        // 그룹 요청 저장
        groupService.saveGroupRequest(groupRequestDto);

        // 그룹 ID 및 리더 ID 가져오기
        Long groupId = groupRequestDto.getGroupId();
        Long leaderId = groupService.getGroupLeaderId(groupId);

        // 그룹 리더에게 알림 전송 (그룹 ID 포함)
        notificationService.createGroupRequestNotification("새로운 그룹 가입 요청이 도착했습니다.", leaderId, groupId);

        resultVO.setResult("success");
        resultVO.setSuccess(true);

        return resultVO;
    }


    @RequestMapping(value = "get-group-request.do", method = RequestMethod.GET)
    @PostMapping("get-group-request.do")
    public ResultVO getRequest(@RequestParam Long groupId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<GroupRequest> groupRequests = groupService.findRequestByGroupId(groupId);
        resultVO.setResult(groupRequests);
        resultVO.setSuccess(true);
        return resultVO;
    }

    @PostMapping("confirm-group-request.do")
    public ResultVO confirmGroupRequest(@RequestBody GroupRequestApprovalDto groupRequestApprovalDto) {
        ResultVO resultVO = new ResultVO(false, null);
        GroupRequestDto groupRequestDto = groupRequestApprovalDto.getGroupRequestDto();

        try {
            // 그룹 요청이 승인된 경우
            if (groupRequestApprovalDto.getApproval()) {
                Group group = groupRepository.findById(groupRequestDto.getGroupId())
                        .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않음"));
                String roomId = group.getChatRoomId();

                // 그룹 멤버 추가
                GroupMemberDto groupMemberDto = GroupMemberDto.builder()
                        .groupId(groupRequestDto.getGroupId())
                        .userId(groupRequestDto.getUserId())
                        .role("MEMBER")
                        .build();
                groupService.saveGroupMember(groupMemberDto);

                // 채팅방 사용자 수 증가 및 사용자 추가
                chatService.increaseUserCount(roomId);
                chatService.addUser(roomId, groupRequestDto.getUserId(), groupService.findUserNameById(groupRequestDto.getUserId()));

                // 그룹 가입 승인된 사용자에게 알림 전송
                notificationService.createGroupJoinedNotification(
                        group.getName() + " 그룹에 가입되었습니다.", groupRequestDto.getUserId()
                );
            }

            // 그룹 요청 삭제
            groupService.deleteGroupRequestById(groupRequestDto.getId());

            resultVO.setSuccess(true);
        } catch (Exception e) {
            resultVO.setSuccess(false);
            e.printStackTrace(); // 예외 로깅
        }

        return resultVO;
    }



    @GetMapping("/get-group-member.do")
    public ResultVO getGroupMember(@RequestParam Long groupId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<GroupMember> groupMembers = groupService.getGroupMemberById(groupId);

        List<GroupMemberDto> groupMemberDtos = groupMembers.stream().map(member -> {
            GroupMemberDto dto = GroupMemberDto.builder()
                    .id(member.getId())
                    .userId(member.getUser().getId())
                    .groupId(member.getGroupId())
                    .role(member.getRoleKey())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        resultVO.setResult(groupMemberDtos);
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "save-group-lang.do", method = RequestMethod.POST)
    public ResultVO saveGroupLang(@RequestBody List<GroupLangDto> groupLangDtoList) {
        ResultVO resultVO = new ResultVO(false, null);
        groupService.saveGroupLang(groupLangDtoList);
        resultVO.setResult("success");
        resultVO.setSuccess(true);
        return resultVO;
    }
}
