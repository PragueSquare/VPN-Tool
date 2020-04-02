package vpntool.models.analysisMethods.kripkeStructure;

import java.io.Serializable;
import java.util.HashMap;

public class APtTerm implements Serializable {
    private String enabledTransition;
    private HashMap<String, String> correspondingBinding;

    public APtTerm(String enabledTransition, HashMap<String, String> correspondingBinding) {
        this.enabledTransition = enabledTransition;
        this.correspondingBinding = correspondingBinding;
    }

    @Override
    public boolean equals(Object obj) {
        APtTerm aPtTermToCompareWith = (APtTerm) obj;
        if (!this.enabledTransition.equals(aPtTermToCompareWith.getEnabledTransition()) || !this.correspondingBinding.equals(aPtTermToCompareWith.getCorrespondingBinding())) {
            return false;
        }
        return true;
    }

    /*不重写hashCode方法没问题？*/

    /*Getter and Setter*/

    public String getEnabledTransition() {
        return enabledTransition;
    }

    public void setEnabledTransition(String enabledTransition) {
        this.enabledTransition = enabledTransition;
    }

    public HashMap<String, String> getCorrespondingBinding() {
        return correspondingBinding;
    }

    public void setCorrespondingBinding(HashMap<String, String> correspondingBinding) {
        this.correspondingBinding = correspondingBinding;
    }
}
