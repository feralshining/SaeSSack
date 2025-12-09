package com.saessac;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Extensions {
    /**
     * 파일을 생성하는 메소드입니다.
     */
    public static void createFile(String dir, String fileName, String content) {
        try {
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

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
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 문자열에서 지정된 검색 문자열이 발생하는 횟수를 반환합니다.
     */
    public static int getCount(String original, String find) {
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

    public static String generateRandom() {
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            randomNumber.append(random.nextInt(10));
        }
        return randomNumber.toString();
    }
}
