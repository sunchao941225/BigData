package streams.highlevel.streams;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * JDK8 streams编程
 * <p>
 * 函数式编程
 */
public class StreamsTest {

    public static void main(String[] args) {

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(4);
        list.add(2);
        list.add(1);
        list.add(3);

        list.stream()
                .map((n) -> n * 2) // 映射
                .filter((n) -> n != 2)
                .sorted()
                .forEach(n -> System.out.println(n));

    }
}