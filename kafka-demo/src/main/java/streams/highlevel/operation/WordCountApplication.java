package streams.highlevel.operation;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * kafka streaming dsl风格（高级API）版的WordCount
 */
public class WordCountApplication {

    public static void main(String[] args) {
        //1. 指定流处理应用的配置信息
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "HadoopNode01:9092,HadoopNode02:9092,HadoopNode03:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-highlevel-application");
        properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);

        //2. 编织拓扑任务
        StreamsBuilder sb = new StreamsBuilder();
        KStream<String, String> stream = sb.stream("t10");

        //============================branch====================================
        /**
        KStream<String, String>[] kStreams = stream.branch(
                (k, v) -> v.startsWith("A"),   // stream: A开头
                (k, v) -> true                 // 其它数据
        );
        kStreams[0].foreach((k,v) -> System.out.println(k + "\t"+v));
        */
        //======================================================================

        //============================filter====================================
        /**
         stream
         .filter((k,v) -> v.startsWith("H"))
         .foreach((k,v)  -> System.out.println(k+"\t"+v));
         **/
        //======================================================================

        //============================filterNot====================================
        /**
         stream
         .filterNot((k,v) -> v.startsWith("H"))
         .foreach((k,v) -> System.out.println(k+"\t"+v));
         **/
        //======================================================================

        //============================flatMap====================================
        /**
         stream
         .flatMap((k,v) -> Arrays.asList(
         new KeyValue<String,String>(k,v.toUpperCase()+"!"),
         new KeyValue<String,String>(k,v.toLowerCase()+"?")))
         .foreach((k,v) -> System.out.println(k +"\t" + v));
         **/
        //======================================================================

        //============================flatMapValues====================================
        /**
         stream
         // null Hello World
         //--------------------
         // null Hello
         // null World
         .flatMapValues((v) -> Arrays.asList(v.split(" ")))
         .foreach((k, v) -> System.out.println(k + "\t" + v));
         **/
        //======================================================================

        //============================groupBy===================================
        /**
         stream
         // null Hello World
         //--------------------
         // null Hello
         // null World
         .flatMapValues((v) -> Arrays.asList(v.split(" ")))
         .groupBy((k,v) -> v)
         .count()
         .toStream()
         .foreach((k,v) -> System.out.println(k+"\t"+v));
         **/
        //======================================================================

        //============================groupByKey===================================
        /**
         stream
         // null Hello World
         //--------------------
         // null Hello
         // null World
         .flatMapValues((v) -> Arrays.asList(v.split(" ")))
         .map((k,v) -> new KeyValue<String,Long>(v,1L))
         .groupByKey(Grouped.with(Serdes.String(),Serdes.Long()))
         .count()
         .toStream()
         .foreach((k,v) -> System.out.println(k+"\t"+v));
         **/
        //======================================================================

        //============================mapValues===================================
        /*
        stream
                // null Hello World
                //--------------------
                // null Hello
                // null World
                .flatMapValues((v) -> Arrays.asList(v.split(" ")))
                .map((k,v) -> new KeyValue<>(v,1L))
                .mapValues(v -> v = v+1)
                .foreach((k,v) -> System.out.println(k+"\t"+v));
         */
        //======================================================================

//        //============================merge===================================
//        KStream<String, String>[] streams = stream
//                .branch(
//                        (k, v) -> v.startsWith("A"),
//                        (k, v) -> v.startsWith("B"),
//                        (k, v) -> true
//
//                );
//        streams[0].merge(streams[2])
//                .foreach((k,v) -> System.out.println(k+"\t"+v));
//        //======================================================================

        //============================peek===================================
        //stream.peek((k,v) -> System.out.println(k+"\t"+v));
        //======================================================================

        //============================print===================================
        // stream.print(Printed.toSysOut());
        //======================================================================


        //============================selectkey===================================
        // stream.selectKey((k,v) -> "Hello:").print(Printed.toSysOut());
        //======================================================================

        //----------------------------aggregate-------------------------------
        stream
                // null Hello Hello
                .flatMapValues(v -> Arrays.asList(v.split(" ")))
                // null Hello
                // null Hello
                .groupBy((k,v) -> v,Grouped.with(Serdes.String(),Serdes.String()))
                // Hello [Hello,Hello].length
                // Hello 2+0
                // 参数一： 初始化器  参数二：聚合器(k: word, v: [])
                .aggregate(()->0L,(k,v,aggs) ->{
                    System.out.println(k + "\t" + v +"\t"+aggs);
                    return aggs + 1L;
                },Materialized.with(Serdes.String(),Serdes.Long()))
                .toStream()
                .print(Printed.toSysOut());
        //--------------------------------------------------------------------

        //----------------------------reduce-------------------------------
        /*
         stream
                 // null Hello Hello
                 .flatMapValues(v -> Arrays.asList(v.split(" ")))
                 // null Hello
                 // null Hello
                 .map((k,v) -> new KeyValue<String,Long>(v,1L))
                 .groupByKey(Grouped.with(Serdes.String(),Serdes.Long()))
                 // Hello [1,1,1]
                 // World [1,1,1,1]
                 // 参数一： 初始化器  参数二：聚合器(k: word, v: [])
                 .reduce((v1,v2) -> {
                     System.out.println(v1 +"\t"+v2);
                     Long result = v1+v2;
                     return result;
                 },Materialized.with(Serdes.String(),Serdes.Long()))

             .toStream()
             .print(Printed.toSysOut());
         */
        //--------------------------------------------------------------------

        Topology topology = sb.build();

        // 打印自动生产的Topology信息
        System.out.println(topology.describe().toString());

        //3. 初始化流处理应用
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        //4. 启动流处理应用
        kafkaStreams.start();
    }
}
