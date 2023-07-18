package com.mayu.practice.alg.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Author: 马瑜
 * @Date: 2023/07/18 22:02
 * @Description: 二分搜索
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class BinarySearch {

    public static void main(String[] args) {
        int[] arr = new int[]{3, 4, 5, 6, 7, 8, 8, 9, 10, 11, 12, 16};
        log.info("查询target数索引: {}", getIndex(arr, 7));

        int[] arr2 = new int[]{1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4};
        log.info("查询target数索引: {}", getLeftIndex(arr2, 3));
        log.info("查询target数索引: {}", getLeftIndex2(arr2, 2));
    }

    // 寻找数组中的某一个数 在数组内的索引（认为数是唯一不重复的）
    // 左右指针 相向而行
    private static int getIndex(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }

        // 取[left, right)
        int left = 0;
        int right = arr.length;

        while (left < right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) {
                return mid;
            }
            if (arr[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    // 在形如已经排序的数组: 11222233344466666 找到目标值最左侧的index
    private static int getLeftIndex(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }

        int left = 0;
        int right = arr.length;
        int index = -1;
        boolean leftMoved = false;
        while (left < right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) {
                // 考虑左侧
                index = mid;
                while (index >= 0 && arr[index] == target) {
                    index--;
                    leftMoved = true;
                }
            }
            if (arr[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return leftMoved ? index + 1 : index;
    }


    // 一直二分 不断收缩右边界 直到left = right，不能再向左移动了，此时返回left
    private static int getLeftIndex2(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }

        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (arr[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
