package com.klkk.CMD.group.repository;

import com.klkk.CMD.group.GroupMemberRole;
import com.klkk.CMD.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByUserId(@Param("userId") Long userId);

    List<GroupMember> findGroupMemberByGroupId(@Param("groupId") Long groupId);

    Optional<GroupMember> findByGroupIdAndRole(Long groupId, GroupMemberRole role);
}
