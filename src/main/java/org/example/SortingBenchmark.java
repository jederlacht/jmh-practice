/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Fork(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SortingBenchmark {

    private static final int ARRAY_SIZE = 20_000;
    private static final int MAX_VALUE = 32_000;

    private Integer[] testArray = new Random().ints(ARRAY_SIZE, 0, MAX_VALUE).boxed().toArray(Integer[]::new);
    private Integer[] sortedArray = new Integer[ARRAY_SIZE];

    @Benchmark
    public void quickSortBench(Blackhole bh) {
        System.arraycopy(testArray, 0, sortedArray, 0, testArray.length);
        quickSort(sortedArray);
        bh.consume(sortedArray);
    }

    @Benchmark
    public void bubbleSortBench(Blackhole bh) {
        System.arraycopy(testArray, 0, sortedArray, 0, testArray.length);
        bubbleSort(sortedArray);
        bh.consume(sortedArray);
    }

    @Benchmark
    public void mergeSortBench(Blackhole bh) {
        System.arraycopy(testArray, 0, sortedArray, 0, testArray.length);
        Arrays.sort(sortedArray, Collections.reverseOrder());
        bh.consume(sortedArray);
        //bh.consume(Arrays.stream(sortedArray).sorted().toArray());
    }



    public static  <T extends Comparable<T>> void  bubbleSort(T[] arr) {
        boolean swap;
        for (int i = 1; i < arr.length; i++) {
            swap = false;
            for (int j = 0; j < arr.length - i; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swap = true;
                }
            }
            if (!swap) {
                break;
            }
        }
    }

    public  static <T extends Comparable<T>> void quickSort(T[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private static <T extends Comparable<T>> void quickSort(T[] arr, int a, int b) {
        if (a < b) {
            int i = a, j = b;
            T x = arr[(i + j) / 2];

            do {
                while (arr[i].compareTo(x) < 0) i++;
                while (x.compareTo(arr[j]) < 0) j--;

                if ( i <= j) {
                    T tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                    i++;
                    j--;
                }

            } while (i <= j);

            quickSort(arr, a, j);
            quickSort(arr, i, b);
        }
    }

}
