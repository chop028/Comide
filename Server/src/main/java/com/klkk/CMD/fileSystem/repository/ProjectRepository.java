package com.klkk.CMD.fileSystem.repository;

import com.klkk.CMD.fileSystem.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> getProjectById(Long projectId);
    List<Project> findByOwnerId(Long ownerId);
}
