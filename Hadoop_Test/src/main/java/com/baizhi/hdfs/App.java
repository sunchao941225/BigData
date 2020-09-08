package com.baizhi.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class App {

    private Configuration configuration;
    private FileSystem fileSystem;

    @Before
    public void getClient() throws Exception {

        System.setProperty("HADOOP_USER_NAME", "root");
        /*
         * 准备配置对象
         * */
        configuration = new Configuration();
        /*
         * 添加相应的配置文件*/
        //configuration.addResource("core-site.xml");
        //configuration.addResource("hdfs-site.xml");

        /*
         * 通过FileSystem.newInstance 获得客户端对象*/
        fileSystem = FileSystem.newInstance(configuration);
    }

    @Test
    public void testUpload01() throws Exception {

        /*
         *
         * 源文件  |   目标文件
         * Path 对象
         * */
        fileSystem.copyFromLocalFile(new Path("G:\\A.docx"), new Path("/baizhi/2.docx"));

    }

    @Test
    public void testUpload02() throws Exception {

        /*
         * 准备 本地输入流
         * */
        FileInputStream inputStream = new FileInputStream("G:\\A.docx");


        /*
         * 准备 hdfs 输出流
         * */
        FSDataOutputStream outputStream = fileSystem.create(new Path("/baizhi/3.docx"));


        /*
         * 使用工具类进行拷贝
         * */
        IOUtils.copyBytes(inputStream, outputStream, 1024, true);
    }

    @Test
    public void testDownload01() throws Exception {

        fileSystem.copyToLocalFile(false, new Path("/1.txt"), new Path("G:\\3.txt"), true);

    }

    @Test
    public void testDownload02() throws Exception {

        FileOutputStream outputStream = new FileOutputStream("G:\\4.txt");

        FSDataInputStream inputStream = fileSystem.open(new Path("/1.txt"));
        IOUtils.copyBytes(inputStream, outputStream, 1024, true);

    }

    @Test
    public void test011() throws IOException {


        RemoteIterator<LocatedFileStatus> list = fileSystem.listFiles(new Path("/"), true);

        while (list.hasNext()) {

            LocatedFileStatus locatedFileStatus = list.next();
            Path path = locatedFileStatus.getPath();
            System.out.println(path.toString());

        }


    }

    @Test
    public void test02() throws Exception{

        fileSystem.delete(new Path("/baizhi"),false);
    }
    @Test
    public void test03() throws Exception{

        boolean exists = fileSystem.exists(new Path("/1.txt"));
        if (exists){
            System.out.println("文件存在");
        }else {

            System.out.println("文件不存在");
        }
    }

    @Test
    public void testy04() throws Exception{

        fileSystem.mkdirs(new Path("/baizhi1243"));
    }
}
