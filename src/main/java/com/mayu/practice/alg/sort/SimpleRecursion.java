package com.mayu.practice.alg.sort;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 马瑜
 * @Date: 2023/07/20 20:59
 * @Description: 简单递归 归并排序
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class SimpleRecursion {


    public static void main(String[] args) {
        int[] arr = new int[]{12, 8, 16, 5, 7, 4, 10, 3, 9, 11, 8, 6};
        log.info("查询最大值: {}", getMaxOfArr(arr, 0, arr.length - 1));
        recursionAndMergeSort(arr);
        log.info("排序后arr: {}", arr);
    }

    // 用递归求一个无序数组中，在[L, R]范围内的最大值
    private static int getMaxOfArr(int[] arr, int left, int right) {
        // 递归终止条件 当l = r时，返回
        if (left == right) {
            return arr[left];
        }

        // 否则就 左右各一半进行子递归
        int mid = left + ((right - left) >> 1);
        int leftMax = getMaxOfArr(arr, left, mid);
        int rightMax = getMaxOfArr(arr, mid + 1, right);
        return Math.max(leftMax, rightMax);
    }

    // 归并排序 N*logN (没有浪费每一次比较行为，比较行为的结果都留下来了，变成了一个整体有序的部分，去跟下一个更大部分的去merge)
    // 将数组分为两半(子问题就持续二分) 两段分别进行排序 然后合并(典型的递归思想)
    private static void recursionAndMergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        doRecursionAndMergeSort(arr, 0, arr.length - 1);
    }

    // 注意数组这个参数在递归里面 一直都是原始数组
    private static void doRecursionAndMergeSort(int[] originArr, int left, int right) {
        // 递归终止条件 当l = r时，返回
        if (left == right) {
            return;
        }

        // 否则就 左右各一半进行子递归
        int mid = left + ((right - left) >> 1);
        doRecursionAndMergeSort(originArr, left, mid);
        doRecursionAndMergeSort(originArr, mid + 1, right);

        // 将子问题结果处理
        merge(originArr, left, mid, right);
    }

    // 要求从小到大排序
    private static void merge(int[] arr, int left, int mid, int right) {
        if (left == right - 1) {
            int temp = arr[left];
            arr[left] = Math.min(arr[left], arr[right]);
            arr[right] = Math.max(temp, arr[right]);
            return;
        }

        // 定义两个指针 分别指向左右两部分 的第一个元素
        // 注意leftPointer指针的值不是0 !!
        int leftPointer = left;
        int rightPointer = mid + 1;

        // 辅助数组
        int[] helpArr = new int[right - left + 1];
        int helpArrIndex = 0;

        while (leftPointer <= mid && rightPointer <= right) {
            // 注意这里 是最小的指针才移动 不是最小的停留，下次还要进行比较
            // 经典归并 这个地方，是小于等于的时候，都移动左边指针
            int min = arr[leftPointer] <= arr[rightPointer] ? arr[leftPointer++] : arr[rightPointer++];
            helpArr[helpArrIndex++] = min;
        }

        // 可能有一边的指针还没有移动到边界
        while (leftPointer <= mid) {
            helpArr[helpArrIndex++] = arr[leftPointer++];
        }

        while (rightPointer <= right) {
            helpArr[helpArrIndex++] = arr[rightPointer++];
        }

        // 因为[L, R] 只是原数组的一部分，所以需要将辅助空间的顺序覆盖到原数组中
        for (int i = 0; i < helpArr.length; i++) {
            arr[left++] =  helpArr[i];
        }
    }
}
