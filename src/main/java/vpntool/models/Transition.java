package vpntool.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Transition extends Connectable implements Serializable {
    private String guardFunction;//用简单的String存守卫函数合适吗？

//    private String linkFunction;//用简单的String存连接函数合适吗？
    private ArrayList<String[]> linkFunction;//三元组列表。其实可以写个类

    public Transition(int id, String name) {
        super(id, name);

        linkFunction = new ArrayList<>();//这里初始化是必须的吗？
        guardFunction = "";//String类型为了避免为空要这样初始化吗？
    }

    /*Getter and Setter*/



    public String getGuardFunction() {
        return guardFunction;
    }

    public void setGuardFunction(String guardFunction) {
        this.guardFunction = guardFunction;
    }

    public ArrayList<String[]> getLinkFunction() {
        return linkFunction;
    }

    public void setLinkFunction(ArrayList<String[]> linkFunction) {
        this.linkFunction = linkFunction;
    }
}
