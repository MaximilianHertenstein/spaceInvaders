package org.example;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public  static V2 charToV2(char dir) {
        return switch (dir){
            case  'a'->  new V2(-1,0);
            case 'd'->  new V2(1,0);
            default -> new V2(0,0);
        };
    }

    public static List<StringWithLocation>  getStringsWithLocation(List<IBasicGameObject> bgos) {
        var acc = new ArrayList<StringWithLocation>();
        for (var bgo: bgos){
            acc.addAll(bgo.show());
        }
        return acc;
    }


}
