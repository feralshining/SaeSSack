package com.example.saessac

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception

object SaessacFTP
{
    private const val _SERVER = "saessak.dothome.co.kr"
    private const val _PORT = 21
    private const val _USER = "saessak"
    private const val _PASSWORD = "xodnjs6923!"
    const val DIARY_DIR = "/user_diary/"
    const val TODOLIST_DIR = "/user_todolist/"
    const val GOODSREQUEST_DIR = "/user_goods_data/"

    private suspend fun FTPClient.setupConnection(): Boolean = withContext(Dispatchers.IO)
    {
        try
        {
            this@setupConnection.controlEncoding = "UTF-8"
            this@setupConnection.connect(_SERVER, _PORT)
            this@setupConnection.login(_USER, _PASSWORD)

            if (!FTPReply.isPositiveCompletion(this@setupConnection.replyCode))
            {
                Log.e("FTP Error", "FTP server refused connection")
                return@withContext false
            }

            this@setupConnection.enterLocalPassiveMode()
            this@setupConnection.setFileType(FTP.BINARY_FILE_TYPE)
            return@withContext true
        }
        catch (e: Exception)
        {
            Log.e("FTP Error", "Error setting up connection: ${e.message}")
            return@withContext false
        }
    }

    /**
     * 서버 내 user_goods_data 디렉토리에 uuid 대상과 일치한 파일명의 개수를 반환합니다.
     */
    suspend fun GetGoodsCount(uuid: String): Int
    {
        val ftp = FTPClient()
        var count = 0
        try
        {
            if (ftp.setupConnection())
            {
                val fileNames = ftp.listNames("/user_goods_data/")
                if (fileNames != null && fileNames.isNotEmpty())
                {
                    for (fileName in fileNames)
                    {
                        if (fileName.contains(uuid))
                        {
                            count++
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.e("FTP Error", "Error retrieving diary count: ${e.message}")
        }
        finally
        {
            try
            {
                ftp.disconnect()
            }
            catch (ex: Exception)
            {
                Log.e("FTP Error", "Error disconnecting FTP: ${ex.message}")
            }
        }
        return count
    }

    /**
     * 서버 내에서 대상 uuid가 존재하는지 여부를 반환합니다
     */
    suspend fun Isvalid(uuid: String): Boolean
    {
        val ftp = FTPClient()
        var isValid = false
        try
        {
            if (ftp.setupConnection())
            {
                var fileNames = ftp.listNames("/user_diary/")
                if (fileNames != null && fileNames.isNotEmpty())
                {
                    for (fileName in fileNames)
                    {
                        if (fileName.contains(uuid))
                        {
                            isValid = true
                        }
                    }
                }
                else
                {
                    fileNames = ftp.listNames("/user_todolist/")
                    if (fileNames != null && fileNames.isNotEmpty())
                    {
                        for (fileName in fileNames)
                        {
                            if (fileName.contains(uuid))
                            {
                                isValid = true
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.e("FTP Error", "Error retrieving diary count: ${e.message}")
        }
        finally
        {
            try
            {
                ftp.disconnect()
            }
            catch (ex: Exception)
            {
                Log.e("FTP Error", "Error disconnecting FTP: ${ex.message}")
            }
        }
        return isValid
    }

    /**
     * 서버 내 user_diary 디렉토리에 uuid 대상과 일치한 파일명의 개수를 반환합니다.
     * diaryNum[0] => 디렉토리 내 일기장의 전체 개수
     * diaryNum[1] => 디렉토리 내 일기장의 전체 개수 중 월(month)이 일치하는 일기장의 개수
     */
    suspend fun GetDiaryCount(month: String, uuid: String): IntArray
    {
        val ftp = FTPClient()
        val diaryNum = IntArray(2)
        try
        {
            if (ftp.setupConnection())
            {
                val fileNames = ftp.listNames("/user_diary/")
                if (fileNames != null && fileNames.isNotEmpty())
                {
                    for (fileName in fileNames)
                    {
                        if (fileName.contains(uuid))
                        {
                            diaryNum[0]++
                            if (fileName.replace("/user_diary/$uuid" + "_2024", "").substring(0, 1) == month)
                            {
                                diaryNum[1]++
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.e("FTP Error", "Error retrieving diary count: ${e.message}")
        }
        finally
        {
            try
            {
                ftp.disconnect()
            }
            catch (ex: Exception)
            {
                Log.e("FTP Error", "Error disconnecting FTP: ${ex.message}")
            }
        }
        return diaryNum
    }

    /**
     * 서버로부터 파일을 다운로드하고, 해당 결과값을 반환합니다.
     * @param srcPath => 다운로드를 요청할 서버 내의 파일 경로
     * @param destPath => 다운로드를 받을 로컬 저장소의 경로
     */
    suspend fun DownloadFile(srcPath: String, destPath: String): Boolean
    {
        val ftp = FTPClient()
        try
        {
            if (ftp.setupConnection())
            {
                FileOutputStream(destPath).use { stream ->
                    if (ftp.retrieveFile(srcPath, stream)) return true //"File downloaded successfully"
                    else return false //"Failed to download file"
                }
            }
        }
        catch (e: Exception)
        {
            Log.e("FTP Error", "Error downloading file: ${e.message}")
        }
        finally
        {
            try
            {
                ftp.disconnect()
            }
            catch (ex: Exception)
            {
                Log.e("FTP Error", "Error disconnecting FTP: ${ex.message}")
            }
        }
        return false
    }

    /**
     * 서버에 파일을 업로드하고, 해당 결과값을 반환합니다.
     * @param srcPath => 서버에 업로드 할 로컬 저장소 파일의 경로
     * @param destPath => 서버 저장 경로
     */
    suspend fun UploadFile(srcPath: String, destPath: String) : Boolean
    {
        val ftp = FTPClient()
        try
        {
            if (ftp.setupConnection())
            {
                FileInputStream(srcPath).use { stream ->
                    if (ftp.storeFile(destPath, stream)) return true //"File uploaded successfully"
                    else return false //"Failed to upload file"
                }
            }
        }
        catch (e: Exception)
        {
            Log.e("FTP Error", "Error uploading file: ${e.message}")
        }
        finally
        {
            try
            {
                ftp.disconnect()
            }
            catch (ex: Exception)
            {
                Log.e("FTP Error", "Error disconnecting FTP: ${ex.message}")
            }
        }
        return false
    }
}
