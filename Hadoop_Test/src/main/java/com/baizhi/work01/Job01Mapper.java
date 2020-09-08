package com.baizhi.work01;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Job01Mapper extends Mapper<LongWritable, Text, NullWritable, Text> {
    /*
    *707a2a9c-c89f-4041-a276-df12611a9363 http://localhost:1211/ShortVideoProject/index.jsp 2019-05-19 22:28:16 115.60.10.225 Chrome Windows Windows-10 zh-CN 河南省郑州市 thisisshortvideoproject'slog
     {dataSource-1} inited
    *
    * */

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();


        if (line.contains("thisisshortvideoproject'slog")) {
            String cleanLog = line.split("thisisshortvideoproject'slog")[0].trim();

            context.write(NullWritable.get(), new Text(cleanLog));
        }
    }
}




