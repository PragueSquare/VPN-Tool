package vpntool.analysis;

import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;

import javax.swing.*;

public class OKSGenerationDialog extends JDialog {
    private PetriNet petriNet;

    public OKSGenerationDialog() {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        if (petriNet.getRealPlaces().size() == 0) {
            JOptionPane.showMessageDialog(null, "请完成VPN的绘制和输入", "提示", JOptionPane.WARNING_MESSAGE, null);
        } else if (petriNet.getInputMatrix() == null) {//因为后续算法实现要用到输入矩阵
            JOptionPane.showMessageDialog(null, "请先生成关联矩阵", "提示", JOptionPane.WARNING_MESSAGE, null);
        } else {
            this.setTitle("Generate Optimized Kripke Structure");
            this.setSize(800, 600);
//            OKSShowPane oksShowPane = new OKSShowPane();
            OKSShowPane2 oksShowPane = new OKSShowPane2();
            this.add(oksShowPane);
            this.setVisible(true);
        }
    }

}
