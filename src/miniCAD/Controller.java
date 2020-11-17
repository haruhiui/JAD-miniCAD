package miniCAD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;

public class Controller {

    private static CadFrame cadFrame = null;
    private static int lastX, lastY;

    public static void setCadFrame(CadFrame cadFrame) {
        Controller.cadFrame = cadFrame;
    }

    public static void addListener() {
        JPanel paintPanel = cadFrame.getPaintPanel();
        paintPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("clicked");
                if (Model.inState(Model.SELECTED)) {
                    // click to select next shape
                    Model.selectNext(e.getX(), e.getY());
                }
                cadFrame.redrawPaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // mouse press just change the state
//                System.out.println("pressed");
                if (Model.inState(Model.SELECT)) {
                    if (Model.hasShapeToSelect(e.getX(), e.getY())) {       // there is a shape to select
                        Model.setState(Model.SELECTED);
                    }
                } else if (Model.inState(Model.SELECTED)) {
                    if (!Model.hasShapeToSelect(e.getX(), e.getY())) {
                        Model.setState(Model.SELECT);
                        Model.releaseSelectedShape();
                    }
                } else if (Model.inState(Model.DRAW)) {
                    Model.createShape();
                    Model.getDrawingShape().setStartPoint(e.getX(), e.getY());
                    Model.getDrawingShape().setEndPoint(e.getX(), e.getY());
                    if (Model.isDrawingType(Model.DRAW_TEXT)) {
                        Model.getDrawingShape().setText(cadFrame.getDialogInput());
                    }
                }
                cadFrame.redrawPaint();
                lastX = e.getX();
                lastY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println("released");
                if (Model.inState(Model.DRAW)) {
                    Model.releaseDrawingShape();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        paintPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (Model.inState(Model.SELECTED)) {
                    // drag the selected shape
                    MyShape s = Model.getSelectedShape();
                    if (s != null) {
                        s.setStartPoint(s.startX + e.getX() - lastX, s.startY + e.getY() - lastY);
                        s.setEndPoint(s.endX + e.getX() - lastX, s.endY + e.getY() - lastY);
                        cadFrame.redrawPaint();
                    }
                } else if (Model.inState(Model.DRAW)) {
                    // draw the drawing shape
                    MyShape s = Model.getDrawingShape();
                    if (s != null) {
                        s.setEndPoint(e.getX(), e.getY());
                        cadFrame.redrawPaint();
                    }
                }
                cadFrame.redrawPaint();
                lastX = e.getX();
                lastY = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        paintPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Model.inState(Model.SELECTED)) {
                    if (Model.getSelectedShape() == null)
                        return;
                    if (e.getKeyChar() == '+' || e.getKeyChar() == '=') {                           // size +
                        Model.getSelectedShape().incSize();
                    } else if (e.getKeyChar() == '_' || e.getKeyChar() == '-') {                    // size -
                        Model.getSelectedShape().decSize();
                    } else if (e.getKeyChar() == '>' || e.getKeyChar() == '.') {                    // thickness >
                        Model.getSelectedShape().incLineWidth();
                    } else if (e.getKeyChar() == '<' || e.getKeyChar() == ',') {                    // thickness <
                        Model.getSelectedShape().decLineWidth();
                    } else if (e.getKeyChar() == (int)KeyEvent.VK_BACK_SPACE || e.getKeyChar() == (int)KeyEvent.VK_DELETE) {
                        Model.deleteSelectedShape();
                    }
                }
                cadFrame.redrawPaint();
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

}
