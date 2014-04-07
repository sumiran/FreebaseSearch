import java.util.ArrayList;


public class Entity {
	
	/*
	 * This class is a superclass for all Entity types. It provides basic functions to help print infobox in a format and holds certain other formatting constants
	 */
	
	public static final int rowLength = 150;
	public static final int headingColLength = 30;
	
	public ArrayList<String> getInfoRows() {
		return null;
	}
	
	public static String whiteSpaceOfLength(int length) {
		return repeatedCharacterOfLength(length, " ");
	}
	
	public static String repeatedCharacterOfLength(int length, String str) {
		StringBuilder sb = new StringBuilder("");
		
		for(int i=0;i<length;i++) {
			sb.append(str);
		}
		
		return sb.toString();
	}
	
	protected String breakStringToBlock(String longString, int indentLevel, int blockLength) {
		String result = "";
		
		result += longString.substring(0, Math.min(longString.length(), blockLength));
		
		if(longString.length() > blockLength) {
			longString = longString.substring(blockLength);
			result += "\n";
			
			result += whiteSpaceOfLength(indentLevel);
			result += breakStringToBlock(longString, indentLevel, blockLength);
		}
		
		
		return result;
	}
	
	protected String trimString(String str, int maxLen) {
		if(str == null) {
			return "";
		}
		if(str.length() >= maxLen) {
			str = str.substring(0, maxLen-3)+"...";
		}
		return str;
	}
	
	protected String multilineBlock(ArrayList<String> data, int indentLevel) {
		String result = "";
		
		for(int i=0;i<data.size();i++) {
			
			result += data.get(i);
			
			if(i != data.size() - 1) {
				result += "\n";
				result += whiteSpaceOfLength(indentLevel);
			}
		}
		
		
		return result;
	}
	
	protected String makeMultiColumnBlock(ArrayList<String> column1, ArrayList<String> column2, int indentLevel, int blockLengthByTwo) {
		String result = "";
		
		int listSize = Math.min(column1.size(),  column2.size());
		
		for(int i=0;i<listSize;i++) {
			
			if(i == 1) {
				result += repeatedCharacterOfLength(blockLengthByTwo*2, "-");
				result += "\n";
				result += whiteSpaceOfLength(indentLevel);
			}
			
			String str1 = trimString(column1.get(i), blockLengthByTwo); 
			result += str1+whiteSpaceOfLength(blockLengthByTwo - str1.length());
			result += "|";
			String str2 = trimString(column2.get(i), blockLengthByTwo);
			result += str2;
			
			if(i != listSize - 1) {
				result += "\n";
				result += whiteSpaceOfLength(indentLevel);
			}
			
		}
		
		return result;
	}
	
	protected String makeMultiColumnBlock(ArrayList<String> column1, ArrayList<String> column2, ArrayList<String> column3, int indentLevel, int blockLengthByThree) {
		String result = "";
		
		int listSize = Math.min(column1.size(),  column2.size());
		
		for(int i=0;i<listSize;i++) {
			
			if(i == 1) {
				result += repeatedCharacterOfLength(blockLengthByThree*3, "-");
				result += "\n";
				result += whiteSpaceOfLength(indentLevel);
			}
			
			String str1 = trimString(column1.get(i), blockLengthByThree); 
			result += str1+whiteSpaceOfLength(blockLengthByThree - str1.length());
			result += "|";
			String str2 = trimString(column2.get(i), blockLengthByThree);
			result += str2+whiteSpaceOfLength(blockLengthByThree - str2.length());
			result += "|";
			String str3 = trimString(column3.get(i), blockLengthByThree); 
			result += str3+whiteSpaceOfLength(blockLengthByThree - str3.length());
			
			if(i != listSize - 1) {
				result += "\n";
				result += whiteSpaceOfLength(indentLevel);
			}
			
		}
		
		return result;
	}
	
	protected String makeMultiColumnBlock(ArrayList<String> column1, ArrayList<String> column2, ArrayList<String> column3, ArrayList<String> column4, int indentLevel, int blockLengthByFour) {
		String result = "";
		
		int listSize = Math.min(column1.size(),  column2.size());
		
		for(int i=0;i<listSize;i++) {
			
			if(i == 1) {
				result += repeatedCharacterOfLength(blockLengthByFour*4, "-");
				result += "\n";
				result += whiteSpaceOfLength(indentLevel);
			}
			
			String str1 = trimString(column1.get(i), blockLengthByFour); 
			result += str1+whiteSpaceOfLength(blockLengthByFour - str1.length());
			result += "|";
			String str2 = trimString(column2.get(i), blockLengthByFour - 1);
			result += str2+whiteSpaceOfLength(blockLengthByFour - str2.length());
			result += "|";
			String str3 = trimString(column3.get(i), blockLengthByFour); 
			result += str3+whiteSpaceOfLength(blockLengthByFour - str3.length());
			result += "|";
			String str4 = trimString(column4.get(i), blockLengthByFour - 1);
			result += str4+whiteSpaceOfLength(blockLengthByFour - str4.length());
			
			if(i != listSize - 1) {
				result += "\n";
				result += whiteSpaceOfLength(indentLevel);
			}
			
		}
		
		return result;
	}
	
	protected String makeSimpleRow(String heading, String data) {
		String row = "|";
		row += heading+(heading.endsWith(":")?"":":");
		
		while(row.length() < headingColLength) {
			row += " ";
		}
		
		do {
			row += data.substring(0, Math.min(data.length() - 1, 48));
			
			if(data.length() > 48) {
				data = data.substring(48);
			}
			
			row += "|\n";
		} while(data.length() < 48);
		
		return row;
	}
	

}
