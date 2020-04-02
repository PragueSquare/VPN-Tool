package vpntool.models;

import java.io.Serializable;

public class Mapping implements Serializable {
    private String var;
    private String con;

    public Mapping(String var, String con) {
        this.var = var;
        this.con = con;
    }

    @Override
    public boolean equals(Object obj) {
        Mapping mappingToCompareWith = (Mapping) obj;
        if (!this.var.equals(mappingToCompareWith.getVar()) || !this.con.equals(mappingToCompareWith.getCon())) {
            return false;
        }
        return true;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }
}
