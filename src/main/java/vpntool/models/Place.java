package vpntool.models;

import java.io.Serializable;
import java.util.ArrayList;

/*在Connectable和RealPlace之间的抽象类Place的设计有意义吗？*/
public abstract class Place extends Connectable implements Serializable {


    public Place(int id, String name) {
        super(id, name);
    }
}
