package streams.lowlevel.stateful.remote;

import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 通过流处理 实现实时的WordCount
 */
public class WordCountApplication {
    public static void main(String[] args) {
        //1. 指定Kafka Streaming配置信息
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "HadoopNode01:9092,HadoopNode02:9092,HadoopNode03:9092");
        // 声明key和value的序列化和反序列化器
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // 流处理应用程序的名称 默认会成为消费组的名称
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        //properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);

        //2. 手动编织拓扑任务
        Topology topology = new Topology();

        topology.addSource("s1", "t8");
        // 添加计算计算逻辑
        topology.addProcessor("p1", new ProcessorSupplier() {
            @Override
            public Processor get() {
                return new WordCountProcessor();
            }
        }, "s1");

        Map<String,String> config = new HashMap<>();

        // kafka topic的数据删除策略： delete(默认) 数据保留周期 7天
        //                          compact(紧凑)  新值覆盖旧值
                                    // Hello 2
                                    // Hello 6
        config.put("cleanup.policy","compact");

        // 状态管理的初始化代码
        StoreBuilder<KeyValueStore<String, Long>> countStoreSupplier = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("Counts"), // 状态存储的类型
                Serdes.String(),  // 状态存储的key的序列化和反序列化器
                Serdes.Long())    // value的序列化和反序列化器
                .withLoggingEnabled(config); // 开启remote state store

        // 将p1处理器计算产生的中间结果 状态存储
        topology.addStateStore(countStoreSupplier,"p1");


        // s1 ---> p1 ---> k1
        // 注意：此时结果输出类型不匹配默认类型，需要手动指定输出类型
        topology.addSink("k1", "t9", new StringSerializer(),new LongSerializer(),"p1");

        //3. 初始化KafkaStreaming应用
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);

        //4. 启动流处理应用
        kafkaStreams.start();
    }
}
