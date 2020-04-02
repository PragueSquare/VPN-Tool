package vpntool.gui;

import java.util.ArrayList;
import java.util.HashMap;

class Sign {
    /*每组元素更换频率，即迭代多少次换下一个元素*/
    public int changeFreq;

    /*每组元素的元素索引位置*/
    public int index;
}

public class tVariableGroup {

    public static ArrayList<ArrayList<String>> combinationOfString(ArrayList<ArrayList<String>> group) {
        /*总迭代次数，即组合总种数*/
        int iterSize = 1;
        for (int i = 0; i < group.size(); i++) {
            iterSize *= group.get(i).size();
        }

        /*当前元素与左边已定元素的组合种数*/
        int median = 1;
        HashMap<Integer, Sign> indexMap = new HashMap<>();
        for (int i = 0; i < group.size(); i++) {
            median *= group.get(i).size();

            /*group中每个位置都要创建一个标记*/
            Sign sign = new Sign();
            sign.index = 0;
            sign.changeFreq = iterSize/median;

            indexMap.put(i, sign);
        }

        ArrayList<ArrayList<String>> res = new ArrayList<>();

        int i = 1;//组合编号

        while (i <= iterSize) {
            ArrayList<String> oneTerm = new ArrayList<>();

            for (int j = 0; j < group.size(); j++) {
                int changeFreq = indexMap.get(j).changeFreq;//组元素更换频率
                int index = indexMap.get(j).index;//组元素索引位置

                oneTerm.add(group.get(j).get(index));

                if (i % changeFreq == 0) {
                    index++;

                    /*该组中的元素组合完了，按照元素索引顺序重新取出再组合*/
                    if (index >= group.get(j).size()) {
                        index = 0;
                    }

                    indexMap.get(j).index = index;
                }
            }

            res.add(oneTerm);
            i++;
        }

        return  res;
    }

    public static ArrayList<ArrayList<HashMap<String, String>>> combination(ArrayList<ArrayList<HashMap<String, String>>> group) {

        System.out.println("进行组合时，组数为：" + group.size());
        for (int j = 0; j < group.size(); j++) {
            System.out.println("以下为进入组合函数时的绑定");
            ArrayList<HashMap<String, String>> oneBinding = group.get(j);
            for (int k = 0; k < oneBinding.size(); k++) {
                HashMap<String, String> oneTerm = oneBinding.get(k);
                for (String var : oneTerm.keySet()) {
                    System.out.println(var + "->" + oneTerm.get(var));
                }
            }
        }

        /*总迭代次数，即组合总种数*/
        int iterSize = 1;
        for (int i = 0; i < group.size(); i++) {
            iterSize *= group.get(i).size();
        }

        /*当前元素与左边已定元素的组合种数*/
        int median = 1;
        HashMap<Integer, Sign> indexMap = new HashMap<>();
        for (int i = 0; i < group.size(); i++) {
            median *= group.get(i).size();

//            System.out.println("group.get(i).size() = " + group.get(i).size() + " while i = " + i);

            Sign sign = new Sign();
            sign.index = 0;
            sign.changeFreq = iterSize/median;

            indexMap.put(i, sign);
        }

        ArrayList<ArrayList<HashMap<String, String>>> res = new ArrayList<>();

        int i = 1;//组合编号

        while (i <= iterSize) {
            ArrayList<HashMap<String, String>> oneTerm = new ArrayList<>();

            for (int j = 0; j < group.size(); j++) {
                int changeFreq = indexMap.get(j).changeFreq;//组元素更换频率
                int index = indexMap.get(j).index;//组元素索引位置

                oneTerm.add(group.get(j).get(index));

                if (i % changeFreq == 0) {
                    index++;

                    /*该组中的元素组合完了，按照元素索引顺序重新取出再组合*/
                    if (index >= group.get(j).size()) {
                        index = 0;
                    }

                    indexMap.get(j).index = index;
                }
            }

            res.add(oneTerm);
            i++;
        }

        return  res;
    }

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

        ArrayList<ArrayList<String>> group = new ArrayList<>();
        group.add(a);
        group.add(b);
        group.add(c);

        ArrayList<ArrayList<String>> res = combinationOfString(group);
        System.out.println(res.size());
        for (int i = 0; i < res.size(); i++) {
            ArrayList<String> oneBinding = res.get(i);
            for (int j = 0; j < oneBinding.size(); j++) {
                System.out.print(oneBinding.get(j) + ",");
            }
            System.out.println();
        }
    }
}
