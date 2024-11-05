package com.klkk.CMD.codeEditer.repository;

import com.klkk.CMD.codeEditer.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    List<Code> findByUserId(@Param("userId") Long userId);
}