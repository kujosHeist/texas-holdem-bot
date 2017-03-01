package poker;

import java.util.Map;
import java.util.TreeMap;

public class HandOfCards {
	// enum to store the String value of each type of hand, also used to generate Default hand game value in PokerHand
	public static enum Type {HighHand, OnePair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse, FourOfAKind, StraightFlush, RoyalFlush};
	
	private static int CARDS_PER_HAND = 5;
	private static int TYPES_OF_CARDS = 13; // A 2 3 ... J Q K
	private PlayingCard[] cards = new PlayingCard[CARDS_PER_HAND];
	private DeckOfCards deckOfCards;
	
	private int[] gameValuesCountInHandArray = new int[TYPES_OF_CARDS]; // array which stores the number of each type of card in a hand
	
	// main constructor which deals 5 cards into HandOfCards
	public HandOfCards(DeckOfCards deckOfCards){
		this.deckOfCards = deckOfCards;
		for(int i = 0; i < CARDS_PER_HAND; i++){
			cards[i] = deckOfCards.dealNext();
		}
		sort();
		
		// returns an array where each item in the array is an int
		// representing the number of that type of card in the hand
		gameValuesCountInHandArray = getGameValueCountInHand();		
	}
	
	// Extra constructor for testing specific types of hand
	public HandOfCards(PlayingCard[] cards){
		this.cards = cards;
		sort();
		gameValuesCountInHandArray = getGameValueCountInHand();		
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
		boolean hasStraightFlag = true;
		
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
				hasStraightFlag = false;
				break;
			}
		}	
		return hasStraightFlag && isFlush();
	}
	
	
	public boolean isFourOfAKind(){
		boolean isFourOfAKindFlag = false;
				
		// loops through array and checks if there are four of any type
		for (int i = 0; i < TYPES_OF_CARDS; i++) {
			if(gameValuesCountInHandArray[i] == 4){
				isFourOfAKindFlag = true; // sets flag to true and breaks from loop
				break;
			}
		}
		return isFourOfAKindFlag;
	}
	
	// similar to isFourOfAKind
	public boolean isThreeOfAKind(){
		boolean isThreeOfAKindFlag = false;
		
		for (int i = 0; i < TYPES_OF_CARDS; i++) {
			if(gameValuesCountInHandArray[i] == 3){
				isThreeOfAKindFlag  = true;
				break;
			}
		}
		return isThreeOfAKindFlag && !isOnePair();
	}
	
	// full house is a hand with a three of a kind and a pair
	public boolean isFullHouse(){
		
		boolean hasThreeOfAKindFlag = false;
		
		for (int i = 0; i < TYPES_OF_CARDS; i++) {
			if(gameValuesCountInHandArray[i] == 3){
				hasThreeOfAKindFlag  = true;
				break;
			}
		}		
		return hasThreeOfAKindFlag && isOnePair();
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
		return isStraightFlag && !isFlush();
	}
	
	
	public boolean isFlush(){
		boolean isFlushFlag = true;
		
		// takes first card from the hand
		PlayingCard card = cards[0];
		
		// loops through remaining cards
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
		
		int pairs = 0; // keeps track of number of pairs
		
		for (int i = 0; i < TYPES_OF_CARDS; i++) {
			if(gameValuesCountInHandArray[i] == 2){ // if pair is found we increment variable
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
		int pairs = 0;
		
		for (int i = 0; i < TYPES_OF_CARDS; i++) {
			if(gameValuesCountInHandArray[i] == 2){
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
	
		for (int i = 0; i < TYPES_OF_CARDS; i++) {
			
			// checks if there is more than 1 of any type of card in the hand, if so it returns false
			if(gameValuesCountInHandArray[i] > 1){
				isHighHand = false;
			}
		}
		return isHighHand;		
	}
	
	// prints hand to console
	public String toString(){
		String handString = "";
		for(PlayingCard card: cards){
			handString += card.toString() + " ";
		}	
		return handString;
	}
	
	// returns an array where each item in the array is an int
	// representing the number of that type of card in the hand
	private int[] getGameValueCountInHand(){
		int[] gameValuesCountInHandArray = new int[TYPES_OF_CARDS];
		
		for (int i = 0; i < CARDS_PER_HAND; i++) {
			// index subtracted by two since game values go from 2-14, and our array's index's goes from 0-12
			int index = cards[i].getGameValue()-2;  
			// increments the number of this cards game value by 1
			gameValuesCountInHandArray[index]++;  
		}
		return gameValuesCountInHandArray;
	}
	
	// returns an integer value represeting the game value of a hand of cards, which can be used to
	// order and two hands of cards according to the rules of poker
	public int getGameValue(){
		int factor = 14;  // is used to multiple with game value of cards in hand
	
		if(isRoyalFlush()){
			// all royal flushes are equal value, so no additional game value component needed
			
			int defaultValue = PokerHand.ROYAL_FLUSH_DEFAULT;
			int additionalGameValueComponent = 0;  
			return defaultValue + additionalGameValueComponent;
		}
		else if(isStraightFlush()){
			// straight flush is decided by the hand with runs to the highest value
			
			int defaultValue = PokerHand.STRAIGHT_FLUSH_DEFAULT;
			PlayingCard highCardInStraightFlush = cards[0]; // gets the highest card in the straight flush (since hand is sorted from high to low)
			int additionalGameValueComponent = (int) Math.pow(highCardInStraightFlush.getGameValue(), 5);    
			return defaultValue + additionalGameValueComponent;
		}		
		else if(isFourOfAKind()){
			// four of a kinds are distinguished by the game value of their 4 of a kind card
			
			int defaultValue = PokerHand.FOUR_OF_A_KIND_DEFAULT;
			int gameValueOfFourOfAKindCard = getGameValueOfCardWithCount(4);
			int additionalGameValueComponent = (int) Math.pow(gameValueOfFourOfAKindCard, 4); 
			return defaultValue + additionalGameValueComponent;
		}		
		
		else if(isFullHouse()){ 
			// Full houses are ranked by the highest Three of a kind card, 
			// It is not possible for two players to have the same 3 of a kind, so we can simply rank the hands by the game value multiplied by a factor 
			// and ignore the pair
			
			int defaultValue = PokerHand.FULL_HOUSE_DEFAULT;
			int gameValueOfThreeOfAKind = getGameValueOfCardWithCount(3);
			int additionalGameValueComponent = ((int) Math.pow(factor, 3)) * gameValueOfThreeOfAKind;
			return defaultValue + additionalGameValueComponent; 
		}	
		
		else if(isFlush()){
			// flushes are ranked by highest card in flush, if there is a match then we check the next card, then the next etc.
			// To rank these properly, we multiple the game value by the cards from high to low by a factor raised to a power, which reduces as we go down the hand to
			// the lower cards, this gives us a correct additionalGameValueComponent
			
			int defaultValue = PokerHand.FLUSH_DEFAULT;
			int additionalGameValueComponent = 0;
			
			int power = 4;
			for (int i = 0; i < cards.length; i++) {
				additionalGameValueComponent += ((int) Math.pow(factor, power--)) * cards[i].getGameValue();
			}
             
			return defaultValue + additionalGameValueComponent;
		}		
		
		else if(isStraight()){
			// Straight's are distinguished by the game value of their highest card
			
			int defaultValue = PokerHand.STRAIGHT_DEFAULT;
			PlayingCard highCardInStraight = cards[0]; 
			int additionalGameValueComponent = (int) Math.pow(highCardInStraight.getGameValue(), 5);  
			return defaultValue + additionalGameValueComponent;
		}	
		
		else if(isThreeOfAKind()){
			// Three of a kind hands are ranked by game value of the three of a kind card, 
			// It is not possible for two players to have the same 3 of a kind, so we can simply rank the hands by the game value multiplied by a factor 
			
			int defaultValue = PokerHand.THREE_OF_A_KIND_DEFAULT;
			int gameValueOfThreeOfAKind = getGameValueOfCardWithCount(3);
			int additionalGameValueComponent = ((int) Math.pow(factor, 3)) * gameValueOfThreeOfAKind;;
			return defaultValue + additionalGameValueComponent;
		}	
		else if(isTwoPair()){
			// Two pair hands are ranked by game value of high pair, then low pair, if there is a match then we check the remaining card. 
			// To rank these properly, we multiple the game value of high pair by the factor squared, then the lower pair by the factor, 
			// then we add the game value of the remaining card				
			
			int defaultValue = PokerHand.TWO_PAIR_DEFAULT;
			int gameValueOfHighPairCard = 0;
			
			// loops through games value count array and gets game value of high pair
			for (int cardIndex = TYPES_OF_CARDS - 1; cardIndex >= 0; cardIndex--) {
				if(gameValuesCountInHandArray[cardIndex] == 2){
					gameValueOfHighPairCard = cardIndex + 2; // adds two to card index as array goes from 0-12, while card game values go from 2-14
					break;
				}
			}
			
			int gameValueOfLowPairCard = getGameValueOfCardWithCount(2); // returns low pair since it checks from start of array of game value card counts
			
			int gameValueOfOtherCard = 0;
			for (int i = 0; i < cards.length; i++) {
				if(cards[i].getGameValue() != gameValueOfHighPairCard || cards[i].getGameValue() != gameValueOfLowPairCard){
					gameValueOfOtherCard = cards[i].getGameValue();
					break;
				}
			}
			
			int additionalGameValueComponent = ((int) Math.pow(factor, 2)) * gameValueOfHighPairCard + factor * gameValueOfLowPairCard + gameValueOfOtherCard;
			return defaultValue + additionalGameValueComponent; 
		}		
		else if(isOnePair()){
			// One pair hands are ranked by game value of pair, if there is a match then we check the next card, then the next etc.
			// To rank these properly, we multiple the game value of the pair by a factor raised to a power which reduces as we go down the hand to
			// the lower cards, this gives us a correct additionalGameValueComponent	
			
			int power = 3;
			
			int defaultValue = PokerHand.ONE_PAIR_DEFAULT;
			int gameValueOfPairCard = getGameValueOfCardWithCount(2);
						
			int additionalGameValueComponent = ((int) Math.pow(factor, power--)) * gameValueOfPairCard;
			
			for (int i = 0; i < cards.length; i++) {
				if(cards[i].getGameValue() !=  gameValueOfPairCard){
					additionalGameValueComponent += ((int) Math.pow(factor, power--)) * cards[i].getGameValue();	
				}
				
			}
             
			return defaultValue + additionalGameValueComponent; 
		}		
		else{
			// It is high hand
			// high hands are ranked by highest card, if there is a match then we check the next card, then the next etc.
			// To rank these properly, we multiple the game value by the cards from high to low by a factor raised to a power, which reduces as we go down the hand to
			// the lower cards, this gives us a correct additionalGameValueComponent
			
			int defaultValue = PokerHand.HIGH_HAND_DEFAULT;
			int additionalGameValueComponent = 0;
			
			int power = 4;
			for (int i = 0; i < cards.length; i++) {
				additionalGameValueComponent += ((int) Math.pow(factor, power--)) * cards[i].getGameValue();
			}
			return defaultValue + additionalGameValueComponent;             
		}		
	}
	
	// returns the game value of the card which has a certain count in hand
	// i.e. if a hand has 3 Aces, then this method returns 14
	private int getGameValueOfCardWithCount(int countOfCardInHand){
		
		// loops through array and checks if there are four of any type
		for (int cardIndex = 0; cardIndex < TYPES_OF_CARDS; cardIndex++) {
			if(gameValuesCountInHandArray[cardIndex] == countOfCardInHand){
				return cardIndex + 2; // adds two to card index as array goes from 0-12, while card game values go from 2-14
			}
		}
		return 0;
	}	
	
	public static void main(String[] args) {	
		long startTime = System.currentTimeMillis();
		
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		
		// simple test:
		
		HandOfCards hand1 = new HandOfCards(deck);
		HandOfCards hand2 = new HandOfCards(deck);
		System.out.println("Player1: " + hand1.toString() + " " + HandOfCards.checkCards(hand1) );
		System.out.println("Player2: " + hand2.toString() + " " + HandOfCards.checkCards(hand2));
		
		if(hand1.getGameValue() > hand2.getGameValue()){
			System.out.println("Player1 wins");
		}else if(hand2.getGameValue() > hand1.getGameValue()){
			System.out.println("Player2 wins");
		}else{
			System.out.println("Draw!");
		}
		System.out.println();
		
	
		// In order to hands of the same type are sorted correctly by their gameValue
		// these method calls deal x amount of hands and checks if it is the hand type we pass as an argument.
		// then it stores up to the first 10 of each hand type, and sorts them from lowest to highest
		// finally it prints them out to the console, which can be inspected to ensure they are sorted in the correct order
		// we don't test for royal flush, as all royal flushes return the same value game value
		// this test can take a while to run (10-15 seconds), as we need to deal 100,000 hands to get a good amount of each hand type
		
		int iterations = 100000;
		runMultipleHandTypeTest(iterations, deck, Type.HighHand);
		runMultipleHandTypeTest(iterations, deck, Type.OnePair);
		runMultipleHandTypeTest(iterations, deck, Type.TwoPair);
		runMultipleHandTypeTest(iterations, deck, Type.ThreeOfAKind);
		runMultipleHandTypeTest(iterations, deck, Type.Straight);
		runMultipleHandTypeTest(iterations, deck, Type.Flush);
		runMultipleHandTypeTest(iterations, deck, Type.FullHouse);
		runMultipleHandTypeTest(iterations, deck, Type.FourOfAKind);
		runMultipleHandTypeTest(iterations, deck, Type.StraightFlush);
		
		
		
		// To Show the different hand types are sorted properly, we deal 100 random hands, sort them by their game value
		// then print them out with handtype along side, these can then be inspected to ensure they are ordered correctly
		// we can also note the same hand types are also ordered correctly
		// we can change the iterations to a much higher value in order to generate the more rare hand types
		
		System.out.println("Printing randomly dealt hands, sorted by gameValue");
		System.out.println("**************************************************");
		Map<Integer, String> results = new TreeMap<Integer, String>();
		
		iterations = 100;
		for(int i = 0; i < iterations; i++){
			deck.shuffle();
			HandOfCards handOfCards = new HandOfCards(deck);
			HandOfCards.Type handType = HandOfCards.checkCards(handOfCards); 
			results.put(handOfCards.getGameValue(), handOfCards.toString() + " " + handType);
		}
		
		for(Integer key: results.keySet()){
			System.out.println(results.get(key) + "  " + key);
		}
		System.out.println();		
		
		
		long endTime = System.currentTimeMillis();
		long timeDifference = endTime - startTime;
		System.out.println("Test took " + (float)timeDifference/1000.0 + " to run");
	}	
	
	
	
	
	
	
	//**************************************************************************************************
	// Static methods to help with testing *************************************************************
	//**************************************************************************************************

	public static void runMultipleHandTypeTest(int iterations, DeckOfCards deck, Type handType){
		int count = 0;
		Map<Integer, String> results = new TreeMap<Integer, String>();
		
		System.out.println("Checking for hand type: " + handType);
		System.out.println("**********************************");

		// deals 10000 hands and checks for type of hand
		for(int i = 0; i < iterations && count < 10; i++){
			deck.shuffle();
			HandOfCards handOfCards = new HandOfCards(deck);
			
			if(HandOfCards.checkCards(handOfCards, handType)){
				results.put(handOfCards.getGameValue(), handOfCards.toString());
				count++;
			}		
		}
		
		for(Integer key: results.keySet()){
			System.out.println(results.get(key) + "  " + key);
		}
		System.out.println();
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
		return HandOfCards.checkCards(handOfCards, Type.Straight);		
	}	
	
	// static method checking the type of hand
	public static boolean checkCards(HandOfCards handOfCards, Type handType){
		
		boolean typeOfHandFlag = false;
		
		if(handType.equals(Type.RoyalFlush)){
			if(handOfCards.isRoyalFlush()){
				typeOfHandFlag = true;
			}			
		}	
		else if(handType.equals(Type.StraightFlush)){
			if(handOfCards.isStraightFlush()){
				typeOfHandFlag = true;
			}		
		}		
		else if(handType.equals(Type.FourOfAKind)){
			if(handOfCards.isFourOfAKind()){
				typeOfHandFlag = true;
			}			
		}		
		else if(handType.equals(Type.FullHouse)){
			if(handOfCards.isFullHouse()){
				typeOfHandFlag = true;
			}			
		}	
		else if(handType.equals(Type.Flush)){
			if(handOfCards.isFlush()){
				typeOfHandFlag = true;
			}		
		}		
		else if(handType.equals(Type.Straight)){
			if(handOfCards.isStraight()){
				typeOfHandFlag = true;
			}		
		}		
		else if(handType.equals(Type.ThreeOfAKind)){
			if(handOfCards.isThreeOfAKind()){
				typeOfHandFlag = true;
			}
			
		}
		else if(handType == Type.TwoPair){
			if(handOfCards.isTwoPair()){
				typeOfHandFlag = true;
			}
			
		}
		else if(handType == Type.OnePair){
			if(handOfCards.isOnePair()){
				typeOfHandFlag = true;
			}
		}		
		
		else if(handType == Type.HighHand){
			if(handOfCards.isHighHand()){
				typeOfHandFlag = true;
			}	
		}
		return typeOfHandFlag;
	}
	
	// static method checking the type of hand
	public static Type checkCards(HandOfCards handOfCards){
			if(handOfCards.isRoyalFlush()){
				return HandOfCards.Type.RoyalFlush;
			}		
			else if(handOfCards.isStraightFlush()){
				return HandOfCards.Type.StraightFlush;
			}			
			else if(handOfCards.isFourOfAKind()){
				return HandOfCards.Type.FourOfAKind;
			}					
			else if(handOfCards.isFullHouse()){
				return HandOfCards.Type.FullHouse;
			}	
			else if(handOfCards.isFlush()){
				return HandOfCards.Type.Flush;
			}
			else if(handOfCards.isStraight()){
				return HandOfCards.Type.Straight;
			}	
			else if(handOfCards.isThreeOfAKind()){
				return HandOfCards.Type.ThreeOfAKind;
			}	
			else if(handOfCards.isTwoPair()){
				return HandOfCards.Type.TwoPair;
			}
			else if(handOfCards.isOnePair()){
				return HandOfCards.Type.OnePair;
			}
			else{
				return HandOfCards.Type.HighHand;
			}						
	}	

}
