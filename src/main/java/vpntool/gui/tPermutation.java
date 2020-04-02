package vpntool.gui;

import vpntool.utils.math.Permutation;

import java.util.ArrayList;

public class tPermutation {

    public static void main(String[] args) {
//        Permutation.kase2(0,23,0,6);
        ArrayList<String> bound = new ArrayList<>(200000000);//能开出来长度1亿的顺序表，十亿不行
        String[] bun = new String[200000000];
        Permutation.kase2(0,2,0,2);
        int res = 1;
        for (int i = 28; i < 34; i++) {
            res *= i;
        }
        System.out.println(res);
    }

}
