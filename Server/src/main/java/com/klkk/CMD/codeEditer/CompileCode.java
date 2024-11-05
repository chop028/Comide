package com.klkk.CMD.codeEditer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CompileCode {
    public static String compileAndRunFile(String fileName) {
        StringBuilder result = new StringBuilder();
        try {
            // working-directory 폴더 경로 설정
            File workingDirectory = new File("working-directory");
            if (!workingDirectory.exists()) {
                workingDirectory.mkdirs(); // 폴더가 없으면 생성
            }

            // 파일명으로부터 확장자 제거
            String baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
            String classFileName = baseFileName + ".class";
            File javaFile = new File(workingDirectory, fileName);
            File classFile = new File(workingDirectory, classFileName);

            // javac 명령어를 사용하여 working-directory 내에서 컴파일
            Process compileProcess = Runtime.getRuntime().exec(
                    "javac -d " + workingDirectory.getAbsolutePath() + " " + javaFile.getAbsolutePath()
            );

            if (compileProcess.waitFor() != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                errorReader.close();
                return result.toString();
            }

            // java 명령어를 사용하여 working-directory 내에서 실행
            Process runProcess = Runtime.getRuntime().exec(
                    "java -cp " + workingDirectory.getAbsolutePath() + " " + baseFileName,
                    null,
                    workingDirectory
            );

            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}