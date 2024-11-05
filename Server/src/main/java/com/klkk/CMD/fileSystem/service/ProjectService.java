package com.klkk.CMD.fileSystem.service;

import com.klkk.CMD.fileSystem.dto.ProjectDto;
import com.klkk.CMD.fileSystem.dto.VersionInfoDto;
import com.klkk.CMD.fileSystem.entity.Project;
import com.klkk.CMD.fileSystem.entity.ProjectGroup;
import com.klkk.CMD.fileSystem.entity.VersionInfo;
import com.klkk.CMD.fileSystem.repository.ProjectRepository;
import com.klkk.CMD.fileSystem.repository.VersionInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@EnableAsync
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final VersionInfoRepository versionInfoRepository;

    public List<Project> getProjectByGroupId(List<ProjectGroup> projectGroups) {
        ArrayList<Project> projectArrayList = new ArrayList<>();
        for(ProjectGroup projectGroup : projectGroups) {
            projectArrayList.add(getProjectById(projectGroup.getProjId()).get());
        }
        return projectArrayList;
    }

    public Project saveVersion(Project project, VersionInfoDto versionInfoDto) {
        Integer newVersion = project.getVersion() + 1;
        VersionInfo versionInfo = new VersionInfo(project.getId(), newVersion);
        versionInfo.setTitle(versionInfoDto.getTitle());
        versionInfo.setComment(versionInfoDto.getComment());
        versionInfoRepository.save(versionInfo);
        project.setVersion(newVersion);
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByUserId(Long userId) {
        return projectRepository.findByOwnerId(userId);
    }

    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }
    @Transactional
    public Project createProject(ProjectDto projectDto) { return projectRepository.save(projectDto.toEntity()); }
}
