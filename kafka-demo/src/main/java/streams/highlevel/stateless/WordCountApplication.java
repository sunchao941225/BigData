package streams.highlevel.stateless;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.ArrayList;
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
        KTable<String, Long> kTable = stream
                // null hello
                // null world
                .flatMap((key, value) -> {
                    String[] words = value.toLowerCase().split(" ");
                    ArrayList<KeyValue<String, String>> list = new ArrayList<>();
                    for (String word : words) {
                        KeyValue<String, String> keyValue = new KeyValue<>(key, word);
                        list.add(keyValue);
                    }

                    return list;

                })
                // hello 1L
                // world 1L
                .map((k, v) -> new KeyValue<String, Long>(v, 1L))
                // hello [1,1...]
                // shuffle
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                // hello 2
                .count();

        kTable.toStream().to("t11", Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = sb.build();

        // 打印自动生产的Topology信息
        System.out.println(topology.describe().toString());

        //3. 初始化流处理应用
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        //4. 启动流处理应用
        kafkaStreams.start();
    }
}
