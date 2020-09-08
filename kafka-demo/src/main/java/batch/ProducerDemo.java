package batch;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;

/**
 * kafka 生产者的测试类
 */
public class ProducerDemo {

    public static void main(String[] args) {
        //1. 准备Kafka生产者配置信息
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"HadoopNode01:9092,HadoopNode02:9092,HadoopNode03:9092");
        // string 序列化（Object ---> byte[]）器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG,8000);

        //2. 创建kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        //3. 生产记录并将其发布

        for (int i = 15; i < 20; i++) {
            // key不为null  第一种策略
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("t3", UUID.randomUUID().toString(),"Hello Kafka"+i);
            // key为null 轮询策略
            producer.send(record);
        }

        //4. 释放资源
        producer.flush();
        producer.close();
    }
}
