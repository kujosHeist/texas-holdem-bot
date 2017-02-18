package poker;

public class HandOfCards {
	
	public static int HIGH_HAND = 0;
	public static int ONE_PAIR = 1;
	public static int TWO_PAIR = 2;
	public static int THREE_OF_A_KIND = 3;
	public static int STRAIGHT = 4;
	public static int FLUSH = 5;
	public static int FULL_HOUSE = 6;
	public static int FOUR_OF_A_KIND = 7;
	public static int STRAIGHT_FLUSH = 8;
	public static int ROYAL_FLUSH = 9;	
	
	private static int CARDS_PER_HAND = 5;
	private static int TYPES_OF_CARDS = 13; // A 2 3 ... J Q K
	private PlayingCard[] cards = new PlayingCard[CARDS_PER_HAND];
	private DeckOfCards deckOfCards;
	
	// main constructor which deals 5 cards into HandOfCards
	public HandOfCards(DeckOfCards deckOfCards){
		this.deckOfCards = deckOfCards;
		for(int i = 0; i < CARDS_PER_HAND; i++){
			cards[i] = deckOfCards.dealNext();
		}
		sort();
	}
	
	// Extra constructor for testing specific types of hand
	public HandOfCards(PlayingCard[] cards){
		this.cards = cards;
		sort();
	}
	
	public DeckOfCards getDeckOfCards(){
		return deckOfCards;
	}
	
	// based on bubble sort, this method orders the cards from highest to lowest based on game value
	private void sort(){
		boolean sorted = false;   
		
		// keeps looping until cards in in correct order
		while (!sorted){
			sorted= true; 
			PlayingCard temp;   
			
			for(int i=0;  i < cards.length -1;  i++){
				
				if(cards[i].getGameValue() < cards[i+1].getGameValue()){   
					// if first card is lower then next card in array, then its not yet sorted
					// so we set flag to false
					sorted = false;
					// then swap the cards positions in array
					temp = cards[i];   
					cards[i] = cards[i+1];
					cards[i+1]= temp;
				} 
			} 
		}		
	}

	// hand is a royal flush if it is straight flush and the last card in the array is a 10, 
	// since cards are sorted from highest to lowest, then 10 must be last card
	public boolean isRoyalFlush(){
		return cards[CARDS_PER_HAND-1].getType() == "10" && isStraightFlush();
	} 
	
	// hand is a straight flush if it is straight and a flush
	public boolean isStraightFlush(){
		return isStraight() && isFlush();
	}
	
	
	public boolean isFourOfAKind(){
		boolean isFourOfAKindFlag = false;
		
		// returns an array where each item in the array is an int
		// representing the number of that type of card in the hand
		int[] gameValuesCount = getGameValuesCount();
		
		// loops through array and checks if there are four of any type
		for (int i = 0; i < gameValuesCount.length; i++) {
			if(gameValuesCount[i] == 4){
				isFourOfAKindFlag = true; // sets flag to true and breaks from loop
				break;
			}
		}
		return isFourOfAKindFlag;
	}
	
	// similar to isFourOfAKind
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
	
	// full house is a hand with a three of a kind and a pair
	public boolean isFullHouse(){
		return isThreeOfAKind() && isOnePair();
	}
	
	public boolean isStraight(){
		boolean isStraightFlag = true;
		
		// loops through hand and checks if each card one higher than the next card
		for(int i = 0, j = 1; i < CARDS_PER_HAND - 1; i++, j++){
			PlayingCard card1 = cards[i];
			PlayingCard card2 = cards[j];
			
			int gameValue1 = card1.getGameValue(); 
			int gameValue2 = card2.getFaceValue();
			
			// this accounts for runs from ACE to 5
			// which after sorting will be in the order: A 5 4 3 2
			if(card1.getType() == "A" && gameValue2 == 5){ 
				continue;
			}
			
			// checks if the the first card is one higher than the next card
			if(gameValue1 == gameValue2 + 1){ 
				continue;
			}else{
				// if not then we can set flag to false and break
				isStraightFlag = false;
				break;
			}
		}
		return isStraightFlag;
	}
	
	
	public boolean isFlush(){
		boolean isFlushFlag = true;
		
		// takes first card from the hand
		PlayingCard card = cards[0];
		
		// loops through remaing cards
		for(int i = 1; i < CARDS_PER_HAND; i++){
			PlayingCard otherCard = cards[i];
			
			// checks is the suit of each remaining card the same as the suit of the first card
			if(card.getSuit() != otherCard.getSuit()){
				// if not we set flag to false and break, since all cards aren't of the same suit
				isFlushFlag = false;
				break;
			}
		}
		
		return isFlushFlag;
	}
	
	public boolean isTwoPair(){
		boolean isTwoPairFlag = false;

		// gets array of the number of each type of cards
		int[] gameValuesCount = getGameValuesCount();
		
		int pairs = 0; // keeps track of number of pairs
		
		for (int i = 0; i < gameValuesCount.length; i++) {
			if(gameValuesCount[i] == 2){ // if pair is found we increment variable
				pairs++;
				
			}
		}
		// returns true if exactly two pairs
		if(pairs == 2){
			isTwoPairFlag = true;
		}
		
		return isTwoPairFlag;
	}
	
	// similar to isTwoPair except checks for exactly one pair
	public boolean isOnePair(){
		boolean isOnePairFlag = false;

		int[] gameValuesCount = getGameValuesCount();
		int pairs = 0;
		
		for (int i = 0; i < gameValuesCount.length; i++) {
			if(gameValuesCount[i] == 2){
				pairs++;
			}
		}
		if(pairs == 1){
			isOnePairFlag = true;
		}
		
		return isOnePairFlag;
	}
	
	
	public boolean isHighHand(){
		boolean isHighHand = true;

		int[] gameValuesCount = getGameValuesCount();
	
		for (int i = 0; i < gameValuesCount.length; i++) {
			
			// checks if there is more than 1 of any type of card in the hand, if so it returns false
			if(gameValuesCount[i] > 1){
				isHighHand = false;
			}
		}
		
		return isHighHand;		
	}
	
	// prints out the hand
	public String toString(){
		String handString = "";
		for(PlayingCard card: cards){
			handString += card.toString() + " ";
		}	
		return handString;
	}
	
	// returns an array where each item in the array is an int
	// representing the number of that type of card in the hand
	private int[] getGameValuesCount(){
		int[] gameValuesCount = new int[TYPES_OF_CARDS];
		
		for (int i = 0; i < CARDS_PER_HAND; i++) {
			// index subtracted by one since face value go from 1-14, and our array goes from 0-13
			int index = cards[i].getFaceValue()-1;  
			// increments the number of this cards face value by 1
			gameValuesCount[index]++;  
		}
		return gameValuesCount;
	}
	
	public int gameValue(){
		return 0;
	}
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		DeckOfCards deck = new DeckOfCards();
		int iterations = 1000000;
		
		runMultipleHandTypeTest(iterations, deck, "HighHand");
		runMultipleHandTypeTest(iterations, deck, "OnePair");
		runMultipleHandTypeTest(iterations, deck, "TwoPair");
		runMultipleHandTypeTest(iterations, deck, "ThreeOfAKind");
		runMultipleHandTypeTest(iterations, deck, "Straight");
		runMultipleHandTypeTest(iterations, deck, "Flush");
		runMultipleHandTypeTest(iterations, deck, "FullHouse");
		runMultipleHandTypeTest(iterations, deck, "FourOfAKind");
		runMultipleHandTypeTest(iterations, deck, "StraightFlush");
		runMultipleHandTypeTest(iterations, deck, "RoyalFlush");
		
		
		long endTime = System.currentTimeMillis();
		long timeDifference = endTime - startTime;
		System.out.println("Test took " + (float)timeDifference/1000.0 + " to run");
	}	
	
	public static boolean runCustomHandTypeTest(){
		HandOfCards handOfCards = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("A", 'H', 1, 14),
				new PlayingCard("K", 'H', 13, 13),
				new PlayingCard("Q", 'H', 12, 12),
				new PlayingCard("J", 'H', 11, 11),
				new PlayingCard("10", 'H', 10, 10)
		});
		return HandOfCards.checkCards(handOfCards, "Straight");		
	}
	
	public static void runMultipleHandTypeTest(int iterations, DeckOfCards deck, String handType){
		int count = 0;	
		
		// deals 10000 hands and checks for type of hand
		for(int i = 0; i < iterations; i++){
			deck.shuffle();
			HandOfCards handOfCards = new HandOfCards(deck);
					
			// checks if hand is a tree of a kind
			boolean result = checkCards(handOfCards, handType);
			if(result){
				count++;
			}				
		}
		
		
		System.out.println(count + " out of " + iterations + " hands have a " + handType);				
	}
	
	// static method checking the type of hand
	public static boolean checkCards(HandOfCards handOfCards, String handType){
		
		boolean typeOfHandFlag = false;
		if(handType == "HighHand"){
			if(handOfCards.isHighHand()){
				typeOfHandFlag = true;
			}
			
		}else if(handType == "OnePair"){
			if(handOfCards.isOnePair()){
				typeOfHandFlag = true;
			}
			
		}else if(handType == "TwoPair"){
			if(handOfCards.isTwoPair()){
				typeOfHandFlag = true;
			}
			
		}else if(handType.equals("ThreeOfAKind")){
			if(handOfCards.isThreeOfAKind()){
				typeOfHandFlag = true;
			}
			
		}else if(handType.equals("Straight")){
			if(handOfCards.isStraight()){
				typeOfHandFlag = true;
			}		
		}
		
		else if(handType.equals("Flush")){
			if(handOfCards.isFlush()){
				typeOfHandFlag = true;
			}		
		}
		
		else if(handType.equals("FullHouse")){
			if(handOfCards.isFullHouse()){
				typeOfHandFlag = true;
			}			
		}
		
		else if(handType.equals("FourOfAKind")){
			if(handOfCards.isFourOfAKind()){
				typeOfHandFlag = true;
			}			
		}
		
		else if(handType.equals("StraightFlush")){
			if(handOfCards.isStraightFlush()){
				typeOfHandFlag = true;
			}		
		}
		
		else if(handType.equals("RoyalFlush")){
			if(handOfCards.isRoyalFlush()){
				typeOfHandFlag = true;
			}			
		}		
		return typeOfHandFlag;
	}

}
