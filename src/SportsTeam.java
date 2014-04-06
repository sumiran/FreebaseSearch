import java.util.ArrayList;


public class SportsTeam {

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
}
