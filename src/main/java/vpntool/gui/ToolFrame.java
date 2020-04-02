package vpntool.gui;

import vpntool.input.ConstantAndVariableDialog;
import vpntool.input.ConstraintFunctionDialog;
import vpntool.models.PetriNet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class ToolFrame extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu, inputMenu, drawMenu, editMenu, helpMenu;
    private JMenuItem newItem, openItem, closeItem, saveItem, saveAsItem, graphShotItem;// TODO: 提醒可设置为局部变量
    private JMenuItem constantAndVariableItem, constraintFunctionItem, guardFunctionItem, linkFunctionItem;
    private JMenuItem realPlaceItem, virtualPlaceItem, transitionItem, arcItem, tokenItem;
    private JMenuItem chooseItem, dragItem, deleteItem;
    private JMenuItem helpItem, aboutItem;
    private JToolBar toolBar;
    private JButton newButton, openButton, closeButton, saveButton, saveAsButton, graphShotButton;//之前明明用到了为啥还提示没用到。。可能也是形参实参的问题吧
    private JButton constantAndVariableButton, constraintFunctionButton, guardFunctionButton, linkFunctionButton;
    private JButton realPlaceButton, virtualPlaceButton, transitionButton, arcButton, tokenButton;
    private JButton chooseButton, dragButton, deleteButton;
    private JButton helpButton, aboutButton;
    private JSplitPane mainSplitPane;//分为左侧选项栏和右侧画板
    private TabbedPane tabbedPane;//选项卡面板其实维护了多个面板
    private int tabCount = 0;//选项卡数量。注意打开工具时自动创建一个选项卡

    private static String seqOperIcon;//用来记录序列操作的第一下点击（选择操作类型）

    public ToolFrame() {
        this.setTitle("VPN Tool by GT");
        Image toolIcon = Toolkit.getDefaultToolkit().getImage("src/main/resources/images/GT.png");//相对路径可以省略到项目名
        this.setIconImage(toolIcon);//还可以放图标
        this.setSize(800, 600);//大小暂时先设置成绝对值
        this.setLocationRelativeTo(null);

        this.buildMenuBar();
        this.buildToolBar();
        this.buildSplitPane();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public void buildMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        buildFileMenu();
        inputMenu = new JMenu("Input");
        buildInputMenu();
        drawMenu = new JMenu("Draw");
        buildDrawMenu();
        editMenu = new JMenu("Edit");
        buildEditMenu();
        helpMenu = new JMenu("Help");
        buildHelpMenu();
        menuBar.add(fileMenu);
        menuBar.add(inputMenu);
        menuBar.add(drawMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);//菜单栏是内置的，直接set
        menuBar.setVisible(true);//还需要设置可见吗？
    }

    /*MenuBar*/
    public void buildFileMenu() {
        newItem = new JMenuItem("New");
        newItem.setIcon(new ImageIcon("src/main/resources/images/file/New.png"));
        newItem.addActionListener(new NewListener());
        openItem = new JMenuItem("Open");
        openItem.setIcon(new ImageIcon("src/main/resources/images/file/Open.png"));
        openItem.addActionListener(new OpenListener());
        closeItem = new JMenuItem("Close");
        closeItem.setIcon(new ImageIcon("src/main/resources/images/file/Close.png"));
        closeItem.addActionListener(new CloseListener());
        saveItem = new JMenuItem("Save");
        saveItem.setIcon(new ImageIcon("src/main/resources/images/file/Save.png"));
        saveItem.addActionListener(new SaveListener());
        saveAsItem = new JMenuItem("Save as");
        saveAsItem.setIcon(new ImageIcon("src/main/resources/images/file/Save as.png"));
        saveAsItem.addActionListener(new SaveAsListener());
        graphShotItem = new JMenuItem("GraphShot");
        graphShotItem.setIcon(new ImageIcon("src/main/resources/images/file/GraphShot.png"));
        graphShotItem.addActionListener(new GraphShotListener());
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(closeItem);
        fileMenu.addSeparator();//注意分割线的设置
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(graphShotItem);
    }

    public void buildInputMenu() {
        constantAndVariableItem = new JMenuItem("Constant and Variable");
        constantAndVariableItem.setIcon(new ImageIcon("src/main/resources/images/input/Constant and Variable2.png"));
        constantAndVariableItem.addActionListener(new ConstantAndVariableListener());
        constraintFunctionItem = new JMenuItem("Constraint Function");
        constraintFunctionItem.setIcon(new ImageIcon("src/main/resources/images/input/Constraint Function2.png"));
        constraintFunctionItem.addActionListener(new ConstraintFunctionListener());
        guardFunctionItem = new JMenuItem("Guard Function");
        guardFunctionItem.setIcon(new ImageIcon("src/main/resources/images/input/Guard Function2.png"));
        guardFunctionItem.addActionListener(new SeqOperListener("GuardFunction"));//icon按照开头大写处理合适吗？
        linkFunctionItem = new JMenuItem("Link Function");
        linkFunctionItem.setIcon(new ImageIcon("src/main/resources/images/input/Link Function2.png"));
        linkFunctionItem.addActionListener(new SeqOperListener("LinkFunction"));
        inputMenu.add(constantAndVariableItem);
        inputMenu.add(constraintFunctionItem);
        inputMenu.addSeparator();
        inputMenu.add(guardFunctionItem);
        inputMenu.add(linkFunctionItem);
    }

    public void buildDrawMenu() {
        realPlaceItem = new JMenuItem("Real Place");
        realPlaceItem.setIcon(new ImageIcon("src/main/resources/images/draw/Real Place.png"));
        realPlaceItem.addActionListener(new SeqOperListener("RealPlace"));
        virtualPlaceItem = new JMenuItem("Virtual Place");
        virtualPlaceItem.setIcon(new ImageIcon("src/main/resources/images/draw/Virtual Place.png"));
        virtualPlaceItem.addActionListener(new SeqOperListener("VirtualPlace"));
        transitionItem = new JMenuItem("Transition");
        transitionItem.setIcon(new ImageIcon("src/main/resources/images/draw/Transition.png"));
        transitionItem.addActionListener(new SeqOperListener("Transition"));
        arcItem = new JMenuItem("Arc");
        arcItem.setIcon(new ImageIcon("src/main/resources/images/draw/Arc.png"));
        arcItem.addActionListener(new SeqOperListener("Arc"));
        tokenItem = new JMenuItem("Token");
        tokenItem.setIcon(new ImageIcon("src/main/resources/images/draw/Token.png"));
        tokenItem.addActionListener(new SeqOperListener("Token"));
        drawMenu.add(realPlaceItem);
        drawMenu.add(virtualPlaceItem);
        drawMenu.add(transitionItem);
        drawMenu.add(arcItem);
        drawMenu.add(tokenItem);
    }

    public void buildEditMenu() {
        chooseItem = new JMenuItem("Choose");
        chooseItem.setIcon(new ImageIcon("src/main/resources/images/edit/Choose.png"));
        chooseItem.addActionListener(new SeqOperListener("Choose"));
        dragItem = new JMenuItem("Drag");
        dragItem.setIcon(new ImageIcon("src/main/resources/images/edit/Drag.png"));
        dragItem.addActionListener(new SeqOperListener("Drag"));
        deleteItem = new JMenuItem("Delete");
        deleteItem.setIcon(new ImageIcon("src/main/resources/images/edit/Delete.png"));
        deleteItem.addActionListener(new SeqOperListener("Delete"));
        editMenu.add(chooseItem);
        editMenu.add(dragItem);
        editMenu.add(deleteItem);
    }

    public void buildHelpMenu() {
        helpItem = new JMenuItem("Help");
        helpItem.setIcon(new ImageIcon("src/main/resources/images/help/Help.png"));
        helpItem.addActionListener(new HelpListener());
        aboutItem = new JMenuItem("About");
        aboutItem.setIcon(new ImageIcon("src/main/resources/images/help/About.png"));
        aboutItem.addActionListener(new AboutListener());
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);
    }

    /*ToolBar*/
    public void buildToolBar() {
        newButton = buildToolBarItem(newButton, "src/main/resources/images/file/New.png", "New", 20, 20, new NewListener());
        openButton = buildToolBarItem(openButton, "src/main/resources/images/file/Open.png", "Open", 20, 20, new OpenListener());
        closeButton = buildToolBarItem(closeButton, "src/main/resources/images/file/Close.png", "Close", 20, 20, new CloseListener());
        saveButton = buildToolBarItem(saveButton, "src/main/resources/images/file/Save.png", "Save", 20, 20, new SaveListener());
        saveAsButton = buildToolBarItem(saveAsButton, "src/main/resources/images/file/Save as.png", "Save as", 20, 20, new SaveAsListener());
        graphShotButton = buildToolBarItem(graphShotButton, "src/main/resources/images/file/GraphShot.png", "GraphShot", 20, 20, new GraphShotListener());
        constantAndVariableButton = buildToolBarItem(constantAndVariableButton, "src/main/resources/images/input/Constant and Variable2.png", "Constant and Variable", 20, 20, new ConstantAndVariableListener());
        constraintFunctionButton = buildToolBarItem(constraintFunctionButton, "src/main/resources/images/input/Constraint Function2.png", "Constraint Function", 20, 20, new ConstraintFunctionListener());
        guardFunctionButton = buildToolBarItem(guardFunctionButton, "src/main/resources/images/input/Guard Function2.png", "Guard Function", 20, 20, new SeqOperListener("GuardFunction"));
        linkFunctionButton = buildToolBarItem(linkFunctionButton, "src/main/resources/images/input/Link Function2.png", "Link Function", 20, 20, new SeqOperListener("LinkFunction"));
        realPlaceButton = buildToolBarItem(realPlaceButton, "src/main/resources/images/draw/Real Place.png", "Real Place", 20, 20, new SeqOperListener("RealPlace"));
        virtualPlaceButton = buildToolBarItem(virtualPlaceButton, "src/main/resources/images/draw/Virtual Place.png", "Virtual Place", 20, 20, new SeqOperListener("VirtualPlace"));
        transitionButton = buildToolBarItem(transitionButton, "src/main/resources/images/draw/Transition.png", "Transition", 20, 20, new SeqOperListener("Transition"));
        arcButton = buildToolBarItem(arcButton, "src/main/resources/images/draw/Arc.png", "Arc", 20, 20, new SeqOperListener("Arc"));
        tokenButton = buildToolBarItem(tokenButton, "src/main/resources/images/draw/Token.png", "Token", 20, 20, new SeqOperListener("Token"));
        chooseButton = buildToolBarItem(chooseButton, "src/main/resources/images/edit/Choose.png", "Choose", 20, 20, new SeqOperListener("Choose"));
        dragButton = buildToolBarItem(dragButton, "src/main/resources/images/edit/Drag.png", "Drag", 20, 20, new SeqOperListener("Drag"));
        deleteButton = buildToolBarItem(deleteButton, "src/main/resources/images/edit/Delete.png", "Delete", 20, 20, new SeqOperListener("Delete"));
        helpButton = buildToolBarItem(helpButton, "src/main/resources/images/help/Help.png", "Help", 20, 20, new HelpListener());
        aboutButton = buildToolBarItem(aboutButton, "src/main/resources/images/help/About.png", "About", 20, 20, new AboutListener());
        toolBar = new JToolBar();
        toolBar.setFloatable(false);//工具栏暂时设为不可拖动
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(closeButton);
        toolBar.add(saveButton);
        toolBar.add(saveAsButton);
        toolBar.add(graphShotButton);
        toolBar.addSeparator();
        toolBar.add(constantAndVariableButton);
        toolBar.add(constraintFunctionButton);
        toolBar.add(guardFunctionButton);
        toolBar.add(linkFunctionButton);
        toolBar.addSeparator();
        toolBar.add(realPlaceButton);
        toolBar.add(virtualPlaceButton);
        toolBar.add(transitionButton);
        toolBar.add(arcButton);
        toolBar.add(tokenButton);
        toolBar.addSeparator();
        toolBar.add(chooseButton);
        toolBar.add(dragButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(helpButton);
        toolBar.add(aboutButton);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);//工具栏不同于菜单栏，需要add
        toolBar.setVisible(true);//工具栏需要设置可见吗？
    }

    /*虽然标签大小初步都定为20*20，但后续可能不一样，为了灵活还是传参width和height*/
    /*难道JButton也有形参和实参的问题？？？！！！*/
//    public void buildToolBarItem(JButton button, String imageIcon, String toolTipText, int width, int height, ActionListener actionListener) {
//        button = new JButton(new ImageIcon(imageIcon));
//        button.setToolTipText(toolTipText);
//        button.setSize(width, height);
//        button.addActionListener(actionListener);
//    }

    public JButton buildToolBarItem(JButton button, String imageIcon, String toolTipText, int width, int height, ActionListener actionListener) {
        button = new JButton(new ImageIcon(imageIcon));
        button.setToolTipText(toolTipText);
        button.setSize(width, height);
        button.addActionListener(actionListener);
        return  button;
    }

    /*SplitPane*/
    public void buildSplitPane() {
        mainSplitPane = new JSplitPane();
        mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(200);//暂时设为绝对值
        mainSplitPane.setLeftComponent(new AnalysisPane());//主面板左侧
        ScrollPane firstScrollPane = new ScrollPane(new DrawPanel());
        tabbedPane = new TabbedPane(firstScrollPane);



        /*debug*/
//        if (tabbedPane.getSelectedPane() == null) {
//            System.out.println("getSelectedPane is null after buildSplitPane()1");//空
//        }
//        if (tabbedPane.getSelectedPane2() == null) {
//            System.out.println("getSelectedPane is null after buildSplitPane()111");//不空
//        }
//        System.out.println(firstScrollPane.getDrawPanel().getColorModel());//是白色，不是蓝色

        tabCount++;
        tabbedPane.add("Petri Net 1", firstScrollPane);

        /*debug*/
//        if (tabbedPane.getSelectedPane() == null) {
//            System.out.println("getSelectedPane is null after buildSplitPane()2");//不空
//        }

        tabbedPane.setSelectedComponent(firstScrollPane);//内置操作

        /*需要同步维护吗？应该不需要，因为在tabbedPane的构造函数中就已经同步了*/
        /*但怎么加上也没变化？？？应该是有变化的*/
//        tabbedPane.addToScrollPanes(firstScrollPane);
////        tabbedPane.addToScrollPanes(new ScrollPane(new DrawPanel()));
//        PaneManager.getInstance().setSelectedPane(firstScrollPane);//同步内置操作
//        ScrollPane t = new ScrollPane(new DrawPanel());
//        PaneManager.getInstance().setSelectedPane(t);
//        System.out.println(tabbedPane.getScrollPanes().size());//2

        mainSplitPane.setRightComponent(tabbedPane);//主面板右侧
        this.getContentPane().add(mainSplitPane, BorderLayout.CENTER);//留出来SOUTH放动作提示栏

        /*debug*/
//        if (tabbedPane.getSelectedPane() == null) {
//            System.out.println("getSelectedPane is null after buildSplitPane()");//不空
//        }
//        if (tabbedPane.getSelectedComponent() == null) {
//            System.out.println("getSelectedComponent is null after buildSplitPane()");//不空
//        }

        System.out.println("getSelectedIndex() is " + tabbedPane.getSelectedIndex());
        System.out.println("getPaneIndexOfScrollPanes() is " + tabbedPane.getPaneIndexOfScrollPanes2(firstScrollPane));//这样一验证没问题了？
        if (tabbedPane.getSelectedPane() == tabbedPane.getSelectedPane2()) {
            System.out.println("right");
        }
    }


    /*利用内部类实现监听机制*/
    /*File*/
    class NewListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tabCount++;
            String tabTitle = "Petri Net " + tabCount;

            DrawPanel drawPanel = new DrawPanel();
            ScrollPane scrollPane = new ScrollPane(drawPanel);

            /*感觉下面一二句和三四句好像一个意思。但是区别在哪里呢？*/
            tabbedPane.addToScrollPanes(scrollPane);
            PaneManager.getInstance().setSelectedPane(scrollPane);
            tabbedPane.add(tabTitle, scrollPane);
            tabbedPane.setSelectedComponent(scrollPane);
        }
    }

    class OpenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tabCount++;
            String tabTitle = "Petri Net " + tabCount;

            DrawPanel drawPanel = new DrawPanel();

            JFileChooser jFileChooser = new JFileChooser("d:/");//暂时默认打开D盘
            jFileChooser.showOpenDialog(null);
//            File openedFile = jFileChooser.getSelectedFile();
//            String path = openedFile.getAbsolutePath();
            String openedPath = jFileChooser.getSelectedFile().getAbsolutePath();
            PetriNet petriNet;//存所有信息

            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(openedPath));

                //利用简单的强转来读取文件
                petriNet = (PetriNet) objectInputStream.readObject();
                drawPanel.setPetriNet(petriNet);
                objectInputStream.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (ClassNotFoundException e3) {
                e3.printStackTrace();
            }

            /*和newListener中操作类似*/
            ScrollPane scrollPane = new ScrollPane(drawPanel);
            tabbedPane.addToScrollPanes(scrollPane);
            PaneManager.getInstance().setSelectedPane(scrollPane);
            tabbedPane.add(tabTitle, scrollPane);
            tabbedPane.setSelectedComponent(scrollPane);
        }
    }

    class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ScrollPane selectedPane = tabbedPane.getSelectedPane();
            int paneNumber = tabbedPane.getScrollPanes().size();
            // TODO:  这里的if逻辑
            if (tabbedPane.getScrollPanes().size() == 1) {
                // TODO: 只剩一个选项卡其实应该也是可以关的
                Object[] options = {"OK"};
                JOptionPane.showOptionDialog(tabbedPane, "已是最后一个面板，无法删除", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);//这里第一个参数parentComponent传null不好吗？
            } else if (selectedPane == tabbedPane.getScrollPanes().get(paneNumber - 1)) {//==比的是地址，equals比的是内容
                ScrollPane preSelectedPane = tabbedPane.getScrollPanes().get(paneNumber - 2);

                /*每件事都要做两遍。。*/
                tabbedPane.remove(selectedPane);
                tabbedPane.getScrollPanes().remove(selectedPane);
                tabbedPane.setSelectedComponent(preSelectedPane);
                PaneManager.getInstance().setSelectedPane(preSelectedPane);
            } else {
                ScrollPane preSelectedPane = tabbedPane.getScrollPanes().get(tabbedPane.getSelectedIndex());

                tabbedPane.remove(selectedPane);
                tabbedPane.getScrollPanes().remove(selectedPane);
                tabbedPane.setSelectedComponent(preSelectedPane);
                PaneManager.getInstance().setSelectedPane(preSelectedPane);
            }
        }
    }

    class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PetriNet petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
            if (petriNet.getSavedFile() == null) {
                JFileChooser jFileChooser = new JFileChooser("d:/");
                jFileChooser.showSaveDialog(null);
                petriNet.setSavedFile(jFileChooser.getSelectedFile().getAbsolutePath());
            }
            writePetriNetFile(petriNet);
        }
    }

    class SaveAsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PetriNet petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
            JFileChooser jFileChooser = new JFileChooser("d:/");
            jFileChooser.showSaveDialog(null);
            petriNet.setSavedFile(jFileChooser.getSelectedFile().getAbsolutePath());
            writePetriNetFile(petriNet);
        }
    }

    /*提取出saveListener和saveAsListener的公共部分*/
    public void writePetriNetFile(PetriNet petriNet) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(petriNet.getSavedFile()));
            objectOutputStream.writeObject(petriNet);
            objectOutputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    class GraphShotListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DrawPanel drawPanel = PaneManager.getInstance().getSelectedPane().getDrawPanel();
            /*必须实现绘制，而不能简单截图*/
            BufferedImage bufferedImage = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);//暂定ARGB格式
            Graphics2D graphics2D = bufferedImage.createGraphics();
            drawPanel.paint(graphics2D);
            /*文件操作*/
            JFileChooser jFileChooser = new JFileChooser("d:/");
            jFileChooser.showSaveDialog(null);
            try {
                ImageIO.write(bufferedImage, "PNG", jFileChooser.getSelectedFile());//暂定PNG
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    /*Input*/
    class ConstantAndVariableListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            if (PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet().getConstants() == null) {//输入""的情况呢？这样确实没有控制住因为构造函数中constants就初始化了
            if (PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet().getConstants().size() == 0) {
                new ConstantAndVariableDialog();
            } else {
                int result = JOptionPane.showConfirmDialog(null, "您已输入常量和变量，是否重新输入？", "常量和变量修改确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                if (result == 0) {
                    new ConstantAndVariableDialog();
                }
            }
        }
    }

    class ConstraintFunctionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet().getMapBetweenVAndC() == null) {//还未输入约束函数
                new ConstraintFunctionDialog();
            } else {
                int result = JOptionPane.showConfirmDialog(null, "您已输入约束函数，是否重新输入？", "约束函数修改确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                if (result == 0) {
                    new ConstraintFunctionDialog();
                }
            }
        }
    }


    /*Draw*/
    /*Edit*/

    class SeqOperListener implements ActionListener {
        String icon;
        public SeqOperListener(String icon) {
            this.icon = icon;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            /*强制所有序列操作之前先输入C&V*/
//            if (PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet().getConstants() == null) {//还用不用加这个条件？应该是恒为假吧？
            if (PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet().getConstants().size() == 0) {
                // TODO: 其实应该提供一个OptionDialog，有转到输入C&V的选项（另一个选项是关闭窗口）
                JOptionPane.showMessageDialog(null, "请先输入常量和变量", "提示", JOptionPane.WARNING_MESSAGE, null);
            } else {
                seqOperIcon = icon;
            }

            /*调试*/
//            System.out.println("SeqOperListener1" + seqOperIcon);
//            System.out.println("SeqOperListener2" + ToolFrame.getSeqOperIcon());

        }
    }

    /*Help*/
    // TODO: 实现help和about
    class HelpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class AboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    /*Getter and Setter*/

    public static String getSeqOperIcon() {
        return seqOperIcon;
    }

    public static void setSeqOperIcon(String seqOperIcon) {
        ToolFrame.seqOperIcon = seqOperIcon;//静态变量的特殊性：这里要用ToolFrame而不是this
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ToolFrame();
            }
        });
    }
}
