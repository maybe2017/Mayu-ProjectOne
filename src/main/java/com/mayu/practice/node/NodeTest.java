package com.mayu.practice.node;

/**
 * @Author: 马瑜
 * @Date: 2022/12/14 21:46
 * @Description:
 */
public class NodeTest {

    public static class Node {
        Node(int value) {
            this.value = value;
        }

        private int value;
        private Node next;

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", next=" + next +
                    '}';
        }
    }

    // 反转链表
    // 思路: 头节点变尾节点 那么头节点的指针域(即next引用变量)应指向null, 但是初始该next是指向第二个节点，所以应该用个变量记录下，不然直接指向null就没了
    // 尾节点变头节点 本来next指向null，那么反转后就应该不为空，指向倒数第二个节点。
    // 且最后要将尾节点返回 即使head指向它
    private static Node reverseLinkedList(Node head) {
        Node next = null;
        Node pre = null;

        while (head != null) { // 因为处理最后一个节点时 next值是指向null的，后面没有节点了嘛，到最后一步head=next,head必然指向null的
            // 用next记录下一个节点
            next = head.next;
            // 将头节点的next指向上一个节点，第一次移动肯定是null
            head.next = pre;
            // 将当前头节点(指针的值 or 引用的地址) 赋予 pre变量，为下一轮移动做准备
            pre = head;
            // 将next指针值赋予head变量，即让head移动到了下一个节点，让head这个引用指向了下一个节点内存
            // 疑惑，改变了head，会改变pre吗？
            // ==> 不会！要深刻理解=号的作用: pre=head指的是将head引用现在的地址赋予pre，指针的角度说，就是让pre指向head目前指向的内存
            // 但是后续head另外指向任何地址，不会影响pre
            head = next;
        }
        return pre;
    }


    // TODO
    // 单向 双向 队列 栈 双端队列
    // 每取k长度元素反转
    // 两个链表相加 值相加 覆盖长链表节点
    // 两个链表合并 有序

    public static void main(String[] args) {
        Node n1 = new Node(1);
        n1.next = new Node(2);
        n1.next.next = new Node(3);
//        changeRef(n1);

        System.out.println(reverseLinkedList(n1));
    }


    // java的引用传递 我的理解是 两个引用变量在开始的时候指向同一内存，
    // 在新方法体中，复制的参数引用变量param，可以指向其他的内存空间，而不影响原始引用
    private static void changeRef(Node param) {
        param.next = new Node(3);
        param.value = 3;
        param = new Node(4);
    }


}
