package com.klkk.CMD.group.repository;

import com.klkk.CMD.group.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
    Optional<GroupRequest> findByUserIdAndGroupId(Long userId, Long groupId);
    List<GroupRequest> findByGroupId(@Param("groupId") Long groupId);
}
