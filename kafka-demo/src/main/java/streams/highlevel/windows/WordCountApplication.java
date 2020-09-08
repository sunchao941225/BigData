package streams.highlevel.windows;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
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

        stream
                .flatMapValues(v -> Arrays.asList(v.split(" ")))
                .map((k,v) -> new KeyValue<String,Long>(v,1L))
                .groupByKey(Grouped.with(Serdes.String(),Serdes.Long()))
                // 引入窗口计算（翻滚窗口）
                // 统计20秒内 单词出现的次数
                //.windowedBy(TimeWindows.of(Duration.ofSeconds(20)))

                // 跳跃窗口(Hopping)
                //.windowedBy(TimeWindows.of(Duration.ofSeconds(10)).advanceBy(Duration.ofSeconds(3)))

                // 会话窗口(Session)
                .windowedBy(SessionWindows.with(Duration.ofSeconds(15)))
                .reduce((v1,v2) -> v1+v2)
                .toStream()
                .foreach((k,v) -> {
                    Window window = k.window();
                    long start = window.start();
                    long end = window.end();
                    Date date1 = new Date(start);
                    Date date2 = new Date(end);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println(sdf.format(date1) +"\t"+sdf.format(date2));
                    System.out.println(k.key() +"\t"+v);
                    System.out.println("----------------------------------------------");
                });
        Topology topology = sb.build();

        // 打印自动生产的Topology信息
        System.out.println(topology.describe().toString());

        //3. 初始化流处理应用
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        //4. 启动流处理应用
        kafkaStreams.start();
    }
}
