package vpntool.models;

import java.util.ArrayList;

public class RealPlace extends Place {
    private ArrayList<Token> tokens;
    private int tokenIndex = 0;

    public RealPlace(int id, String name) {
        super(id, name);

        tokens = new ArrayList<Token>();//注意初始化
    }

    /*Gettr and Setter*/

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public int getTokenIndex() {
        return tokenIndex;
    }

    public void setTokenIndex(int tokenIndex) {
        this.tokenIndex = tokenIndex;
    }
}
