package com.klkk.CMD.fileSystem.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.codeEditer.entity.Code;
import com.klkk.CMD.fileSystem.dto.ClientFileSystemDto;
import com.klkk.CMD.fileSystem.dto.ProjGroupDto;
import com.klkk.CMD.fileSystem.dto.ProjectDto;
import com.klkk.CMD.fileSystem.dto.ProjectFileSystemRequest;
import com.klkk.CMD.fileSystem.entity.FileSystem;
import com.klkk.CMD.fileSystem.entity.Project;
import com.klkk.CMD.fileSystem.entity.ProjectGroup;
import com.klkk.CMD.fileSystem.entity.VersionInfo;
import com.klkk.CMD.fileSystem.service.FileSystemService;
import com.klkk.CMD.fileSystem.service.ProjectGroupService;
import com.klkk.CMD.fileSystem.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file-system")
public class FileSystemController {
    private final FileSystemService fileSystemService;
    private final ProjectService projectService;

    private final ProjectGroupService projectGroupService;

    @GetMapping("/get-individual-project")
    public ResultVO getProjectsByUserId(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<Project> projects = projectService.getProjectsByUserId(userId);

        resultVO.setSuccess(true);
        resultVO.setResult(projects);
        return resultVO;
    }

    @GetMapping("/get-group-project")
    public ResultVO getProjectsByGroupId(@RequestParam Long groupId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<ProjectGroup> projectGroups = projectGroupService.getProjectGroupByGroupId(groupId);
        List<Project> projects = projectService.getProjectByGroupId(projectGroups);
        resultVO.setSuccess(true);
        resultVO.setResult(projects);
        return resultVO;
    }
    @PostMapping("/create-project")
    public ResultVO createProject(@RequestBody ProjectDto projectDto) {
        ResultVO resultVO = new ResultVO(false, null);
        Project project = projectService.createProject(projectDto);
        if(project.getOwnerId() == null) {
            ProjGroupDto projGroupDto = new ProjGroupDto();
            projGroupDto.setGroupId(projectDto.getGroupId());
            projGroupDto.setProjId(project.getId());
            projectGroupService.createProjectByGroup(projGroupDto);
        }
        ProjectDto responseDto = new ProjectDto(project.getId(), project.getName(), project.getOwnerId(), projectDto.getType(), projectDto.getGroupId());
        resultVO.setSuccess(true);
        resultVO.setResult(responseDto);
        return resultVO;
    }

    @PostMapping("/file-save")
    public ResultVO saveFileSystem(@RequestBody ProjectFileSystemRequest request) {
        ResultVO resultVO = new ResultVO(false, null);
        Project project = projectService.getProjectById(request.getProjectDto().getId()).get();
        project = projectService.saveVersion(project, request.getVersionInfoDto());
        // 파일 시스템 저장
        for (ClientFileSystemDto clientFileSystemDto : request.getFileSystemDtos()) {
            fileSystemService.saveFileSystem(clientFileSystemDto, project);
        }

        resultVO.setSuccess(true);
        return resultVO;
    }

    @GetMapping("/get-file-system")
    public ResultVO getFileSystemsByProjectAndVersion(@RequestParam Long projectId, @RequestParam int version) {
        ResultVO resultVO = new ResultVO(false, null);
        List<ClientFileSystemDto> fileSystemDtos;
        if(version < 0) {
            Project project = projectService.getProjectById(projectId).get();
            fileSystemDtos = fileSystemService.getFileSystemDtosByProjectAndVersion(projectId, project.getVersion());
        } else {
            fileSystemDtos = fileSystemService.getFileSystemDtosByProjectAndVersion(projectId, version);
        }

        resultVO.setSuccess(true);
        resultVO.setResult(fileSystemDtos);
        return resultVO;
    }

    @GetMapping("/get-project-code")
    public ResultVO getProjectCodeList(@RequestParam Long projectId) {
        ResultVO resultVO = new ResultVO(false, null);
        Project project = projectService.getProjectById(projectId).get();
        List<FileSystem> fileSystems = fileSystemService.getProjectIdAndVersionAndType(projectId, project.getVersion(), "FILE");
        resultVO.setResult(fileSystems);
        resultVO.setSuccess(true);
        return resultVO;
    }

    @GetMapping("/get-project-version")
    public ResultVO getProjectVersion(@RequestParam Long projectId) {
        ResultVO resultVO = new ResultVO(false, null);
        List<VersionInfo> versionInfos = fileSystemService.getVersionInfoByProjectId(projectId);
        resultVO.setSuccess(true);
        resultVO.setResult(versionInfos);
        return resultVO;
    }
}
