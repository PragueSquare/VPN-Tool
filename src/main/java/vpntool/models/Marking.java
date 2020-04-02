package vpntool.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/*其实不一定要设置这个类？*/
public class Marking implements Serializable {
    private int id;//标识的id应该和配置结点的id保持一致

//    private HashMap<Place, ArrayList<Token>> M;//有更合适的命名吗？
    private HashMap<String, ArrayList<Token>> M;
//    private HashMap<String, HashMap<ArrayList<Token>, Integer>> M;


    public Marking(int id) {
        this.id = id;

        M = new HashMap<String, ArrayList<Token>>();//不标明类型没有任何影响吗？
    }

    /*这种按需设置的构造函数合理吗？*/
    public Marking(HashMap<String, ArrayList<Token>> m) {
        M = m;
    }

    /*Getter and Setter*/

    public HashMap<String, ArrayList<Token>> getM() {
        return M;
    }

    public void setM(HashMap<String, ArrayList<Token>> m) {
        M = m;
    }
}
