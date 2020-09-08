package com.baizhi.outformat;

import com.baizhi.mr.test01.WCMapper;
import com.baizhi.mr.test01.WCReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
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
        job.setOutputFormatClass(OwnOutputFormat.class);

        /*
         *设置数据输入输出路径
         * */

        //CombineTextInputFormat.setMinInputSplitSize(job, 1048576);
        //CombineTextInputFormat.setInputPaths(job, new Path(" v  "));
        //NLineInputFormat.setInputPaths(job, new Path("G:\\Note\\Day02-Hadoop\\数据文件\\data02"));
        // NLineInputFormat.setNumLinesPerSplit(job,3);
        TextInputFormat.setInputPaths(job, new Path("G:\\Note\\Day02-Hadoop\\数据文件\\flow.dat"));
        /*
         * 注意： 此输出路径不能存在
         * */
        //TextOutputFormat.setOutputPath(job, new Path("/baizhi/out8121231233"));
        OwnOutputFormat.setOutputPath(job, new Path("G:\\Note\\Day02-Hadoop\\数据文件\\outbaizhi"));


        /*
         * 设置MAP 和 REDUCE 处理逻辑
         * */
        job.setMapperClass(FileMapper.class);
        job.setReducerClass(FileReducer.class);

        /*
         * 设置 map任务和reduce任务的输出泛型
         * */
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);


        //  提交

        //job.submit();

        job.waitForCompletion(true);
    }
}
