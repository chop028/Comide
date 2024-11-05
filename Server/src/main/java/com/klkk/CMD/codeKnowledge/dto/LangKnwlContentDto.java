package com.klkk.CMD.codeKnowledge.dto;

import com.klkk.CMD.codeKnowledge.entity.LangKnwl;
import com.klkk.CMD.codeKnowledge.entity.LangKnwlContent;
import jakarta.persistence.Column;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LangKnwlContentDto {
    private Long id;
    private String category;
    private String content;
    private Long codeId;
    private Long langKnwlId;

    public LangKnwlContent toEntity(){
        LangKnwlContent langKnwlContent = LangKnwlContent.builder()
                .id(id)
                .category(category)
                .content(content)
                .codeId(codeId)
                .langKnwlId(langKnwlId)
                .build();
        return langKnwlContent;

    }
}
