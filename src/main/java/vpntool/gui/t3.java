package vpntool.gui;

import vpntool.utils.parser.CommonStringMethods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;


public class t3 {

    public static Stack<Integer> stack = new Stack<Integer>();

    public static void main(String[] args) {
//        Stack<Integer> stack = new Stack<Integer>();
        int shu[] = {1, 2, 3, 4};
        ArrayList<Stack<Integer>> res = new ArrayList<Stack<Integer>>();

        f(res, shu, 3, 0, 0);

        for (int i = 0; i < res.size(); i++) {
            Stack<Integer> e = res.get(i);
            for (int j = 0; j < e.size() - 1; j++) {
                System.out.println(e.pop());
            }
//            System.out.println((Integer) e.pop());
        }

    }

    public static void f(ArrayList<Stack<Integer>> res, int[] shu, int targ, int has, int cur) {


        if (has == targ) {
            System.out.println(stack);
            res.add(stack);
            return;
        }

        for (int i = cur; i < shu.length; i++) {
            if (!stack.contains(shu[i])) {
                stack.add(shu[i]);
                f (res, shu, targ, has + 1, i);
                stack.pop();
            }


        }

    }

}


