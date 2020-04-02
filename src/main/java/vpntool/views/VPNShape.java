package vpntool.views;

import java.io.Serializable;

public abstract class VPNShape implements Serializable {
    private int id;//这里的id对应于PetriNet中的顺序表
    private String name;

    private int shapeCenterX, shapeCenterY;

    public VPNShape(int id, String name, int shapeCenterX, int shapeCenterY) {
        this.id = id;
        this.name = name;
        this.shapeCenterX = shapeCenterX;
        this.shapeCenterY = shapeCenterY;
    }

    /*Getter and Setter*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShapeCenterX() {
        return shapeCenterX;
    }

    public void setShapeCenterX(int shapeCenterX) {
        this.shapeCenterX = shapeCenterX;
    }

    public int getShapeCenterY() {
        return shapeCenterY;
    }

    public void setShapeCenterY(int shapeCenterY) {
        this.shapeCenterY = shapeCenterY;
    }
}
