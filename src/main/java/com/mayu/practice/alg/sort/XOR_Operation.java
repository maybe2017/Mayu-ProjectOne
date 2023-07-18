package com.mayu.practice.alg.sort;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 马瑜
 * @Date: 2022/12/13 10:13
 * @Description: 位运算 异或
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class XOR_Operation {


    public static void main(String[] args) {
        int[] arr = new int[]{12,8,3,7,7,3,3,3,8,8,8};
        log.info("找到唯一出现奇数次的数: {}", findOne(arr));


        int[] arr1 = new int[]{12,8,7,7,3,3,3,8,8,8};
        log.info("找到两个不同的、出现奇数次数的数: {}", JSON.toJSONString(findTwo(arr1)));
    }

    private static int findOne(int[] arr) {
        int res = 0;
        if (arr == null) {
            return res;
        }
        // o ^ N = N;
        int xor = 0;
        for (int a : arr) {
            xor ^= a;
        }
        return xor;
    }

    // 已知int数组中存储了一批数，已知有两个不同的数字出现了奇数次，其余的数都出现了偶数次，找出这两个数
    private static Integer[] findTwo(int[] arr) {
        if (arr == null) {
            return new Integer[0];
        }

        Integer[] res = new Integer[2];
        int xor = 0;
        for (int value : arr) {
            xor ^= value;
        }

        // 找到xor二进制位序列中 最后一个为1的bit
        int rightBit1Last = xor & (~xor + 1);

        int one = 0;
        for (int value : arr) {
            // 取到value二进制位序列中 最后一个为1的bit  如:00001000
            int rightBit1 = value & (~value + 1);
            // 两个位置上的bit都为1 （如:00001000 ^ 00001000）==> 取数组中 第N位值为1 的一部分
            // !=0的话（如:00000010 ^ 00001000）就是取数组中 第N位值不为1 的另一部分
            if ((rightBit1Last ^ rightBit1) != 0) {
                one ^= value;
            }
        }

        int second = xor ^ one;
        res[0] = one;
        res[1] = second;
        return res;
    }

}
