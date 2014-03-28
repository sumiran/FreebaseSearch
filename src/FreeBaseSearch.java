
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class FreeBaseSearch {
	
	
	public static void searchTest(String query, String key, String params) throws Exception
	{        
	       String query_envelope = "{\"query\":" + query + "}";
	       String service_url = "https://www.googleapis.com/freebase/v1/search";      

	       String url = service_url    + "?query=" + URLEncoder.encode(query, "UTF-8")
	                                    + params 
	                                    + "&key=" + key;     

	       HttpClient httpclient = new DefaultHttpClient();   
	       HttpResponse response = httpclient.execute(new HttpGet(url));  

	       JSONParser parser = new JSONParser();
	       JSONObject json_data = 
	       (JSONObject)parser.parse(EntityUtils.toString(response.getEntity()));
	       JSONArray results = (JSONArray)json_data.get("result");            

	       if(results != null)
	       {
	           for (Object planet : results) 
	           {
	              System.out.println(((JSONObject)planet).get("name"));
	           }
	       }        
	}
	
	public static void main(String args[]) {
		System.out.println("Hello, World");
	}
}
