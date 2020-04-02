package vpntool.gui;

import vpntool.analysis.*;
import vpntool.utils.tree.CommonJTreeMethods;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class AnalysisPane extends JPanel {
    private JTree analysisItem;

    public AnalysisPane() {
        this.setBackground(Color.WHITE);

        DefaultMutableTreeNode upperNode = new DefaultMutableTreeNode("VPN Modeling and Analysis");
        analysisItem = new JTree(upperNode);
        // TODO: 可以再分为生成和检测两部分
        DefaultMutableTreeNode matrixNode = new DefaultMutableTreeNode("Generate Incidence Matrix");
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Generate Configuration Tree");
        DefaultMutableTreeNode graphNode = new DefaultMutableTreeNode("Generate Configuration Graph");
        DefaultMutableTreeNode detectionNode = new DefaultMutableTreeNode("Deadlock Detection");
        DefaultMutableTreeNode kripkeStructureNode = new DefaultMutableTreeNode("Generate Kripke Structure");
        DefaultMutableTreeNode optimizedKSNode = new DefaultMutableTreeNode("Generate Optimized Kripke Structure");
        upperNode.add(matrixNode);
        upperNode.add(treeNode);
        upperNode.add(graphNode);
        upperNode.add(detectionNode);
        upperNode.add(kripkeStructureNode);
        upperNode.add(optimizedKSNode);

        analysisItem.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) analysisItem.getLastSelectedPathComponent();

                if (selectedNode == matrixNode) {
                    new IMGeneration();
                } else if (selectedNode == treeNode) {
                    new CTGenerationDialog();
                } else if (selectedNode == graphNode) {
                    new CGGeneration();
                } else if (selectedNode == detectionNode) {
                    new DeadlockDetection();
                } else if (selectedNode == kripkeStructureNode) {
                    new KSGenerationDialog();
                } else if (selectedNode == optimizedKSNode) {
                    new PropositionSelectionDialog();
                }
            }
        });

        this.add(analysisItem);
        CommonJTreeMethods.expandAllRows(analysisItem, 0, analysisItem.getRowCount());
    }
}
