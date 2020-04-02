package vpntool.gui;

import vpntool.gui.listeners.DrawPanelMouseListener;
import vpntool.gui.listeners.DrawPanelMouseMotionListener;
import vpntool.models.PetriNet;
import vpntool.utils.parser.CommonStringMethods;
import vpntool.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ColorModel;
import java.util.ArrayList;

public class DrawPanel extends JPanel {

//    坐标相关变量移到监听器类中
//    private int shapeCenterX, shapeCenterY;
//    private int sourceX, sourceY;
//    private int targetX, targetY;

    private PetriNet petriNet;//存所有信息，包括模型层和视图层。

    private int realPlaceIndex = 0;
    private int virtualPlaceIndex = 0;//对库所设置索引是否有意义？至少能让model和view统一
    private int transitionIndex = 0;
    private int arcIndex = 0;

    private int mouseClickCountAboutArc = 0;//注意是记录画板上的已点击次数，对于画弧图标的点击并不计入

    public DrawPanel() {

        /*调试*/
        System.out.println("DrawPanel");

        repaint();//刷新画板。有必要吗？

        /*debug*/
        /*等下，重写了paint之后并没有变化说明了什么？？？*/
        /*说明初始化时候的对象画板和显示的画板并不是一个？*/
//        ColorModel c = this.getColorModel();
//        c.getBlue(30);
//        System.out.println("after paint()");

        petriNet = new PetriNet();


        this.addMouseListener(new DrawPanelMouseListener(this));

        /*debug*/
        /*这样也不对*/
//        this.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("mouseClicked");
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                System.out.println("mousePressed");
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                System.out.println("mouseReleased");
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                System.out.println("mouseEntered");
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                System.out.println("mouseExited");
//            }
//        });

        this.addMouseMotionListener(new DrawPanelMouseMotionListener());
    }

/*如果不重写paint点击画板仍然无反应*/
//    public void paint(Graphics2D graphics2D) {//注意形参为Graphics2D时并不是重写的方法！
    public void paint(Graphics graphics) {
//        System.out.println("paintttttttttttttttttttttttttttttttttt");//为什么没有调用到paint？？？

        // TODO:
        super.paint(graphics);
        this.setBackground(Color.WHITE);//这里改成蓝色是没有用的？！说明drawPanel根本就没有插进去，还有滚动框就没插进去！


        // TODO: 画布大小
//        setPreferredSize(new Dimension(400, 400));
//
//        graphics2D.setColor(Color.BLACK);

        ArrayList<TransitionView> transitionViews = petriNet.getTransitionViews();
        for (int i = 0; i < transitionViews.size(); i++) {
            transitionViews.get(i).shapeDrawing((Graphics2D) graphics);
        }

        ArrayList<RealPlaceView> realPlaceViews = petriNet.getRealPlaceViews();
        for (int i = 0; i < realPlaceViews.size(); i++) {
            realPlaceViews.get(i).shapeDrawing((Graphics2D) graphics);

            ArrayList<TokenView> tokenViews = realPlaceViews.get(i).getTokenViews();
            for (int j = 0; j < tokenViews.size(); j++) {
//                System.out.println("tokenViews.size is " + tokenViews.size());
                tokenViews.get(j).shapeDrawing((Graphics2D) graphics);
            }

        }

        ArrayList<VirtualPlaceView> virtualPlaceViews = petriNet.getVirtualPlaceViews();
        for (int i = 0; i < virtualPlaceViews.size(); i++) {
            virtualPlaceViews.get(i).shapeDrawing((Graphics2D) graphics);
        }

        ArrayList<ArcView> arcViews = petriNet.getArcViews();
        for (int i = 0; i < arcViews.size(); i++) {

            /*画弧标签*/
            String arcLabel = CommonStringMethods.arrayListToStringOfW(petriNet.getArcs().get(i));

            arcViews.get(i).shapeDrawing((Graphics2D) graphics, arcLabel);
        }



    }

    /*Getter and Setter*/

    public PetriNet getPetriNet() {
        return petriNet;
    }

    public void setPetriNet(PetriNet petriNet) {
        this.petriNet = petriNet;
    }

    public int getRealPlaceIndex() {
        return realPlaceIndex;
    }

    public void setRealPlaceIndex(int realPlaceIndex) {
        this.realPlaceIndex = realPlaceIndex;
    }

    public int getVirtualPlaceIndex() {
        return virtualPlaceIndex;
    }

    public void setVirtualPlaceIndex(int virtualPlaceIndex) {
        this.virtualPlaceIndex = virtualPlaceIndex;
    }

    public int getTransitionIndex() {
        return transitionIndex;
    }

    public void setTransitionIndex(int transitionIndex) {
        this.transitionIndex = transitionIndex;
    }

    public int getArcIndex() {
        return arcIndex;
    }

    public void setArcIndex(int arcIndex) {
        this.arcIndex = arcIndex;
    }

    public int getMouseClickCountAboutArc() {
        return mouseClickCountAboutArc;
    }

    public void setMouseClickCountAboutArc(int mouseClickCountAboutArc) {
        this.mouseClickCountAboutArc = mouseClickCountAboutArc;
    }
}
