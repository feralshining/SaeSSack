package com.example.saessac

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

object SaessacUserData
{
    data class babyInfo(
        val name: String,
        val birth: String,
        val sex: String,
        val bloodtype: String,
        val hypocorism: String,
        val headcircum: String,
        val height: String,
        val weight: String)

    data class userInfo(
        val name: String,
        val birth: String,
        val sex: String,
        val bloodtype: String,
        val relationship: String)

    var uuid:String = ""

    /**
     * json 파일을 파싱하여 객체 데이터로 반환합니다.
     */
    fun LoadJSON(filepath: String) : JsonObject?
    {
        val file = File(filepath)
        if (!file.exists()) return null

        val bufferedReader = BufferedReader(FileReader(file))
        val jsonString = bufferedReader.use { it.readText() }

        val gson = Gson()
        return try
        {
            val jsonElement: JsonElement = gson.fromJson(jsonString, JsonElement::class.java)
            if (jsonElement.isJsonObject)
            {
                jsonElement.asJsonObject
            }
            else
            {
                null
            }
        }
        catch (e: JsonSyntaxException)
        {
            e.printStackTrace()
            null
        }
    }

    /**
     * json 파일을 파싱하여 baby_info 데이터 클래스에 맞춰서 반환합니다.
     */
    fun LoadBabyInfo(filepath: String): babyInfo?
    {
        val data = Gson().fromJson(LoadJSON(filepath), babyInfo::class.java)
        return data
    }

    /**
     * json 파일을 파싱하여 user_info 데이터 클래스에 맞춰서 반환합니다.
     */
    fun LoadUserInfo(filepath: String): userInfo?
    {
        val data = Gson().fromJson(LoadJSON(filepath), userInfo::class.java)
        return data
    }

    /**
     * 텍스트 파일을 읽어 각 줄에 개행 문자를 추가한 후 다시 반환합니다.
     * @param file_path => 읽을 텍스트 파일의 경로
     */
    fun LoadDiary(file_path: String): String
    {
        val reader = FileReader(file_path)
        val buffer = BufferedReader(reader)
        val result = StringBuffer()

        try
        {
            var temp: String? = buffer.readLine()
            while (temp != null)
            {
                result.append(temp)
                result.append('\n') // 각 줄 끝에 개행 문자 추가
                temp = buffer.readLine()
            }
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }
        finally
        {
            try
            {
                buffer.close()
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }
        }

        return result.toString()
    }


}