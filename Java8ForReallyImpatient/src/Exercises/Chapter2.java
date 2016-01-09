/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercises;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Trung Pham
 */
public class Chapter2 {

    public int parallelForLoopWordCount(List<String> list, int processor) throws InterruptedException, ExecutionException {
        if (processor <= 0) {
            throw new IllegalArgumentException("Invalid number of processor!");
        }
        int num = Integer.max(1, list.size() / processor);
        int index = 0;
        ExecutorService es = Executors.newFixedThreadPool(processor);
        int total = 0;
        for (int i = 0; i < processor; i++) {
            final List<String> tmp = new ArrayList();
            for (int j = 0; j < num && index < list.size(); j++) {
                tmp.add(list.get(index++));
            }
            if (i + 1 == processor) {
                while (index < list.size()) {
                    tmp.add(list.get(index++));
                }
            }
            Callable<Integer> call = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int result = 0;
                    for (String word : tmp) {
                        if (word.length() > 12) {
                            result++;
                        }
                    }
                    return result;
                }
            };
            Future<Integer> result = es.submit(call);
            total += result.get();
        }
        es.shutdown();
        return total;
    }

    public long firstFiveLongWords(List<String> list) {
        return list.stream().filter(w
                -> w.length() > 5
        ).limit(5).count();
    }

    public long firstFiveLongWordsParallel(List<String> list) {
        return list.parallelStream().filter(w
                -> w.length() > 5
        ).limit(5).count();
    }

    public long countLongWords(List<String> list) {
        return list.stream().filter(w
                -> w.length() > 5
        ).count();
    }

    public long countLongWordsParallel(List<String> list) {
        return list.parallelStream().filter(w
                -> w.length() > 5
        ).count();
    }

    public Stream<BigInteger> generateIntegerStream() {
        final long a = 25214903917L, c = 11, m = 1L << 48;
        Stream<BigInteger> stream = Stream.iterate(BigInteger.ZERO, n -> n.multiply(BigInteger.valueOf(a)).add(BigInteger.valueOf(c)).mod(BigInteger.valueOf(m)));
        return stream;
    }

    public static Stream<Character> characterStream(String s) {
        return IntStream.iterate(0, w -> w + 1).limit(s.length()).mapToObj(w -> s.charAt(w));
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Stream<String> stream = Stream.generate(() -> {
            int v = (int) (Math.random() * 10);
            String result = "";
            for (int i = 0; i < v; i++) {
                result += 'a';
            }
            return result;
        }).limit(100000);
        List<String> list = stream.collect(Collectors.toList());
        long start = System.nanoTime();
        System.out.println(new Chapter2().countLongWords(list));
        long end = System.nanoTime();
        System.out.println((end - start));
        start = System.nanoTime();
        System.out.println(new Chapter2().countLongWordsParallel(list));
        end = System.nanoTime();
        System.out.println((end - start));

        int[] values = {1, 4, 9, 16};
        Stream<Object> test = Stream.of(values);
        IntStream primitiveStream = IntStream.of(values);
        Stream<Character> charStream = characterStream("Java 8");
        charStream.forEach(w -> System.out.println(w));
    }
}
