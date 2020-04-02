package vpntool.gui;

import java.util.ArrayList;

public class tRecursion {


    public static ArrayList<ArrayList<String>> res = new ArrayList<>();

    public static ArrayList<ArrayList<String>> group = new ArrayList<>();

//    public static void combination(ArrayList<String> oneTerm, int depth, int backTrace) {
    public static void combination(ArrayList<String> oneTerm, int depth) {

        if (depth == group.size()) {
            System.out.print("完成一次组合：");
            for (int i = 0; i < oneTerm.size(); i++) {
                System.out.print(oneTerm.get(i) + ",");
            }
            System.out.println();
//            res.add(oneTerm);



            ArrayList<String> tOneTerm = new ArrayList<>();
            tOneTerm.addAll(oneTerm);
            oneTerm.clear();

//            for (int i = oneTerm.size() - 1; i > oneTerm.size() - backTrace; i--) {
//                oneTerm.remove(i);
//            }

            res.add(tOneTerm);

        } else {
            ArrayList<String> mapOfOnePlace = group.get(depth);
            int start = 0;
            int end = mapOfOnePlace.size();
            for (int i = start; i < end; i++) {//循环的是一个库所中的所有token

//                if (depth == 0) {
//                    System.out.println("将oneTerm清零");
//                    oneTerm.clear();
//                }

//                if (oneTerm.size() == group.size()) {
//                    System.out.println("将oneTerm清零");
//                    oneTerm.clear();
//                }



//                oneTerm.add(depth, mapOfOnePlace.get(i));
                oneTerm.add(mapOfOnePlace.get(i));
                System.out.println("添加一次token：" + mapOfOnePlace.get(i) + "。此时循环中的i为：" + i);

//                if (depth == group.size() - 1) {
//                    oneTerm.clear();
//                }

//                ArrayList<String> tOneTerm = new ArrayList<>();
//                tOneTerm.addAll(oneTerm);

//                backTrace = mapOfOnePlace.size() - i;

                System.out.println("进入第" + (depth + 1) + "层递归");
                combination(oneTerm, depth + 1);//递归的是所有库所
                System.out.println("退出第" + (depth + 1) + "层递归");

//                if (depth == group.size() - 1) {
//                    oneTerm.clear();
//                }

                System.out.println("结束一次循环");
            }
            System.out.println("结束一个循环");
        }


    }

//    public static void combination(ArrayList<ArrayList<String>> group, ArrayList<String> oneTerm, int depth) {
//
//        if (depth == group.size()) {
//            res.add(oneTerm);
//        } else {
//            ArrayList<String> mapOfOnePlace = group.get(depth);
//            int start = 0;
//            int end = mapOfOnePlace.size();
//            for (int i = start; i < end; i++) {
//
//                oneTerm.add(mapOfOnePlace.get(i));
//
//                combination(group, oneTerm, depth + 1);
//            }
//        }
//
//
//    }

    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            a.add("a" + i);
        }

        ArrayList<String> b = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            b.add("b" + i);
        }

        ArrayList<String> c = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            c.add("c" + i);
        }

//        ArrayList<ArrayList<String>> group = new ArrayList<>();
        group.add(a);
        group.add(b);
//        group.add(c);
        ArrayList<String> oneTerm = new ArrayList<>();

        combination(oneTerm, 0);
//        combination(group, oneTerm, 0);

        System.out.println(res.size());
        for (int i = 0; i < res.size(); i++) {
            ArrayList<String> oneBinding2 = res.get(i);
            for (int j = 0; j < oneBinding2.size(); j++) {
                System.out.print(oneBinding2.get(j) + ",");
            }
            System.out.println();
        }

    }

}
