import java.util.ArrayList;


public class Person extends Entity{

	public  String birthday;
	public  String placeOfBirth;
	public  ArrayList<String> siblings;
	public  ArrayList<String> spouses;
	public  String description;
	
	public Person(){
		siblings = new ArrayList<String> ();
		spouses = new ArrayList<String> ();
	}
	
	public ArrayList<String> getInfoRows() {
		ArrayList<String> rows = new ArrayList<String>();
		
		if(birthday != null) {
			rows.add(("Birthday:"+whiteSpaceOfLength(headingColLength - "Birthday:".length())+birthday));
		}
		
		if(placeOfBirth != null) {
			rows.add(("Place of Birth:"+whiteSpaceOfLength(headingColLength - "Place of Birth:".length())+placeOfBirth));
		}
		
		if(siblings.size() > 0) {
			String siblingLineOne = "Siblings:"+whiteSpaceOfLength(headingColLength - "Siblings:".length())+siblings.get(0)+((siblings.size() > 1)?"\n":"");
			String siblingRest = "";
			
			for(int i=1;i<siblings.size();i++) {
				siblingRest += whiteSpaceOfLength(siblingLineOne.length() - siblings.get(0).length() - 1);
				siblingRest += siblings.get(i);
				
				if(i != siblings.size() - 1) {
					siblingRest += "\n";
				}
			}
			
			rows.add(siblingLineOne+siblingRest);
		}
		
		if(spouses.size() > 0) {
			String spousesLineOne = "Spouses:"+whiteSpaceOfLength(headingColLength - "Spouses:".length())+spouses.get(0)+((spouses.size() > 1)?"\n":"");
			String spousesRest = "";
			
			for(int i=1;i<spouses.size();i++) {
				spousesRest += whiteSpaceOfLength(spousesLineOne.length() - spouses.get(0).length() - 1);
				spousesRest += spouses.get(i);
				
				if(i != spouses.size() - 1) {
					spousesRest += "\n";
				}
			}
			
			rows.add(spousesLineOne+spousesRest);
		}
		
		if(description != null) {
			rows.add("Description:"+whiteSpaceOfLength(headingColLength - "Description:".length())+breakStringToBlock(description,headingColLength, rowLength - headingColLength));
		}
		
		return rows;
	}
	
}
