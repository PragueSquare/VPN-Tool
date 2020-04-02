package vpntool.models;

import vpntool.models.analysisMethods.CTNode;
import vpntool.models.analysisMethods.KSNode;
import vpntool.views.ArcView;
import vpntool.views.RealPlaceView;
import vpntool.views.TransitionView;
import vpntool.views.VirtualPlaceView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PetriNet implements Serializable {
    private ArrayList<String> constants;
    private ArrayList<String> variables;

    // TODO: 这种设置正确吗？
    private HashMap<String, ArrayList<String>> mapBetweenVAndC;

//    private ArrayList<Place> places;
    private ArrayList<RealPlace> realPlaces;
    private ArrayList<VirtualPlace> virtualPlaces;
    private ArrayList<Transition> transitions;
    private ArrayList<Arc> arcs;//八元组中的弧是包括虚弧的
    private ArrayList<Configuration> configurations;

    private  ArrayList<RealPlaceView> realPlaceViews;
    private  ArrayList<VirtualPlaceView> virtualPlaceViews;
    private  ArrayList<TransitionView> transitionViews;
    private  ArrayList<ArcView> arcViews;

    private String savedFile;//记录被保存的文件位置

    /*注意输入、输出矩阵并未在构造方法中初始化*/
    private int[][] inputMatrix;//实际存的是输入矩阵各位置对应弧的id
    private int[][] outputMatrix;//实际存的是输出矩阵各位置对应弧的id

    private CTNode ctRootNode;//配置树的根结点
    private KSNode ksRootNode;//Kripke结构的根结点

    private int nodeNum;//配置树的结点数

    private boolean[] propositionType;//用于优化KS中命题类型的选择

    public PetriNet() {
        /*注意初始化创建对象*/
        this.constants = new ArrayList<String>(100);//这里必须初始化
        this.variables = new ArrayList<String>(100);

        this.mapBetweenVAndC = new HashMap<String, ArrayList<String>>(100);

        this.realPlaces = new ArrayList<RealPlace>(100);
        this.virtualPlaces = new ArrayList<VirtualPlace>(100);
        this.transitions = new ArrayList<Transition>(100);
        this.arcs = new ArrayList<Arc>(100);

        this.realPlaceViews = new ArrayList<RealPlaceView>(100);
        this.virtualPlaceViews = new ArrayList<VirtualPlaceView>(100);
        this.transitionViews = new ArrayList<TransitionView>(100);
        this.arcViews = new ArrayList<ArcView>(100);

        propositionType = new boolean[5];

    }

    /*Getter and Setter*/
    public ArrayList<String> getConstants() {
        return constants;
    }

    public void setConstants(ArrayList<String> constants) {
        this.constants = constants;
    }

    public ArrayList<String> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<String> variables) {
        this.variables = variables;
    }

    public HashMap<String, ArrayList<String>> getMapBetweenVAndC() {
        return mapBetweenVAndC;
    }

    public void setMapBetweenVAndC(HashMap<String, ArrayList<String>> mapBetweenVAndC) {
        this.mapBetweenVAndC = mapBetweenVAndC;
    }

    /*Model*/

    public ArrayList<RealPlace> getRealPlaces() {
        return realPlaces;
    }

    public void setRealPlaces(ArrayList<RealPlace> realPlaces) {
        this.realPlaces = realPlaces;
    }

    public ArrayList<VirtualPlace> getVirtualPlaces() {
        return virtualPlaces;
    }

    public void setVirtualPlaces(ArrayList<VirtualPlace> virtualPlaces) {
        this.virtualPlaces = virtualPlaces;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<Transition> transitions) {
        this.transitions = transitions;
    }

    public ArrayList<Arc> getArcs() {
        return arcs;
    }

    public void setArcs(ArrayList<Arc> arcs) {
        this.arcs = arcs;
    }
    /*View*/

    public ArrayList<RealPlaceView> getRealPlaceViews() {
        return realPlaceViews;
    }

    public void setRealPlaceViews(ArrayList<RealPlaceView> realPlaceViews) {
        this.realPlaceViews = realPlaceViews;
    }

    public ArrayList<VirtualPlaceView> getVirtualPlaceViews() {
        return virtualPlaceViews;
    }

    public void setVirtualPlaceViews(ArrayList<VirtualPlaceView> virtualPlaceViews) {
        this.virtualPlaceViews = virtualPlaceViews;
    }

    public ArrayList<TransitionView> getTransitionViews() {
        return transitionViews;
    }

    public void setTransitionViews(ArrayList<TransitionView> transitionViews) {
        this.transitionViews = transitionViews;
    }

    public ArrayList<ArcView> getArcViews() {
        return arcViews;
    }

    public void setArcViews(ArrayList<ArcView> arcViews) {
        this.arcViews = arcViews;
    }

    public String getSavedFile() {
        return savedFile;
    }

    public void setSavedFile(String savedFile) {
        this.savedFile = savedFile;
    }

    public int[][] getInputMatrix() {
        return inputMatrix;
    }

    public void setInputMatrix(int[][] inputMatrix) {
        this.inputMatrix = inputMatrix;
    }

    public int[][] getOutputMatrix() {
        return outputMatrix;
    }

    public void setOutputMatrix(int[][] outputMatrix) {
        this.outputMatrix = outputMatrix;
    }

    public CTNode getCtRootNode() {
        return ctRootNode;
    }

    public void setCtRootNode(CTNode ctRootNode) {
        this.ctRootNode = ctRootNode;
    }

    public KSNode getKsRootNode() {
        return ksRootNode;
    }

    public void setKsRootNode(KSNode ksRootNode) {
        this.ksRootNode = ksRootNode;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }

    public boolean[] getPropositionType() {
        return propositionType;
    }

    public void setPropositionType(boolean[] propositionType) {
        this.propositionType = propositionType;
    }
}
