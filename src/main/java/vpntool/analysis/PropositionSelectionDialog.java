package vpntool.analysis;

import vpntool.gui.PaneManager;
import vpntool.models.PetriNet;
import vpntool.utils.parser.CommonStringMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PropositionSelectionDialog extends JDialog {
    private JTextField propositionText, variableInput;
    private PetriNet petriNet;

    private StringBuilder propositionString;

    public PropositionSelectionDialog() {
        petriNet = PaneManager.getInstance().getSelectedPane().getDrawPanel().getPetriNet();

        propositionString = new StringBuilder();

        this.setTitle("Proposition Selection");
        this.setSize(500, 200);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel propositionPanel = new JPanel();
        propositionPanel.setLayout(new BorderLayout());

        JPanel showPanel = new JPanel();
        propositionText = new JTextField(20);
        propositionText.setEditable(false);
        showPanel.add(propositionText);

        JPanel choosePanel = new JPanel();
        ArrayList<String> propositionType = new ArrayList<>();
        propositionType.add("APm");
        propositionType.add("APp");
        propositionType.add("APc");
        propositionType.add("APl");
        propositionType.add("APt");
        for (int i = 0; i < 5; i++) {
            String type = propositionType.get(i);
            JButton typeBtn = new JButton(type);
            typeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    propositionString = CommonStringMethods.appendParameters(propositionString, type);
                    propositionText.setText(propositionString.toString());
                }
            });
            choosePanel.add(typeBtn);
        }
        propositionPanel.add(showPanel, BorderLayout.NORTH);
        propositionPanel.add(choosePanel, BorderLayout.CENTER);

        JPanel confirmPanel = new JPanel();
        JButton confirmBtn = new JButton("OK");
        confirmPanel.add(confirmBtn);
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectionRes = propositionText.getText();
                if (selectionRes.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please finish the selection", "Warning", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    ArrayList<String> selectionList = CommonStringMethods.stringToArrayList(selectionRes);
                    boolean[] propositionType = petriNet.getPropositionType();
                    if (selectionList.contains("APm")) {
                        propositionType[0] = true;
                    } else {
                        propositionType[0] = false;
                    }
                    if (selectionList.contains("APp")) {
                        propositionType[1] = true;
                    } else {
                        propositionType[1] = false;
                    }
                    if (selectionList.contains("APc")) {
                        propositionType[2] = true;
                    } else {
                        propositionType[2] = false;
                    }
                    if (selectionList.contains("APl")) {
                        propositionType[3] = true;
                    } else {
                        propositionType[3] = false;
                    }
                    if (selectionList.contains("APt")) {
                        propositionType[4] = true;
                    } else {
                        propositionType[4] = false;
                    }

                    dispose();

                    new OKSGenerationDialog();
                }

            }
        });




        this.add(propositionPanel, BorderLayout.NORTH);
        this.add(confirmPanel, BorderLayout.SOUTH);



        this.setVisible(true);
    }
}
