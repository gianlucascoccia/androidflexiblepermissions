package gssi.aq.it.afpapp;

import org.apache.commons.collections15.keyvalue.MultiKey;
import org.apache.commons.collections15.map.MultiKeyMap;

class AFPUtils {

    static String modelToString(long startTimestamp, long endTimestamp, String defaultModel, MultiKeyMap<String, Integer> model){
        String str = "Start: " + startTimestamp + "; ";
        str += defaultModel + "; ";
        for (Object key : model.keySet()){
            MultiKey<String> k = (MultiKey<String>) key;
            str += k.toString() + " " +  model.get(key).toString() + "; ";
        }
        str += "End: " + endTimestamp + "; ";
        return str;

    }
}
