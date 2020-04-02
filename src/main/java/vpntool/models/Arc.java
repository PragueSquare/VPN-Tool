package vpntool.models;

import java.io.Serializable;
import java.util.ArrayList;

/*最初Arc设置为抽象类，有两个子类，RealArc和VirtualArc*/
public class Arc implements Serializable {
    private int id;//这里的id对应于PetriNet中的顺序表
    private String name;//暂定弧的命名规则和变迁类似

    private Connectable source;
    private Connectable target;

    private boolean isReal;//标记虚实

//    private String arcLabelFunction;//用简单的String存权值合适吗？换成下面这个
    private ArrayList<String[]> tuplesInArcLabel;//二元组列表

    public Arc(int id, String name, Connectable source, Connectable target, boolean isReal, ArrayList<String[]> tuplesInArcLabel) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.target = target;
        this.isReal = isReal;
        this.tuplesInArcLabel = tuplesInArcLabel;
    }

    /*Getter and Setter*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public Connectable getSource() {
        return source;
    }

    public void setSource(Connectable source) {
        this.source = source;
    }

    public Connectable getTarget() {
        return target;
    }

    public void setTarget(Connectable target) {
        this.target = target;
    }

    public ArrayList<String[]> getTuplesInArcLabel() {
        return tuplesInArcLabel;
    }

    public void setTuplesInArcLabel(ArrayList<String[]> tuplesInArcLabel) {
        this.tuplesInArcLabel = tuplesInArcLabel;
    }
}
