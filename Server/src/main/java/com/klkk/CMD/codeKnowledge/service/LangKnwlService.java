package com.klkk.CMD.codeKnowledge.service;

import com.klkk.CMD.codeKnowledge.dto.LangKnwlContentDto;
import com.klkk.CMD.codeKnowledge.dto.LangKnwlDto;
import com.klkk.CMD.codeKnowledge.entity.LangKnwl;
import com.klkk.CMD.codeKnowledge.entity.LangKnwlContent;
import com.klkk.CMD.codeKnowledge.repository.LangKnwlContentRepository;
import com.klkk.CMD.codeKnowledge.repository.LangKnwlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableAsync
@RequiredArgsConstructor
public class LangKnwlService {
    private final LangKnwlContentRepository langKnwlContentRepository;
    private final LangKnwlRepository langKnwlRepository;

    public List<LangKnwl> getLangKnwlList(){
        return langKnwlRepository.findAll();
    }

    public List<LangKnwl> getLangKnwlListByCategory(String category) {
        return langKnwlRepository.findByCategory(category);
    }

    public LangKnwl getLangKnwl(Long id){
        return langKnwlRepository.findById(id).get();
    }

    public List<LangKnwlContent> getContentByLangKnwlId(Long id){
        return langKnwlContentRepository.findByLangKnwlId(id);
    }

    public void updateViewCnt(Long id) {
        LangKnwl langKnwl = langKnwlRepository.findById(id).get();
        langKnwl.setViewCnt(langKnwl.getViewCnt() + 1);
        langKnwlRepository.save(langKnwl);
    }

    @Transactional
    public void saveLangKnwlContent(LangKnwlContentDto langKnwlContentDto) {
        langKnwlContentRepository.save(langKnwlContentDto.toEntity());
    }
    @Transactional
    public Long saveLangKnwl(LangKnwlDto langKnwlDto) {
        return langKnwlRepository.save(langKnwlDto.toEntity()).getId();
    }
}
