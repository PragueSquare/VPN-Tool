package vpntool.models.analysisMethods;

import vpntool.models.Marking;
import vpntool.models.Transition;
import vpntool.models.analysisMethods.kripkeStructure.APlTerm;
import vpntool.models.analysisMethods.kripkeStructure.APtTerm;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class KSNode extends DefaultMutableTreeNode implements Serializable {
    private KSNode parentNode;//为了与内置的parent区分改为parentNode
//    private LinkedList<CTNode> brothers;//等会，存所有的兄弟好像太冗余了，想想肯定会有重复，所以只存右兄弟（下一个兄弟）
//    private CTNode nextSibling;
// TODO: 2020/3/15 children实为postNodes，记录图中该结点指向的所有结点
    private ArrayList<KSNode> children;//等会，和内置的protected Vector children没冲突？应该是因为数据类型不同吧？

    /*记录指向该结点的所有结点*/
    private ArrayList<KSNode> preNodes;


    // TODO: 2019/12/2 必须存在节点上的信息，无法通过简单元素获取？
    private Marking marking;
    private HashMap<String, ArrayList<String>> constraintFunction;
    private ArrayList<String> placeSet;//只存库所的名字即可？


    private HashMap<Transition, ArrayList<HashMap<String, String>>> arcs;//变迁和绑定是一对多的映射。这样定义是不是有点复杂？维护这个有必要吗？

    private String lable;//新，旧，端点。但无界怎么标？

    private int id;//在树中的id。也是在可达集中的id？
    private String name;//"C" + id

    private int transitionId;//由此Id对应的变迁得到此配置
    private HashMap<String, String> bindingMap;//在此绑定条件下得到此配置

    /*相比CTNode多的属性*/
    private ArrayList<String> APp;//记录增加的库所
    private ArrayList<APlTerm> APl;//记录γ的增减
//    private HashMap<String, ArrayList<HashMap<String, String>>> Apt;
    private ArrayList<APtTerm> APt;//变迁和绑定是一对多的映射。哪种存储方式比较好呢？


    public KSNode(int id, String name, KSNode parentNode, Marking marking, HashMap<String, ArrayList<String>> constraintFunction, ArrayList<String> placeSet, String lable) {
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

        APp = new ArrayList<>();
        APl = new ArrayList<>();
        APt = new ArrayList<>();

    }

    /*是多写几个接参不同的构造函数还是就用一个构造函数然后通过set方法赋值？*/
    public KSNode(int id, String name, KSNode parentNode, Marking marking, HashMap<String, ArrayList<String>> constraintFunction, ArrayList<String> placeSet, String lable, ArrayList<String> APp, ArrayList<APlTerm> APl) {
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

        this.APp = APp;
        this.APl = APl;

        /*还要初始化？*/
        this.children = new ArrayList<>();

        this.APt = new ArrayList<>();

        this.preNodes = new ArrayList<>();

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


    public KSNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(KSNode parentNode) {
        this.parentNode = parentNode;
    }

    public ArrayList<KSNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<KSNode> children) {
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

    public ArrayList<String> getAPp() {
        return APp;
    }

    public void setAPp(ArrayList<String> APp) {
        this.APp = APp;
    }

    public ArrayList<APlTerm> getAPl() {
        return APl;
    }

    public void setAPl(ArrayList<APlTerm> APl) {
        this.APl = APl;
    }

    public ArrayList<APtTerm> getAPt() {
        return APt;
    }

    public void setAPt(ArrayList<APtTerm> APt) {
        this.APt = APt;
    }

    public ArrayList<KSNode> getPreNodes() {
        return preNodes;
    }

    public void setPreNodes(ArrayList<KSNode> preNodes) {
        this.preNodes = preNodes;
    }
}
