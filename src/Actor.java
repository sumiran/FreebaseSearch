import java.util.ArrayList;


public class Actor extends Entity{

	public ArrayList<String> characters;
	public ArrayList<String> filmName;
	
	public Actor(){
		characters = new ArrayList<String>();
		filmName = new ArrayList<String>();
	}
	
	public ArrayList<String> getInfoRows() {
		ArrayList<String> rows = new ArrayList<String>();
		
		if(Math.min(characters.size(), filmName.size()) > 0) {
			
			String filmsRow = "Films:"+whiteSpaceOfLength(headingColLength - "Films:".length());
			
			ArrayList<String> charList = new ArrayList<String>();
			charList.add("Character");
			charList.addAll(charList.size(), characters);
			
			ArrayList<String> filmList = new ArrayList<String>();
			filmList.add("Film");
			filmList.addAll(filmList.size(), filmName);
			
			filmsRow += makeMultiColumnBlock(charList, filmList, headingColLength, (rowLength - headingColLength)/4);
			
			rows.add(filmsRow);
		}
		
		return rows;
	}
}
