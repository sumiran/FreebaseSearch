import java.util.HashMap;


public class Helper {
	final static String[] freeBaseEntities = {"/people/person","/organization/organization_founder",
		"/business/board_member","/book/author","/film/actor","/sports/sports_league",
		"/sports/sports_team","/sports/professional_sports_team","/common/topic/description"};
	public HashMap<String, Boolean> typeOfEntity;
	public HashMap<String, Boolean> objectTypes;
public Helper(){
	typeOfEntity = new HashMap<String, Boolean>();
	objectTypes = new HashMap<String, Boolean>();
	HashMap<String, Boolean> map = new HashMap<String, Boolean>();
	map.put("Person", false);
	map.put("Author", false);
	map.put("Actor", false);
	map.put("BusinessPerson", false);
	map.put("League", false);
	map.put("Sports", false);
	typeOfEntity = map;
	
	HashMap<String, Boolean> maps = new HashMap<String, Boolean>();
	maps.put("/people/person", false);
	maps.put("/book/author", false);
	maps.put("/film/actor", false);
	maps.put("/tv/tv_actor", false);
	maps.put("/organization/organization_founder", false);
	maps.put("/business/board_member", false);
	maps.put("/sports/sports_league", false);
	maps.put("/sports/sports_team", false);
	maps.put("/sports/professional_sports_team", false);
	objectTypes = maps;
}
}
