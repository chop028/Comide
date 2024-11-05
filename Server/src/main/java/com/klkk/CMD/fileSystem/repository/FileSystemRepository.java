package com.klkk.CMD.fileSystem.repository;

import com.klkk.CMD.fileSystem.entity.FileSystem;
import com.klkk.CMD.fileSystem.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileSystemRepository extends JpaRepository<FileSystem, Long> {
    void deleteByProject(Project project);
    List<FileSystem> findByProjectIdAndVersion(Long projectId, int version);

    List<FileSystem> findByProjectIdAndVersionAndType(Long projectId, int version, FileSystem.FileType type);
    List<FileSystem> findByParent(FileSystem parent);
}
