package vpntool.gui;

import javax.swing.*;

/*awt包中也有ScrollPane类，所以此类这样命名合适吗？*/
/*要不改成ScrollPaneWithDrawPanel？*/
public class ScrollPane extends JScrollPane {
    private DrawPanel drawPanel;

    public ScrollPane(DrawPanel drawPanel) {
        super(drawPanel);//这个super是必须加的！！！因为是有参构造方法

        this.drawPanel = drawPanel;

        /*debug*/
//        System.out.println("scrollPane");
//        System.out.println("scrollPane and the arcClickCount is " + drawPanel.getMouseClickCountAboutArc());

        /*滚动模式的设置*/
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setAutoscrolls(true);
        this.getHorizontalScrollBar().setUnitIncrement(20);
        this.getVerticalScrollBar().setUnitIncrement(20);
    }

    public DrawPanel getDrawPanel() {

        /*debug*/
//        System.out.println("scrollPane-getDrawPanel and the arcClickCount is " + drawPanel.getMouseClickCountAboutArc());

        return drawPanel;
    }

    public void setDrawPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }
}
