package vpntool.views;

import java.awt.*;
import java.io.Serializable;

public class TransitionView extends VPNShape implements Serializable {

    public TransitionView(int id, String name, int shapeCenterX, int shapeCenterY) {
        super(id, name, shapeCenterX, shapeCenterY);
    }


    public boolean containsCursor(int cursorX, int cursorY) {//注意变迁是矩形
        if ((cursorX >= this.getShapeCenterX() - 5)&&(cursorX <= this.getShapeCenterX() + 5)&&(cursorY >= this.getShapeCenterY() - 15)&&(cursorY <= this.getShapeCenterY() + 15)) {
            return true;
        } else {
            return false;
        }
    }

    public void shapeDrawing(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(this.getName(), this.getShapeCenterX() - 2, this.getShapeCenterY() + 30);
        graphics2D.drawRect(this.getShapeCenterX() - 5, this.getShapeCenterY() - 15, 10, 30);
    }
}
