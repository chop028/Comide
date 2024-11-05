package com.klkk.CMD.fileSystem.repository;

import com.klkk.CMD.fileSystem.entity.VersionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionInfoRepository extends JpaRepository<VersionInfo, Long> {
    List<VersionInfo> findByProjectId(Long projectId);
}
