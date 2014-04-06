import java.util.ArrayList;


public class BusinessPerson {

	

	public ArrayList<Leadership> leaderships;
	public ArrayList<BoardMember> boardMembers;
	public ArrayList<String> founded;
	
	
	public BusinessPerson(){
		leaderships = new ArrayList<Leadership> ();
		boardMembers = new ArrayList<BoardMember> ();
		founded = new ArrayList<String>();
	}
	
}
