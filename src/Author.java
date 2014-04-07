import java.util.ArrayList;


public class Author extends Entity {

	public ArrayList<String> books;
	public ArrayList<String> booksAbout;
	public ArrayList<String> influenced;
	public ArrayList<String> influencedBy;
	
	public Author(){
		books = new ArrayList<String>();
		booksAbout = new ArrayList<String>();
		influenced = new ArrayList<String>();
		influencedBy = new ArrayList<String>();
	}
	
	public ArrayList<String> getInfoRows() {
		ArrayList<String> rows = new ArrayList<String>();
		
		if(books.size() > 0) {
			String booksRow = "Books:"+whiteSpaceOfLength(headingColLength - "Books:".length());
			booksRow += multilineBlock(books, headingColLength);
			rows.add(booksRow);
		}
		
		if(booksAbout.size() > 0) {
			String booksAboutRow = "Books about:"+whiteSpaceOfLength(headingColLength - "Books about:".length());
			booksAboutRow += multilineBlock(booksAbout, headingColLength);
			rows.add(booksAboutRow);
		}
		
		if(influenced.size() > 0) {
			String influencedRow = "Influenced:"+whiteSpaceOfLength(headingColLength - "Influenced:".length());
			influencedRow += multilineBlock(influenced, headingColLength);
			rows.add(influencedRow);
		}
		
		if(influencedBy.size() > 0) {
			String influencedByRow = "Influenced by:"+whiteSpaceOfLength(headingColLength - "Influenced by:".length());
			influencedByRow += multilineBlock(influencedBy, headingColLength);
			rows.add(influencedByRow);
		}
		
		return rows;
	}
}
