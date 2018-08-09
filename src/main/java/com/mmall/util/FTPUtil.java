package com.mmall.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Luyue
 * @date 2018/8/1 12:44
 **/
public class FTPUtil {
    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIP = PropertiesUtil.getKey("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getKey("ftp.user");
    private static String ftpPwd = PropertiesUtil.getKey("ftp.pass");
    private String ip;
    private Integer port;
    private String userName;
    private String password;
    private FTPClient ftpClient;

    public FTPUtil(String ip, Integer port, String userName, String password) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public static boolean ftpUpload(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIP, 21, ftpUser, ftpPwd);
        //上传ftp服务器
        logger.info("开始连接ftp服务器，准备上传");
        boolean result = ftpUtil.upload("img", fileList);
        logger.info("开始上传文件，上传结果：{}", result);
        return result;
    }

    /**
     * 上传文件
     *
     * @param remotePath
     * @param fileList
     * @return
     * @throws IOException
     */
    private boolean upload(String remotePath, List<File> fileList) throws IOException {
        boolean uploadFile = true;
        FileInputStream fis = null;

        //连接ftp服务器
        if (connect(this.ip, this.userName, this.password)) {
            try {
                //切换工作目录
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                //设置文件格式为二进制
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                uploadFile = false;
                logger.error("上传文件失败", e);
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }

        return uploadFile;
    }

    /**
     * 连接ftp服务器
     *
     * @param ip
     * @param userName
     * @param password
     * @return
     */
    private boolean connect(String ip, String userName, String password) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(userName, password);
        } catch (IOException e) {
            logger.error("连接ftp服务器失败", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
