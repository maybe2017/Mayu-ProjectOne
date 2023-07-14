package com.mayu.practice.node;

/**
 * @Author: 马瑜
 * @Date: 2022/12/19 11:16
 * @Description: 双向链表
 */
public class TwoPointerLinkedList {

    private static class TwoPointerNode<V> {
        TwoPointerNode(V value) {
            this.value = value;
        }

        V value;
        TwoPointerNode<V> next;
        TwoPointerNode<V> last;

        @Override
        public String toString() {
            return "TwoPointerNode{" +
                    "value=" + value +
                    ", next=" + next +
                    ", last=" + last +
                    '}';
        }
    }

    // 实现双向链表
    public static class MyLinkedList<V> {
        // 头结点
        private TwoPointerNode<V> head;
        // 尾结点
        private TwoPointerNode<V> tail;
        // 当前节点数量
        private int size;


        // 打印链表节点
        private void displayFromHead() {
            if (null == head) {
                System.out.println("无节点信息");
            } else {

                TwoPointerNode<V> cur = head;
                while (cur != null) {
                    System.out.println(cur.value);
                    cur = cur.next;
                }
            }
        }

        // 增加节点
        private void addNode(V nodeValue) {
            TwoPointerNode<V> cur = new TwoPointerNode<>(nodeValue);
            if (null == head) {
                head = cur;
                tail = cur;
            } else {
                // 头插法
                cur.next = head;
                head.last = cur;
                head = cur;
            }
            size++;
        }

        private void addNodeAfterTail(V nodeValue) {
            TwoPointerNode<V> cur = new TwoPointerNode<>(nodeValue);
            if (null == head) {
                head = cur;
                tail = cur;
            } else {
                // 尾插法
                cur.last = head;
                tail.next = cur;
                tail = cur;
            }
            size++;
        }


        // 在某个位置上插入
        private void insetNode(int index) {
            if (index < 0 || index >= size) {
                return;
            }




        }
    }

    public static void main(String[] args) {
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        linkedList.displayFromHead();
        linkedList.addNode(1);
        linkedList.addNode(2);
        linkedList.addNode(3);
        linkedList.addNodeAfterTail(4);
        linkedList.addNodeAfterTail(5);
        linkedList.addNodeAfterTail(6);
        linkedList.displayFromHead();
    }

    // 反转双向链表
    private static <V> TwoPointerNode<V> reverse(TwoPointerNode<V> head) {
        return null;
    }


}
