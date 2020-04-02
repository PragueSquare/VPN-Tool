package vpntool.input;

import vpntool.gui.DrawPanel;
import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;
import vpntool.models.RealPlace;
import vpntool.models.Token;
import vpntool.utils.parser.CommonStringMethods;
import vpntool.views.RealPlaceView;
import vpntool.views.TokenView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TokenDialog extends JDialog {
    private PetriNet petriNet;

    private JTextField tokenText;
    private StringBuilder tokenString;

    private ArrayList<String> constantsInToken;

    public TokenDialog(int shapeCenterX, int shapeCenterY, DrawPanel drawPanel, int placeIndex, Graphics2D graphics2D) {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
        tokenString = new StringBuilder();//注意初始化

        constantsInToken = new ArrayList<String>();//这里初始化必要吗？

        this.setTitle("请选择token中的常量");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel showPanel = new JPanel();
        showPanel.add(new Label("Composition of the token:"));
        tokenText = new JTextField(10);
        tokenText.setEditable(false);//只能选择，不能输入
        showPanel.add(tokenText);
        JPanel choosePanel = new JPanel();
        choosePanel.add(new Label("Choose please:"));
        ArrayList<String> constants = petriNet.getConstants();
        if (constants.size() != 0) {
            for (int i = 0; i < constants.size(); i++) {
                String constant = constants.get(i);
                JButton constantButton = new JButton(constant);
                constantButton.addActionListener(new ActionListener() {//不用考虑final的问题了？
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tokenString = CommonStringMethods.appendParameters(tokenString, constant);//常量也是参数
                        tokenText.setText(tokenString.toString());

                        constantsInToken.add(constant);//存入常量列表，然后传给库所
                    }
                });
                choosePanel.add(constantButton);
            }
            // TODO: 对普通token的处理
            JPanel buttonPanel = new JPanel();
            JButton confirmButton = new JButton("确认");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tokenText.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "请输入token", "提示", JOptionPane.WARNING_MESSAGE, null);
                    } else {
                        //注意这里
                        RealPlace place = petriNet.getRealPlaces().get(placeIndex);
                        int tokenIndex = place.getTokenIndex();
                        String tokenName = tokenString.toString();
                        place.getTokens().add(tokenIndex, new Token(placeIndex, tokenIndex, tokenName, constantsInToken));
                        place.setTokenIndex(tokenIndex + 1);

                        RealPlaceView placeView = petriNet.getRealPlaceViews().get(placeIndex);
                        TokenView tokenView = new TokenView(placeIndex, tokenIndex, tokenName, shapeCenterX, shapeCenterY);
                        placeView.getTokenViews().add(tokenIndex, tokenView);
                        placeView.setTokenViewIndex(tokenIndex + 1);
                        tokenView.shapeDrawing(graphics2D);

                        drawPanel.repaint();//有必要吗？
                        dispose();
                    }
                }
            });
            buttonPanel.add(confirmButton);

            this.add(showPanel, BorderLayout.NORTH);
            this.add(choosePanel, BorderLayout.CENTER);
            this.add(buttonPanel, BorderLayout.SOUTH);
            this.setVisible(true);
        } else {
            // TODO: 普通PN的情况
        }
    }
}
