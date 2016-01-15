/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercises;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
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

    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Iterator<T> it = second.iterator();
        Stream.Builder<T> builder = Stream.builder();

        first.forEach(w -> {
            builder.add(w);
            if (it.hasNext()) {
                builder.add(it.next());
            } else {
                first.close();
            }
        });
        return builder.build();
    }

    public static <T> ArrayList<T> joinOne(Stream<ArrayList<T>> stream) {

        Optional<ArrayList<T>> result = stream.reduce((a, b) -> {
            a.addAll(b);
            return a;
        });

        return result.get();
    }

    public static <T> ArrayList<T> joinTwo(Stream<ArrayList<T>> stream) {
        ArrayList<T> identity = new ArrayList();
        ArrayList<T> result = stream.reduce(identity, (a, b) -> {
            a.addAll(b);
            return a;
        });

        return result;
    }

    public static <T> ArrayList<T> joinThree(Stream<ArrayList<T>> stream) {
        ArrayList<T> identity = new ArrayList();
        ArrayList<T> result = stream.reduce(identity, (a, b) -> {
            a.addAll(b);
            return a;
        }, (a, b) -> {
            a.addAll(b);
            return a;
        });

        return result;
    }

    public static double average(Stream<Double> stream) {

        return stream.reduce(new Average(), Average::accept, Average::combine).average();
    }

    static class Average {

        int count;
        double total;

        Average() {
            count = 0;
            total = 0;
        }

        Average accept(Double val) {
            count++;
            total += val;
            return this;
        }

        Average combine(Average other) {
            count += other.count;
            total += other.total;
            return this;
        }

        double average() {
            return total / count;
        }

    }

    public static <T> ArrayList<T> merge(Stream<T> stream, int n) {
        AtomicInteger index = new AtomicInteger(0);

        ArrayList<T> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(null);
        }
        stream.parallel().forEach((a) -> {
            int v = index.getAndIncrement();
            result.set(v, a);
        });
        return result;
    }

    public static AtomicInteger[] countShortWord(Stream<String> stream, final int n) {
        AtomicInteger[] count = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            count[i] = new AtomicInteger(0);
        }
        stream.parallel().forEach(a -> {
            System.out.println(a.length());
            if (a.length() < n) {
                
                count[a.length()].getAndIncrement();
            }
        });
        return count;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Stream<String> stream = Stream.generate(() -> {
            int v = (int) (Math.random() * 50);
            String result = "";
            for (int i = 0; i < v; i++) {
                result += 'a';
            }
            return result;
        }).limit(50);
        

        int[] values = {1, 4, 9, 16};
        Stream<Object> test = Stream.of(values);
        IntStream primitiveStream = IntStream.of(values);
        Stream<Character> charStream = characterStream("Java 8");
        charStream.forEach(w -> System.out.println(w));
        Stream<Character> testZip = zip(characterStream("AAAA"), characterStream("BBB"));
        testZip.forEach(w -> System.out.println(w));
        Stream<ArrayList<Integer>> listStream = Stream.generate(() -> {
            ArrayList<Integer> result = new ArrayList();
            result.add((int) (Math.random() * 100));
            return result;
        }).limit(3);
        System.out.println(joinThree(listStream));
        System.out.println(average(Stream.of(5.0, 3.0, 4.0, 9.0)));
        String testStream = "abcdefghijklmn";
        System.out.println(merge(characterStream(testStream), testStream.length()));
        System.out.println(Arrays.toString(countShortWord(stream, 12)));
    }
}
