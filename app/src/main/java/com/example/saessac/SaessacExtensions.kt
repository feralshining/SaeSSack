package com.example.saessac

import android.media.MediaPlayer
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import kotlin.random.Random

object MediaPlayerManager
{
    var player: MediaPlayer? = null
}

object Extensions
{
    /**
     * 파일을 생성하는 메소드입니다.
     * @param dir => 파일이 생성될 경로 (디렉토리)
     * @param fileName => 파일명
     * @param content => 파일에 쓰여질 문자열 데이터
     */
    fun CreateFile(dir: String, fileName: String, content: String)
    {
        val dir = File(dir)
        if (!dir.exists())
        {
            dir.mkdirs()
        }

        val writer = FileWriter("$dir/$fileName")
        val buffer = BufferedWriter(writer)

        buffer.write(content)
        buffer.close()
    }

    /**
     * 파일을 삭제하는 메소드입니다.
     * @param filePath => 삭제할 대상 파일의 경로
     */
    fun DeleteFile(filePath: String)
    {
        if (File(filePath).exists()) File(filePath).delete()
    }

    /**
     * 문자열에서 지정된 검색 문자열이 발생하는 횟수를 반환합니다.
     * @param original => 검색 대상
     * @param find => 검색할 내용
     */
    fun GetCount(original: String, find: String): Int
    {
        var count = 0
        var lastIndex = 0

        while (lastIndex != -1)
        {
            lastIndex = original.indexOf(find, lastIndex)
            if (lastIndex != -1)
            {
                count++
                lastIndex += find.length
            }
        }
        return count
    }

    fun GenerateRandom(): String
    {
        val randomNumber = (1..8)
            .map { Random.nextInt(0, 10) }
            .joinToString("")
        return randomNumber
    }
}

