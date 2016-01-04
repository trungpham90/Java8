/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Trung Pham
 */
public class Chapter2 {
    public int parallelForLoopWordCount(List<String> list , int processor) throws InterruptedException, ExecutionException{
        if(processor <= 0){
            throw new IllegalArgumentException("Invalid number of processor!");
        }
        int num = Integer.max(1, list.size()/processor);
        int index = 0;
        ExecutorService es = Executors.newFixedThreadPool(processor);
        int total = 0;
        for(int i = 0; i < processor; i++){
            final List<String> tmp = new ArrayList();
            for(int j = 0; j < num && index < list.size(); j++){
                tmp.add(list.get(index++));
            }
            if(i + 1 == processor){
                while(index < list.size()){
                    tmp.add(list.get(index++));
                }
            }
            Callable<Integer> call = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int result = 0;
                    for(String word : tmp){
                        if(word.length() > 12){
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
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<String> list = Arrays.asList("TESTTESTTESdsadasdsadsaTTEST" , "abcdefghijkasdasdhlmn" , "aaaaaa");
        System.out.println(new Chapter2().parallelForLoopWordCount(list, 2));
    }
}
