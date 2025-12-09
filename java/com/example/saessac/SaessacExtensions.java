package com.example.saessac;

import android.media.MediaPlayer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MediaPlayerManager {
    public static MediaPlayer player = null;
}

public class Extensions {
    /**
     * 파일을 생성하는 메소드입니다.
     * @param dir => 파일이 생성될 경로 (디렉토리)
     * @param fileName => 파일명
     * @param content => 파일에 쓰여질 문자열 데이터
     */
    public static void CreateFile(String dir, String fileName, String content) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        try {
            FileWriter writer = new FileWriter(dir + "/" + fileName);
            BufferedWriter buffer = new BufferedWriter(writer);

            buffer.write(content);
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 파일을 삭제하는 메소드입니다.
     * @param filePath => 삭제할 대상 파일의 경로
     */
    public static void DeleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) file.delete();
    }

    /**
     * 문자열에서 지정된 검색 문자열이 발생하는 횟수를 반환합니다.
     * @param original => 검색 대상
     * @param find => 검색할 내용
     */
    public static int GetCount(String original, String find) {
        int count = 0;
        int lastIndex = 0;

        while (lastIndex != -1) {
            lastIndex = original.indexOf(find, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += find.length();
            }
        }
        return count;
    }

    public static String GenerateRandom() {
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            randomNumber.append(random.nextInt(10));
        }
        return randomNumber.toString();
    }
}