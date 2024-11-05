package com.klkk.CMD.codeKnowledge.repository;

import com.klkk.CMD.codeKnowledge.entity.LangKnwl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LangKnwlRepository extends JpaRepository<LangKnwl, Long> {
    List<LangKnwl> findByCategory(@Param("category") String category);
}
