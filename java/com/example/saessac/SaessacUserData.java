package com.example.saessac;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SaessacUserData {
    public static class BabyInfo {
        public String name;
        public String birth;
        public String sex;
        public String bloodtype;
        public String hypocorism;
        public String headcircum;
        public String height;
        public String weight;
    }

    public static class UserInfo {
        public String name;
        public String birth;
        public String sex;
        public String bloodtype;
        public String relationship;
    }

    public static String uuid = "";

    /**
     * json 파일을 파싱하여 객체 데이터로 반환합니다.
     */
    public static JsonObject LoadJSON(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) return null;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String jsonString = bufferedReader.readLine(); // Wait, readText() reads all, but in Java, need to read all lines.

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();

            Gson gson = new Gson();
            try {
                JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
                if (jsonElement.isJsonObject()) {
                    return jsonElement.getAsJsonObject();
                } else {
                    return null;
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json 파일을 파싱하여 baby_info 데이터 클래스에 맞춰서 반환합니다.
     */
    public static BabyInfo LoadBabyInfo(String filepath) {
        return new Gson().fromJson(LoadJSON(filepath), BabyInfo.class);
    }

    /**
     * json 파일을 파싱하여 user_info 데이터 클래스에 맞춰서 반환합니다.
     */
    public static UserInfo LoadUserInfo(String filepath) {
        return new Gson().fromJson(LoadJSON(filepath), UserInfo.class);
    }

    /**
     * 텍스트 파일을 읽어 각 줄에 개행 문자를 추가한 후 다시 반환합니다.
     * @param file_path => 읽을 텍스트 파일의 경로
     */
    public static String LoadDiary(String file_path) {
        FileReader reader = new FileReader(file_path);
        BufferedReader buffer = new BufferedReader(reader);
        StringBuffer result = new StringBuffer();

        try {
            String temp = buffer.readLine();
            while (temp != null) {
                result.append(temp);
                result.append('\n'); // 각 줄 끝에 개행 문자 추가
                temp = buffer.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }
}