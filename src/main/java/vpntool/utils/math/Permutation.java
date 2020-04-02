package vpntool.utils.math;

import java.util.ArrayList;
import java.util.Stack;

public class Permutation {
    public static ArrayList<ArrayList<String>> generateArrayListForEqualNum(ArrayList<String> constants, int mapNum) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        kase2(0, constants.size() - 1, 0, mapNum);
        for (int i = 0; i < res.size(); i++) {
            ArrayList<String> oneTerm = new ArrayList<String>();
            Stack<Integer> e = res.get(i);
//            System.out.println(e);
            for (int j = 0; j < e.size(); j++) {

//                /*————debug————*/
//                System.out.print(constants.get(e.get(j)) + ", ");
//                /*————debug————*/

                oneTerm.add(constants.get(e.get(j)));
            }

//            /*————debug————*/
//            System.out.println();
//            /*————debug————*/

            result.add(oneTerm);
        }

        return result;
    }

    /*——————下面部分都是为了排列——————*/
    static int cnt = 0;
    static Stack<Integer> s = new Stack<Integer>();
    static boolean[] used = new boolean[10000];

    static ArrayList<Stack<Integer>> res = new ArrayList<Stack<Integer>>();//以栈列表的形式存储结果

    public static void kase2(int minv, int maxv, int curnum, int maxnum) {


        if (curnum == maxnum) {
            cnt++;
//            System.out.println(s + "p");


            Stack<Integer> ss = (Stack<Integer>)s.clone();

//            /*————debug————*/
//            System.out.println("cnt = " + cnt);
//            /*————debug————*/

            res.add(ss);

//            return;
        }

        for (int i = minv; i <= maxv; i++) {
            if (!used[i]) {

//                /*————debug————*/
//                System.out.print(i + ",");
//                /*————debug————*/

                s.push(i);
                used[i] = true;

//                /*————debug————*/
//                System.out.println("\n进入递归，curnum = " + curnum + " and i = " + i);
//                /*————debug————*/

                kase2(minv, maxv, curnum + 1, maxnum);

//                /*————debug————*/
//                System.out.println("\n退出递归，curnum = " + curnum + " and i = " + i);
//                /*————debug————*/

                s.pop();
                used[i] = false;
            }
        }
    }

    /*刷新静态变量。有必要吗？*/
    public static void freshGlobalVar() {
        cnt = 0;
        s = new Stack<Integer>();
        used = new boolean[10000];
        res = new ArrayList<Stack<Integer>>();
    }

    /*——————上面部分都是为了排列——————*/
}
