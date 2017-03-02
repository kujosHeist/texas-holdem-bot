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
	
	public static enum Suit {Hearts, Diamonds, Spades, Clubs};
	
	private Suit enumSuit;
	
	public PlayingCard(String type, char suit, int faceValue, int gameValue) {
		this.type = type;
		this.suit = suit;
		
		if(suit == 'H'){
			enumSuit = Suit.Hearts;
		}else if(suit == 'D'){
			enumSuit = Suit.Diamonds;				
		}else if(suit == 'S'){
			enumSuit = Suit.Spades;
		}else if(suit == 'C'){
			enumSuit = Suit.Clubs;
		}		
		
		this.faceValue = faceValue;
		this.gameValue = gameValue;
	}
	
	public String toString(){
		return type + suit;
	}
	
	public String getType(){
		return type;
	}
	
	public char getSuit(){
		return suit;
	}
	
	public Suit getEnumSuit(){
		return enumSuit;
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
		
		char[] suits = {PlayingCard.HEARTS, PlayingCard.DIAMONDS, PlayingCard.SPADES, PlayingCard.CLUBS};
		String[] types = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
		
		int counter = 0;
		for(int i = 0; i < suits.length; i++){
			for(int j = 0; j < types.length; j++){
				int gameValue = types[j] == "A" ? 14 : j + 1;
				deck[counter++] = new PlayingCard(types[j], suits[i], j + 1, gameValue);
			}
		}
		
		for(int i = 0; i < deck.length; i++){
			System.out.println(deck[i].toString());
		}
	}
	
	
}
