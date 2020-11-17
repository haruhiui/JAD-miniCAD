package miniCAD;

import java.awt.*;

public enum ShapeDrawer {

    NULL {
        public void drawShape(Graphics g, MyShape shape) { }
    },
    LINE {
        public void drawShape(Graphics g, MyShape shape) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(shape.lineWidth));
            if (shape.selected)
                g2.setColor(Color.RED);
            else
                g2.setColor(shape.lineColor);

            g2.drawLine(shape.startX, shape.startY, shape.endX, shape.endY);
        }
    },
    POLYGON {
        public void drawShape(Graphics g, MyShape shape) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(shape.lineWidth));
            if (shape.selected)
                g2.setColor(Color.RED);
            else
                g2.setColor(shape.lineColor);

            if (shape.filled)
                g2.fillRect(shape.startX, shape.startY, shape.endX, shape.endY);
            else
                g2.drawRect(shape.startX, shape.startY, shape.endX - shape.startX, shape.endY - shape.startY);
        }
    },
    CYCLE {
        public void drawShape(Graphics g, MyShape shape) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(shape.lineWidth));
            if (shape.selected)
                g2.setColor(Color.RED);
            else
                g2.setColor(shape.lineColor);

            if (shape.filled)
                g2.fillArc(shape.startX, shape.startY, shape.endX - shape.startX, shape.endY - shape.startY, 0, 360);
            else
                g2.drawArc(shape.startX, shape.startY, shape.endX - shape.startX, shape.endY - shape.startY, 0, 360);
        }
    },
    TEXT {
        public void drawShape(Graphics g, MyShape shape) {
            Graphics2D g2 = (Graphics2D)g;
            double fontSize = 2.4 * Math.max(Math.abs(shape.endX - shape.startX), Math.abs(shape.endY - shape.startY)) / shape.text.length();
            g2.setFont(new Font(null, 0, (int)fontSize));

            if (shape.selected)
                g2.setColor(Color.RED);
            else
                g2.setColor(shape.lineColor);

            g2.drawString(shape.text, shape.startX, shape.endY);
        }
    };

    public abstract void drawShape(Graphics g, MyShape shape);

}
