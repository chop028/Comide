package com.klkk.CMD.group.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GroupRequestApprovalDto {
    private GroupRequestDto groupRequestDto;
    private Boolean approval;
}