package poker;

public class PlayingCard {
	
	private String type;
	private char suit;
	private int faceValue;
	private int gameValue;
	
	public static final char HEARTS = 'H';
	public static final char DIAMONDS = 'D';
	public static final char SPADES = 'S';
	public static final char CLUBS = 'C';
	
	public PlayingCard(String type, char suit, int faceValue, int gameValue){
		this.type = type;
		this.suit = suit;
		this.faceValue = faceValue;
		this.gameValue = gameValue;
	}
	
	public String toString(){
		return type + suit;
	}
	
	public char getSuit(){
		return suit;
	}
	
	public int getFaceValue(){
		return faceValue;
	}
	
	public int getGameValue(){
		return gameValue;
		
	}
	
	
	
	
	public static void main(String[] args) {
		PlayingCard card = new PlayingCard("A", 'H', 1, 14);
		
		PlayingCard[] deck = new PlayingCard[52];
		
		System.out.println("Card: " + card.toString());
		
	}
	
	
}
