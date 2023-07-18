package com.mayu.practice.alg.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Author: 马瑜
 * @Date: 2022/12/13 10:13
 * @Description: 数组排序
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class ArrSortMethod {

    public static void main(String[] args) {
        int[] arr = new int[]{12,8,16,5,7,4,10,3,9,11,8,6};
        selectSort(arr);
        log.info("排序后arr: {}", Arrays.toString(arr));
        int[] arr2 = new int[]{12,8,16,5,7,4,10,3,9,11,8,6};
        bubbleSort(arr2);
        log.info("排序后arr: {}", Arrays.toString(arr2));
        int[] arr3 = new int[]{12,8,16,5,7,4,10,3,9,11,8,6};
        insertSort(arr3);
        log.info("排序后arr: {}", Arrays.toString(arr3));
    }

    // 选择排序 每一趟选择一个最小的元素出来，放在已经排序元素的后面
    // 那么n个元素 至多需要n-1趟去选择最小的元素（最后一个元素必然是最大的，不用再选择）
    public static void selectSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            int currentMixIndex = i;
            // 从剩下未排序的元素中(特别注意起始点是i) 选择最小的
            for (int j = i; j < arr.length; j++) {
                currentMixIndex = arr[j] < arr[currentMixIndex] ? j : currentMixIndex;
            }

            // 如果最小值不是当前i位置上的数 那么进行交换
            if (i != currentMixIndex) {
                int temp = arr[i];
                arr[i] = arr[currentMixIndex];
                arr[currentMixIndex] = temp;
            }
        }
    }

    // 冒泡 属于交换排序 两两比较交换
    // 每一趟对未排序的元素依次进行两两交换 大的元素向后移 那么一趟下来后，最大的元素会被移动到最后面。
    // n个元素至多需要 n-1趟（当某一次没有）
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            // 在剩下未排序的元素中 进行两两比较交换 （比较的次数: n个元素比较n-1次...）
            for (int j = 0; j < arr.length - 1 - i ; j++) {
                // 两两比较 大的向后移动
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    // 交换数组中两个索引位置的数
    // 需要注意的一点是: 在交换数组中i位置和j位置的元素时，i不能等于j，否则就是对同一块内存区域异或
    // 自己异或自己 N ^ N = 0
    public static void swap(int[] arr, int i, int j) {
        // 找到不同的bit 用arr[i]记录
        arr[i] = arr[i] ^ arr[j];
        // 对arr[j]的bit序列 取反，让arr[j]变成arr[i]
        arr[j] = arr[i] ^ arr[j];
        // 对arr[i]的bit序列 取反，(此时arr[j]实际已经变成了原来的arr[i])
        arr[i] = arr[i] ^ arr[j];
    }


    // 插入 斗地主摸牌时的一张张插入动作 第一张不用比较，需要n-1趟
    // 第n趟至多比较n-1次，把最小的依次两两比较交换到最前面去，第二张比较1次，有点像于往前的冒泡排序
    public static int[] insertSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return arr;
        }

        for (int i = 1; i < arr.length; i++) {
            // arr[i]为当前摸的牌
            // 要与前面已经排序的牌比较 直到不小于
            for (int j = i - 1; j >= 0; j--) {
                // 第一次 arr[j+1] 就是 arr[i]，让0 - i(j+1)范围内有序，两两比较把最小的移动到最前面去
                // 不能直接用a[i]，因为i没有移动
                if (arr[j+1] < arr[j]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                } else {
                    break;
                }
            }
        }
        return arr;
    }

}
