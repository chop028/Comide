package com.klkk.CMD.group.dto;

import com.klkk.CMD.group.entity.GroupRequest;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupRequestDto {
    private Long id;
    private Long userId;
    private Long groupId;
    private Boolean opt1;
    private Boolean opt2;
    private Boolean opt3;
    private String opt4;
    private String techStack;
    private String comment;
    private Boolean permit;

    public GroupRequest toEntity() {
        return GroupRequest.builder()
                .id(id)
                .groupId(groupId)
                .userId(userId)
                .opt1(opt1)
                .opt2(opt2)
                .opt3(opt3)
                .opt4(opt4)
                .techStack(techStack)
                .comment(comment)
                .permit(permit)
                .build();
    }
}
