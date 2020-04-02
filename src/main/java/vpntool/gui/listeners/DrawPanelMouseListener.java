package vpntool.gui.listeners;

import vpntool.gui.PaneManager;
import vpntool.input.*;
import vpntool.gui.DrawPanel;
import vpntool.gui.ToolFrame;
import vpntool.models.Connectable;
import vpntool.models.Transition;
import vpntool.views.RealPlaceView;
import vpntool.views.TransitionView;
import vpntool.views.VirtualPlaceView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

//public class DrawPanelMouseListener extends MouseAdapter {//换成MouseAdapter也不对
public class DrawPanelMouseListener implements MouseListener {
    private DrawPanel drawPanel;//在监听类中定义成员变量，通过构造函数把画板对象传进来

    private int shapeCenterX, shapeCenterY;
    private int sourceX, sourceY;
    private int targetX, targetY;

    public DrawPanelMouseListener(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;

//        drawPanel.addMouseListener(this);//此处不必添加

        /*debug*/
//        System.out.println("DrawPanelMouseListener and the icon is " + ToolFrame.getSeqOperIcon());
//        if (drawPanel == null) {
//            System.out.println("Dp is null");
//        }
//        System.out.println("DrawPanelMouseListener and the arcClickCount is " + drawPanel.getMouseClickCountAboutArc());
//        if (PaneManager.getInstance().getSelectedPane().getDrawPanel() == drawPanel) {
//            System.out.println("画板没问题");
//        }
//        if (PaneManager.getInstance().getSelectedPane().getDrawPanel() == null) {
//            System.out.println("画板没问题");
//        }
//        if (PaneManager.getInstance().getSelectedPane() == null) {
//            System.out.println("画板为空");
//        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        /*debug*/
//        String seqOper = ToolFrame.getSeqOperIcon();
//
//        System.out.println("mouseEntered1" + seqOper);
//
//        if (seqOper.equals("RealPlace")) {
//
//            System.out.println("mouseEntered2" + seqOper);
//
//        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        String seqOper = ToolFrame.getSeqOperIcon();

        /*debug*/
        System.out.println("mousePressed" + seqOper);


        if (seqOper.equals("GuardFunction")) {
            Boolean withinATransition = false;
            int transitionIndex = -1;

            shapeCenterX = e.getX();//从这里感觉这个名字起的也不是很合适
            shapeCenterY = e.getY();

            ArrayList<TransitionView> transitionViews = drawPanel.getPetriNet().getTransitionViews();
            for (int i = 0; i < transitionViews.size(); i++) {
                if (transitionViews.get(i).containsCursor(shapeCenterX, shapeCenterY)) {
                    withinATransition = true;
                    transitionIndex = i;
                    break;
                }
            }

            if (!withinATransition) {
                JOptionPane.showMessageDialog(null, "未选中变迁，请重新选择", "提示", JOptionPane.WARNING_MESSAGE, null);
            } else {
                new GuardFunctionDialog(transitionIndex);
            }
        } else if (seqOper.equals("LinkFunction")) {
            Boolean withinATransition = false;
            int transitionIndex = -1;

            shapeCenterX = e.getX();//从这里感觉这个名字起的也不是很合适
            shapeCenterY = e.getY();

            ArrayList<TransitionView> transitionViews = drawPanel.getPetriNet().getTransitionViews();
            for (int i = 0; i < transitionViews.size(); i++) {
                if (transitionViews.get(i).containsCursor(shapeCenterX, shapeCenterY)) {
                    withinATransition = true;
                    transitionIndex = i;
                    break;
                }
            }

            if (!withinATransition) {
                JOptionPane.showMessageDialog(null, "未选中变迁，请重新选择", "提示", JOptionPane.WARNING_MESSAGE, null);
            } else {
                new LinkFunctionDialog(transitionIndex);
            }
        } else if (seqOper.equals("Transition")) {
            int transitionIndex = drawPanel.getTransitionIndex();
            String transitionName = "t" + (transitionIndex + 1);
            drawPanel.getPetriNet().getTransitions().add(transitionIndex, new Transition(transitionIndex, transitionName));//存model

            shapeCenterX = e.getX();
            shapeCenterY = e.getY();
            Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            graphics2D.setStroke(new BasicStroke(2));
//            graphics2D.setColor(Color.BLACK);
//            graphics2D.drawString(transitionName, shapeCenterX, shapeCenterY + 30);
//            graphics2D.drawRect(shapeCenterX - 5, shapeCenterY - 15, 10, 30);
            TransitionView transitionView = new TransitionView(transitionIndex, transitionName, shapeCenterX, shapeCenterY);
            transitionView.shapeDrawing(graphics2D);
            drawPanel.getPetriNet().getTransitionViews().add(transitionIndex, transitionView);//存view


            drawPanel.setTransitionIndex(transitionIndex + 1);

            // TODO: 在画布边缘操作时自动扩大画布

        } else if (seqOper.equals("RealPlace")) {

            /*调试*/
//            System.out.println("real place21");

            shapeCenterX = e.getX();
            shapeCenterY = e.getY();
            Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            graphics2D.setStroke(new BasicStroke(2));
//            graphics2D.setColor(Color.BLACK);
//            graphics2D.drawOval(shapeCenterX - 15, shapeCenterY - 15, 30, 30);
            // TODO: 画库所名
            new RealPlaceNameDialog(shapeCenterX, shapeCenterY, drawPanel, graphics2D);


            drawPanel.setRealPlaceIndex(drawPanel.getRealPlaceIndex() + 1);//放在这里比较安全，如果放在对话框中会有问题吗？

            // TODO: 在画布边缘操作时自动扩大画布

            /*调试*/
//            System.out.println("real place22");

        } else if (seqOper.equals("VirtualPlace")) {
            shapeCenterX = e.getX();
            shapeCenterY = e.getY();
            Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            /*注意虚线的画法*/
//            Stroke dash = new BasicStroke(2f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND, 1.5f, new float[] { 5, 5, },0f);
//            graphics2D.setStroke(dash);
//            graphics2D.setColor(Color.BLACK);
//            graphics2D.drawOval(shapeCenterX - 15, shapeCenterY - 15, 30, 30);
            new VirtualPlaceNameDialog(shapeCenterX, shapeCenterY, drawPanel, graphics2D);

            drawPanel.setVirtualPlaceIndex(drawPanel.getVirtualPlaceIndex() + 1);//放在这里比较安全，如果放在对话框中会有问题吗？

            // TODO: 在画布边缘操作时自动扩大画布

        } else if (seqOper.equals("Token")) {
            Boolean withinAPlace = false;
            int placeIndex = -1;//记录token所在库所的编号。必须要设初值

            shapeCenterX = e.getX();
            shapeCenterY = e.getY();
            ArrayList<RealPlaceView> realPlaceViews = drawPanel.getPetriNet().getRealPlaceViews();
            for (int i = 0; i < realPlaceViews.size(); i++) {
                if (realPlaceViews.get(i).containsTokenInView(shapeCenterX, shapeCenterY)) {
                    withinAPlace = true;

                    placeIndex = i;

                    break;
                }
            }
            if (!withinAPlace) {
                JOptionPane.showMessageDialog(null, "token不在库所中，请重新绘制", "提示", JOptionPane.WARNING_MESSAGE, null);
            } else {
                // TODO: 在画token的时候要考虑覆盖问题，其实其他图形也应该考虑，只不过token最为明显
                Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                graphics2D.setStroke(new BasicStroke(2));
//                graphics2D.setColor(Color.BLACK);
//                graphics2D.fillOval(shapeCenterX - 2, shapeCenterY - 2, 4, 4);
                new TokenDialog(shapeCenterX, shapeCenterY, drawPanel, placeIndex, graphics2D);

                drawPanel.setVirtualPlaceIndex(drawPanel.getVirtualPlaceIndex() + 1);//放在这里比较安全，如果放在对话框中会有问题吗？
                drawPanel.repaint();//有必要吗？
            }
        } else if (seqOper.equals("Arc")) {
            int mouseClickCount = drawPanel.getMouseClickCountAboutArc();
            if (mouseClickCount == 0) {//第一下点击是弧的源位置
                /*仅记录源位置坐标即可，不必定位所在对象，因为可能有弧画一半的情况*/
                sourceX = e.getX();
                sourceY = e.getY();

                drawPanel.setMouseClickCountAboutArc(mouseClickCount + 1);
            } else if (mouseClickCount == 1) {//第二下点击是弧的目位置。用else if而不是else会比较安全
                /*记录目位置坐标*/
                targetX = e.getX();
                targetY = e.getY();

                /*标记*/
                boolean sourceDetermined = false;
                boolean targetDetermined = false;


                ArrayList<TransitionView> transitionViews = drawPanel.getPetriNet().getTransitionViews();
                for (int i = 0; i < transitionViews.size(); i++) {
                    if (transitionViews.get(i).containsCursor(sourceX, sourceY)) {//源是变迁
                        sourceDetermined = true;
                        int transitionCenterX = transitionViews.get(i).getShapeCenterX();
                        int transitionCenterY = transitionViews.get(i).getShapeCenterY();

                        ArrayList<RealPlaceView> realPlaceViews = drawPanel.getPetriNet().getRealPlaceViews();
                        for (int j = 0; j < realPlaceViews.size(); j++) {
                            if (realPlaceViews.get(j).containsCursor(targetX, targetY)) {//源是变迁，目是实库所
                                targetDetermined = true;
                                int realPlaceCenterX = realPlaceViews.get(j).getShapeCenterX();
                                int realPlaceCenterY = realPlaceViews.get(j).getShapeCenterY();

                                /*————debug————*/
                                System.out.println("sourceX before calculation is " + sourceX);
                                System.out.println("sourceY before calculation is " + sourceY);
                                /*————debug————*/

                                int[] sourceResult = calcArcSrcLoc(transitionCenterX, transitionCenterY, realPlaceCenterX, realPlaceCenterY, true);
                                sourceX = sourceResult[0];
                                sourceY = sourceResult[1];//弧的起点坐标

                                /*————debug————*/
                                System.out.println("sourceX after calculation is " + sourceX);
                                System.out.println("sourceY after calculation is " + sourceY);
                                /*————debug————*/

                                int[] targetResult = calcArcTarLoc(transitionCenterX, transitionCenterY, realPlaceCenterX, realPlaceCenterY, false);
                                targetX = targetResult[0];
                                targetY = targetResult[1];//弧的终点坐标

                                Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//                                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                                graphics2D.setStroke(new BasicStroke(2));
//                                graphics2D.setColor(Color.BLACK);
//                                // TODO: 当前弧是不带箭头的，先带个圆吧
//                                graphics2D.drawLine(sourceX, sourceY, targetX, targetY);
//                                graphics2D.fillOval(targetX - 2, targetY - 2, 1, 1);

                                Connectable source = drawPanel.getPetriNet().getTransitions().get(i);
                                Connectable target = drawPanel.getPetriNet().getRealPlaces().get(j);
                                new ArcLabelFunctionDialog(source, target, sourceX, sourceY, targetX, targetY, true, drawPanel, graphics2D);

                                drawPanel.setArcIndex(drawPanel.getArcIndex() + 1);
                            }
                        }

                        if (!targetDetermined) {
                            ArrayList<VirtualPlaceView> virtualPlaceViews = drawPanel.getPetriNet().getVirtualPlaceViews();
                            for (int j = 0; j < virtualPlaceViews.size(); j++) {
                                if (virtualPlaceViews.get(j).containsCursor(targetX, targetY)) {//源是变迁，目是虚库所
                                    targetDetermined = true;
                                    int virtualPlaceCenterX = virtualPlaceViews.get(j).getShapeCenterX();
                                    int virtualPlaceCenterY = virtualPlaceViews.get(j).getShapeCenterY();

                                    /*————debug————*/
                                    System.out.println("sourceX before calculation is " + sourceX);
                                    System.out.println("sourceY before calculation is " + sourceY);
                                    /*————debug————*/

                                    int[] sourceResult = calcArcSrcLoc(transitionCenterX, transitionCenterY, virtualPlaceCenterX, virtualPlaceCenterY, true);
                                    sourceX = sourceResult[0];
                                    sourceY = sourceResult[1];//弧的起点坐标

                                    /*————debug————*/
                                    System.out.println("sourceX after calculation is " + sourceX);
                                    System.out.println("sourceY after calculation is " + sourceY);
                                    /*————debug————*/

                                    int[] targetResult = calcArcTarLoc(transitionCenterX, transitionCenterY, virtualPlaceCenterX, virtualPlaceCenterY, false);
                                    targetX = targetResult[0];
                                    targetY = targetResult[1];//弧的终点坐标

                                    Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//                                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                                    Stroke dash = new BasicStroke(2f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND, 1.5f, new float[] { 5, 5, },0f);
//                                    graphics2D.setStroke(dash);
//                                    graphics2D.setColor(Color.BLACK);
//                                    // TODO: 当前弧是不带箭头的，先带个圆吧
//                                    graphics2D.drawLine(sourceX, sourceY, targetX, targetY);
//                                    graphics2D.fillOval(targetX - 2, targetY - 2, 1, 1);

                                    Connectable source = drawPanel.getPetriNet().getTransitions().get(i);
                                    Connectable target = drawPanel.getPetriNet().getVirtualPlaces().get(j);
                                    new ArcLabelFunctionDialog(source, target, sourceX, sourceY, targetX, targetY, false, drawPanel, graphics2D);

                                    drawPanel.setArcIndex(drawPanel.getArcIndex() + 1);
                                }
                            }
                        }
                    }
                }

                if (!sourceDetermined) {
                    ArrayList<RealPlaceView> realPlaceViews = drawPanel.getPetriNet().getRealPlaceViews();
                    for (int i = 0; i < realPlaceViews.size(); i++) {
                        if (realPlaceViews.get(i).containsCursor(sourceX, sourceY)) {//源是实库所
                            sourceDetermined = true;
                            int realPlaceCenterX = realPlaceViews.get(i).getShapeCenterX();
                            int realPlaceCenterY = realPlaceViews.get(i).getShapeCenterY();

//                            ArrayList<TransitionView> transitionViews = drawPanel.getPetriNet().getTransitionViews();//前面已有
                            for (int j = 0; j < transitionViews.size(); j++) {
                                if (transitionViews.get(j).containsCursor(targetX, targetY)) {//源是实库所，目是变迁
                                    targetDetermined = true;
                                    int transitionCenterX = transitionViews.get(j).getShapeCenterX();
                                    int transitionCenterY = transitionViews.get(j).getShapeCenterY();

                                    /*————debug————*/
                                    System.out.println("sourceX before calculation is " + sourceX);
                                    System.out.println("sourceY before calculation is " + sourceY);
                                    /*————debug————*/

                                    int[] sourceResult = calcArcSrcLoc(realPlaceCenterX, realPlaceCenterY, transitionCenterX, transitionCenterY, false);
                                    sourceX = sourceResult[0];
                                    sourceY = sourceResult[1];//弧的起点坐标

                                    /*————debug————*/
                                    System.out.println("sourceX after calculation is " + sourceX);
                                    System.out.println("sourceY after calculation is " + sourceY);
                                    /*————debug————*/

                                    int[] targetResult = calcArcTarLoc(realPlaceCenterX, realPlaceCenterY, transitionCenterX, transitionCenterY, true);
                                    targetX = targetResult[0];
                                    targetY = targetResult[1];//弧的终点坐标

                                    Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//                                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                                    graphics2D.setStroke(new BasicStroke(2));
//                                    graphics2D.setColor(Color.BLACK);
//                                    // TODO: 当前弧是不带箭头的，先带个圆吧
//                                    graphics2D.drawLine(sourceX, sourceY, targetX, targetY);
//                                    graphics2D.fillOval(targetX - 2, targetY - 2, 1, 1);

                                    Connectable source = drawPanel.getPetriNet().getRealPlaces().get(i);
                                    Connectable target = drawPanel.getPetriNet().getTransitions().get(j);
                                    new ArcLabelFunctionDialog(source, target, sourceX, sourceY, targetX, targetY, true, drawPanel, graphics2D);

                                    drawPanel.setArcIndex(drawPanel.getArcIndex() + 1);
                                }
                            }
                        }
                    }
                }

                if (!sourceDetermined) {
                    ArrayList<VirtualPlaceView> virtualPlaceViews = drawPanel.getPetriNet().getVirtualPlaceViews();
                    for (int i = 0; i < virtualPlaceViews.size(); i++) {
                        if (virtualPlaceViews.get(i).containsCursor(sourceX, sourceY)) {//源是虚库所

                            // TODO: 下面的内容是可以提取出函数的

                            sourceDetermined = true;
                            int virtualPlaceCenterX = virtualPlaceViews.get(i).getShapeCenterX();
                            int virtualPlaceCenterY = virtualPlaceViews.get(i).getShapeCenterY();

//                            ArrayList<TransitionView> transitionViews = drawPanel.getPetriNet().getTransitionViews();//前面已有
                            for (int j = 0; j < transitionViews.size(); j++) {
                                if (transitionViews.get(j).containsCursor(targetX, targetY)) {//源是虚库所，目是变迁
                                    targetDetermined = true;
                                    int transitionCenterX = transitionViews.get(j).getShapeCenterX();
                                    int transitionCenterY = transitionViews.get(j).getShapeCenterY();

                                    /*————debug————*/
                                    System.out.println("sourceX before calculation is " + sourceX + " 且源是虚库所");
                                    System.out.println("sourceY before calculation is " + sourceY);
                                    /*————debug————*/

                                    int[] sourceResult = calcArcSrcLoc(virtualPlaceCenterX, virtualPlaceCenterY, transitionCenterX, transitionCenterY, false);
                                    sourceX = sourceResult[0];
                                    sourceY = sourceResult[1];//弧的起点坐标

                                    /*————debug————*/
                                    System.out.println("sourceX after calculation is " + sourceX);
                                    System.out.println("sourceY after calculation is " + sourceY);
                                    /*————debug————*/

                                    int[] targetResult = calcArcTarLoc(virtualPlaceCenterX, virtualPlaceCenterY, transitionCenterX, transitionCenterY, true);
                                    targetX = targetResult[0];
                                    targetY = targetResult[1];//弧的终点坐标

                                    Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
//                                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                                    Stroke dash = new BasicStroke(2f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND, 1.5f, new float[] { 5, 5, },0f);
//                                    graphics2D.setStroke(dash);
//                                    graphics2D.setColor(Color.BLACK);
//                                    // TODO: 当前弧是不带箭头的，先带个圆吧
//                                    graphics2D.drawLine(sourceX, sourceY, targetX, targetY);
//                                    graphics2D.fillOval(targetX - 2, targetY - 2, 1, 1);

                                    Connectable source = drawPanel.getPetriNet().getVirtualPlaces().get(i);//bug fixed
                                    Connectable target = drawPanel.getPetriNet().getTransitions().get(j);
                                    new ArcLabelFunctionDialog(source, target, sourceX, sourceY, targetX, targetY, false, drawPanel, graphics2D);

                                    drawPanel.setArcIndex(drawPanel.getArcIndex() + 1);
                                }
                            }
                        }
                    }
                }

                drawPanel.setMouseClickCountAboutArc(0);

            }

//            drawPanel.setMouseClickCountAboutArc(0);//放这里不对

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        /*debug*/
//        String seqOper = ToolFrame.getSeqOperIcon();
//
//
//        System.out.println("mouseClicked1" + seqOper);
//
//        if (seqOper.equals("RealPlace")) {
//
//            System.out.println("mouseClicked2" + seqOper);
//
//        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /*方法*/
    public int[] calcArcSrcLoc(int sourceX, int sourceY, int targetX, int targetY, boolean isTransition) {//这里的判断逻辑还是要好好理一理
        int[] result = new int[2];//这里必须要初始化，设置为null是不行的
        if((targetY - sourceY >= sourceX - targetX)&&(targetY - sourceY >= targetX - sourceX))
        {
            /*取底边中点*/
            result[0] = sourceX;
            result[1] = sourceY + 15;

            /*————debug————*/
            System.out.println("\n底边中点");
            /*————debug————*/
        }
        else if((sourceX - targetX >= sourceY - targetY)&&(sourceX - targetX >= targetY - sourceY))
        {
            /*取左边中点*/
            if (isTransition) {
                result[0] = sourceX - 5;
            } else {
                result[0] = sourceX - 15;
            }
            result[1] = sourceY;

            /*————debug————*/
            System.out.println("\n左边中点");
            /*————debug————*/
        }
        else if((sourceY - targetY >= sourceX - targetX)&&(sourceY - targetY >= targetX - sourceX))
        {
            /*取顶边中点*/
            result[0] = sourceX;
            result[1] = sourceY - 15;

            /*————debug————*/
            System.out.println("\n顶边中点");
            /*————debug————*/
        }
        else if((targetX - sourceX >= sourceY - targetY)&&(targetX - sourceX >= targetY - sourceY))
        {
            /*取右边中点*/
            if (isTransition) {
                result[0] = sourceX + 5;
            } else {
                result[0] = sourceX + 15;
            }
            result[1] = sourceY;

            /*————debug————*/
            System.out.println("\n右边中点");
            /*————debug————*/
        }

        /*————debug————*/
        System.out.println("original X is " + sourceX + " while newly is " + result[0]);
        System.out.println("original Y is " + sourceY + " while newly is " + result[1]);
        /*————debug————*/

        return result;
    }

    public int[] calcArcTarLoc(int sourceX, int sourceY, int targetX, int targetY, boolean isTransition) {//取反
        return calcArcSrcLoc(targetX, targetY, sourceX, sourceY, isTransition);
    }


}
