package com.baizhi;

import com.baizhi.mr.JobRunner;
import com.baizhi.mr.UserMapper;
import com.baizhi.mr.UserReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.*;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.IOException;

public class Job02 {
    public static class Job02Mapper extends TableMapper<Text, IntWritable> {

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

            byte[] addressBytes = value.getValue("cf1".getBytes(), "address".getBytes());

            context.write(new Text(addressBytes), new IntWritable(1));


        }
    }


    public static class Job02Reducer extends TableReducer<Text, IntWritable, NullWritable> {

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            if (key.toString().contains("河南省郑州市")) {
                int count = 0;

                for (IntWritable value : values) {
                    count += value.get();
                }

                Put put = new Put(key.toString().getBytes());

                put.addColumn("cf1".getBytes(), "count".getBytes(), (count + "").getBytes());


                context.write(NullWritable.get(), put);

            }

        }
    }


    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");

        Configuration configuration = new Configuration();

        configuration.set("hbase.zookeeper.quorum", "HadoopNode00");
        //configuration.set("hbase.zookeeper.quorum", "192.168.11.31,192.168.11.32,192.168.11.33");

        configuration.set("hbase.zookeeper.property.clientPort", "2181");

        configuration.addResource("conf2/core-site.xml");
        configuration.addResource("conf2/hdfs-site.xml");
        configuration.addResource("conf2/mapred-site.xml");
        configuration.addResource("conf2/yarn-site.xml");
        configuration.set(MRJobConfig.JAR, "G:\\IDEA_WorkSpace\\BigData\\HBase_Test\\target\\HBase_Test-1.0-SNAPSHOT.jar");
        configuration.set("mapreduce.app-submission.cross-platform", "true");

        Job job = Job.getInstance(configuration);
        job.setJarByClass(Job02.class);

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
                "baizhi:t_result1",
                new Scan(),
                Job02Mapper.class,
                Text.class,
                IntWritable.class,
                job
        );


        TableMapReduceUtil.initTableReducerJob(
                "baizhi:t_result2",
                Job02Reducer.class,
                job);


        job.waitForCompletion(true);
    }

}
