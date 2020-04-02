package vpntool.input;

import vpntool.gui.DrawPanel;
import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;
import vpntool.models.RealPlace;
import vpntool.views.RealPlaceView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RealPlaceNameDialog extends JDialog {
    private PetriNet petriNet;

    public RealPlaceNameDialog(int shapeCenterX, int shapeCenterY, DrawPanel drawPanel, Graphics2D graphics2D) {//传参要考虑创建库所对象的参数
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
//        petriNet = drawPanel.getPetriNet();//考虑一下这句和上面是否等效？？？

        ArrayList<String> actualConstants = petriNet.getConstants();
//        constants.remove(constants.size() - 1);//不要展示普通token的常量
        ArrayList<String> constants = new ArrayList<>();
        constants.addAll(actualConstants);
        constants.remove(constants.size() - 1);//不要展示普通token的常量

        int realPlaceIndex = drawPanel.getRealPlaceIndex();

        this.setTitle("请选择库所的名字");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel namePanel = new JPanel();
        for (int i = 0; i < constants.size(); i++) {
            String realPlaceName = constants.get(i);
            JButton nameButton = new JButton(realPlaceName);
            nameButton.addActionListener(new ActionListener() {//不用考虑final的问题了？
                @Override
                public void actionPerformed(ActionEvent e) {
                    petriNet.getRealPlaces().add(realPlaceIndex, new RealPlace(realPlaceIndex, realPlaceName));
                    RealPlaceView realPlaceView = new RealPlaceView(realPlaceIndex, realPlaceName, shapeCenterX, shapeCenterY);
                    petriNet.getRealPlaceViews().add(realPlaceIndex, realPlaceView);
                    realPlaceView.shapeDrawing(graphics2D);

                    drawPanel.repaint();//有必要repaint吗？

                    dispose();
                }
            });
            namePanel.add(nameButton);
        }
        this.add(namePanel);

//        drawPanel.setRealPlaceIndex(realPlaceIndex + 1);//这样有问题吗？

        this.setAlwaysOnTop(true);//有必要设置吗？
        this.setVisible(true);
    }

}

