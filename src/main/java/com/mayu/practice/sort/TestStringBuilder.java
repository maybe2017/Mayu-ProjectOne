package com.mayu.practice.sort;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @Author: 马瑜
 * @Date: 2022/11/18 14:09
 * @Description: TODO
 */
public class TestStringBuilder {

    char[] value = new char[16];

    private void test() {
        final char[] value = this.value;
        int c = 0;
        value[c++] = 'n';
        value[c++] = 'u';
        value[c++] = 'l';
        value[c++] = 'l';
        System.out.println(this.value);
        System.out.println(value);
    }


    private void test2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Integer integer = 100;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(integer);

        // 放入String类型
        list.getClass().getMethod("add", Object.class).invoke(list, "AAAA");
        System.out.println(list.get(1).byteValue());


    }


    public static void main(String[] args) throws Exception {
        System.out.println(Integer.parseInt("8634021411"));;
        new TestStringBuilder().test2();
    }

}
