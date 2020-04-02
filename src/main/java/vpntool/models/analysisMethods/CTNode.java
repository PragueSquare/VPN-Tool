package vpntool.models.analysisMethods;

import vpntool.models.Marking;
import vpntool.models.RealPlace;
import vpntool.models.Transition;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CTNode extends DefaultMutableTreeNode implements Serializable {
    private CTNode parentNode;//为了与内置的parent区分改为parentNode
//    private LinkedList<CTNode> brothers;//等会，存所有的兄弟好像太冗余了，想想肯定会有重复，所以只存右兄弟（下一个兄弟）
//    private CTNode nextSibling;
    private ArrayList<CTNode> children;


    // TODO: 2019/12/2 必须存在节点上的信息，无法通过简单元素获取？
    private Marking marking;
//    private String constraintFunction;//用简单的String存约束函数合适吗？
//    HashMap<String, String> bindingMap。利用父结点的γ函数加上此时的绑定得到此结点的γ函数
    private HashMap<String, ArrayList<String>> constraintFunction;
//    private ArrayList<RealPlace> realPlaces;//这里如果维护库所的列表，那库所中的token也要维护。token在库所中的编号怎么办？并且牵扯到增删？
    private ArrayList<String> placeSet;//只存库所的名字即可？


//    private ArrayList<Transition> enableTransitions;
    /*注意这里面存的绑定应该是使此标识使能的绑定？*/
    private HashMap<Transition, ArrayList<HashMap<String, String>>> arcs;//变迁和绑定是一对多的映射。这样定义是不是有点复杂？维护这个有必要吗？

    private String lable;//新，旧，端点。但无界怎么标？

    private int id;//在树中的id。也是在可达集中的id？
    private String name;//"C" + id

    private int transitionId;//由此Id对应的变迁得到此配置
    private HashMap<String, String> bindingMap;//在此绑定条件下得到此配置

//    public CTNode(int id, String name, CTNode parent, Marking marking, HashMap<String, ArrayList<String>> constraintFunction, ArrayList<RealPlace> realPlaces, String lable) {
//        this.id = id;
//        this.name = name;
//
//        this.parent = parent;
//        this.marking = marking;
//        this.constraintFunction = constraintFunction;
//        this.realPlaces = realPlaces;
//        this.lable = lable;
//    }

    public CTNode(int id, String name, CTNode parentNode, Marking marking, HashMap<String, ArrayList<String>> constraintFunction, ArrayList<String> placeSet, String lable) {
        this.id = id;
        this.name = name;

//        this.parent = parent;///** this node's parent, or null if this node has no parent */protected MutableTreeNode   parent;

        /*——bugFix——*/
        this.parentNode = parentNode;
        /*——bugFix——*/

        this.marking = marking;
        this.constraintFunction = constraintFunction;
        this.placeSet = placeSet;
        this.lable = lable;

        /*还要初始化？*/
        this.children = new ArrayList<>();

    }

    /*Getter and Setter*/

//    @Override
//    public CTNode getParent() {
//        return parent;
//    }// TODO: 这样对内置的parent有影响吗？add的时候体现出来了
//
//    public void setParent(CTNode parent) {
//        this.parent = parent;
//    }


    public CTNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(CTNode parentNode) {
        this.parentNode = parentNode;
    }

    public ArrayList<CTNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CTNode> children) {
        this.children = children;
    }

    public Marking getMarking() {
        return marking;
    }

    public void setMarking(Marking marking) {
        this.marking = marking;
    }

    public ArrayList<String> getPlaceSet() {
        return placeSet;
    }

    public void setPlaceSet(ArrayList<String> placeSet) {
        this.placeSet = placeSet;
    }

    public HashMap<String, ArrayList<String>> getConstraintFunction() {
        return constraintFunction;
    }

    public void setConstraintFunction(HashMap<String, ArrayList<String>> constraintFunction) {
        this.constraintFunction = constraintFunction;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public int getTransitionId() {
        return transitionId;
    }

    public void setTransitionId(int transitionId) {
        this.transitionId = transitionId;
    }

    public HashMap<String, String> getBindingMap() {
        return bindingMap;
    }

    public void setBindingMap(HashMap<String, String> bindingMap) {
        this.bindingMap = bindingMap;
    }

    /*重写并不方便，尤其是比标识的时候*/
//    @Override
//    public boolean equals(Object obj) {
//        CTNode nodeToCompareWith = (CTNode) obj;
//
//        /*比库所集*/
//        /*注意库所集中的常量顺序是无序的*/
//        ArrayList<String> placeSet = this.placeSet;
//        ArrayList<String> placeSetToCompareWith = nodeToCompareWith.placeSet;
//
//        for (int i = 0; i < placeSet.size(); i++) {
//            if (!placeSetToCompareWith.contains(placeSet.get(i))) {
//                return false;//有一个不相等则直接返回
//            }
//        }
//        for (int i = 0; i < placeSetToCompareWith.size(); i++) {
//            if (!placeSet.contains(placeSetToCompareWith.get(i))) {
//                return false;//有一个不相等则直接返回
//            }
//        }
//
//
//        /*比γ函数*/
//        HashMap<String, ArrayList<String>> constraintFunction = this.constraintFunction;
//        HashMap<String, ArrayList<String>> constraintFunctionToCompareWith = nodeToCompareWith.constraintFunction;
//        for (int i = 0; i < constraintFunction.size(); i++) {
//            if (!constraintFunctionToCompareWith.containsKey(constraintFunction.get(i))) {
//                return false;//有一个不包含则直接返回
//            }
//
//        }
//
//
//
//        /*比标识*/
//
//    }
}
