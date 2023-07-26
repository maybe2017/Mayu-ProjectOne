package com.mayu.practice.alg.sort;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 马瑜
 * @Date: 2023/7/22 23:23
 * @Description: 快速排序（相对最快）
 * 时间复杂度(平均期望): N*logN
 * 空间复杂度(平均期望): logN
 * 不稳定
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class QuickSort {
    // 快排的子分区问题:
    // 荷兰国旗1: 给定一个数组，及一个数target，小于等于target放数组左边，大于放右边，要求O(n)
    // 荷兰国旗2: 给定一个数组，及一个数target，小于target放数组左边，等于target放中间，大于放右边，要求O(n)
    public static void main(String[] args) {
        int[] arr0 = new int[]{3, 2, 5, 8, 5, 1, 6, 7, 10, 5, 7};
        partition(arr0, 0, arr0.length - 1, 5);
        log.info("partition后arr: {}", arr0);
    }

    /**
     * 对原始数组arr中，在范围[left, right]上，用数值target进行分区，使[<target区域, =target区域, >target区域]
     * 注意: 数值target一定存在于范围[left, right]中，即=target区域一定存在
     * @param arr 原始数组arr
     * @param left 要进行分区的左边界
     * @param right 要进行分区的右边界
     * @param target 用于进行分区的数值
     * @return 返回分区后的 =target区域的左右边界索引
     */
    private static int[] partition(int[] arr, int left, int right, int target) {

        if (left == right) {
            return new int[]{left, left};
        }

        // 用一个快指针去遍历范围上的数字，使 <target 的数交换到小于区域末尾，>target的数交换到大于区域的开头，=target的跳过。
        int fast = left;

        // slowLess慢指针, 表示小于区域的末尾位置, 先初始化为一个取不到的位置
        int slowLess = left - 1;
        // 大于区域慢指针, 表示大于区域的头部位置
        int slowGreater = right + 1;

        // 当快指针撞上大于区域的头部时 数组范围内的数字已经被遍历完
        while (fast < slowGreater) {
            if (arr[fast] < target) {
                // 小于区域右移 与当前小于target位置上的数交换
                swap(arr, ++slowLess, fast++);
            } else if (arr[fast] > target) {
                // 注意交换后，当前fast位置变成了新数字，不能移
                swap(arr, --slowGreater, fast);
            } else {
                fast ++;
            }
        }

        // 如果小于区域存在 那么等于区域的左边界必定在小于区域末尾+1
        int equalsTargetAreaLeftIndex = slowLess == left - 1 ? left : slowLess + 1;

        // 如果大于区域存在 那么等于区域的右边界必定在大于区域头部-1
        int equalsTargetAreaRightIndex = slowGreater == right + 1 ? right : slowGreater - 1;
        return new int[]{equalsTargetAreaLeftIndex, equalsTargetAreaRightIndex};
    }



    // 引出快速排序(递归)，分区的好坏决定了算法的复杂度，用master公式可计算复杂度，最坏情况是O(N^2)
    // 快排1.0 (每次选择末尾的数去分区，划分结束后与<=区域末尾+1的位置交换) N^2
    // 快排2.0 (每次选择末尾的数去分区，多了等于区域) N^2
    /**
     * @param arr 原始数组arr
     * @param left 要进行分区的左边界
     * @param right 要进行分区的右边界
     */
    private static void quickSortArr(int[] arr, int left, int right) {
        if (arr == null || arr.length < 2) {
            return;
        }

        // partition 子问题:
        // 1、每次选定要排序范围内最后一个元素target，用target进行分区
        int[] equalsTargetAreaIndex = partition(arr, left, right - 1, arr[right]);

        // 2、最终确定target的位置，将选择元素target与等于区域末端位置+1 的位置元素交换


        // 3、递归分区处理target左边与 target右边
        quickSortArr(arr, left, equalsTargetAreaIndex[0] - 1);
        quickSortArr(arr, equalsTargetAreaIndex[1] + 1, right - 1);

    }


    // 快排3.0
    // 选择元素进行分区时，用随机选择的方式进行选择(选定后交换到最后)，那么算法复杂度也会是master公式几种不同情况复杂度的平均值
    // 即平均复杂度 = O(N*logN)
    // 在数组上等概率随机选择一个位置 left + Math.random() * (right - left + 1)
    // 快指针最终会撞上右侧区域的左边界

    private static void swap(int arr[], int i, int j) {
        if (i == j) {
            return;
        }
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}
