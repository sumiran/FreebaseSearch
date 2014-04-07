import java.util.ArrayList;


public class SportsTeam extends Entity{

	public String sport;
	public String arena;
	public ArrayList<String> championships;
	public String founded;
	public ArrayList<String> leagues;
	public ArrayList<String> locations;
	public ArrayList<Coach> coaches;
	public ArrayList<PlayersRoster> playersRosters;
	public String description;
	public SportsTeam(){
		this.sport = "";
		this.arena = "";
		this.founded = "";
		this.description = "";
		this.championships = new ArrayList<String>();
		this.leagues = new ArrayList<String>();
		this.locations = new ArrayList<String>();
		this.coaches = new ArrayList<Coach>();
		this.playersRosters = new ArrayList<PlayersRoster>();
	}
	
	
	public ArrayList<String> getInfoRows() {
		
		ArrayList<String> rows = new ArrayList<String>();
		
		if(sport != null) {
			rows.add(("Sport:"+whiteSpaceOfLength(headingColLength - "Sport:".length())+sport));
		}
		
		if(arena != null) {
			rows.add(("Arena:"+whiteSpaceOfLength(headingColLength - "Arena:".length())+arena));
		}
		
		if(championships.size() > 0) {
			String foundedRow = "Championships:"+whiteSpaceOfLength(headingColLength - "Championships:".length());
			foundedRow += multilineBlock(championships, headingColLength);
			rows.add(foundedRow);
		}
		
		if(founded != null) {
			rows.add(("Founded:"+whiteSpaceOfLength(headingColLength - "Founded:".length())+founded));
		}
		
		if(leagues.size() > 0) {
			String foundedRow = "Leagues:"+whiteSpaceOfLength(headingColLength - "Leagues:".length());
			foundedRow += multilineBlock(leagues, headingColLength);
			rows.add(foundedRow);
		}
		
		if(locations.size() > 0) {
			String foundedRow = "Locations:"+whiteSpaceOfLength(headingColLength - "Locations:".length());
			foundedRow += multilineBlock(locations, headingColLength);
			rows.add(foundedRow);
		}
		
		if(coaches.size() > 0) {
			ArrayList<String> coachNameList = new ArrayList<String>();
			ArrayList<String> coachPosList = new ArrayList<String>();
			ArrayList<String> coachFromToList = new ArrayList<String>();
			
			coachNameList.add("Name");
			coachPosList.add("Position");
			coachFromToList.add("From -> To");
			
			for(Coach l : coaches) {
				coachNameList.add(l.name);
				coachPosList.add(l.position);
				coachFromToList.add(l.from+" -> "+l.to);
			}
			String coachRow = "Coaches:"+whiteSpaceOfLength(headingColLength-"Coaches:".length())+makeMultiColumnBlock(coachNameList, coachPosList, coachFromToList, headingColLength, (rowLength - headingColLength) / 3);
			rows.add(coachRow);
		}
		
		if(playersRosters.size() > 0) {
			ArrayList<String> playerNameList = new ArrayList<String>();
			ArrayList<String> playerPosList = new ArrayList<String>();
			ArrayList<String> playerNumberList = new ArrayList<String>();
			ArrayList<String> playerfromToList = new ArrayList<String>();
			
			playerNameList.add("Name");
			playerPosList.add("Position");
			playerNumberList.add("Number");
			playerfromToList.add("From -> To");
			
			for(PlayersRoster l : playersRosters) {
				playerNameList.add(l.name);
				playerPosList.add(l.position);
				playerNumberList.add(l.number);
				playerfromToList.add(l.from+" -> "+l.to);
			}
			String playersRow = "Players:"+whiteSpaceOfLength(headingColLength-"Players:".length())+makeMultiColumnBlock(playerNameList, playerPosList, playerNumberList, playerfromToList, headingColLength, (rowLength - headingColLength) / 4);
			rows.add(playersRow);
		}
		
		if(description != null) {
			rows.add("Description:"+whiteSpaceOfLength(headingColLength - "Description:".length())+breakStringToBlock(description,headingColLength, rowLength - headingColLength));
		}
		
		
		
		return rows;
		
	}
	
}
