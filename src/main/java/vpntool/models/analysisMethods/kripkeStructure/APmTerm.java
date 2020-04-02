package vpntool.models.analysisMethods.kripkeStructure;

import vpntool.models.Token;

import java.io.Serializable;
import java.util.ArrayList;

public class APmTerm implements Serializable {
    private String place;
    private Token token;
    private int num;

    public APmTerm(String place, Token token, int num) {
        this.place = place;
        this.token = token;
        this.num = num;
    }

    @Override
    public boolean equals(Object obj) {
        APmTerm aPmTermToCompareWith = (APmTerm) obj;
        if (!this.place.equals(aPmTermToCompareWith.getPlace()) || !this.token.equals(aPmTermToCompareWith.getToken()) || !(this.num == (aPmTermToCompareWith.getNum()))) {
            return false;
        }
        return true;//放到else中效率是否会提高？
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
