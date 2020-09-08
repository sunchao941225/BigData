package com.baizhi.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;

import javax.swing.*;

public class JobRunner {
    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");

        Configuration configuration = new Configuration();

        // configuration.set("hbase.zookeeper.quorum", "HadoopNode00");
        configuration.set("hbase.zookeeper.quorum", "192.168.11.31,192.168.11.32,192.168.11.33");

        configuration.set("hbase.zookeeper.property.clientPort", "2181");

        configuration.addResource("conf3/core-site.xml");
        configuration.addResource("conf3/hdfs-site.xml");
        configuration.addResource("conf3/mapred-site.xml");
        configuration.addResource("conf3/yarn-site.xml");
        configuration.set(MRJobConfig.JAR, "G:\\IDEA_WorkSpace\\BigData\\HBase_Test\\target\\HBase_Test-1.0-SNAPSHOT.jar");
        configuration.set("mapreduce.app-submission.cross-platform", "true");

        Job job = Job.getInstance(configuration);
        job.setJarByClass(JobRunner.class);

        /*
         *
         * */
        job.setInputFormatClass(TableInputFormat.class);
        job.setOutputFormatClass(TableOutputFormat.class);



        /*
         *
         * 设置mapper 相关
         * */
        TableMapReduceUtil.initTableMapperJob(
                "baizhi:t_user1",
                new Scan(),
                UserMapper.class,
                Text.class,
                DoubleWritable.class,
                job
        );


        TableMapReduceUtil.initTableReducerJob(
                "baizhi:t_result",
                UserReducer.class,
                job);


        job.waitForCompletion(true);
    }
}
