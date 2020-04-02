package vpntool.views;

import java.awt.*;
import java.io.Serializable;

public class TokenView extends VPNShape implements Serializable {
    private int placeIndex;//所在库所的编号。这样设置会不会有点冗余？

    public TokenView(int placeIndex, int indexInAPlace, String name, int shapeCenterX, int shapeCenterY) {
        super(indexInAPlace, name, shapeCenterX, shapeCenterY);
        this.placeIndex = placeIndex;
    }

    public void shapeDrawing(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillOval(this.getShapeCenterX() - 2, this.getShapeCenterY() - 2, 4, 4);
    }
}
