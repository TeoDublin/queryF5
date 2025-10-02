package app;

import app.objs.objQueries;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Queries {
    public static ConcurrentHashMap<String,objQueries> queriesList = new ConcurrentHashMap<>();

    public static String uniq(){
        String ret = UUID.randomUUID().toString().replace("-", "");
        while(queriesList.containsKey(ret)){
            ret = UUID.randomUUID().toString().replace("-", "");
        }
        return ret;
    }

    public static void addQuery(String query, int milliseconds){
        queriesList.put(uniq(),new objQueries(query,milliseconds));
    }
}
