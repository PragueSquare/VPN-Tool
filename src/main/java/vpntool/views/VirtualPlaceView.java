package vpntool.views;

import java.awt.*;
import java.io.Serializable;

public class VirtualPlaceView extends VPNShape implements Serializable {

    public VirtualPlaceView(int id, String name, int shapeCenterX, int shapeCenterY) {
        super(id, name, shapeCenterX, shapeCenterY);
    }


    public boolean containsCursor(int cursorX, int cursorY) {
        if (Math.pow(this.getShapeCenterX() - cursorX, 2) + Math.pow(this.getShapeCenterY() - cursorY, 2) <= 225) {
            return true;
        } else {
            return false;
        }
    }

    public void shapeDrawing(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke dash = new BasicStroke(2f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND, 1.5f, new float[] { 5, 5, },0f);
        graphics2D.setStroke(dash);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(this.getName(), this.getShapeCenterX() - 5, this.getShapeCenterY() + 30);
        graphics2D.drawOval(this.getShapeCenterX() - 15, this.getShapeCenterY() - 15, 30, 30);
    }
}
