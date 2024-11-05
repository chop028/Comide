package com.klkk.CMD.fileSystem.service;

import com.klkk.CMD.fileSystem.dto.ProjGroupDto;
import com.klkk.CMD.fileSystem.entity.ProjectGroup;
import com.klkk.CMD.fileSystem.repository.ProjectGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableAsync
@RequiredArgsConstructor
public class ProjectGroupService {
    private final ProjectGroupRepository projectGroupRepository;

    public List<ProjectGroup> getProjectGroupByGroupId(Long groupId) {
        return projectGroupRepository.findByGroupId(groupId);
    }
    @Transactional
    public ProjectGroup createProjectByGroup(ProjGroupDto projGroupDto) {
        return projectGroupRepository.save(projGroupDto.toEntity());
    }
}
