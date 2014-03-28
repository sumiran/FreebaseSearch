import java.util.HashMap;


public class InfoBoxGenerator {
	
	public static String printInfoToString(HashMap<Object, Object> h) {
		
		return printHashmapToString(h, 0);
	}
	
	private static String printHashmapToString(HashMap<Object, Object> h, int nestLevel) {
		
		String infoBox = "";
		
		for(Object o : h.keySet()) {
			if (o instanceof String) {
				for(int i=0;i<nestLevel;i++) {
					infoBox += "\t";
				}
				infoBox += (String)o;
			}
			
			Object value = h.get(o);
			
			if(value instanceof String) {
				infoBox += ": "+((String)value);
			} else if (value instanceof HashMap) {
				infoBox += printHashmapToString((HashMap<Object, Object>)value, nestLevel+1);
			}
			
			infoBox += "\n";
		}
		
		return infoBox;
	}

}
