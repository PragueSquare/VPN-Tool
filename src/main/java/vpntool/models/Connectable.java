package vpntool.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connectable implements Serializable {
    /*这样算是通过弧把整个网连起来了，但是前集和后集那种考虑呢？*/
    /*这里是用ArrayList好还是用LinkedList好呢？？？*/
    private ArrayList<Arc> inputArcs;
    private ArrayList<Arc> outputArcs;

    private int id;//这里的id是对应于PetriNet中的顺序表吗？
    private String name;

    public Connectable(int id, String name) {
        this.id = id;
        this.name = name;
        inputArcs = new ArrayList<Arc>();
        outputArcs = new ArrayList<Arc>();
    }

    /*Getter and Setter*/

    public ArrayList<Arc> getInputArcs() {
        return inputArcs;
    }

    public void setInputArcs(ArrayList<Arc> inputArcs) {
        this.inputArcs = inputArcs;
    }

    public ArrayList<Arc> getOutputArcs() {
        return outputArcs;
    }

    public void setOutputArcs(ArrayList<Arc> outputArcs) {
        this.outputArcs = outputArcs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
