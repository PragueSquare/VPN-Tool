package vpntool.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Configuration implements Serializable {
    private Marking marking;
    private ArrayList<RealPlace> realPlaces;
    private ArrayList<String> constraintFunctions;
}
