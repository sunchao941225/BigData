package com.baizhi.work02;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Job02Mapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    /*
     * apple sanxing xiaomi heimei guge nihao awsl
     * */

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String line = value.toString();
        String[] data = line.split(" ");

        for (String datum : data) {
            context.write(new Text(datum), NullWritable.get());
        }


    }
}
