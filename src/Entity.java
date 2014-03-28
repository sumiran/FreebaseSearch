import java.util.HashMap;


public class Entity {
	
	public HashMap<String, Object> fields;
	
	public Entity() {
		fields = new HashMap<String, Object>();
	}
	
	public Entity(HashMap<String, Object> values) {
		fields = values;
	}

}
