package customclasses;

public class Setw {
	public static String setw(String str, int width) {
		String output = "";
		output += str;
		int spaces = width - str.length();
		for(int i=0; i<spaces; i++)
			output += " ";
		return output;
	}
}
