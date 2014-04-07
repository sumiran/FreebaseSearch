import java.util.ArrayList;


public class BusinessPerson extends Entity{

	

	public ArrayList<Leadership> leaderships;
	public ArrayList<BoardMember> boardMembers;
	public ArrayList<String> founded;
	
	
	public BusinessPerson(){
		leaderships = new ArrayList<Leadership> ();
		boardMembers = new ArrayList<BoardMember> ();
		founded = new ArrayList<String>();
	}
	
	public ArrayList<String> getInfoRows() {
		ArrayList<String> rows = new ArrayList<String>();
		
		if(founded.size() > 0) {
			String foundedRow = "Founded:"+whiteSpaceOfLength(headingColLength - "Founded:".length());
			foundedRow += multilineBlock(founded, headingColLength);
			rows.add(foundedRow);
		}
		
		if(leaderships.size() > 0) {
			ArrayList<String> leadershipOrgList = new ArrayList<String>();
			ArrayList<String> leadershipRoleList = new ArrayList<String>();
			ArrayList<String> leadershipTitleList = new ArrayList<String>();
			ArrayList<String> leadershipFromToList = new ArrayList<String>();
			
			leadershipOrgList.add("Organization");
			leadershipRoleList.add("Role");
			leadershipTitleList.add("Title");
			leadershipFromToList.add("From -> To");
			
			for(Leadership l : leaderships) {
				leadershipOrgList.add(l.organization);
				leadershipRoleList.add(l.role);
				leadershipTitleList.add(l.title);
				leadershipFromToList.add(l.from+" -> "+l.to);
			}
			String leadershipRow = "Leadership:"+whiteSpaceOfLength(headingColLength-"Leadership:".length())+makeMultiColumnBlock(leadershipOrgList, leadershipRoleList, leadershipTitleList, leadershipFromToList, headingColLength, (rowLength - headingColLength) / 4);
			rows.add(leadershipRow);
		}
		
		
		if(boardMembers.size() > 0) {
			ArrayList<String> boardMemberOrgList = new ArrayList<String>();
			ArrayList<String> boardMemberRoleList = new ArrayList<String>();
			ArrayList<String> boardMemberTitleList = new ArrayList<String>();
			ArrayList<String> boardMemberFromToList = new ArrayList<String>();
			
			boardMemberOrgList.add("Organization");
			boardMemberRoleList.add("Role");
			boardMemberTitleList.add("Title");
			boardMemberFromToList.add("From -> To");
			
			for(BoardMember bm : boardMembers) {
				boardMemberOrgList.add(bm.organization);
				boardMemberRoleList.add(bm.role);
				boardMemberTitleList.add(bm.title);
				boardMemberFromToList.add(bm.from+" -> "+bm.to);
			}
			String boardMembershipRow = "Board Member:"+whiteSpaceOfLength(headingColLength-"Board Member:".length())+makeMultiColumnBlock(boardMemberOrgList, boardMemberRoleList, boardMemberTitleList, boardMemberFromToList, headingColLength, (rowLength - headingColLength) / 4);
			rows.add(boardMembershipRow);
		}
		
		return rows;
	}
	
	
	
}
