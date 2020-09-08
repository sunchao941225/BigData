package com.baizhi.mr.test01;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/*
 *keyIn Text 与mapper的keyOut的数据类型相对应
 *valeuIn IntWritable   与mapper的ValueOut的数据类型相对应
 * KeyOut
 * valueOut
 * */
public class WCReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        context.write(key, new IntWritable(sum));
    }
}
