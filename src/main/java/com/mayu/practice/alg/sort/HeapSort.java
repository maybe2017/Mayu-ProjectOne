package com.mayu.practice.alg.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;

/**
 * @Author: 马瑜
 * @Date: 2023/7/24 09:37
 * @Description: 堆排序 及相关问题
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class HeapSort {

    public static void main(String[] args) {
        int[] arr0 = new int[]{3, 2, 4, 5, 0, 1, 4, 7, 8, 6, 7};
        int[] arr = new int[]{12, 8, 16, 5, 7, 4, 10, 3, 9, 11, 8, 6};
        heapSortArr(arr);
        log.info("堆排序后arr: {}", arr);

        sortArrDistanceLessK(arr0, 6);
        log.info("排序后arr: {}", arr0);
    }

    // 给定一个几乎有序的数组，若使整个数组有序，任意元素移动的距离不会超过k，请用一种合适的排序方法使数组有序
    private static void sortArrDistanceLessK(int[] arr, int k) {
        // 默认小根堆 移动距离不超过k -> 每k + 1 个元素可以确定一个元素在排序后数组的位置
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(k + 1);
        for (int i = 0; i <  k + 1; i++) {
            priorityQueue.add(arr[i]);
        }

        // 堆顶元素 与 (堆尾位置 + 1) 元素交换
        int sortIndex = 0;
        for (int i = k + 1; i < arr.length; i++) {
            arr[sortIndex ++] = priorityQueue.poll();
            priorityQueue.add(arr[i]);
        }

        // 处理堆中剩余的元素
        while (priorityQueue.size() > 0) {
            arr[sortIndex++] = priorityQueue.poll();
        }
    }


    private static void heapSortArr(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 先一个一个元素构建堆(后面的元素从下向上调整)
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }

        // 如果题目直接是给出整个数组元素，而不是一个一个输入元素，可采用下面的方式O(logN)
        // 从最底层开始(其实可以从倒数第二层)，从尾至顶，对每个元素进行向下调整
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }


        // 取出堆顶放到数组结尾 (两个指针 相向而行)
        int heapSize = arr.length;
        int frontOfSortArr = arr.length - 1;
        while (heapSize > 0) {
            // 交换堆顶元素到数组尾巴
            swap(arr, 0, frontOfSortArr);
            heapSize--;

            // 交换后 重新从顶部向下调整
            heapify(arr, 0, heapSize);
            // 指针移动
            frontOfSortArr--;
        }
    }


    // 一个一个元素构建堆(大根堆)
    // 从下向上调整
    private static void heapInsert(int arr[], int cur) {
        int parent = (cur - 1) / 2;
        while (arr[cur] > arr[parent]) {
            swap(arr, cur, parent);
            cur = parent;
            parent = (cur - 1) / 2;
        }
    }


    // 从上向下调整
    private static void heapify(int arr[], int cur, int curHeapSize) {
        int left = 2 * cur + 1;
        while (left < curHeapSize) {
            // where max left or right ?
            int largest = curHeapSize > left + 1 && arr[left + 1] > arr[left] ? left + 1 : left;
            // where max parent or children ?
            largest = arr[cur] > arr[largest] ? cur : largest;
            if (cur == largest) {
                break;
            }
            swap(arr, cur, largest);
            cur = largest;
            left = 2 * cur + 1;
        }
    }

    private static void swap(int arr[], int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}
