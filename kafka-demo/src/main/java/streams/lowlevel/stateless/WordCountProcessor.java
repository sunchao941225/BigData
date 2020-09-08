package streams.lowlevel.stateless;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.Punctuator;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WordCountProcessor implements Processor<String, String> {

    private Map<String, Long> pairs;
    private ProcessorContext processorContext;

    /**
     * 初始化方法
     *
     * @param processorContext 处理器的上下文对象
     */
    @Override
    public void init(ProcessorContext processorContext) {
        this.processorContext = processorContext;
        this.pairs = new HashMap<>();
        // 周期性将处理器的处理结果 发送给下游的处理器
        processorContext.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, new Punctuator() {
            /**
             * 指定方法
             * @param timestamp
             */
            @Override
            public void punctuate(long timestamp) {
                pairs.forEach((k, v) -> {
                    // 转发：将当前处理器的处理结果 转发给下游的处理器
                    processorContext.forward(k, v);
                });
            }
        }); // 第三个参数：周期性执行的内容
        processorContext.commit();
    }

    /**
     * 处理方法
     *
     * @param key
     * @param value
     */
    @Override
    public void process(String key, String value) {
        String[] words = value.split(" ");
        for (String word : words) {
            Long num = pairs.get(word);
            System.out.println(word + "\t" + num);
            if (num == null) {
                pairs.put(word, 1L);
            } else {
                pairs.put(word, num + 1L);
            }
        }
    }

    /**
     * 关闭
     */
    @Override
    public void close() {

    }
}
