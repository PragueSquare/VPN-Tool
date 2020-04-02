package vpntool.models.analysisMethods.kripkeStructure;

import java.io.Serializable;

public class APlTerm implements Serializable {
    private String con;
    private String var;
    private String oper;

    public APlTerm(String con, String var, String oper) {
        this.con = con;
        this.var = var;
        this.oper = oper;
    }

    @Override
    public boolean equals(Object obj) {
        APlTerm aPlTermToCompareWith = (APlTerm) obj;
        if (!this.con.equals(aPlTermToCompareWith.getCon()) || !this.var.equals(aPlTermToCompareWith.getVar()) || !this.oper.equals(aPlTermToCompareWith.getOper())) {
            return false;
        }
        return true;
    }

    /*这里不重写hashCode方法没问题？*/
//    @Override
//    public int hashCode() {
//        return 1;
//    }

    /*Getter and Setter*/

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }
}
