package poker;

public class PokerPlayer {

	
	private DeckOfCards deckOfCards;
	
	private HandOfCards handOfCards;
	
	public PokerPlayer(DeckOfCards deckOfCards){
		this.deckOfCards = deckOfCards;
		this.handOfCards = new HandOfCards(deckOfCards);
	}
	
	public int discard(){
		return 0;
	}
}
