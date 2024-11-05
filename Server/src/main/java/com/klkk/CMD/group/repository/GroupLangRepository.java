package com.klkk.CMD.group.repository;

import com.klkk.CMD.group.entity.GroupLang;
import com.klkk.CMD.group.entity.GroupTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupLangRepository extends JpaRepository<GroupLang, Long> {
    List<GroupLang> getByGroupId(@Param("groupId") Long userId);
}
