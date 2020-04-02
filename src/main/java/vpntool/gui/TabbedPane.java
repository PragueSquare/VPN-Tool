package vpntool.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

public class TabbedPane extends JTabbedPane {
    private ArrayList<ScrollPane> scrollPanes;//维护不同画板（文件）

    public TabbedPane(ScrollPane scrollPane) {
        scrollPanes = new ArrayList<ScrollPane>();
        scrollPanes.add(scrollPane);
        PaneManager.getInstance().setSelectedPane(scrollPane);
        /*利用匿名类实现监听机制*/
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ScrollPane selectedPane = getSelectedPane();
                TabbedPane.this.setSelectedComponent(selectedPane);
                PaneManager.getInstance().setSelectedPane(selectedPane);
            }
        });


        /*debug*/
//        if (scrollPane == null) {
//            System.out.println(scrollPane + " is null now");//经验证不为空
//        }
//        if (getSelectedPane() == null) {
//            System.out.println("getSelectedPane is null now");//确实为空，为空是肯定的
//        }
//        System.out.println("getSelectedIndex() is " + getSelectedIndex());//终于看出有问题了！
//        System.out.println("getPaneIndexOfScrollPanes() is " + getPaneIndexOfScrollPanes());//终于看出有问题了！

    }

    public void addToScrollPanes(ScrollPane scrollPane) {
        scrollPanes.add(scrollPane);
    }

    /*public int getPaneIndex() {

    }*/

    /*返回当前激活的选项卡*/
    public ScrollPane getSelectedPane() {
        return (ScrollPane) TabbedPane.this.getSelectedComponent();
//        return PaneManager.getInstance().getSelectedPane();//这样实现也行？
    }

    /*Getter and Setter*/
    public ArrayList<ScrollPane> getScrollPanes() {
        return scrollPanes;
    }

    public void setScrollPanes(ArrayList<ScrollPane> scrollPanes) {
        this.scrollPanes = scrollPanes;
    }

    /*debug*/
    public int getPaneIndexOfScrollPanes() {
        return TabbedPane.this.getSelectedIndex();
    }
    public int getPaneIndexOfScrollPanes2(ScrollPane s) {
        return scrollPanes.indexOf(s);
    }
    public ScrollPane getSelectedPane2() {
        return PaneManager.getInstance().getSelectedPane();
    }
}
