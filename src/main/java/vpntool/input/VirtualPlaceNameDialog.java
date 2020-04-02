package vpntool.input;

import vpntool.gui.DrawPanel;
import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;
import vpntool.models.VirtualPlace;
import vpntool.views.VirtualPlaceView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VirtualPlaceNameDialog extends JDialog {
    private PetriNet petriNet;

    public VirtualPlaceNameDialog(int shapeCenterX, int shapeCenterY, DrawPanel drawPanel, Graphics2D graphics2D) {//传参要考虑创建库所对象的参数
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();
//        petriNet = drawPanel.getPetriNet();//考虑一下这句和上面是否等效？？？
        ArrayList<String> actualVariables = petriNet.getVariables();
        ArrayList<String> variables = new ArrayList<>();
        variables.addAll(actualVariables);
        variables.remove(variables.size() - 1);//不要展示普通token的变量

        int virtualPlaceIndex = drawPanel.getVirtualPlaceIndex();

        this.setTitle("请选择虚库所的名字");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel namePanel = new JPanel();
        for (int i = 0; i < variables.size(); i++) {
            String virtualPlaceName = variables.get(i);
            JButton nameButton = new JButton(virtualPlaceName);
            nameButton.addActionListener(new ActionListener() {//不用考虑final的问题了？
                @Override
                public void actionPerformed(ActionEvent e) {
                    petriNet.getVirtualPlaces().add(virtualPlaceIndex, new VirtualPlace(virtualPlaceIndex, virtualPlaceName));
                    VirtualPlaceView virtualPlaceView = new VirtualPlaceView(virtualPlaceIndex, virtualPlaceName, shapeCenterX, shapeCenterY);
                    petriNet.getVirtualPlaceViews().add(virtualPlaceIndex, virtualPlaceView);
                    virtualPlaceView.shapeDrawing(graphics2D);

                    drawPanel.repaint();//有必要repaint吗？

                    dispose();
                }
            });
            namePanel.add(nameButton);
        }
        this.add(namePanel);

//        drawPanel.setVirtualPlaceIndex(virtualPlaceIndex + 1);//这样有问题吗？

        this.setAlwaysOnTop(true);//有必要设置吗？
        this.setVisible(true);
    }

}

