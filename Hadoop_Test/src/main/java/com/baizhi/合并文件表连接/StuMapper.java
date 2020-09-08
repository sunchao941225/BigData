package com.baizhi.合并文件表连接;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.IOException;

public class StuMapper extends Mapper<LongWritable, Text, Text, Text> {


    public static final String STU_INFO = "student_info.txt";
    public static final String STU_INFO_CLASS = "student_info_class.txt";

    public static final String STU_INFO_FLAG = "a";
    public static final String STU_INFO_CLASS_FLAG = "b";


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        FileSplit inputSplit = (FileSplit) context.getInputSplit();

        String filname = inputSplit.getPath().getName();

        String[] data = value.toString().split(" ");
        String userid = "";
        String flag = "";
        String valueName = "";

        if (filname.contains(STU_INFO)) {

            userid = data[1];
            flag = STU_INFO_FLAG;

            /*
             * 名字
             * */
            valueName = data[0];

        }

        if (filname.contains(STU_INFO_CLASS)) {


            userid = data[0];
            flag = STU_INFO_CLASS_FLAG;
            /*
            学科
            * */
            valueName = data[1];
        }


        context.write(new Text(userid), new Text(flag + " " + valueName));




    }
}
