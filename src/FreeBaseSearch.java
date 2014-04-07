import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@SuppressWarnings("deprecation")
public class FreeBaseSearch {
	
	
	static Helper help;
	static ArrayList<HashMap<String, Object>> infoBoxContentsList;
	static HashMap<Object, Object> infoBoxResult;
	@SuppressWarnings("unchecked")
	public static ArrayList<String> searchTest(String query, String key) throws IOException, ParseException, org.json.simple.parser.ParseException
	{        
	       String service_url = "https://www.googleapis.com/freebase/v1/search";      

	       String url = service_url    + "?query=" + URLEncoder.encode(query, "UTF-8")
	                                    + "&key="+key;     

	       @SuppressWarnings("resource")
	       HttpClient httpclient = new DefaultHttpClient();   
	       HttpResponse response = httpclient.execute(new HttpGet(url));  

	       JSONParser parser = new JSONParser();
	       JSONObject json_data = 
	       (JSONObject)parser.parse(EntityUtils.toString(response.getEntity()));

	       ObjectMapper mapper = new ObjectMapper();
	       HashMap<String,Object> result = mapper.readValue(json_data.toString(), HashMap.class);
	       
	       ArrayList<HashMap<String, Object>> resultSet = (ArrayList<HashMap<String, Object>>)result.get("result");
	       ArrayList<String> mIdAndNameList = new ArrayList<String>();
	       for(HashMap<String, Object> h : resultSet) {
	    	   mIdAndNameList.add(((String) h.get("mid"))+":"+h.get("name"));
	       }
	       
	       return mIdAndNameList;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean topicSearch(String mid, String key) throws ClientProtocolException, IOException, ParseException, org.json.simple.parser.ParseException
	{
		
			boolean helpfulTopic = false;
			
			help = new Helper();
			String service_url = "https://www.googleapis.com/freebase/v1/topic";      
	       String url = service_url + mid + "?key="+key;

	       @SuppressWarnings("resource")
	       HttpClient httpclient = new DefaultHttpClient();   
	       HttpResponse response = httpclient.execute(new HttpGet(url));  

	       JSONParser parser = new JSONParser();
	       JSONObject json_data = (JSONObject)parser.parse(EntityUtils.toString(response.getEntity()));

	       @SuppressWarnings("unused")
			String x = json_data.toString();
	       
	       ObjectMapper mapper = new ObjectMapper();       
	       JSONObject results = (JSONObject)json_data.get("property");
	       HashMap<String,Object> o  = mapper.readValue(results.toJSONString(), HashMap.class);
	       JsonNode jNode = mapper.valueToTree(results);
	      
	       
	       
	       /*if(!jNode.path("/business/board_member/organization_board_memberships").path("values").get(0).path("property").path("/organization/organization_board_membership/from").isMissingNode())
	       System.out.println(jNode.path("/business/board_member/organization_board_memberships").path("values").get(0).path("property").path("/organization/organization_board_membership/from").path("values").get(0).path("text"));
	       */
	       objectTypeInitial(results);
	       typeOfEntityInitial();
	       
	       
	       //System.out.println(help.typeOfEntity);
	       
	       System.out.print("Type of entity:"+Entity.whiteSpaceOfLength(Entity.headingColLength - "Type of entity:".length()));
	       for(String k : help.typeOfEntity.keySet()) {
	    	   System.out.print(help.typeOfEntity.get(k)?k+" ":"");
	       }
	       System.out.println("\n"+Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
	       
	       BusinessPerson bp = businessPersonInit(jNode);
	       Author auth = authorInit(jNode);
	       Actor act = actorInit(jNode);
	       League l = leagueInit(jNode);
	       SportsTeam st = sportsTeamInit(jNode);
	       Person p = personInit(jNode);
	       
	       ArrayList<String> data;
	       
	       if(help.typeOfEntity.get("Person")) {
	    	   helpfulTopic = true;
		       data = p.getInfoRows();
		       for(String row : data) {
		    	   System.out.println(row);
		    	   System.out.println(Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
		       }
	       }
	       
	       if(help.typeOfEntity.get("BusinessPerson")) {
	    	   helpfulTopic = true;
		       data = bp.getInfoRows();
		       for(String row : data) {
		    	   System.out.println(row);
		    	   System.out.println(Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
		       }
	       }
	       
	       if(help.typeOfEntity.get("Author")) {
	    	   helpfulTopic = true;
		       data = auth.getInfoRows();
		       for(String row : data) {
		    	   System.out.println(row);
		    	   System.out.println(Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
		       }
	       }
	       
	       if(help.typeOfEntity.get("Actor")) {
	    	   helpfulTopic = true;
		       data = act.getInfoRows();
		       for(String row : data) {
		    	   System.out.println(row);
		    	   System.out.println(Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
		       }
	       }
	       
	       if(help.typeOfEntity.get("SportsTeam")) {
	    	   helpfulTopic = true;
		       data = st.getInfoRows();
		       for(String row : data) {
		    	   System.out.println(row);
		    	   System.out.println(Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
		       }
	       }
	       
	       if(help.typeOfEntity.get("League")) {
	    	   helpfulTopic = true;
		       data = l.getInfoRows();
		       for(String row : data) {
		    	   System.out.println(row);
		    	   System.out.println(Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
		       }
	       }
	       
	       
	       return helpfulTopic;
	}
	
	
	public static void freebaseSearchAndPrintInfobox(String queryTerm, String key) {
		
	}
	

	public static Person personInit(JsonNode node){
		Person p = new Person();
		int count = 0;
		if(!node.path("/people/person/date_of_birth").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/people/person/date_of_birth").path("count").toString());
			for(int i = 0;i < count; i ++){
			p.birthday = node.path("/people/person/date_of_birth").path("values").get(i).path("text").toString();
			}
		}
		if(!node.path("/people/person/sibling_s").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/people/person/sibling_s").path("count").toString());
			for(int i = 0;i < count; i ++){
			p.siblings.add(node.path("/people/person/sibling_s").path("values").get(i).path("property").path("/people/sibling_relationship/sibling").path("values").get(0).path("text").toString());
			}
		}
		if(!node.path("/people/person/place_of_birth").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/people/person/place_of_birth").path("count").toString());
			for(int i = 0;i < count; i ++){
			p.placeOfBirth = node.path("/people/person/place_of_birth").path("values").get(i).path("text").toString();
			}
		}
		if(!node.path("/people/person/spouse_s").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/people/person/spouse_s").path("count").toString());
			for(int i = 0;i < count; i ++){
			p.spouses.add(node.path("/people/person/spouse_s").path("values").get(i).path("property").path("/people/marriage/spouse").path("values").get(0).path("text").toString());
			}
		}
		if(!node.path("/common/topic/description").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/common/topic/description").path("count").toString());
			for(int i = 0;i < count; i ++){
			p.description = node.path("/common/topic/description").path("values").get(0).path("value").toString();
			}
		}
		return p;
		
	}
		
	
	public static Author authorInit(JsonNode node){
		Author a = new Author();
		int count = 0;
		if(!node.path("/book/author/works_written").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/book/author/works_written").path("count").toString());
		for(int i = 0;i < count; i ++){
		a.books.add(node.path("/book/author/works_written").path("values").get(i).path("text").toString());
		}
		}
		if(!node.path("/book/book_subject/works").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/book/book_subject/works").path("count").toString());
		for(int i = 0;i < count; i ++){
			try {
		a.booksAbout.add(node.path("/book/book_subject/works").path("values").get(i).path("text").toString());
			} catch(Exception e) {}
		}
		}
		if(!node.path("/influence/influence_node/influenced").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/influence/influence_node/influenced").path("count").toString());
		for(int i = 0;i < count; i ++){
		a.influenced.add(node.path("/influence/influence_node/influenced").path("values").get(i).path("text").toString());
		}
		}
		if(!node.path("/influence/influence_node/influenced_by").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/influence/influence_node/influenced_by").path("count").toString());
		for(int i = 0;i < count; i ++){
		a.influencedBy.add(node.path("/influence/influence_node/influenced_by").path("values").get(i).path("text").toString());
		}
		}
		return a;
	}
	public static Actor actorInit(JsonNode node){
		Actor a = new Actor();
		int count = 0;
		if(!node.path("/film/actor/film").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/film/actor/film").path("count").toString());
			for(int i = 0;i < count; i ++){
			
				try {
			if(!node.path("/film/actor/film").path("values").get(i).path("property").path("/film/performance/character").isMissingNode())	
				a.characters.add(node.path("/film/actor/film").path("values").get(i).path("property").path("/film/performance/character").path("values").get(0).path("text").toString());
			if(!node.path("/film/actor/film").path("values").get(i).path("property").path("/film/performance/film").isMissingNode())
				a.filmName.add(node.path("/film/actor/film").path("values").get(i).path("property").path("/film/performance/film").path("values").get(0).path("text").toString());
				} catch(Exception e) {}
			}
		}
		return a;
	}
	public static BusinessPerson businessPersonInit(JsonNode node){
		BusinessPerson bp = new BusinessPerson();
		int count = 0;
		if(!node.path("/organization/organization_founder/organizations_founded").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/organization/organization_founder/organizations_founded").path("count").toString());
			for(int i = 0;i < count; i ++){
			bp.founded.add(node.path("/organization/organization_founder/organizations_founded").path("values").get(i).path("text").toString());
			}
		}
		
		
		if(!node.path("/business/board_member/organization_board_memberships").path("count").isMissingNode())
		{
			
			count = (int) Double.parseDouble(node.path("/business/board_member/organization_board_memberships").path("count").toString());
			for(int i = 0;i < count; i ++){
				BoardMember bm = new BoardMember();
				if(!node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/organization").isMissingNode())
				bm.organization = node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/organization").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/title").isMissingNode())
				bm.title = node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/title").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/from").isMissingNode())
				bm.from = node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/from").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/role").isMissingNode())
				bm.role = node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/role").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/to").isMissingNode())
				bm.to = node.path("/business/board_member/organization_board_memberships").path("values").get(i).path("property").path("/organization/organization_board_membership/to").path("values").get(0).path("text").toString();
				bp.boardMembers.add(bm);
			}
		}
		
		if(!node.path("/business/board_member/leader_of").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/business/board_member/leader_of").path("count").toString());
			for(int i = 0;i < count; i ++){
				Leadership ls = new Leadership();
				if(!node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/from").isMissingNode())
				ls.from = node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/from").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/organization").isMissingNode())
				ls.organization = node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/organization").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/role").isMissingNode())
				ls.role = node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/role").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/to").isMissingNode())
				ls.to = node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/to").path("values").get(0).path("text").toString();
				if(!node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/title").isMissingNode())
				ls.title = node.path("/business/board_member/leader_of").path("values").get(i).path("property").path("/organization/leadership/title").path("values").get(0).path("text").toString();
				bp.leaderships.add(ls);
			}
			
		}
		return bp;
	}
	public static League leagueInit(JsonNode node){
		League l = new League();
		int count = 0;
		if(!node.path("/sports/sports_league/championship").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/sports/sports_league/championship").path("count").toString());
		for(int i = 0;i < count; i ++){
			if(!node.path("/sports/sports_league/championship").isMissingNode())
			l.championship = node.path("/sports/sports_league/championship").path("values").get(i).path("text").toString();
			if(!node.path("/sports/sports_league/sport").isMissingNode())
			l.sport = node.path("/sports/sports_league/sport").path("values").get(i).path("text").toString();
			if(!node.path("/common/topic/official_website").isMissingNode())
			l.website = node.path("/common/topic/official_website").path("values").get(i).path("text").toString();
			if(!node.path("/organization/organization/slogan").isMissingNode())
			l.slogan = node.path("/organization/organization/slogan").path("values").get(i).path("text").toString();
			if(!node.path("/organization/organization/description").isMissingNode())
			l.description = node.path("/common/topic/description").path("values").get(i).path("value").toString();
			
		}
		}
		
		if(!node.path("/sports/sports_league/teams").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/sports/sports_league/teams").path("count").toString());
		
		if(!node.path("/common/topic/description").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/common/topic/description").path("count").toString());
			for(int i = 0;i < count; i ++){
			l.description = node.path("/common/topic/description").path("values").get(0).path("value").toString();
			}
		}
		
		for(int i = 0;i < count; i ++){
			try {
			if(!node.path("/sports/sports_league/teams").isMissingNode() && !node.path("/sports/sports_league/teams").path("values").get(i).path("property").path("/sports/sports_league_participation/team").isMissingNode())
			l.teams.add(node.path("/sports/sports_league/teams").path("values").get(i).path("property").path("/sports/sports_league_participation/team").path("values").get(0).path("text").toString());
			} catch(Exception e) {}
		}
		}
		return l;
	}
	public static SportsTeam sportsTeamInit(JsonNode node){
		SportsTeam st = new SportsTeam();
		int count = 0;
		if(!node.path("/sports/sports_team/sport").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/sports/sports_team/sport").path("count").toString());
		for(int i = 0;i < count; i ++){
			if(!node.path("/sports/sports_team/sport").isMissingNode())
			st.sport = node.path("/sports/sports_team/sport").path("values").get(i).path("text").toString();
			if(!node.path("/sports/sports_team/arena_stadium").isMissingNode())
			st.arena = node.path("/sports/sports_team/arena_stadium").path("values").get(i).path("text").toString();
			
		}
		}
		if(!node.path("/sports/sports_team/championships").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/sports/sports_team/championships").path("count").toString());
		for(int i = 0; i < count; i++){
			st.championships.add(node.path("/sports/sports_team/championships").path("values").get(i).path("text").toString());
		}
		}
		if(!node.path("/sports/sports_team/coaches").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/sports/sports_team/coaches").path("count").toString());
		for(int i = 0; i < count; i++){
			try {
			Coach c = new Coach();
			if(!node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/coach").isMissingNode())
			c.name = node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/coach").path("values").get(0).path("text").toString();
			
			if(!node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/position").isMissingNode())
			c.position = node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/position").path("values").get(0).path("text").toString();
			
			if(!node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/from").isMissingNode())
				c.from = node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/from").path("values").get(0).path("text").toString();
			
			if(!node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/to").isMissingNode())
				c.to = node.path("/sports/sports_team/coaches").path("values").get(i).path("property").path("/sports/sports_team_coach_tenure/to").path("values").get(0).path("text").toString();
			st.coaches.add(c);
			} catch(Exception e) {}
		}
		}
		
		if(!node.path("/sports/sports_team/location").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/sports/sports_team/location").path("count").toString());
		for(int i = 0; i < count; i++){
			st.locations.add(node.path("/sports/sports_team/location").path("values").get(i).path("text").toString());
		}
		}
		
		if(!node.path("/common/topic/description").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/common/topic/description").path("count").toString());
			for(int i = 0;i < count; i ++){
			st.description = node.path("/common/topic/description").path("values").get(0).path("value").toString();
			}
		}
		
		if(!node.path("/sports/sports_team/founded").path("count").isMissingNode()){
			count = (int) Double.parseDouble(node.path("/sports/sports_team/founded").path("count").toString());
			for(int i = 0;i < count; i ++){
			st.founded = node.path("/sports/sports_team/founded").path("values").get(0).path("value").toString();
			}
		}
		
		if(!node.path("/sports/sports_team/roster").path("count").isMissingNode()){
		count = (int) Double.parseDouble(node.path("/sports/sports_team/roster").path("count").toString());
		for(int i = 0; i < count; i++){
			try {
			PlayersRoster pr = new PlayersRoster();
			if(!node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/player").isMissingNode())
			pr.name = node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/player").path("values").get(0).path("text").toString();
			if(!node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/position").isMissingNode())
			pr.position = node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/position").path("values").get(0).path("text").toString();
			if(!node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/from").isMissingNode())
			pr.from = node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/from").path("values").get(0).path("text").toString();
			if(!node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/number").isMissingNode())
			pr.number = node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/number").path("values").get(0).path("text").toString();
			if(!node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/to").isMissingNode())
			pr.to = node.path("/sports/sports_team/roster").path("values").get(i).path("property").path("/sports/sports_team_roster/to").path("values").get(0).path("text").toString();
			st.playersRosters.add(pr);
			} catch(Exception e) {}
		}
		}
		return st;
	}
	
	public static void typeOfEntityInitial(){
		if(help.objectTypes.get("/people/person") == true){
			help.typeOfEntity.put("Person", true);
		}
		if(help.objectTypes.get("/book/author") == true){
			help.typeOfEntity.put("Author", true);
		}
		if(help.objectTypes.get("/film/actor") == true || help.objectTypes.get("/tv/tv_actor") == true){
			help.typeOfEntity.put("Actor", true);
		}
		if(help.objectTypes.get("/organization/organization_founder") == true || help.objectTypes.get("/business/board_member") == true){
			help.typeOfEntity.put("BusinessPerson", true);
		}
		if(help.objectTypes.get("/sports/sports_league") == true){
			help.typeOfEntity.put("League", true);
		}
		if(help.objectTypes.get("/sports/sports_team") == true || help.objectTypes.get("/sports/professional_sports_team") == true){
			help.typeOfEntity.put("SportsTeam", true);
		}
	}
	/**
	 * initial object types, define the type of the query result
	 * @param results the json object from query
	 */
	public static void objectTypeInitial(JSONObject results){
	      JSONObject types = (JSONObject)results.get("/type/object/type");
	      JSONArray values = (JSONArray)types.get("values");
	    
	      for(Object value : values){
	    	String type = ((JSONObject)value).get("id").toString();
	    	if(help.objectTypes.containsKey(type)){
	    		help.objectTypes.put(type, true);
	    	}
	      }
	}
	/**
	 * check if a key is the subkey of another
	 * @param key
	 * @param subKey
	 * @return
	 */
	public static boolean checkIsSubKey(String key, String subKey){
		if(subKey.toLowerCase().contains(key.toLowerCase())){
			return true;
		} else {
			return false;
		}
	}
	
	public static void dataSetParser(){
		for(HashMap<String,Object> o: infoBoxContentsList){
			o = mapHandler(o);
		}
	}

	/**
	 * Refine the key name of the hashmap
	 * @param map
	 * @return the new map with new key
	 */
	public static HashMap<String,Object> mapHandler(HashMap<String,Object> map){
		HashMap<String,Object> newMap = new HashMap<String,Object>();
		for(String k : map.keySet()){
			Object obj = map.get(k);
			String newKey = keyRename(k);    			   
			newMap.put(newKey, obj);	
		}
		return newMap;
	}
	
	/**
	 * handle the key so that can delete unuseful charactors
	 * @param key
	 * @return
	 */
	public static String keyRename(String key){
		return key.substring(key.lastIndexOf("/")+1);
	}
	
	public static HashMap<String, Object> jsonToHashMap(String jsonString) throws Exception
	{
		return new ObjectMapper().readValue(jsonString, new TypeReference<HashMap<String, Object>>(){});
	}
	
	/*
	 * Does the HTTP heavywork and returns an ArrayList of HashMaps (constructed from the JSON response) of the resultset.
	 * Each element of the ArrayList corresponds to one result and each HashMap element holds the key-value pairs defining the result.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>> freebaseMQLQuery(String query, String key) throws Exception {
		String service_url = "https://www.googleapis.com/freebase/v1/mqlread";
		String url = service_url + "?query=" + URLEncoder.encode(makeMQLQuery(query), "UTF-8") + "&key=" + key;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		HttpResponse response = httpclient.execute(new HttpGet(url));
		JSONObject json_data = (JSONObject)(new JSONParser()).parse(EntityUtils.toString(response.getEntity()));
		
		HashMap<String,Object> result = jsonToHashMap(json_data.toString());

		ArrayList<HashMap<String, Object>> searchResultSet = (ArrayList<HashMap<String, Object>>)result.get("result");
		
		return searchResultSet;
	}
	
	/*
	 * This type is used to store the relations of creators and their creations. Role is defined as either "BusinessPerson" or "Author" in our implementation scope.
	 */
	public static class Creator implements Comparable<Creator> {
		public String name;
		public String role;
		public ArrayList<String> creations;
		
		public Creator() {name = ""; role=""; creations = new ArrayList<String>();}

		/**
		 * Used when sorting alphabetically by library sort method
		 */
		@Override
		public int compareTo(Creator c) {
			return name.compareTo(c.name);
		}
		
		/*
		 * Used to print the creator-creations relationships to string
		 */
		public String toString() {
			String ret = "";
			ret += name+" (as "+role+") created: ";
			for(String creation : creations) {
				ret += ("<"+creation+">, ");
			}
			
			ret = ret.replaceAll(",\\s*$", "");
			return ret;
		}
	}
	
	/*
	 * This function parses the query result into our internal implementation of an ArrayList of Creator. It makes use of the idempotentInsert
	 * function defined just below.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Creator> getCreatedList(ArrayList<HashMap<String, Object>> queryResult) {
		ArrayList<Creator> createdList = new ArrayList<Creator>();
		
		if(queryResult == null)	return createdList;
		
		for(HashMap<String, Object> resultItem : queryResult) {
			
			if(resultItem.containsKey("/organization/organization/founders")) {
				ArrayList<String> founders = (ArrayList<String>)resultItem.get("/organization/organization/founders");
				for(String founder : founders) {
						idempotentInsert(createdList, founder, (String)resultItem.get("name"), "BusinessPerson");
				}
			}
			
			if(resultItem.containsKey("/book/written_work/author")) {
				ArrayList<String> founders = (ArrayList<String>)resultItem.get("/book/written_work/author");
				for(String founder : founders) {
					idempotentInsert(createdList, founder, (String)resultItem.get("name"), "Author");
				}
			}
		}
		
		return createdList;
	}
	
	/*
	 * Inserts a creator-creation relationship into the master array if it doesn't already exist.
	 * It adds a creation to the existing creations of the creator if the creator is already represented by other
	 * creations with the same role.
	 */
	public static void idempotentInsert(ArrayList<Creator> createdList, String creator, String creation, String role) {
		boolean exists = false;
		for(Creator c : createdList) {
			if(c.name.equalsIgnoreCase(creator)) {
				if(c.role.equals(role)) {
					exists = true;
					if(!c.creations.contains(creation)) {
						c.creations.add(creation);
					}
				}
			}
		}
		
		if(!exists) {
			Creator c = new Creator();
			c.name = creator;
			c.creations = new ArrayList<String>();
			c.creations.add(creation);
			c.role = role;
			createdList.add(c);
		}
	}
	
	/*
	 * Used to print the list of Creator objects to a string
	 */
	public static String printCreatedListToString(ArrayList<Creator> createdList) {
		if((createdList == null) || (createdList.size() == 0)) {
			System.out.println("Sorry, there are no results to display.");
		}
		
		StringBuilder print = new StringBuilder("");
		
		Collections.sort(createdList);
		int iterator = 1;
		
		for(Creator c : createdList) {
			print.append((iterator++)+". "+c.toString());
			print.append("\n");
		}
				
		return print.toString();
	}
	
	/*
	 * Prepares the MQL query string for a "Who created [X]" query. It queries for both types, books and authors in one go.
	 */
	public static String makeMQLQuery(String term) {
		String query = "[{\"name~=\": \"??\",  \"name\": null,  \"type|=\": [\"/book/book\",\"/organization/organization\"],\"/book/written_work/author\": [], \"/organization/organization/founders\": []}]";
		query = query.replace("??", term.replace("\"","\\\""));
		return query;
	}
	
	/*
	 * Extracts the "Lord of the Rings" term from "Who created Lord of the Rings?"
	 */
	public static String getObjectFromUserQuery(String whoCreatedQuery) {
		return whoCreatedQuery.replaceAll("(?i)who created ", "").replace("?","");
	}
	
	/*
	 * Primitive command line argument parser. option must be specified with a dash (eg. -t)
	 */
	public static String getCommandlineParameter(String option, String[] params) {
		int i = 0;
		String returnVal = "";
		while((i < params.length) && (!params[i].equalsIgnoreCase(option))) {
			i++;
		}
		i += 1;
		
		if(i >= params.length)	return returnVal;
		
		if(!params[i].startsWith("\"")) {
			returnVal = params[i];
		}
		else {
			returnVal = params[i].substring(1);
			i += 1;
			while((i < params.length) && (!params[i].endsWith("\""))) {
				returnVal += " "+params[i];
				i++;
			}
			if(i < params.length) {
				returnVal += " "+params[i].substring(0, params[i].length() - 1);
			}
		}
		
		return returnVal.trim();
	}
	
	
	/*
	 * Reads an entire file into String
	 */
	public static String readEntireFile(String path) throws FileNotFoundException {
		FileInputStream is = new FileInputStream(path);
	    StringBuilder sb = new StringBuilder(512);
	    Reader r = null;
	    try {
	        r = new InputStreamReader(is);
	        int c = 0;
	        while ((c = r.read()) != -1) {
	            sb.append((char) c);
	        }
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    } finally {
	    	try {
	    		is.close();
	    		r.close();
	    	} catch(Exception e) {}
	    }
	    return sb.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		String[] args2 = {"-key","AIzaSyDFgRTZ_yfvXwf_t46ovPHC0WlnJ2Ny_hM","-q","Robert Downey Jr","-f","D:\\q1.txt" ,"-t","infobox"}; //"-q","\"Who", "created","microsoft?\"","",
		args = args2;
		
		String key = getCommandlineParameter("-key", args);
		String fileName = getCommandlineParameter("-f", args);
		String mode = getCommandlineParameter("-t", args);
		String query = getCommandlineParameter("-q", args);
		
		System.out.println("Here's what I could figure out...");
		System.out.println("Key (-key): \""+key+"\"");
		System.out.println("Filename (-f): \""+fileName+"\"");
		System.out.println("Mode (-t): \""+mode+"\"");
		System.out.println("Query (-q): \""+query+"\"");
		System.out.println("\nRunning the "+ (fileName.equals("")?"query from commandline":"queries from file") +" in "+ (mode.equalsIgnoreCase("question")?"question":"infobox") +" mode...");
		System.out.println("\n\n");
		
		ArrayList<String> queries = null; 
		
		if(!fileName.equals("")) {
			try {
				String newLineDelim = "\n";

				//Use CRLF if executed in Windows
				if(System.getProperty("os.name").toLowerCase().contains("windows")) {
					newLineDelim = "\r\n";
				}
				
				queries = new ArrayList<String>(Arrays.asList(readEntireFile(fileName).split(newLineDelim)));
			} catch(Exception e) {
				System.out.println("Could not read input file. This happened: ");
				e.printStackTrace();
			}
		} else {
			queries = new ArrayList<String>();
			queries.add(query);
		}
		
		
		if(mode.equalsIgnoreCase("question")) {
			for(String q : queries) {
				if(!q.trim().equals("")) {
					System.out.println("Asking freebase: "+q+": ");
					ArrayList<Creator> c = getCreatedList(freebaseMQLQuery(getObjectFromUserQuery(q), key));
					System.out.println(printCreatedListToString(c));
				}
			}
		} else {
			for(String q : queries) {
				System.out.println("Querying for: "+q);
				ArrayList<String> mids = searchTest(q, key);
				for(String mid : mids) {
					System.out.println("Found an entity: "+mid.split(":")[1]);
					System.out.println("Name:"+Entity.whiteSpaceOfLength(Entity.headingColLength - "Name:".length())+mid.split(":")[1]);
					System.out.println(Entity.repeatedCharacterOfLength(Entity.rowLength, "-"));
					if(topicSearch(mid.split(":")[0], key)) {
						break;
					}
				}
			}
		}
		
		System.out.println("----Done----");
		
	}
}


