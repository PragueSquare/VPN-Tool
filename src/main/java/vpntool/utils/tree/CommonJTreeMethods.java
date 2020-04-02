package vpntool.utils.tree;

import javax.swing.*;

public class CommonJTreeMethods {

    public static void expandAllRows(JTree tree, int startIndex, int rowCount) {
        for (int i = startIndex; i < rowCount; i++) {
            tree.expandRow(i);
        }

        /*还是直接去考虑递归，不到指定行数就继续展开*/
        /*还要再想想这里的逻辑*/
        if (tree.getRowCount() != rowCount) {
            expandAllRows(tree, rowCount, tree.getRowCount());
        }
    }

}
