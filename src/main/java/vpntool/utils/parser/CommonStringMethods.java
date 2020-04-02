package vpntool.utils.parser;

import vpntool.models.Arc;
import vpntool.models.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommonStringMethods {
    /*感觉相比String，更推荐使用StringBuffer*/
    public static String deleteAllSpacesInString(String string) {
        char[] charArray = string.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();//之前是StringBuffer，但IDEA推荐使用StringBuilder
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] != ' ') {
                stringBuilder.append(charArray[i]);
            }
        }
        return stringBuilder.toString();
    }

    public static StringBuilder appendParameters(StringBuilder stringBuilder, String string) {//暂时不加括号
        if (!stringBuilder.toString().equals("")) {
            stringBuilder.append(",");
        }
        stringBuilder.append(string);
        return stringBuilder;
    }

    public static ArrayList<String> stringToArrayList(String string) {
        ArrayList<String> list = new ArrayList<String>();

        String s = CommonStringMethods.deleteAllSpacesInString(string);
        if  (!s.equals("")) {//输入为""则直接返回空列表
            char[] arr = s.toCharArray();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i] != ',') {
                    stringBuilder.append(arr[i]);
                } else {
                    list.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
            }
            /*针对最后一个字符串的特殊处理*/
            stringBuilder.append(arr[arr.length - 1]);
            list.add(stringBuilder.toString());
        }

        return list;
    }

    public static String arrayListToStringOfW(Arc arc) {
        ArrayList<String[]> tuplesInArcLabel = arc.getTuplesInArcLabel();//二元组列表
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tuplesInArcLabel.size() - 1; i++) {
            String string = "(" + tuplesInArcLabel.get(i)[0] + "){" + tuplesInArcLabel.get(i)[1] + "},";
            stringBuilder.append(string);
        }
        /*针对最后一个元组的特殊处理*/

        /*针对标签为空的情况进行特殊处理*/
        if (tuplesInArcLabel.size() == 0) {
            return "φ";
        }

        String string = "(" + tuplesInArcLabel.get(tuplesInArcLabel.size() - 1)[0] + "){" + tuplesInArcLabel.get(tuplesInArcLabel.size() - 1)[1] + "}";
        stringBuilder.append(string);
        return stringBuilder.toString();
    }

    public static String[] stringToStringArray(String string) {//解析权值元组中的变量
        String[] tempResult = new String[10];
        int resIndex = 0;

        char[] charArray = string.toCharArray();
        int varStart = 0;
        int varEnd = 0;//其实varEnd可以省略？
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == ',') {
                varEnd = i;
                tempResult[resIndex++] = string.substring(varStart, varEnd);
                varStart = i + 1;
            }
        }
        tempResult[resIndex] = string.substring(varStart, charArray.length);

        return Arrays.copyOf(tempResult, resIndex + 1);
    }

    //(R1,D1){1},(R2,D2){1}
    public static String HashMapToStringOfToken(HashMap<Token, Integer> tokenHashMap) {
        ArrayList<String> kindsOfToken = new ArrayList<>();
        StringBuilder resultBuilder = new StringBuilder();

        for (Token token : tokenHashMap.keySet()) {
            int tokenNum = tokenHashMap.get(token);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            ArrayList<String> constants = token.getConstants();
            int conNum = constants.size();
            for (int i = 0; i < conNum - 1; i++) {
                stringBuilder.append(constants.get(i) + ",");
            }
            stringBuilder.append(constants.get(conNum - 1));//针对最后一个常量的特殊处理
            stringBuilder.append("){" + tokenNum + "}");

            kindsOfToken.add(stringBuilder.toString());
        }

        //针对最后一种token的特殊处理
        for (int i = 0; i < kindsOfToken.size() - 1; i++) {
            resultBuilder.append(kindsOfToken.get(i) + ",");
        }
        resultBuilder.append(kindsOfToken.get(kindsOfToken.size() - 1));

        return resultBuilder.toString();
    }

    public static ArrayList<String> splitGuardFunc(String guardFunc) {
        ArrayList<String> boolExprs = new ArrayList<>(10);

        /*针对守卫函数为空的情况*/
        if (guardFunc.equals("")) {
            return boolExprs;
        }

        char[] arr = guardFunc.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            if ((arr[i] != '|')&&(arr[i] != '&')) {
                stringBuilder.append(arr[i]);
            } else {
                boolExprs.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }
        stringBuilder.append(arr[arr.length - 1]);
        boolExprs.add(stringBuilder.toString());

        return boolExprs;

    }

    /*根据'&'分出对每个前集库所的要求*/
    /*这样分解出来每个条目都是头尾带括号的*/
    public static ArrayList<String> splitGuardFuncToEachPlace(String guardFunc) {
        ArrayList<String> boolExprsForEachPlace = new ArrayList<>(10);

        /*针对守卫函数为空的情况*/
        if (guardFunc.equals("")) {
            return boolExprsForEachPlace;
        }

        char[] arr = guardFunc.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] != '&') {
                stringBuilder.append(arr[i]);
            } else {
                boolExprsForEachPlace.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }
        stringBuilder.append(arr[arr.length - 1]);
        boolExprsForEachPlace.add(stringBuilder.toString());

        return boolExprsForEachPlace;

    }

    /*根据'|'分出每个前集库所中所有可能的赋值*/
    /*这样分解出来每个条目可能头尾带括号，也可能不带*/
    /*怎么就这个方法前面没有@符号？？？*/
    public static ArrayList<String> splitToAllAllowedAssignments(String boolExprForOnePlace) {
        ArrayList<String> allAllowedAssignments = new ArrayList<>(10);

        /*去掉头尾的括号*/
        boolExprForOnePlace = boolExprForOnePlace.substring(1, boolExprForOnePlace.length() - 1);

        char[] arr = boolExprForOnePlace.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == '(' || arr[i] == ')') {
                continue;
            }

            if (arr[i] != '|') {
                stringBuilder.append(arr[i]);
            } else {
                allAllowedAssignments.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }

        if (arr[arr.length - 1] != ')') {
            stringBuilder.append(arr[arr.length - 1]);
        }

        allAllowedAssignments.add(stringBuilder.toString());

        return allAllowedAssignments;

    }

    /*根据','分出每个常变量的映射*/
    /*这样分解出来每个条目一定不带括号*/
    public static ArrayList<String> splitToAllMappings(String boolExprForOnePlace) {
        ArrayList<String> allMappings = new ArrayList<>(10);

        /*如果头尾有括号则去掉*/
        if (boolExprForOnePlace.charAt(0) == '(') {
            boolExprForOnePlace = boolExprForOnePlace.substring(1, boolExprForOnePlace.length() - 1);
        }

        char[] arr = boolExprForOnePlace.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] != ',') {
                stringBuilder.append(arr[i]);
            } else {
                allMappings.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }
        stringBuilder.append(arr[arr.length - 1]);
        allMappings.add(stringBuilder.toString());

        return allMappings;

    }


}
