package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Luyue
 * @date 2018/8/1 12:03
 **/
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    public static void main(String[] args) {
        File file = new File("E:\\JavaWeb_intelliJ IDEA文件\\mmall\\src\\main\\java\\com\\mmall\\service\\impl\\CategoryServiceImpl.java");
        System.out.println(file.getName());
    }

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExpansionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExpansionName;

        log.info("开始上传文件，上传文件名：{}, 上传文件的路径：{}, 新文件名：{}", fileName, fileExpansionName, path);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);
        try {
            //到这，文件已经上传成功了
            file.transferTo(targetFile);

            //将文件上传到ftp服务器
            if (!FTPUtil.ftpUpload(Lists.newArrayList(targetFile))) {
                return null;
            }

            //删除upload本地文件
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return null;
        }
        return targetFile.getName();
    }
}
