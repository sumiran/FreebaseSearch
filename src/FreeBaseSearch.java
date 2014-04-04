import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * It still has some issues such as has some useless words, and some FreeBaseEntity did
 * not include in the search filed, but the professor's demo has it, such as NBA  slogan part
 * 
 * 
 * 
 * 
 * @author Terrence
 *
 */

@SuppressWarnings("deprecation")
public class FreeBaseSearch {
	
	final static String[] freeBaseEntities = {"/people/person","/organization/organization_founder",
			"/business/board_member","/book/author","/film/actor","/sports/sports_league",
			"/sports/sports_team","/sports/professional_sports_team","/common/topic/description"};
	static HashMap<String, Boolean> objectTypes;
	static {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("/people/person", false);
		map.put("/book/author", false);
		map.put("/film/actor", false);
		map.put("/tv/tv_actor", false);
		map.put("/organization/organization_founder", false);
		map.put("/business/board_member", false);
		map.put("/sports/sports_league", false);
		map.put("/sports/sports_team", false);
		map.put("/sports/professional_sports_team", false);
		objectTypes = map;
	}
	
	static ArrayList<HashMap<String, Object>> infoBoxContentsList;
	static HashMap<Object, Object> infoBoxResult;
	@SuppressWarnings("unchecked")
	public static void searchTest(String query) throws IOException, ParseException, org.json.simple.parser.ParseException
	{        
	       String service_url = "https://www.googleapis.com/freebase/v1/search";      

	       String url = service_url    + "?query=" + URLEncoder.encode(query, "UTF-8")
	                                    + "&key=AIzaSyCbbYRSqL848PVdRRE3BXRFJ1i8TS-TxRE";     

	       @SuppressWarnings("resource")
	       HttpClient httpclient = new DefaultHttpClient();   
	       HttpResponse response = httpclient.execute(new HttpGet(url));  

	       JSONParser parser = new JSONParser();
	       JSONObject json_data = 
	       (JSONObject)parser.parse(EntityUtils.toString(response.getEntity()));

	       ObjectMapper mapper = new ObjectMapper();
	       HashMap<String,Object> result = mapper.readValue(json_data.toString(), HashMap.class);
	}
	
	@SuppressWarnings("unchecked")
	public static void topicSearch(String mid) throws ClientProtocolException, IOException, ParseException, org.json.simple.parser.ParseException
	{
		   String service_url = "https://www.googleapis.com/freebase/v1/topic";      
	       String url = service_url + mid;

	       @SuppressWarnings("resource")
	       HttpClient httpclient = new DefaultHttpClient();   
	       HttpResponse response = httpclient.execute(new HttpGet(url));  

	       JSONParser parser = new JSONParser();
	       JSONObject json_data = 
	       (JSONObject)parser.parse(EntityUtils.toString(response.getEntity()));

	       ObjectMapper mapper = new ObjectMapper();       
	       JSONObject results = (JSONObject)json_data.get("property");
	       HashMap<String,Object> o  = mapper.readValue(results.toJSONString(), HashMap.class);
	       
	       objectTypeInitial(results);
	       Set<String> key = o.keySet();
	       infoBoxContentsList = new ArrayList<HashMap<String, Object>>();
	       
	       for(String k: key){
	    	   for(String e: freeBaseEntities){    		 
	    		   if(FreeBaseSearch.checkIsSubKey(e, k)){
	    			  
	    			   String newKey = keyRename(k);
	    			   Object obj = (HashMap<?, ?>) o.get(k);
	    			   HashMap<String,Object> newMap = new HashMap<String,Object>();    			   
	    			   newMap.put(newKey, obj);
	    			   infoBoxContentsList.add(newMap);	    			   
	    		   }
	    	   }
	       }
	       
	       dataSetParser();
	       infoBoxContentsList = (ArrayList<HashMap<String, Object>>) dataParser(infoBoxContentsList);
	       infoBoxResult = dataResult(infoBoxContentsList);
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
	    	if(objectTypes.containsKey(type)){
	    		objectTypes.put(type, true);
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
	 * recusively go through the Hashmap, and delete useless entries
	 * all the useless key values.
	 * @param obj
	 * @return obj 
	 */
	@SuppressWarnings("unchecked")
	public static Object dataParser(Object obj){
		
		if(obj instanceof HashMap){
			Iterator<?> iter = ((HashMap<?, ?>) obj).entrySet().iterator();
		
			while (iter.hasNext()) {
			    Map.Entry<String,Object> entry = (Entry<String, Object>) iter.next();
			   
			    if("valueType".equalsIgnoreCase(entry.getKey())||"count".equalsIgnoreCase(entry.getKey())){
			        iter.remove();
			    }
			}
			
			if(((HashMap<?, ?>)(obj)).containsKey("text")){
				iter = ((HashMap<?, ?>) obj).entrySet().iterator();
				while (iter.hasNext()) {
				
					Map.Entry<String,Object> entry = (Entry<String, Object>) iter.next();
				    if(!"text".equalsIgnoreCase(entry.getKey())){
				        iter.remove();
				    } 
				}
				return obj;
			} else {
			
				obj = mapHandler((HashMap<String, Object>)obj);
				Set<String> key = ((HashMap<String, ?>)obj).keySet();
				for(String k : key){
					dataParser(((HashMap<?, ?>) obj).get(k));
				}
				
			}
		}
		
		if(obj instanceof ArrayList){
			for(Object o : (ArrayList<?>)obj){
				dataParser(o);
			}
		}
		return obj;
		
	}
	/**
	 * Finalize the final output
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<Object, Object> dataResult(Object obj){
		
		HashMap<Object, Object> result = new HashMap<Object, Object>();
		for(Object object : (ArrayList<?>)obj){
			Set<String> keys = ((HashMap<String, ?>)object).keySet();
		
			for(String o : keys){
				HashMap<?, ?> values = (HashMap<?, ?>) ((HashMap<?, ?>)object).get(o);
				ArrayList<Object> value = (ArrayList<Object>)(((HashMap<?, ?>)values).get("values"));
				ArrayList<String> text = new ArrayList<String>();
		
				for(Object map : value){
					text.addAll(((HashMap<?, String>)map).values());
				}
				result.put(o, text);
			}
		}
		return result;
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
	
	public static void main(String[] args) throws ParseException, IOException, org.json.simple.parser.ParseException
	{
		
		topicSearch("/m/017nt");
		System.out.println(InfoBoxGenerator.printInfoToString(infoBoxResult));
		
	}
}


