package com.saessac;

import android.util.Log;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SaessacFTP {
    private static final String SERVER = "saessak.dothome.co.kr";
    private static final int PORT = 21;
    private static final String USER = "saessak";
    private static final String PASSWORD = "xodnjs6923!";
    public static final String DIARY_DIR = "/user_diary/";
    public static final String TODOLIST_DIR = "/user_todolist/";
    public static final String GOODSREQUEST_DIR = "/user_goods_data/";

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private static boolean setupConnection(FTPClient ftp) {
        try {
            ftp.setControlEncoding("UTF-8");
            ftp.connect(SERVER, PORT);
            ftp.login(USER, PASSWORD);

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
     * 서버 내 user_goods_data 디렉토리에 uuid 접두사와 일치하는 파일명의 개수를 반환합니다.
     */
    public static Future<Integer> getGoodsCount(String uuid) {
        return executorService.submit(() -> {
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
                Log.e("FTP Error", "Error retrieving goods count: " + e.getMessage());
            } finally {
                try {
                    ftp.disconnect();
                } catch (Exception ex) {
                    Log.e("FTP Error", "Error disconnecting FTP: " + ex.getMessage());
                }
            }
            return count;
        });
    }

    /**
     * 서버 내에 해당 uuid가 존재하는지 여부를 반환합니다.
     */
    public static Future<Boolean> isValid(String uuid) {
        return executorService.submit(() -> {
            FTPClient ftp = new FTPClient();
            boolean isValid = false;
            try {
                if (setupConnection(ftp)) {
                    String[] fileNames = ftp.listNames("/user_diary/");
                    if (fileNames != null && fileNames.length > 0) {
                        for (String fileName : fileNames) {
                            if (fileName.contains(uuid)) {
                                isValid = true;
                                break;
                            }
                        }
                    }
                    if (!isValid) {
                        fileNames = ftp.listNames("/user_todolist/");
                        if (fileNames != null && fileNames.length > 0) {
                            for (String fileName : fileNames) {
                                if (fileName.contains(uuid)) {
                                    isValid = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("FTP Error", "Error checking validity: " + e.getMessage());
            } finally {
                try {
                    ftp.disconnect();
                } catch (Exception ex) {
                    Log.e("FTP Error", "Error disconnecting FTP: " + ex.getMessage());
                }
            }
            return isValid;
        });
    }

    /**
     * 서버 내 user_diary 디렉토리에 uuid 접두사와 일치하는 파일명의 개수를 반환합니다.
     */
    public static Future<int[]> getDiaryCount(String month, String uuid) {
        return executorService.submit(() -> {
            FTPClient ftp = new FTPClient();
            int[] diaryNum = new int[2];
            try {
                if (setupConnection(ftp)) {
                    String[] fileNames = ftp.listNames("/user_diary/");
                    if (fileNames != null && fileNames.length > 0) {
                        for (String fileName : fileNames) {
                            if (fileName.contains(uuid)) {
                                diaryNum[0]++;
                                String replaced = fileName.replace("/user_diary/" + uuid + "_2025", "");
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
        });
    }

    /**
     * 서버로부터 파일을 다운로드하고, 해당 결과값을 반환합니다.
     */
    public static Future<Boolean> downloadFile(String srcPath, String destPath) {
        return executorService.submit(() -> {
            FTPClient ftp = new FTPClient();
            try {
                if (setupConnection(ftp)) {
                    try (FileOutputStream stream = new FileOutputStream(destPath)) {
                        return ftp.retrieveFile(srcPath, stream);
                    }
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
        });
    }

    /**
     * 서버에 파일을 업로드하고, 해당 결과값을 반환합니다.
     */
    public static Future<Boolean> uploadFile(String srcPath, String destPath) {
        return executorService.submit(() -> {
            FTPClient ftp = new FTPClient();
            try {
                if (setupConnection(ftp)) {
                    try (FileInputStream stream = new FileInputStream(srcPath)) {
                        return ftp.storeFile(destPath, stream);
                    }
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
        });
    }
}
