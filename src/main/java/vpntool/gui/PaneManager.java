package vpntool.gui;

/*单例模式*/
public class PaneManager {
    private static PaneManager instance = new PaneManager();//不初始化会报空指针异常
    private ScrollPane selectedPane;//最初叫currentPane，后来改为selectedPane，为了对应

    private PaneManager() {
//        instance = new PaneManager();//这样仍会报空指针异常
    }

    public static PaneManager getInstance() {
        return instance;
    }

    public ScrollPane getSelectedPane() {
        return selectedPane;
    }

    public void setSelectedPane(ScrollPane selectedPane) {
        this.selectedPane = selectedPane;
    }
}
