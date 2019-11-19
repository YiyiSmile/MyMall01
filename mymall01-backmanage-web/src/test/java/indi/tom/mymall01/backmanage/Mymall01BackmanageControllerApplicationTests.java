package indi.tom.mymall01.backmanage;



import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Mymall01BackmanageControllerApplicationTests {


    @Test
    public void uploadFile() throws IOException, MyException {
        //file是一个全路径的配置文件
        String file = this.getClass().getResource("/tracker.conf").getFile();
        //System.out.println(file);
        ClientGlobal.init(file);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackServer);
        String[] paths = storageClient.upload_appender_file("d://SR//TMP//test9.jpg", "jpg", null);
//        for (String path : paths) {
//            System.out.println(path);
//        }
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            System.out.println(path);
        }

    }

}
