package vpntool.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Token implements Serializable {
//    private int currentMarking;//这样就要求给标识编号

    private int placeIndex;//所在库所的编号。等会，如果token进入了虚库所实化的库所，则这里应该是virtualPlaceIndex。或者暂时把虚库所加到实库所中？

    private int indexInAPlace;//在所在库所中的编号
    private String name;//比如“token1”，或“1OfPlaceName”，或库所内容？暂定库所内容

//    private boolean isOrdinary;//标识是否为普通库所

    private ArrayList<String> constants;//普通token这一项为空。应该可以从库所内容解析出来

    public Token(int placeIndex, int indexInAPlace, String name, ArrayList<String> constants) {
        this.placeIndex = placeIndex;
        this.indexInAPlace = indexInAPlace;
        this.name = name;
        this.constants = constants;
    }

    /*为弧标签元组转化为token服务*/
    public Token(ArrayList<String> constants) {
        this.constants = constants;
    }

    // TODO: 如果能保证token名就是库所内容，那么是否相等直接比token名即可
    /*可以将弧标签中的元组通过映射转成token然后比较？*/
    @Override
    public boolean equals(Object obj) {

//        /*————debug————*/
//        System.out.println("调用了重写的equals方法");
//        /*————debug————*/

        Token tokenToCompareWith = (Token) obj;

//        //注意这样比对token中常量顺序是有要求的。要不改一改？
//        if (this.constants.equals(tokenToCompareWith.constants)) {
//            return true;
//        }

        //对常量顺序无要求
        ArrayList<String> cons1 = this.constants;
        ArrayList<String> cons2 = tokenToCompareWith.constants;

        for (int i = 0; i < cons1.size(); i++) {
            if (!cons2.contains(cons1.get(i))) {
                return false;//有一个不相等则直接返回
            }
        }
        for (int i = 0; i < cons2.size(); i++) {
            if (!cons1.contains(cons2.get(i))) {
                return false;//有一个不相等则直接返回
            }
        }

        /*上面只能保证两个集合中元素相等，但无法体现顺序！！！token中的常量也是有序的*/
        for (int i = 0; i < cons1.size(); i++) {
            if (!cons1.get(i).equals(cons2.get(i))) {
                return false;
            }
        }

//        return false;
        return true;
    }

    /*既然重写了equals也要重写hashCode*/
    /*这种重写方法有点违反原则：不同对象返回的hash值应该尽量不同*/
    @Override
    public int hashCode() {
        return this.constants.size();
    }

    /*Getter and Setter*/

    public ArrayList<String> getConstants() {
        return constants;
    }

    public void setConstants(ArrayList<String> constants) {
        this.constants = constants;
    }
}
