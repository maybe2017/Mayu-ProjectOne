package com.mayu.practice.dto;

import lombok.Data;

@Data
public class RemoteLinkProperties {
    private String ip;
    private int port;
    private String username;
    private String userPwd;

    private String ftpIsActiveMode; //主被动模式 1表示主动模式 0表示被动模式

    // ftp: 根目录
    private String linkRootPath;

    // ftp: 转写结果存放的根目录(客户约定给出)
    private String linkUploadPath;

    // 本地: 下拉ftp时临时文件目录
    private String localTempDirPath;

    // 本地: 回传时临时文件目录
    private String uploadLocalTempDirPath;

    // 协议 sftp 可以是ftp
    private String linkProtocol;

    // TODO
    private String fileNameFormat;
    private Integer timeOffSet;

    /**
     * 编码格式
     */
    private String encoding = "UTF-8";
    /**
     * 连接超时时间
     */
    private int connectTimeout = 30000;
    /**
     * 缓存
     */
    private int bufferSize = 8096;

    public boolean isPassiveMode() {
        return "0".equals(ftpIsActiveMode);
    }
}
