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
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * It still has some issues such as has some useless words, and some FreeBaseEntity did
 * not include in the search filed, but the professor's demo has it, such as NBA  slogan part
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
		System.out.println("New Ver");
		topicSearch("/m/017nt");


		String[] args2 = {"-key","AIzaSyDFgRTZ_yfvXwf_t46ovPHC0WlnJ2Ny_hM","-f","D:\\q2.txt","" ,"-t","question"}; //"-q","\"Who", "created","microsoft?\"","",
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
		System.out.println("\nRunning the "+ (fileName.equals("")?"query from commandline":"queries from file") +" as a "+ (mode.equalsIgnoreCase("question")?"question":"infobox") +"...");
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
					System.out.println("Executing query for "+q+": ");
					
					ArrayList<Creator> c = getCreatedList(freebaseMQLQuery(getObjectFromUserQuery(q), key));
					System.out.println(printCreatedListToString(c));
				}
			}
		} else {
			
		}
		
		
	}
}


