package indi.tom.mymall01.backmanage.controller;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author Tom
 * @Date 2019/11/18 16:28
 * @Version 1.0
 * @Description
 */
@RestController
public class FileController {

    @Value("${fileServer.url}")
    String fileServerURL;;
    //文件上传，客户端访问url类似于: http://localhost:xxxx/fileUpload


    @PostMapping("fileUpload")
    public String fileUpload(MultipartFile file) throws IOException, MyException {
        //file是一个全路径的配置文件
        String confFileAbsolutPath = this.getClass().getResource("/tracker.conf").getFile();
        //System.out.println(file);
        ClientGlobal.init(confFileAbsolutPath);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackServer);
        String originalFilename = file.getOriginalFilename();
        String extName = StringUtils.substringAfterLast(originalFilename, ".");
        String[] paths = storageClient.upload_appender_file(file.getBytes(), extName, null);
        String fileURL=fileServerURL;
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            fileURL += "/" + path;
        }

        return fileURL;

    }
}
