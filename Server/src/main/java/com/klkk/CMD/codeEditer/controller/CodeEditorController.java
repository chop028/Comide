package com.klkk.CMD.codeEditer.controller;

import com.klkk.CMD.board.ResultVO;
import com.klkk.CMD.codeEditer.dto.CodeDto;
import com.klkk.CMD.codeEditer.entity.Code;
import com.klkk.CMD.codeEditer.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.klkk.CMD.codeEditer.CompileCode.compileAndRunFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code-editor")
public class CodeEditorController {

    private final CodeService codeService;

    @PostMapping(value = "compile-code.do", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultVO compileCode(@RequestBody CodeDto codeDto) {
        ResultVO resultVO = new ResultVO(true, null);
        String compileCode = codeDto.getContent();

        // working-directory 폴더 경로 설정
        String workingDirectoryPath = "working-directory";
        File workingDirectory = new File(workingDirectoryPath);

        // working-directory 폴더가 없으면 생성
        if (!workingDirectory.exists()) {
            workingDirectory.mkdirs();
        } else {
            // 폴더가 이미 존재하면 내부 파일을 전부 삭제
            for (File file : workingDirectory.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }

        // main.java 파일을 working-directory 내에 생성
        String codeFilename = workingDirectoryPath + "/Main.java";
        try {
            FileWriter writer = new FileWriter(codeFilename);
            writer.write(compileCode);
            writer.close();

            // compileAndRunFile 메서드에서는 파일 이름만 전달
            // 메서드 내부에서 working-directory 경로를 추가할 필요 없음
            String result = compileAndRunFile("Main.java");

            resultVO.setResult(result);
            resultVO.setSuccess(true);

            return resultVO;
        } catch (IOException e) {
            e.printStackTrace();
            resultVO.setResult("Failed to save code file");
            return resultVO;
        }
    }

    @RequestMapping(value = "save-code.do", method = RequestMethod.POST)
    @PostMapping("save-code.do")
    public ResultVO saveCodeEdit(@RequestBody CodeDto codeDto) {
        ResultVO resultVO = new ResultVO(true, null);
        codeService.saveCode(codeDto);
        resultVO.setSuccess(true);
        return resultVO;

    }

    @RequestMapping(value = "/get-code-list.do", method = RequestMethod.GET)
    public ResultVO getCodeList(@RequestParam Long userId) {
        ResultVO resultVO = new ResultVO(false, null);
        resultVO.setResult(codeService.getCodeListByUserId(userId));
        resultVO.setSuccess(true);
        return resultVO;
    }

    @RequestMapping(value = "/get-code-detail.do", method = RequestMethod.GET)
    public ResultVO getCodeDetail(@RequestParam Long codeId) {
        ResultVO resultVO = new ResultVO(false, null);
        Code codeDetail = codeService.getCodeById(codeId).get();
        resultVO.setSuccess(true);
        resultVO.setResult(codeDetail);
        return resultVO;
    }
}