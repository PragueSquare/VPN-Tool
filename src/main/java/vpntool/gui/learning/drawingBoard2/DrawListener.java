package vpntool.gui.learning.drawingBoard2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawListener extends MouseAdapter implements ActionListener {
    private int x1, y1, x2, y2, x3, y3, sx, sy, ex, ey;
    private Graphics2D g2;
    private String str = "Line";
    private Color color;
    private int flag = 1;//用于标记是否是第一次画线。其实应该是记录鼠标的点击次数

    /*——重绘——*/
    private Shape[] array;
    private int index = 0;//不知道后续图形计数有没有问题

    public void setArray(Shape[] array) {
        this.array = array;
    }
    /*——重绘——*/

    public void setG(Graphics g) {
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (button.getText() != "") {
            str = button.getText();
        } else {
            color = button.getBackground();
            System.out.println("color " + color);
        }
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("点击");//按下和释放要在同一位置才是点击
        /*用x2，y2承接鼠标位置*/
        x2 = e.getX();
        y2 = e.getY();

        //画空心矩形
        if (str.equals("Rect")) {
            Shape s = new Shape(x2, y2, 80, 40, color,
                    new BasicStroke(1), "Rect");
            s.draw(g2);
            /*——重绘——*/
            if (index < 1000) {
                array[index++] = s;
            }
            /*——重绘——*/
        }
        //画填充矩形
        if (str.equals("fillRect")) {
            Shape s = new Shape(x2, y2, 80, 40, color,
                    new BasicStroke(1), "fillRect");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画圆角矩形
        if (str.equals("RoundRect")) {
            Shape s = new Shape(x2, y2, 80, 40, color,
                    new BasicStroke(1), "fillRect");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画空心圆
        if (str.equals("Oval")) {
            Shape s = new Shape(x2, y2, 80, 40, color,
                    new BasicStroke(1), "Oval");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画填充圆
        if (str.equals("fillOval")) {
            Shape s = new Shape(x2, y2, 80, 40, color,
                    new BasicStroke(1), "fillOval");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画填充弧
        if (str.equals("fillArc")) {
            Shape s = new Shape(x2, y2, 80, 80, color,
                    new BasicStroke(1), "fillArc");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画图片
        if (str.equals("Image")) {
            //先用ImageIcon设置路径，再获取图片(绝对路径)
            ImageIcon icon = new ImageIcon("C:\\Users\\lenovo\\Desktop\\background1.jpg");
            Image img = icon.getImage();
            Shape s = new Shape(x2, y2, 200, 150,
                    img, "Image");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画文本
        if (str.equals("Text")) {
            String text = "Hello World";
            Font font = new Font("华文行楷", Font.BOLD + Font.ITALIC, 26);//字体，粗体，斜体，大小
            g2.setFont(font);
            Shape s = new Shape(x2, y2, text, "Text");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画任意多边形
        if (str.equals("Polygon")) {
            if (flag == 2) {
                Shape s = new Shape(ex, ey, x2, y2, color,
                        new BasicStroke(1), "Line");
                s.draw(g2);
                ex = x2;
                ey = y2;
                if (index < 1000) {
                    array[index++] = s;
                }
            }
            //如果与起点较近或鼠标双击，则闭合多边形
            if ((Math.abs(sx - x2) < 10 && Math.abs(sy - y2) < 10)
                    || (e.getClickCount() == 2)) {
                Shape s = new Shape(sx, sy, x2, y2, color,
                        new BasicStroke(1), "Line");
                s.draw(g2);
                flag = 1;
                if (index < 1000) {
                    array[index++] = s;
                }
            }
        }
    }

    public void mousePressed(MouseEvent e){
        /*用x1，y1承接鼠标位置*/
        x1 = e.getX();
        y1 = e.getY();
        g2.setColor(color);
        /*用sx，sy承接鼠标位置*/
        if(flag == 1){
            sx = x1;
            sy = y1;
        }
    }

    public void mouseReleased(MouseEvent e){
        /*用x2，y2承接鼠标位置*/
        x2 = e.getX();
        y2 = e.getY();

        //画直线
        if(str.equals("Line")){
            Shape s = new Shape(x1,y1,x2,y2,color,
                    new BasicStroke(1),"Line");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
        //画任意多边形
        if(str.equals("Polygon")){
            if(flag == 1){
                Shape s = new Shape(x1,y1,x2,y2,color,
                        new BasicStroke(1),"Line");
                s.draw(g2);
                ex = x2;
                ey = y2;
                flag = 2;
            }
        }
        //画等腰三角形
        if(str.equals("Triangle")){
            Shape s = new Shape(x1,y1,x2,y2,color,
                    new BasicStroke(1),"Triangle");
            s.draw(g2);
            if (index < 1000) {
                array[index++] = s;
            }
        }
    }

    public void mouseDragged(MouseEvent e){
        /*用x3，y3承接鼠标位置*/
        x3 = e.getX();
        y3 = e.getY();

        //画曲线
        if(str.equals("Pencil")){
            Shape s = new Shape(x1, y1, x3, y3, color,
                    new BasicStroke(10),"Pencil");
            s.draw(g2);

            x1 = x3;
            y1 = y3;
            if (index < 1000) {
                array[index++] = s;
            }
        }
    }

}
