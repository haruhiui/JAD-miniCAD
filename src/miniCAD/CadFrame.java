package miniCAD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

public class CadFrame extends JFrame {

    private JPanel paintPanel;
    private JPanel buttonPanel;
    private List<JButton> buttons;

    CadFrame() {
        super();
    }
    public JPanel getPaintPanel() {
        return paintPanel;
    }

    public void initInterface() {
        // frame 1
        setTitle("miniCAD");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        // paintPanel
        paintPanel  = new JPanel();
        paintPanel.setBackground(Color.WHITE);
        paintPanel.setFocusable(true);              // for keyboard
        add(paintPanel);

        // buttonPanel
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5));
        // buttons
        buttons = new ArrayList<>();
        buttons.add(genJButton("SELECT", Model.SELECT, Model.DRAW_NULL));
        buttons.add(genJButton("LINE", Model.DRAW, Model.DRAW_LINE));
        buttons.add(genJButton("POLYGON", Model.DRAW, Model.DRAW_POLYGON));
        buttons.add(genJButton("CYCLE", Model.DRAW, Model.DRAW_CYCLE));
        buttons.add(genJButton("TEXT", Model.DRAW, Model.DRAW_TEXT));
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }
        add(buttonPanel, BorderLayout.EAST);

        // menu
        JMenuBar topMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem[] fileMenuItem = {
                new JMenuItem("打开") {{
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String loadFileName = callFileDialog("打开", FileDialog.LOAD);
                            try {
                                FileInputStream loadFileStream = new FileInputStream(loadFileName);
                                ObjectInputStream objectStream = new ObjectInputStream(loadFileStream);
                                Model.setShapes((List<MyShape>)objectStream.readObject());
                                redrawPaint();
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                            paintPanel.requestFocus();                  // refocus to paintPanel, IMPORTANT
                        }
                    });
                }},
                new JMenuItem("保存") {{
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String saveFileName = callFileDialog("保存", FileDialog.SAVE);
                            try {
                                FileOutputStream saveFileStream = new FileOutputStream(saveFileName);
                                ObjectOutputStream objectStream = new ObjectOutputStream(saveFileStream);
                                objectStream.writeObject(Model.getShapes());
                                saveFileStream.flush();
                            } catch (Exception ex) { }
                            paintPanel.requestFocus();                  // refocus to paintPanel, IMPORTANT
                        }
                    });
                }},
                new JMenuItem("关闭") {{
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dispose();
                            System.exit(0);
                            paintPanel.requestFocus();                  // refocus to paintPanel, IMPORTANT
                        }
                    });
                }}
        };
        JMenu otherMenu = new JMenu("其他");
        JMenuItem[] otherMenuItem = {
                new JMenuItem("帮助") {{
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            callMessageDialog(new String[] {
                                    "=/-: 选中后改变大小",
                                    "./,: 选中后改变粗细",
                                    "颜色可以通过菜单栏的按钮改变",
                                    "选中时图形是红色，取消选中后才能看到改变颜色的结果"
                            });
                            paintPanel.requestFocus();                  // refocus to paintPanel, IMPORTANT
                        }
                    });
                }},
                new JMenuItem("相关") {{
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            callMessageDialog(new String[] {
                                    "相关信息: miniCAD for JAD by lyy"
                            });
                            paintPanel.requestFocus();                  // refocus to paintPanel, IMPORTANT
                        }
                    });
                }}
        };

        for (JMenuItem item : fileMenuItem) {
            fileMenu.add(item);
        }
        for (JMenuItem item : otherMenuItem) {
            otherMenu.add(item);
        }
        topMenuBar.add(fileMenu);
        topMenuBar.add(otherMenu);

        // color
        Color[] colors = {
                Color.WHITE,
                Color.BLACK,
                Color.BLUE,
                Color.CYAN,
                Color.DARK_GRAY,
                Color.GRAY,
                Color.GREEN,
                Color.LIGHT_GRAY,
                Color.MAGENTA,
                Color.ORANGE,
                Color.PINK,
                Color.RED,
                Color.YELLOW
        };
        JButton[] colorButtons = new JButton[colors.length];
        for (int i = 0; i < colors.length; i++) {
            int j = i;
            colorButtons[i] = new JButton(" ") {{
                setFocusPainted(false);
                setBackground(colors[j]);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Model.getSelectedShape() != null) {
                            Model.getSelectedShape().lineColor = colors[j];
                            paintPanel.requestFocus();                  // refocus to paintPanel, IMPORTANT
                        }
                    }
                });
            }};
        }
        topMenuBar.add(Box.createHorizontalGlue());
        for (JButton cBtn : colorButtons) {
            topMenuBar.add(cBtn);
        }

        topMenuBar.setBackground(Color.WHITE);
        setJMenuBar(topMenuBar);

        // frame 2
        setBackground(Color.WHITE);
        setVisible(true);
    }

    public JButton genJButton(String text, int toState, int toDrawingType) {
        JButton ret = new JButton(text);
        ret.setBackground(Color.WHITE);
        ret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Model.setState(toState);
                Model.setDrawingType(toDrawingType);
                paintPanel.requestFocus();                  // refocus to paintPanel, IMPORTANT
                if (text.equals("TEXT")) {      // for text, we call a dialog and save the input to dialogInput
                    callInputDialog();
                }
            }
        });
        return ret;
    }

    public void clearPaint() {
        paintPanel.getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }
    public void drawPaint(List<MyShape> shapes) {
        Graphics g = paintPanel.getGraphics();
        for (MyShape shape : shapes) {
            shape.drawer.drawShape(g, shape);
        }
    }
    public void redrawPaint() {
        clearPaint();
        drawPaint(Model.getShapes());
    }

    public void paint(Graphics g) {
        super.paint(g);
        redrawPaint();
    }

    private String dialogInput = "";
    public String getDialogInput() {
        return dialogInput;
    }
    public void callInputDialog() {
        JDialog dialog = new JDialog();
        JLabel label = new JLabel("请输入要显示的文字：");
        JTextField input = new JTextField();
        JButton btnOk = new JButton("确认");
        JButton btnCancel = new JButton("取消");

        // button
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogInput = input.getText();
                dialog.dispose();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();                               // just dispose
            }
        });

        dialog.setTitle("miniCAD input");
        dialog.setSize(320, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(2, 2, 5, 5));
        dialog.add(label);
        dialog.add(input);
        dialog.add(btnOk);
        dialog.add(btnCancel);

        dialog.setVisible(true);
    }

    public void callMessageDialog(String[] text) {
        JDialog dialog = new JDialog();
        JLabel[] labels = new JLabel[text.length];
        for (int i = 0; i < text.length; i++) {
            labels[i] = new JLabel(text[i]);
            dialog.add(labels[i]);
        }
        dialog.setTitle("miniCAD message");
        dialog.setSize(340, 120);
        dialog.setLayout(new FlowLayout());
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public String callFileDialog(String title, int mode) {
        FileDialog dialog = new FileDialog(this, title, mode);
        dialog.setFile("*.minicad");
        dialog.setVisible(true);
        return dialog.getDirectory() + dialog.getFile();
    }

//    private MyShape changedPropertiesShape = null;
//    public void callPropertiesDialog(MyShape shape) {
//        JDialog dialog = new JDialog();
//        JLabel lineWidthLabel = new JLabel("line width:");
//        JLabel lineColorLabel = new JLabel("line color:");
//        JLabel filledLabel = new JLabel("filled:");
//        JLabel fillColorLabel = new JLabel("fill color:");
//        JLabel textLabel = new JLabel("text:");
//        JLabel fontSizeLabel = new JLabel("font size:");
//        JTextField lineWidth = new JTextField((int)shape.lineWidth);
//        JTextField lineColor = new JTextField(shape.lineColor);
//    }
}
