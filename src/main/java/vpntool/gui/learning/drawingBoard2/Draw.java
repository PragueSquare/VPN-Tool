package vpntool.gui.learning.drawingBoard2;

import javax.swing.*;
import java.awt.*;

public class Draw extends JFrame {

    /*——重绘——*/
    public Shape[] shapeArray = new Shape[1000];

    /*重写paint方法*/
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println("绘制了");
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < shapeArray.length; i++) {
            Shape s = shapeArray[i];
            if (s != null) {
                s.draw(g2);
            }
        }
    }
    /*——重绘——*/


    public static void main(String[] args) {
        Draw dr = new Draw();
        dr.showdraw();
    }

    public void showdraw() {
        /*继承后JFrame无需实例化*/
        this.setTitle("画图板");
        this.setSize(700, 500);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        FlowLayout f1 = new FlowLayout();
        this.setLayout(f1);

        //实例化监视器
        DrawListener lis = new DrawListener();

        String[] array ={"Line","Pencil","Rect","Oval","RoundRect","fillRect",
                "fillArc","fillOval","Text","Image","Triangle","Polygon"};
        for(int i=0 ; i<array.length ;i++){
            JButton button = new JButton(array[i]);
            this.add(button);
            button.addActionListener(lis);
        }
        Color[] colorArray ={Color.black,Color.blue,
                Color.green,Color.red};
        for(int i=0 ; i<colorArray.length ; i++){
            JButton button = new JButton();
            button.setBackground(colorArray[i]);
            button.setPreferredSize(new Dimension(30,30));//设置按钮大小
            this.add(button);
            button.addActionListener(lis);
        }
        this.setVisible(true);  //setVisible设置窗体可见，放在所有组件后

        Graphics g = this.getGraphics();
        this.addMouseListener(lis);
        this.addMouseMotionListener(lis);

        lis.setG(g);

        /*——重绘——*/
        lis.setArray(shapeArray);
        /*——重绘——*/
    }
}
