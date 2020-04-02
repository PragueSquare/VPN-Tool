package vpntool.models.analysisMethods;

import vpntool.models.Place;
import vpntool.models.RealPlace;
import vpntool.models.Transition;
import vpntool.models.VirtualPlace;
import vpntool.utils.math.Matrix;

import java.util.ArrayList;

public class IncidenceMatrix extends Matrix {
    private ArrayList<Place> places;
    private ArrayList<RealPlace> realPlaces;//这样设计冗余吗？其实VPN定义中是实库所
    private ArrayList<VirtualPlace> virtualPlaces;
    private ArrayList<Transition> transitions;

}
