package play.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import play.data.parsing.DataParser;
import play.libs.Utils;

public class Scope {
    
    public static class Flash {

        // ThreadLocal access
        private static ThreadLocal<Flash> current = new ThreadLocal<Flash>();    
        public static Flash current() {
            return current.get();
        }

    }
    
    public static class Session {

        // ThreadLocal access
        public static ThreadLocal<Session> current = new ThreadLocal<Session>();    
        public static Session current() {
            return current.get();
        }

    }
    
    public static class Params {
        
        // ThreadLocal access
        public static ThreadLocal<Params> current = new ThreadLocal<Params>();    
        public static Params current() {
            return current.get();
        }
        
        boolean requestIsParsed;
        Map<String, String[]> data = new HashMap<String, String[]>();
        
        void checkAndParse() {
            if(!requestIsParsed) {
                Http.Request request = Http.Request.current();
                String contentType = request.contentType;
                if(contentType != null) {
                    DataParser dataParser = DataParser.parsers.get(contentType);
                    if(dataParser != null) {
                        _mergeWith(dataParser.parse(request.body));
                    }
                }
                requestIsParsed = true;
            }
        }

        public String get(String key) {
            checkAndParse();
            if(data.containsKey(key)) {
                return data.get(key)[0];
            }
            return null;
        }
        
        public String[] getAll(String key) {
            checkAndParse();
            return data.get(key);
        }
        
        void _mergeWith(Map<String, String[]> map) {
            for(String key : map.keySet()) {
                Utils.Maps.mergeValueInMap(data, key, map.get(key));
            }
        }
        
        void __mergeWith(Map<String, String> map) {
            for(String key : map.keySet()) {
                Utils.Maps.mergeValueInMap(data, key, map.get(key));
            }
        }
        
        
    }
    
    public static class RenderArgs {
        
        Map<String, Object> data = new HashMap<String, Object>();
        
        // ThreadLocal access
        public static ThreadLocal<RenderArgs> current = new ThreadLocal<RenderArgs>();    
        public static RenderArgs current() {
            return current.get();
        }
        
        public void put(String key, Object arg) {
            this.data.put(key, arg);
        }
                
    }


}