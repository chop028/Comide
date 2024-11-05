package com.klkk.CMD.fileSystem.service;

import com.klkk.CMD.codeEditer.dto.CodeDto;
import com.klkk.CMD.codeEditer.entity.Code;
import com.klkk.CMD.codeEditer.service.CodeService;
import com.klkk.CMD.fileSystem.dto.ClientFileSystemDto;
import com.klkk.CMD.fileSystem.dto.ProjectDto;
import com.klkk.CMD.fileSystem.entity.FileSystem;
import com.klkk.CMD.fileSystem.entity.Project;
import com.klkk.CMD.fileSystem.entity.VersionInfo;
import com.klkk.CMD.fileSystem.repository.FileSystemRepository;
import com.klkk.CMD.fileSystem.repository.ProjectRepository;
import com.klkk.CMD.fileSystem.repository.VersionInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableAsync
@RequiredArgsConstructor
public class FileSystemService {
    private final FileSystemRepository fileSystemRepository;
    private final ProjectRepository projectRepository;
    private final CodeService codeService;
    private final VersionInfoRepository versionInfoRepository;

    @Transactional
    public void deleteFileSystemByProject(Project project) {
        // 프로젝트와 연결된 파일 시스템 삭제
        fileSystemRepository.deleteByProject(project);
    }

    @Transactional
    public void saveFileSystem(ClientFileSystemDto clientFileSystemDto, Project project) {

        saveFileSystemRecursive(clientFileSystemDto, null, project);
    }

    private void saveFileSystemRecursive(ClientFileSystemDto dto, FileSystem parent, Project project) {
        //1. 이름 추출
        Integer newVersion = project.getVersion();
        String name = dto.getName();

        //2. 타입 추출
        FileSystem.FileType type = FileSystem.FileType.valueOf(dto.getType());

        //3. 코드 저장
        Long codeId = null;
        if (type == FileSystem.FileType.FILE) {
            CodeDto codeDto = CodeDto.builder()
                    .content(dto.getContent())
                    .langId("java")
                    .userId(project.getOwnerId())
                    .version(newVersion)
                    .name(dto.getName())
                    .build();
            codeId = codeService.saveCode(codeDto);
        }

        //4. 파일 시스템 구조 형성
        FileSystem fileSystem = FileSystem.builder()
                .name(name)
                .parent(parent)
                .absolutePath(parent == null ? "/" + name : parent.getAbsolutePath() + "/" + name)
                .type(type)
                .project(project)
                .version(newVersion)
                .code(codeId == null ? null : codeService.getCodeById(codeId).get())
                .build();
        fileSystem = fileSystemRepository.save(fileSystem);


        //6. 재귀 함수
        if (dto.getChildren() != null) {
            for (ClientFileSystemDto child : dto.getChildren()) {
                saveFileSystemRecursive(child, fileSystem, project);
            }
        }
    }

    public List<FileSystem> getFileSystemsByProjectAndVersion(Long projectId, int version) {
        return fileSystemRepository.findByProjectIdAndVersion(projectId, version);
    }

    @Transactional
    public List<ClientFileSystemDto> getFileSystemDtosByProjectAndVersion(Long projectId, int version) {
        List<FileSystem> fileSystems = getFileSystemsByProjectAndVersion(projectId, version);
        List<FileSystem> topLevelFileSystems = fileSystems.stream()
                .filter(fs -> fs.getParent() == null) // 최상위 파일 시스템만 가져옴
                .collect(Collectors.toList());

        return topLevelFileSystems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ClientFileSystemDto convertToDto(FileSystem fileSystem) {
        String content = null;
        if (fileSystem.getCode() != null) {
            content = codeService.getCodeById(fileSystem.getCode().getId()).orElse(null).getContent();
        }

        List<ClientFileSystemDto> childrenDtos = new ArrayList<>();
        // 하위 파일 시스템을 찾아서 DTO로 변환
        List<FileSystem> children = fileSystemRepository.findByParent(fileSystem); // 부모가 현재 파일 시스템인 하위 파일 시스템 찾기
        for (FileSystem child : children) {
            childrenDtos.add(convertToDto(child)); // 재귀적으로 DTO로 변환
        }

        return ClientFileSystemDto.builder()
                .name(fileSystem.getName())
                .type(fileSystem.getType().name())
                .content(content)
                .children(childrenDtos) // 하위 DTO 리스트 설정
                .build();
    }

    public List<VersionInfo> getVersionInfoByProjectId(Long projectId) {
        return versionInfoRepository.findByProjectId(projectId);
    }

    public List<FileSystem> getProjectIdAndVersionAndType(Long projectId, int version, String type) {
        List<FileSystem> fileSystems = fileSystemRepository.findByProjectIdAndVersionAndType(projectId, version, FileSystem.FileType.valueOf(type));
        return fileSystems;
    }

}
