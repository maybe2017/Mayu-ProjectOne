package com.mayu.practice.alg.sort;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 马瑜
 * @Date: 2023/07/20 23:04
 * @Description: 归并排序 求数组小和问题
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class MergeSortForCalcSmallSum {

    // 对无序数组中元素形如: 1 3 4 2 5 求数组的小和，
    // 小和 = 每个元素，左边的所有小于当前元素的和； 数组的小和: 每个元素的小和的累加
    public static void main(String[] args) {
        int[] arr0 = new int[]{1, 3, 4, 2, 5};
        int[] arr = new int[]{12, 8, 16, 5, 7, 4, 10, 3, 9, 11, 8, 6};
        int smallSum = calcArrSmallSum(arr);
        log.info("排序后arr: {}", arr);
        log.info("数组最小和: {}", smallSum);
    }

    private static int calcArrSmallSum(int[] arr) {
        int smallSum = 0;
        return doRecursionAndMergeSort(arr, 0, arr.length - 1, smallSum);
    }

    private static int doRecursionAndMergeSort(int[] originArr, int left, int right, int smallSum) {

        // 递归终止条件 当l = r时，单个元素的区间不会进行合并动作，直接返回
        if (left == right) {
            return smallSum;
        }

        // 否则就 左右各一半进行子递归
        int mid = left + ((right - left) >> 1);
        smallSum = doRecursionAndMergeSort(originArr, left, mid, smallSum);
        smallSum = doRecursionAndMergeSort(originArr, mid + 1, right, smallSum);

        // 将子问题结果处理
        return smallSum + merge(originArr, left, mid, right);
    }

    private static int merge(int[] arr, int left, int mid, int right) {
        int sum = 0;
        if (left == right - 1) {
            int temp = arr[left];
            arr[left] = Math.min(arr[left], arr[right]);
            arr[right] = Math.max(temp, arr[right]);
            return arr[left];
        }

        int leftPointer = left;
        int rightPointer = mid + 1;

        // 辅助数组
        int[] helpArr = new int[right - left + 1];
        int helpArrIndex = 0;

        while (leftPointer <= mid && rightPointer <= right) {
            if (arr[leftPointer] > arr[rightPointer]) {
                helpArr[helpArrIndex++] = arr[rightPointer++];

            } else {
                // 左边指针指向的数 需要和右边的数都做比较(因为已经排好序，故找到第一个大于当前左边的数即可)
                if (arr[leftPointer] < arr[rightPointer]) {
                    // 此时，[rightPointer, right] 这个范围内的数，都会比arr[leftPointer]大
                    sum += arr[leftPointer] * (right - rightPointer + 1);
                }

                // 特别注意: 值相等的时候，先拷贝右边的元素到辅助空间
                helpArr[helpArrIndex++] = arr[leftPointer++];
            }

        }

        // 左边未到边界，说明左边剩下的元素都是大的，不再存在小和
        while (leftPointer <= mid) {
            helpArr[helpArrIndex++] = arr[leftPointer++];
        }

        // 右边未到边界，说明剩下的元素都比左边大，但是都已经被计算过
        while (rightPointer <= right) {
            helpArr[helpArrIndex++] = arr[rightPointer++];
        }

        // 因为[L, R] 只是原数组的一部分，所以需要将辅助空间的顺序覆盖到原数组中
        for (int i = 0; i < helpArr.length; i++) {
            arr[left++] = helpArr[i];
        }
        return sum;
    }
}
