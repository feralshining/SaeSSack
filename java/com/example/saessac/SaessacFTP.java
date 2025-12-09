package com.example.saessac;

import android.util.Log;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaessacFTP {
    private static final String _SERVER = "saessak.dothome.co.kr";
    private static final int _PORT = 21;
    private static final String _USER = "saessak";
    private static final String _PASSWORD = "xodnjs6923!";
    public static final String DIARY_DIR = "/user_diary/";
    public static final String TODOLIST_DIR = "/user_todolist/";
    public static final String GOODSREQUEST_DIR = "/user_goods_data/";

    private static boolean setupConnection(FTPClient ftp) {
        try {
            ftp.setControlEncoding("UTF-8");
            ftp.connect(_SERVER, _PORT);
            ftp.login(_USER, _PASSWORD);

            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                Log.e("FTP Error", "FTP server refused connection");
                return false;
            }

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            return true;
        } catch (Exception e) {
            Log.e("FTP Error", "Error setting up connection: " + e.getMessage());
            return false;
        }
    }

    /**
     * 서버 내 user_goods_data 디렉토리에 uuid 대상과 일치한 파일명의 개수를 반환합니다.
     */
    public static int GetGoodsCount(String uuid) {
        FTPClient ftp = new FTPClient();
        int count = 0;
        try {
            if (setupConnection(ftp)) {
                String[] fileNames = ftp.listNames("/user_goods_data/");
                if (fileNames != null && fileNames.length > 0) {
                    for (String fileName : fileNames) {
                        if (fileName.contains(uuid)) {
                            count++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("FTP Error", "Error retrieving diary count: " + e.getMessage());
        } finally {
            try {
                ftp.disconnect();
            } catch (Exception ex) {
                Log.e("FTP Error", "Error disconnecting FTP: " + ex.getMessage());
            }
        }
        return count;
    }

    /**
     * 서버 내에서 대상 uuid가 존재하는지 여부를 반환합니다
     */
    public static boolean Isvalid(String uuid) {
        FTPClient ftp = new FTPClient();
        boolean isValid = false;
        try {
            if (setupConnection(ftp)) {
                String[] fileNames = ftp.listNames("/user_diary/");
                if (fileNames != null && fileNames.length > 0) {
                    for (String fileName : fileNames) {
                        if (fileName.contains(uuid)) {
                            isValid = true;
                        }
                    }
                } else {
                    fileNames = ftp.listNames("/user_todolist/");
                    if (fileNames != null && fileNames.length > 0) {
                        for (String fileName : fileNames) {
                            if (fileName.contains(uuid)) {
                                isValid = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("FTP Error", "Error retrieving diary count: " + e.getMessage());
        } finally {
            try {
                ftp.disconnect();
            } catch (Exception ex) {
                Log.e("FTP Error", "Error disconnecting FTP: " + ex.getMessage());
            }
        }
        return isValid;
    }

    /**
     * 서버 내 user_diary 디렉토리에 uuid 대상과 일치한 파일명의 개수를 반환합니다.
     * diaryNum[0] => 디렉토리 내 일기장의 전체 개수
     * diaryNum[1] => 디렉토리 내 일기장의 전체 개수 중 월(month)이 일치하는 일기장의 개수
     */
    public static int[] GetDiaryCount(String month, String uuid) {
        FTPClient ftp = new FTPClient();
        int[] diaryNum = new int[2];
        try {
            if (setupConnection(ftp)) {
                String[] fileNames = ftp.listNames("/user_diary/");
                if (fileNames != null && fileNames.length > 0) {
                    for (String fileName : fileNames) {
                        if (fileName.contains(uuid)) {
                            diaryNum[0]++;
                            String replaced = fileName.replace("/user_diary/" + uuid + "_2024", "");
                            if (replaced.length() > 0 && replaced.substring(0, 1).equals(month)) {
                                diaryNum[1]++;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("FTP Error", "Error retrieving diary count: " + e.getMessage());
        } finally {
            try {
                ftp.disconnect();
            } catch (Exception ex) {
                Log.e("FTP Error", "Error disconnecting FTP: " + ex.getMessage());
            }
        }
        return diaryNum;
    }

    /**
     * 서버로부터 파일을 다운로드하고, 해당 결과값을 반환합니다.
     * @param srcPath => 다운로드를 요청할 서버 내의 파일 경로
     * @param destPath => 다운로드를 받을 로컬 저장소의 경로
     */
    public static boolean DownloadFile(String srcPath, String destPath) {
        FTPClient ftp = new FTPClient();
        try {
            if (setupConnection(ftp)) {
                FileOutputStream stream = new FileOutputStream(destPath);
                boolean result = ftp.retrieveFile(srcPath, stream);
                stream.close();
                return result;
            }
        } catch (Exception e) {
            Log.e("FTP Error", "Error downloading file: " + e.getMessage());
        } finally {
            try {
                ftp.disconnect();
            } catch (Exception ex) {
                Log.e("FTP Error", "Error disconnecting FTP: " + ex.getMessage());
            }
        }
        return false;
    }

    /**
     * 서버에 파일을 업로드하고, 해당 결과값을 반환합니다.
     * @param srcPath => 서버에 업로드 할 로컬 저장소 파일의 경로
     * @param destPath => 서버 저장 경로
     */
    public static boolean UploadFile(String srcPath, String destPath) {
        FTPClient ftp = new FTPClient();
        try {
            if (setupConnection(ftp)) {
                FileInputStream stream = new FileInputStream(srcPath);
                boolean result = ftp.storeFile(destPath, stream);
                stream.close();
                return result;
            }
        } catch (Exception e) {
            Log.e("FTP Error", "Error uploading file: " + e.getMessage());
        } finally {
            try {
                ftp.disconnect();
            } catch (Exception ex) {
                Log.e("FTP Error", "Error disconnecting FTP: " + ex.getMessage());
            }
        }
        return false;
    }
}