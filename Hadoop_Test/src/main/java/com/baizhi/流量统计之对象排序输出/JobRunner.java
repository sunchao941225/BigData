package com.baizhi.流量统计之对象排序输出;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class JobRunner {

    public static void main(String[] args) throws Exception {

        //System.setProperty("HADOOP_USER_NAME", "root");

        Configuration conf = new Configuration();
       /* conf.addResource("conf2/core-site.xml");
        conf.addResource("conf2/hdfs-site.xml");
        conf.addResource("conf2/mapred-site.xml");
        conf.addResource("conf2/yarn-site.xml");
        conf.set(MRJobConfig.JAR, "G:\\IDEA_WorkSpace\\BigData\\Hadoop_Test\\target\\Hadoop_Test-1.0-SNAPSHOT.jar");
        conf.set("mapreduce.app-submission.cross-platform", "true");
*/
        Job job = Job.getInstance(conf);


        job.setJarByClass(JobRunner.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);


        TextInputFormat.setInputPaths(job, new Path("G:\\Note\\Day02-Hadoop\\数据文件\\flow.dat"));

        TextOutputFormat.setOutputPath(job, new Path("G:\\Note\\Day02-Hadoop\\数据文件\\out4"));
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(NullWritable.class);


        job.setOutputKeyClass(FlowBean.class);
        job.setOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(1);

        job.waitForCompletion(true);
    }
}
