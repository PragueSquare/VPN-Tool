package vpntool.gui;

import vpntool.utils.parser.CommonStringMethods;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;


public class t2 {

    static ArrayList<Integer> intarray = new ArrayList<>();


    public static void main(String[] args) {
//        ArrayList<String> arr = StringToArrayList.parse(" R 1, R2 , D ");
////        ArrayList<String> arr = StringToArrayList.parse("");
//        for (int i = 0; i < arr.size(); i++) {
//            System.out.println(arr.get(i));
//        }
        ArrayList<String> arrayList = new ArrayList<String>();
//        arrayList.add("a");
//        arrayList.add("b");
//        arrayList.add("c");
        for (int i = 0; i < 3; i++) {
//            arrayList.set(i, "ha");//如果不先add，而是直接set的话会报越界
            arrayList.add(i, null);
        }
        System.out.println(arrayList.size());

        String a = "abc";
        if (a.equals("abc")) {
            System.out.println(10);
        }

        System.out.println("ha" + (3 + 4));//ha7

        ArrayList<String> as = new ArrayList<String>();
        as.add(0, "1");
        as.add(1, "2");
        ArrayList<String> as2 = new ArrayList<String>();
        as2.add(0, "3");
        as2.add(1, "4");
        as.addAll(as2);

        for (int i = 0; i < as.size(); i++) {
            System.out.println(as.get(i));
        }

        /*if语句具有短路性*/
        int aa = 5;
        if (aa < 6) {
            System.out.println("小于6");
        } else if (aa < 10) {
            System.out.println("小于10");
        }

        LinkedList<String> linkedList = new LinkedList<>();
        changeLL(linkedList);

        System.out.println(linkedList.size());
        System.out.println(linkedList.get(0));

        /*ArrayList可以按内容删除，且不会占据虚位置*/
        ArrayList<String> als = new ArrayList<String>();
        if (als.isEmpty()) {
            System.out.println("als is empty");
        }
        als.add("a");
        als.add("b");
        als.add("c");
        als.add("d");
//        als.remove("a");
        for (int i = 0; i < 4; i++) {
            als.remove(0);
        }

        System.out.println(als.size());
//        System.out.println(als.get(0));


        /*可以重新设置String*/
        String str = "ha";
        str = setString("hahaha");
        System.out.println(str);

        String partStr = str.substring(0,2);//前闭后开
        System.out.println(partStr);
        System.out.println(str);//str其实并未受影响

        System.out.println("----------------");
        String strarraystring = "R,D,C";
        String[] strarray = CommonStringMethods.stringToStringArray(strarraystring);
        for (int i = 0; i < strarray.length; i++) {
            System.out.println(strarray[i]);
        }
        System.out.println();


//        ArrayList<Integer> intarray = new ArrayList<>();
        System.out.println(intarray.size());

//        String str = "   h a  ";
//        System.out.println(CommonStringMethods.deleteAllSpacesInString(str));
        Stack<Integer> s = new Stack<Integer>();
        s.add(3);
        System.out.println(s.size());
        System.out.println(s.pop());
//        ArrayList<Stack<Integer>> stackarray = new ArrayList<Stack<Integer>>();
//        stackarray.add(s);

        Stack<Integer> intstack = new Stack<>();
        intstack.add(1);
        intstack.add(2);
        intstack.add(3);
        for (int j = 0; j < intstack.size() - 1; j++) {
            System.out.print(intstack.get(j) + ", ");
        }
        System.out.println(intstack.get(intstack.size() - 1));

        ArrayList<String> s1 = new ArrayList<>();
        s1.add("a");
        s1.add("b");
        s1.add("c");

        ArrayList<String> s2 = new ArrayList<>();
        s2.add("a");
        s2.add("b");
        s2.add("c");

        /*说明针对列表来说，==比的是引用，equals比的是内容*/
        System.out.println(s1 == s2);//false
        System.out.println(s1.equals(s2));//true

        int[] intarr = new int[3];
        for (int i = 0; i < intarr.length; i++) {
            System.out.print(intarr[i] + " ");
        }

        /*用clone再add的方法是可以的*/
        ArrayList<String> als0112 = new ArrayList<>();
        als0112.add("a");
        als0112.add("b");
        als0112.add("c");

        ArrayList<String> als0112c = (ArrayList<String>) als0112.clone();//为啥一定要加强转呢？
        als0112c.add("d");
        System.out.println(als0112c.get((3)));
        System.out.println("被克隆对象受影响吗？" + als0112.size());//不受影响


        ArrayList<HashSet<String>> ah = new ArrayList<>(4);//设置容量仍会报越界错误
        ah.add(new HashSet<>());
        System.out.println("ah.get(0).size() = " + ah.get(0).size());

        String s59 = "123456";
        System.out.println(s59.substring(0, 2));
        System.out.println(s59);


    }


    public static void changeLL(LinkedList<String> linkedList) {
        linkedList.add("a");
        linkedList.add("b");
    }

    public static String setString(String s) {
        return s;
    }

    public static void changeArrayList(ArrayList<Integer> intarray) {
        for (int i = 0; i < 5; i++) {
            intarray.add(i);
        }
    }
}


