package poker;

public class HandOfCards {
	
	private static int CARDS_PER_HAND = 5;
	private static int TYPES_OF_CARDS = 13;
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
		// if we have a straight flush with the last card in the hand a 10, 
		// then we have a royal flush, since cards are sorted from highest to lowest
		return cards[CARDS_PER_HAND-1].getType() == "10" && isStraightFlush();
	} 
	
	public boolean isStraightFlush(){
		return isStraight() && isFlush();
	}
	public boolean isFourOfAKind(){
		boolean isFourOfAKindFlag = false;

		int[] gameValuesCount = getGameValuesCount();
		
		for (int i = 0; i < gameValuesCount.length; i++) {
			if(gameValuesCount[i] == 4){
				isFourOfAKindFlag = true;
				break;
			}
		}
		return isFourOfAKindFlag;
	}
	
	public boolean isThreeOfAKind(){
		boolean isTreeOfAKindFlag = false;

		int[] gameValuesCount = getGameValuesCount();
		
		for (int i = 0; i < gameValuesCount.length; i++) {
			if(gameValuesCount[i] == 3){
				isTreeOfAKindFlag  = true;
				break;
			}
		}
		return isTreeOfAKindFlag ;
	}
	
	public boolean isFullHouse(){
		return true;
	}
	public boolean isStraight(){
		boolean isStraightFlag = true;
		
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
				isStraightFlag = false;
				break;
			}
		}
		return isStraightFlag;
	}
	
	public boolean isFlush(){
		boolean isFlushFlag = true;
		PlayingCard card = cards[0];
		
		for(int i = 1; i < CARDS_PER_HAND; i++){
			PlayingCard otherCard = cards[i];
			
			if(card.getSuit() != otherCard.getSuit()){
				isFlushFlag = false;
				break;
			}
		}
		
		return isFlushFlag;
	}
	
	public boolean isTwoPair(){
		boolean isTwoPairFlag = false;

		int[] gameValuesCount = getGameValuesCount();
		int pairs = 0;
		
		for (int i = 0; i < gameValuesCount.length; i++) {
			if(gameValuesCount[i] == 2){
				pairs++;
				
			}
		}
		if(pairs == 2){
			isTwoPairFlag = true;
		}
		
		return isTwoPairFlag;
	}
	
	public boolean isOnePair(){
		return true;
	}
	
	public boolean isHighHand(){
		return true;
	}
	
	private int[] getGameValuesCount(){
		int[] gameValuesCount = new int[TYPES_OF_CARDS];
		
		for (int i = 0; i < CARDS_PER_HAND; i++) {
			gameValuesCount[cards[i].getGameValue()-2]++;
		}
		return gameValuesCount;
	}
	
	public static void main(String[] args) {
		DeckOfCards deck = new DeckOfCards();
		HandOfCards handOfCards;
		
		
		int count = 0;
		int iterations = 10000;
		for(int i = 0; i < iterations; i++){
			deck.shuffle();
			handOfCards = new HandOfCards(deck);
			boolean result = checkCards(handOfCards);
			if(result){
				count++;
			}
			
		}
		System.out.println();
		System.out.println(count + " out of " + iterations + " hands");
		
		/*
		handOfCards = new HandOfCards(
			new PlayingCard[]{
				new PlayingCard("A", 'H', 1, 14),
				new PlayingCard("K", 'H', 13, 13),
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

		*/
	}	
	
	public static boolean checkCards(HandOfCards handOfCards){
		PlayingCard[] hand = handOfCards.getCards();		
	
		if(handOfCards.isTwoPair()){
			for(PlayingCard card: hand){
				System.out.print(card + " ");
			}			
			
			System.out.println();
			return true;
		}else{
			return false;
		}
		
		
	}

}
