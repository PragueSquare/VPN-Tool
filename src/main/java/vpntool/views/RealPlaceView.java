package vpntool.views;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class RealPlaceView extends VPNShape implements Serializable {
    private ArrayList<TokenView> tokenViews;
    private int tokenViewIndex = 0;

    public RealPlaceView(int id, String name, int shapeCenterX, int shapeCenterY) {
        super(id, name, shapeCenterX, shapeCenterY);

        tokenViews = new ArrayList<TokenView>();
    }

    public boolean containsTokenInView(int tokenX, int tokenY) {//tokenX即tokenShapeCenterX
        if (Math.pow(this.getShapeCenterX() - tokenX, 2) + Math.pow(this.getShapeCenterY() - tokenY, 2) <= 169) {
            return true;
        } else {
            return false;
        }
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
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(this.getName(), this.getShapeCenterX() - 5, this.getShapeCenterY() + 30);
        graphics2D.drawOval(this.getShapeCenterX() - 15, this.getShapeCenterY() - 15, 30, 30);
    }

    /*Getter and Setter*/

    public ArrayList<TokenView> getTokenViews() {
        return tokenViews;
    }

    public void setTokenViews(ArrayList<TokenView> tokenViews) {
        this.tokenViews = tokenViews;
    }

    public int getTokenViewIndex() {
        return tokenViewIndex;
    }

    public void setTokenViewIndex(int tokenViewIndex) {
        this.tokenViewIndex = tokenViewIndex;
    }
}
