package miniCAD;

import java.awt.*;
import java.io.Serializable;

public class MyShape implements Serializable {

    // geometric position
    protected int startX, startY;
    protected int endX, endY;
    // line property
    protected float lineWidth = 2.0f;
    protected Color lineColor = Color.BLACK;
    // fill property
    protected Color fillColor = Color.WHITE;
    protected boolean filled = false;
    // text
    protected String text = "";
    protected int fontSize = 18;
    // shape drawer
    protected ShapeDrawer drawer;
    // other properties
    protected boolean selected = false;

    MyShape(ShapeDrawer drawer) {
        this.drawer = drawer;
    }
    public void draw(Graphics g) {
        drawer.drawShape(g, this);
    }

    public void setStartPoint(int x, int y) {
        startX = x;
        startY = y;
    }
    public void setEndPoint(int x, int y) {
        endX = x;
        endY = y;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void incLineWidth() {
        lineWidth += 1.0f;
    }
    public void decLineWidth() {
        lineWidth -= 1.0f;
    }
    public void incSize() {
        int w = endX - startX, h = endY - startY;
        int deltaW = 10, deltaH = h * deltaW / w;
        endX += deltaW;
        endY += deltaH;
    }
    public void decSize() {
        int w = endX - startX, h = endY - startY;
        int deltaW = 10, deltaH = h * deltaW / w;
        endX -= deltaW;
        endY -= deltaH;
    }

}
