package com.klkk.CMD.codeKnowledge.repository;

import com.klkk.CMD.codeKnowledge.entity.LangKnwl;
import com.klkk.CMD.codeKnowledge.entity.LangKnwlContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LangKnwlContentRepository extends JpaRepository<LangKnwlContent, Long> {
    public List<LangKnwlContent> findByLangKnwlId(@Param("langKnwlId") Long langKnwlId);
}
