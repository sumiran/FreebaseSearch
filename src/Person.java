import java.util.ArrayList;


public class Person {

	public  String birthday;
	public  String placeOfBirth;
	public  ArrayList<String> siblings;
	public  ArrayList<String> spouses;
	public  String description;
	
	public Person(){
		siblings = new ArrayList<String> ();
		spouses = new ArrayList<String> ();
	}
	
}
