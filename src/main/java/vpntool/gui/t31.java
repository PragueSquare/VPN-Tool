package vpntool.gui;

import java.util.ArrayList;
import java.util.Stack;

public class t31 {
    static int cnt = 0;
    static Stack<Integer> s = new Stack<Integer>();
    static boolean[] used = new boolean[10000];

    static ArrayList<Stack<Integer>> res = new ArrayList<Stack<Integer>>();

    public static void kase2(ArrayList<Stack<Integer>> res, int minv, int maxv, int curnum, int maxnum) {
        if (curnum == maxnum) {
            cnt++;
            System.out.println(s);

            res.add(s);

            return;
        }

        for (int i = minv; i < maxv; i++) {
            if (!used[i]) {
                s.push(i);
                used[i] = true;
                kase2(res, minv, maxv, curnum + 1, maxnum);
                s.pop();
                used[i] = false;
            }
        }
    }

    public static void main(String[] args) {
//        ArrayList<Stack<Integer>> res = new ArrayList<Stack<Integer>>();

        kase2(res, 1, 5, 0, 3);
        System.out.println(cnt + "-------------------------");

        for (int i = 0; i < res.size(); i++) {
            Stack<Integer> e = res.get(i);
            for (int j = 0; j < e.size() - 1; j++) {
                System.out.println(e.pop());
            }
//            System.out.println((Integer) e.pop());
        }

    }
}
