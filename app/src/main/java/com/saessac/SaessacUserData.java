package com.saessac;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SaessacUserData {
    public static String uuid = "";

    public static class BabyInfo {
        public String name;
        public String birth;
        public String sex;
        public String bloodtype;
        public String hypocorism;
        public String headcircum;
        public String height;
        public String weight;

        public BabyInfo(String name, String birth, String sex, String bloodtype,
                String hypocorism, String headcircum, String height, String weight) {
            this.name = name;
            this.birth = birth;
            this.sex = sex;
            this.bloodtype = bloodtype;
            this.hypocorism = hypocorism;
            this.headcircum = headcircum;
            this.height = height;
            this.weight = weight;
        }
    }

    public static class UserInfo {
        public String name;
        public String birth;
        public String sex;
        public String bloodtype;
        public String relationship;

        public UserInfo(String name, String birth, String sex, String bloodtype, String relationship) {
            this.name = name;
            this.birth = birth;
            this.sex = sex;
            this.bloodtype = bloodtype;
            this.relationship = relationship;
        }
    }

    /**
     * json 파일을 파싱하여 객체 타입으로 반환합니다.
     */
    public static JsonObject loadJSON(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return null;
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String jsonString = readAll(bufferedReader);
            bufferedReader.close();

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
            if (jsonElement.isJsonObject()) {
                return jsonElement.getAsJsonObject();
            } else {
                return null;
            }
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json 파일을 파싱하여 baby_info 타입의 클래스에 맞췄서 반환합니다.
     */
    public static BabyInfo loadBabyInfo(String filepath) {
        JsonObject jsonObject = loadJSON(filepath);
        if (jsonObject == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, BabyInfo.class);
    }

    /**
     * json 파일을 파싱하여 user_info 타입의 클래스에 맞췄서 반환합니다.
     */
    public static UserInfo loadUserInfo(String filepath) {
        JsonObject jsonObject = loadJSON(filepath);
        if (jsonObject == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, UserInfo.class);
    }

    /**
     * 텍스트 파일을 읽어 각 줄에 개행 문자를 추가하여 반환합니다.
     */
    public static String loadDiary(String filePath) {
        StringBuilder result = new StringBuilder();
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader buffer = new BufferedReader(reader);

            String temp = buffer.readLine();
            while (temp != null) {
                result.append(temp);
                result.append('\n');
                temp = buffer.readLine();
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static String readAll(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
