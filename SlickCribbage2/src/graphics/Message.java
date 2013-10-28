package graphics;


import java.util.ArrayList;

public class Message {
	String[] lines;
	
	public Message(String s){
		String[] words = s.split(" ");
		lines = parseWords(words);
	}
	
	public String[] parseWords(String[] words){
		ArrayList<String> lines = new ArrayList<String>();
		
		String s = "";
		for (int i = 0; i < words.length; i ++){
			if (s.length() + words[i].length() + 1 < 32){
				s += words[i] + " ";
			}
			else {
				lines.add(s);
				s = words[i] + " ";
			}
			
			if (i == words.length - 1)
				lines.add(s);
		}
		String[] arrLines = new String[lines.size()];
		for (int i = 0; i < lines.size(); i ++){
			arrLines[i] = lines.get(i);
		}
		return arrLines;
		
	}
	
	public boolean equals(Message m){
		boolean equals = true;
		
		if (lines.length != m.getLines().length) return false;
		for (int i = 0; i < lines.length; i ++){
			if (!lines[i].equals(m.getLines()[i]))
				equals = false;
		}
		return equals;
	}
	
	public String[] getLines(){
		return lines;
	}
	
	public static void main(String[] args){
		Message m = new Message("Click on the community card in order to cut it. After that select two cards to discard");
		
		for (String s: m.getLines()){
			System.out.println(s);
		}
	}
}
