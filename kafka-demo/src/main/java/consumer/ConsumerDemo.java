package consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * kafka消费者测试类
 * 1. 订阅 subscribe
 * 2. 拉取 pull
 */
public class ConsumerDemo {
    public static void main(String[] args) throws InterruptedException {
        //1. 指定kafka消费者的配置信息
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "HadoopNode01:9092,HadoopNode02:9092,HadoopNode03:9092");
        // 反序列化器 byte[] ---> Object
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 注意：此配置项 修改偏移量消费策略的默认行为
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        // 关闭消费位置offset的自动提交功能
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);

        // 消费组必须得指定
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group4");


        //2. 创建kafka消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        //3. 订阅主体topic
        // consumer.subscribe(Arrays.asList("t3"));

        // 指定消费分区
        consumer.assign(Arrays.asList(new TopicPartition("t3",0)));

        // 重置消费位置
        consumer.seek(new TopicPartition("t3",0),1);

        //4. 拉取新产生的记录
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.key() + "\t" + record.value() + "\t"
                        + record.topic() + "\t" + record.offset()
                        + "\t" + record.timestamp() + "\t" + record.partition());
            }
            // 手动提交消费位置
            consumer.commitSync();
        }
    }
}
