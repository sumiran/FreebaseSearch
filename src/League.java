import java.util.ArrayList;


public class League extends Entity{

	public String championship;
	public String sport;
	public String slogan;
	public String website;
	public ArrayList<String> teams;
	public String description;
	public League(){
		teams = new ArrayList<String>();
	}
	
	public ArrayList<String> getInfoRows() {
		ArrayList<String> rows = new ArrayList<String>();
		
		if(championship != null) {
			rows.add(("Championship:"+whiteSpaceOfLength(headingColLength - "Championship:".length())+championship));
		}
		
		if(sport != null) {
			rows.add(("Sport:"+whiteSpaceOfLength(headingColLength - "Sport:".length())+sport));
		}
		
		if(slogan != null) {
			rows.add(("Slogan:"+whiteSpaceOfLength(headingColLength - "Slogan:".length())+slogan));
		}
		
		if(website != null) {
			rows.add(("Website:"+whiteSpaceOfLength(headingColLength - "Website:".length())+website));
		}
		
		if(teams.size() > 0) {
			String foundedRow = "Teams:"+whiteSpaceOfLength(headingColLength - "Teams:".length());
			foundedRow += multilineBlock(teams, headingColLength);
			rows.add(foundedRow);
		}
		
		if(description != null) {
			rows.add("Description:"+whiteSpaceOfLength(headingColLength - "Description:".length())+breakStringToBlock(description,headingColLength, rowLength - headingColLength));
		}
		
		return rows;
	}
	
}
