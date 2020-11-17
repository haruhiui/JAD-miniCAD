package miniCAD;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Model {

    // state constants
    public static final int SELECT = 1;
    public static final int SELECTED = 2;
    public static final int DRAW = 3;
    // drawing type constants
    public static final int DRAW_NULL = 10;
    public static final int DRAW_LINE = 11;
    public static final int DRAW_POLYGON = 12;
    public static final int DRAW_CYCLE = 13;
    public static final int DRAW_TEXT = 14;

    // state
    private static int state = SELECT;
    public static int getState() {
        return state;
    }
    public static boolean inState(int state) {
        return Model.state == state;
    }
    public static void setState(int state) {
        Model.state = state;
    }
    // drawing type
    private static int drawingType = DRAW_NULL;
    public static void setDrawingType(int drawingType) {
        Model.drawingType = drawingType;
    }
    public static boolean isDrawingType(int type) {
        return Model.drawingType == type;
    }

    // drawing shape and selected shape and shapes
    private static MyShape drawingShape = null;
    public static MyShape getDrawingShape() {
        return drawingShape;
    }
    private static MyShape selectedShape = null;
    public static MyShape getSelectedShape() {
        return selectedShape;
    }
    private static List<MyShape> shapes = new ArrayList<>();
    public static List<MyShape> getShapes() {
        return shapes;
    }
    public static void setShapes(List<MyShape> shapes) {
        Model.shapes = shapes;
    }
    // other
    private static int selectShapeIndex = 0;

    public static void releaseDrawingShape() {
        drawingShape = null;
    }
    public static void releaseSelectedShape() {
        if (selectedShape != null)
            selectedShape.selected = false;
        selectedShape = null;
    }
    public static void clearSelectShapeIndex() {
        selectShapeIndex = 0;
    }

    // whether (x, y) is in the region of shape
    public static boolean inShapeRegion(int x, int y, MyShape shape) {
        if (x > Math.min(shape.startX, shape.endX) && x < Math.max(shape.startX, shape.endX)) {
            if (y > Math.min(shape.startY, shape.endY) && y < Math.max(shape.startY, shape.endY)) {
                return true;
            }
        }
        return false;
    }
    public static boolean hasShapeToSelect(int x, int y) {
        for (MyShape shape : shapes) {
            if (inShapeRegion(x, y, shape))
                return true;
        }
        return false;
    }
    public static void selectNext(int x, int y) {
        for (int i = 0; i < shapes.size(); i++) {
            if (selectShapeIndex >= shapes.size()) {
                selectShapeIndex = 0;
            }
            MyShape shape = shapes.get(selectShapeIndex++);
            if (inShapeRegion(x, y, shape)) {
                if (selectedShape != null)
                    selectedShape.selected = false;
                selectedShape = shape;
                selectedShape.selected = true;
                return ;
            }
        }
    }

    public static void createShape() {
        ShapeDrawer drawer;
        if (drawingType == DRAW_LINE) {
            drawer = ShapeDrawer.LINE;
        } else if (drawingType == DRAW_POLYGON) {
            drawer = ShapeDrawer.POLYGON;
        } else if (drawingType == DRAW_CYCLE) {
            drawer = ShapeDrawer.CYCLE;
        } else if (drawingType == DRAW_TEXT) {
            drawer = ShapeDrawer.TEXT;
        } else {
            drawer = ShapeDrawer.NULL;
        }
        drawingShape = new MyShape(drawer);
        shapes.add(drawingShape);
    }

    public static void deleteSelectedShape() {
        shapes.remove(selectedShape);
        selectedShape = null;
    }

}
