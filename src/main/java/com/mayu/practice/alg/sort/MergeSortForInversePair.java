package com.mayu.practice.alg.sort;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2023/07/21 01:05
 * @Description: 归并排序 求数组逆序对问题
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class MergeSortForInversePair {

    public static void main(String[] args) {
        int[] arr0 = new int[]{3, 2, 4, 5, 0};
        int[] arr = new int[]{12, 8, 16, 5, 7, 4, 10, 3, 9, 11, 8, 6};
        List<Pair> allPairs = calcInversePair(arr0);
        log.info("排序后arr: {}", arr0);
        log.info("数组所有逆序对: {}", JSON.toJSONString(allPairs));
    }

    // 对无序数组中元素形如: 3 2 4 5 0 求数组的所有逆序对，
    // 逆序对 = 任意左边元素x 大于任意右侧元素y，组成一个逆序对(x，y)
    private static List<Pair> calcInversePair(int[] originArr) {
        return doRecursionAndMergeSort(originArr, 0, originArr.length - 1);
    }

    private static List<Pair> doRecursionAndMergeSort(int[] originArr, int left, int right) {
        List<Pair> res = new ArrayList<>();
        // 递归终止条件 当l = r时，返回
        if (left == right) {
            return res;
        }

        // 否则就 左右各一半进行子递归
        int mid = left + ((right - left) >> 1);
        List<Pair> leftPairs = doRecursionAndMergeSort(originArr, left, mid);
        List<Pair> rightPairs = doRecursionAndMergeSort(originArr, mid + 1, right);

        // 将子问题结果处理
        List<Pair> mergePairs = merge(originArr, left, mid, right);

        res.addAll(leftPairs);
        res.addAll(rightPairs);
        res.addAll(mergePairs);
        return res;
    }

    // 要求从小到大排序
    private static List<Pair> merge(int[] arr, int left, int mid, int right) {
        List<Pair> res = new ArrayList<>();
        if (left == right - 1) {
            int temp = arr[left];
            arr[left] = Math.min(arr[left], arr[right]);
            arr[right] = Math.max(temp, arr[right]);

            // 不等于说明被交换过 原来的arr[left]要大于右侧arr[right]
            if (temp != arr[left]) {
                res.add(new Pair(temp, arr[left]));
            }
            return res;
        }

        // 定义两个指针 分别指向左右两部分 的第一个元素
        // 注意leftPointer指针的值不是0 !!
        int leftPointer = left;
        int rightPointer = mid + 1;

        // 辅助数组
        int[] helpArr = new int[right - left + 1];
        int helpArrIndex = 0;

        while (leftPointer <= mid && rightPointer <= right) {
            if (arr[leftPointer] > arr[rightPointer]) {

                // 因为已经排好序，故找到左侧第一个大于当前右边的数即可
                for (int i = leftPointer; i <= mid; i++) {
                    res.add(new Pair(arr[i], arr[rightPointer]));
                }
                helpArr[helpArrIndex++] = arr[rightPointer++];
            } else {
                // 特别注意: 值相等的时候，先拷贝右边的元素到辅助空间
                helpArr[helpArrIndex++] = arr[leftPointer++];
            }
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
        return res;
    }

    @Data
    static class Pair {
        int left;
        int right;
        Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
