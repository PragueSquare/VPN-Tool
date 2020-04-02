package vpntool.analysis;

import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;

import javax.swing.*;

public class CTGenerationDialog extends JDialog {
    private PetriNet petriNet;

    public CTGenerationDialog() {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        if (petriNet.getRealPlaces().size() == 0) {
            JOptionPane.showMessageDialog(null, "请完成VPN的绘制和输入", "提示", JOptionPane.WARNING_MESSAGE, null);
        } else if (petriNet.getInputMatrix() == null) {//因为后续算法实现要用到输入矩阵
            JOptionPane.showMessageDialog(null, "请先生成关联矩阵", "提示", JOptionPane.WARNING_MESSAGE, null);
        } else {
            this.setTitle("Generate Configuration Tree");
            this.setSize(800, 600);
            CTShowPane ctShowPane = new CTShowPane();
            this.add(ctShowPane);
            this.setVisible(true);
        }
    }

}
