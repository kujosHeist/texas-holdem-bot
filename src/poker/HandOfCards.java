package poker;

public class HandOfCards {
	
	private static int CARDS_PER_HAND = 5;
	
	private PlayingCard[] cards = new PlayingCard[CARDS_PER_HAND]; 
	
	public PlayingCard[] getCards(){
		return cards;
	}
	
	public HandOfCards(DeckOfCards deckOfCards){
		for(int i = 0; i < CARDS_PER_HAND; i++){
			cards[i] = deckOfCards.dealNext();
		}
	}	

	public static void main(String[] args) {

	}

}
