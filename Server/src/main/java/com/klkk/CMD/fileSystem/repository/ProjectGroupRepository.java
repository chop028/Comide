package com.klkk.CMD.fileSystem.repository;

import com.klkk.CMD.fileSystem.entity.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long> {
    List<ProjectGroup> findByGroupId(Long groupId);
}
