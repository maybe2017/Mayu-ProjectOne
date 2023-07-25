package com.mayu.practice.alg.sort;

/**
 * @Author: 马瑜
 * @Date: 2023/7/22 23:23
 * @Description: 快速排序（相对最快）
 * 时间复杂度(平均期望): N*logN
 * 空间复杂度(平均期望): logN
 * 不稳定
 */
public class QuickSort {
    // 快排的子分区问题:
    // 荷兰国旗1: 给定一个数组，及一个数target，小于等于target放数组左边，大于放右边，要求O(n)
    // 荷兰国旗2: 给定一个数组，及一个数target，小于target放数组左边，等于target放中间，大于放右边，要求O(n)

    // 引出快速排序(递归)，分区的好坏决定了算法的复杂度，用master公式可计算复杂度，最坏情况是O(N^2)


    // 快排1.0 (每次选择末尾的数去分区，划分结束后与<=区域末尾+1的位置交换) N^2
    // 快排2.0 (每次选择末尾的数去分区，多了等于区域) N^2
    // partition 子问题:
    // 1、每次选定要排序范围内最后一个元素target，最终确定target的位置（最后一步将选择元素与等于区间末端位置+1 的位置元素交换）;
    // 2、递归分区处理，target左边、右边


    // 快排3.0
    // 选择元素进行分区时，用随机选择的方式进行选择(选定后交换到最后)，那么算法复杂度也会是master公式几种不同情况复杂度的平均值
    // 即平均复杂度 = O(N*logN)
    // 在数组上等概率随机选择一个位置 left + Math.random() * (right - left + 1)
    // 快指针最终会撞上右侧区域的左边界
}
