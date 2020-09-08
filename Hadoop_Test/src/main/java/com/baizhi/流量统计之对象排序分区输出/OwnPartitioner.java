package com.baizhi.流量统计之对象排序分区输出;

import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

public class OwnPartitioner<KEY, VALUE> extends Partitioner<KEY, VALUE> {

    private static HashMap<String, Integer> areaMap = new HashMap<String, Integer>();


    static {
        areaMap.put("hn", 0);
        areaMap.put("henna", 0);

        areaMap.put("zz", 1);
        areaMap.put("kf", 2);
        areaMap.put("bj", 3);
        areaMap.put("xy", 4);
    }

    public int getPartition(KEY key, VALUE value, int i) {

        FlowBean flowBean = (FlowBean) key;

        return areaMap.get(flowBean.getArea()) == null ? 5 : areaMap.get(flowBean.getArea());
    }
}
