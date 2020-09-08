package com.baizhi.work02;

import com.baizhi.work01.Job01Mapper;
import com.baizhi.work01.Job01Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class JobRunner {
    public static void main(String[] args) throws Exception {

        /*
         * 获取配置对象
         * */


        Configuration conf = new Configuration();
        /*System.setProperty("HADOOP_USER_NAME", "root");
        conf.addResource("conf2/core-site.xml");
        conf.addResource("conf2/hdfs-site.xml");
        conf.addResource("conf2/mapred-site.xml");
        conf.addResource("conf2/yarn-site.xml");
        conf.set(MRJobConfig.JAR, "G:\\IDEA_WorkSpace\\BigData\\Hadoop_Test\\target\\Hadoop_Test-1.0-SNAPSHOT.jar");
        conf.set("mapreduce.app-submission.cross-platform", "true");*/
        /*
         * 获取Job对象
         * */
        Job job = Job.getInstance(conf);

        // // 设置jar 类加载器 否则MapReduce框架找不到Map和Reuce
        job.setJarByClass(JobRunner.class);


        /*
         * 设置数据输入输出组件
         * */
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        /*
         *设置数据输入输出路径
         * */

        TextInputFormat.setInputPaths(job, new Path("G:\\Note\\Day02-Hadoop\\作业\\数据\\word.txt"));
        //TextInputFormat.setInputPaths(job, new Path("/wordcount1.txt"));
        /*
         * 注意： 此输出路径不能存在
         * */
        //TextOutputFormat.setOutputPath(job, new Path("/baizhi/out8121231233"));
        TextOutputFormat.setOutputPath(job, new Path("G:\\Note\\Day02-Hadoop\\作业\\数据\\out2"));


        /*
         * 设置MAP 和 REDUCE 处理逻辑
         * */
        job.setMapperClass(Job02Mapper.class);
        job.setReducerClass(Job02Reduce.class);

        /*
         * 设置 map任务和reduce任务的输出泛型
         * */
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);


        //  提交

        //job.submit();

        job.waitForCompletion(true);
    }
}
