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
		// TODO Auto-generated method stub
		
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		HandOfCards handOfCards = new HandOfCards(deck);
		
		PlayingCard[] hand = handOfCards.getCards();
		
		for(PlayingCard card: hand){
			System.out.print(card + " ");
		}
	}

}
