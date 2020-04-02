package vpntool.views;

import java.awt.*;
import java.io.Serializable;

public class ArcView extends VPNShape implements Serializable {
    private int targetX;
    private int targetY;

    private boolean isReal;//标记虚实


    public ArcView(int id, String name, int sourceX, int sourceY, int targetX, int targetY, boolean isReal) {
        super(id, name, sourceX, sourceY);
        this.targetX = targetX;
        this.targetY = targetY;
        this.isReal = isReal;
    }

    public void shapeDrawing(Graphics2D graphics2D, String arcLabel) {
        Stroke dash;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isReal) {
            dash = new BasicStroke(2);
        } else {
            dash = new BasicStroke(2f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND, 1.5f, new float[] { 5, 5, },0f);
        }
        graphics2D.setStroke(dash);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawLine(this.getShapeCenterX(), this.getShapeCenterY(), targetX, targetY);
        graphics2D.setColor(Color.RED);
        graphics2D.fillOval(targetX - 2, targetY - 2, 5, 5);

        /*画弧标签。还要优化*/
        int arcLabelX = (this.getShapeCenterX() + targetX)/2;
        int arcLabelY = (this.getShapeCenterY() + targetY)/2;
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(arcLabel, arcLabelX, arcLabelY);
    }

    /*Getter and Setter*/


}
