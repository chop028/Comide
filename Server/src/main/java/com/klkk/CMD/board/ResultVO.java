package com.klkk.CMD.board;

import lombok.Data;

@Data
public class ResultVO {
    boolean success;
    Object result;

    public ResultVO(boolean success, Object result) {
        this.result = result;
        this.success = false;
    }
}