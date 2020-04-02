package vpntool.gui;

import vpntool.models.analysisMethods.kripkeStructure.APlTerm;
import vpntool.models.analysisMethods.kripkeStructure.APtTerm;
import vpntool.utils.parser.CommonStringMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/*m个数中取出n个数的排列*/
public class t311 {
    static int cnt = 0;
    static Stack<Integer> s = new Stack<Integer>();
    static boolean[] used = new boolean[10000];

    static ArrayList<Stack<Integer>> res = new ArrayList<Stack<Integer>>();//以栈列表的形式存储结果

    public static void kase2(int minv, int maxv, int curnum, int maxnum) {


        if (curnum == maxnum) {
            cnt++;
            System.out.println(s + "p");


            Stack<Integer> ss = (Stack<Integer>)s.clone();
            res.add(ss);

//            return;
        }

        for (int i = minv; i <= maxv; i++) {
            if (!used[i]) {
                s.push(i);
                used[i] = true;
                kase2(minv, maxv, curnum + 1, maxnum);
                s.pop();
                used[i] = false;
            }
        }
    }

    public static void main(String[] args) {


//        kase2(0, 3, 0, 3);
//        System.out.println(cnt + "-------------------------");
//
//        System.out.println(res.size());
//        for (int i = 0; i < res.size(); i++) {
//            Stack<Integer> e = res.get(i);
//            System.out.println(e);
//            for (int j = 0; j < e.size() - 1; j++) {
//                System.out.print(e.get(j) + ", ");
//            }
//            System.out.println(e.get(e.size() - 1));
//        }
        ArrayList<String> testBounds = new ArrayList<>();
        testBounds.add("a");
        testBounds.add("b");
        testBounds.add("γ");
        System.out.println("ε");
        System.out.println(testBounds.get(2));
        System.out.println(testBounds.contains("γ"));

        HashMap<String, String> romanLang = new HashMap<>();
//        romanLang.put("ε", "ordinaryToken");
        romanLang.put("ε", "ε");
        for (String var : romanLang.keySet()) {
            System.out.println(var + "->" + romanLang.get(var));
        }

        ArrayList<String> arr1 = new ArrayList<>();
        arr1.add("a");
        arr1.add("b");

        ArrayList<String> arr2 = new ArrayList<>();
        arr2.add("b");
        arr2.add("a");

        if (arr1.containsAll(arr2)) {
            System.out.println("arr1.containsAll(arr2)");
        }

        if (arr2.containsAll(arr1)) {
            System.out.println("arr2.containsAll(arr1)");
        }

        APlTerm apl1 = new APlTerm("I", "Iu-a", "+");
        APlTerm apl2 = new APlTerm("I", "Iu-a", "+");
        APlTerm apl3 = new APlTerm("I", "Iu-a", "-");
        APlTerm apl4 = new APlTerm("I", "Iu-a", "-");
        if (apl1.equals(apl2)) {//如果不重写equals方法这里返回不相等
            System.out.println("apl1.equals(apl2)");
        }
        if (apl1.equals(apl3)) {
            System.out.println("apl1.equals(apl3)");
        }

        ArrayList<APlTerm> aplArr1 = new ArrayList<>();

        ArrayList<APlTerm> aplArr2 = new ArrayList<>();

        /*二者都为空时返回真*/
        if (aplArr1.containsAll(aplArr2)) {
            System.out.println("为空时aplArr1.containsAll(aplArr2)");
        }

        aplArr1.add(apl1);
        aplArr1.add(apl3);

        aplArr2.add(apl2);
        aplArr2.add(apl4);

        if (aplArr1.containsAll(aplArr2)) {
            System.out.println("aplArr1.containsAll(aplArr2)");
        }
        if (aplArr2.containsAll(aplArr1)) {
            System.out.println("aplArr2.containsAll(aplArr1)");
        }

        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("R", "R1");
        hashMap1.put("D", "D1");

        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("R", "R1");
        hashMap2.put("D", "D1");

        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("D", "D1");
        hashMap3.put("R", "R1");


        if (hashMap1.equals(hashMap2)) {
            System.out.println("hashMap1.equals(hashMap2)");
        }

        if (hashMap1.equals(hashMap3)) {//HashMap默认的equals方法也是无序比较
            System.out.println("hashMap1.equals(hashMap3)");
        }

        APtTerm aPtTerm1 = new APtTerm("t0", hashMap1);
        APtTerm aPtTerm2 = new APtTerm("t0", hashMap3);
        if (aPtTerm1.equals(aPtTerm2)) {
            System.out.println("aPtTerm1.equals(aPtTerm2)");
        }

//        String guardFunc = "(R=R1)|(R=R2)|(R=R3)|(R=R4)";
//        String guardFunc = "(CName=lockerUsage)&(PortType=gymLockerPT)";
//        String guardFunc = "((N=Ca,N'=Sa)|(N=Cb,N'=Sb))&(RE=REQ)&(TY=SLA)";
        String guardFunc = "((N=Cb,N'=Sa)|(N=Ca,N'=Sb))";
        ArrayList<String> boolExprsForEachPlace = CommonStringMethods.splitGuardFuncToEachPlace(guardFunc);
        for (int i = 0; i < boolExprsForEachPlace.size(); i++) {
            String boolExprForOnePlace = boolExprsForEachPlace.get(i);
            System.out.println(boolExprForOnePlace);
            ArrayList<String> allAllowedAssignments = CommonStringMethods.splitToAllAllowedAssignments(boolExprForOnePlace);
            for (int j = 0; j < allAllowedAssignments.size(); j++) {
                String oneAllowedAssignment = allAllowedAssignments.get(j);
                System.out.println(oneAllowedAssignment);
            }

        }

        String gamaT = "γ=null";
        System.out.println(gamaT.charAt(0));

        ArrayList<String> whyNot = new ArrayList<>(10);
        System.out.println(whyNot.size());//返回还是0

        ArrayList<String> a03181 = new ArrayList<>();
        a03181.add("a");
        a03181.add("b");

        ArrayList<String> a03182 = new ArrayList<>();
        a03181.add("a");
        a03181.add("b");

        ArrayList<String> a03183 = new ArrayList<>();
        a03181.add("b");
        a03181.add("a");

        if (a03181.equals(a03182)) {//返回假
            System.out.println("a03181.equals(a03182)");
        }

        if (a03181.equals(a03183)) {//返回假
            System.out.println("a03181.equals(a03183)");
        }

        /*数组中的布尔值默认为假*/
        boolean[] boolArr = new boolean[3];
        for (int i = 0; i < boolArr.length; i++) {
            System.out.println(boolArr[i]);
        }


    }
}
