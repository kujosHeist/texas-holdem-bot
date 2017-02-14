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
		sort();
	}
	
	private void sort(){
		int i;
		boolean flag = true;   
		PlayingCard temp;   

		while (flag){
			flag= false; 
			for( i=0;  i < cards.length -1;  i++ ){
				if(cards[ i ].getGameValue() < cards[i+1].getGameValue()){   
					temp = cards[i];   //swap elements
					cards[i] = cards[i+1];
					cards[i+1]= temp;
					flag = true;              
				} 
			} 
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
	
	
	boolean isRoyalFlush(){
		return true;
	} 
	
	boolean isStraightFlush(){
		return true;
	}
	boolean isFourOfAKind(){
		return true;
	}
	
	boolean isThreeOfAKind(){
		return true;
	}
	
	boolean isFullHouse(){
		return true;
	}
	boolean isStraight(){
		return true;
	}
	boolean isFlush(){
		return true;
	}
	
	boolean isTwoPair(){
		return true;
	}
	
	boolean isOnePair(){
		return true;
	}
	
	boolean isHighHand(){
		return true;
	}
	

}
