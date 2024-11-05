package com.klkk.CMD.codeEditer.service;

import com.klkk.CMD.codeEditer.dto.CodeDto;
import com.klkk.CMD.codeEditer.entity.Code;
import com.klkk.CMD.codeEditer.repository.CodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableAsync
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;

    public List<Code> getCodeListByUserId(Long userId) {
        return codeRepository.findByUserId(userId);
    }

    public Optional<Code> getCodeById(Long codeId) {
        return codeRepository.findById(codeId);
    }

    @Transactional
    public long saveCode(CodeDto codeDto) {
        return codeRepository.save(codeDto.toEntity()).getId();
    }
}