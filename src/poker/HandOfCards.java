package poker;

public class HandOfCards {
	
	private static int CARDS_PER_HAND = 5;
	private PlayingCard[] cards = new PlayingCard[CARDS_PER_HAND]; 
	
	public HandOfCards(DeckOfCards deckOfCards){
		for(int i = 0; i < CARDS_PER_HAND; i++){
			cards[i] = deckOfCards.dealNext();
		}
		sort();
	}
	
	public HandOfCards(PlayingCard[] cards){
		this.cards = cards;
		sort();
	}
	
	public PlayingCard[] getCards(){
		return cards;
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
	
	public boolean isRoyalFlush(){
		return true;
	} 
	
	public boolean isStraightFlush(){
		return true;
	}
	public boolean isFourOfAKind(){
		return true;
	}
	
	public boolean isThreeOfAKind(){
		return true;
	}
	
	public boolean isFullHouse(){
		return true;
	}
	public boolean isStraight(){
		boolean straightFlag = true;
		
		for(int i = 0, j = 1; i < CARDS_PER_HAND - 1; i++, j++){
			PlayingCard card1 = cards[i];
			PlayingCard card2 = cards[j];
			
			int gameValue1 = card1.getGameValue(); 
			int gameValue2 = card2.getFaceValue();
			
			// this accounts for runs from low ACE to 5
			// which after sorting will be in the order: A 5 4 3 2
			if(card1.getType() == "A" && gameValue2 == 5){ 
				continue;
			}
			
			if(gameValue1 == gameValue2 + 1){ 
				continue;
			}else{
				straightFlag = false;
				break;
			}
		}
		
		return straightFlag;
	}
	
	public boolean isFlush(){
		return true;
	}
	
	public boolean isTwoPair(){
		return true;
	}
	
	public boolean isOnePair(){
		return true;
	}
	
	public boolean isHighHand(){
		return true;
	}
	
	public static void main(String[] args) {
		DeckOfCards deck = new DeckOfCards();
		HandOfCards handOfCards;
		
		for(int i = 0; i < 1000; i++){
			deck.shuffle();
			handOfCards = new HandOfCards(deck);
			checkCards(handOfCards);
		}
		
		
		handOfCards = new HandOfCards(
			new PlayingCard[]{
				new PlayingCard("A", 'H', 1, 14),
				new PlayingCard("K", 'C', 13, 13),
				new PlayingCard("Q", 'H', 12, 12),
				new PlayingCard("J", 'H', 11, 11),
				new PlayingCard("10", 'H', 10, 10)
		});
		checkCards(handOfCards);
		
		handOfCards = new HandOfCards(
			new PlayingCard[]{
				new PlayingCard("A", 'H', 1, 14),
				new PlayingCard("A", 'C', 1, 14),
				new PlayingCard("2", 'H', 2, 2),
				new PlayingCard("3", 'H', 3, 3),
				new PlayingCard("4", 'H', 4, 4)
		});
		checkCards(handOfCards);			
		
		handOfCards = new HandOfCards(
			new PlayingCard[]{
				new PlayingCard("A", 'H', 1, 14),
				new PlayingCard("2", 'C', 2, 2),
				new PlayingCard("3", 'H', 3, 3),
				new PlayingCard("4", 'H', 4, 4),
				new PlayingCard("5", 'H', 5, 5)
		});		
		checkCards(handOfCards);
		
		handOfCards = new HandOfCards(
			new PlayingCard[]{
				new PlayingCard("2", 'H', 1, 2),
				new PlayingCard("3", 'C', 3, 3),
				new PlayingCard("3", 'H', 3, 3),
				new PlayingCard("4", 'H', 4, 4),
				new PlayingCard("5", 'H', 5, 5)
		});		
		checkCards(handOfCards);			

		
	}	
	
	public static void checkCards(HandOfCards handOfCards){
		PlayingCard[] hand = handOfCards.getCards();		
		

		
		if(handOfCards.isStraight()){
			for(PlayingCard card: hand){
				System.out.print(card + " ");
			}			
			System.out.print("[True]");
			System.out.println();
		}else{
			//System.out.println("[False]");
		}		
		
		
	}

}
