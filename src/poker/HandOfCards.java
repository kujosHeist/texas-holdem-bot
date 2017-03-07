package poker;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class HandOfCards {
	// enum to store the String value of each type of hand, also used to generate Default hand game value in PokerHand
	public static enum Type {HighHand, OnePair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse, FourOfAKind, StraightFlush, RoyalFlush};
	
	
	private static int DEFAULT_HAND_VALUE = 1000000;
	
	// defines default value for each hand type, based on their order in HandOfCards enum Type (from 0-9)
	// therefore default hand values range from 0 (for high hand) to 9000 (for royal flush) 
	public static int HIGH_HAND_DEFAULT = Type.HighHand.ordinal() * DEFAULT_HAND_VALUE;
	public static int ONE_PAIR_DEFAULT = Type.OnePair.ordinal() * DEFAULT_HAND_VALUE;
	public static int TWO_PAIR_DEFAULT = Type.TwoPair.ordinal() * DEFAULT_HAND_VALUE;
	public static int THREE_OF_A_KIND_DEFAULT = Type.ThreeOfAKind.ordinal() * DEFAULT_HAND_VALUE;
	public static int STRAIGHT_DEFAULT = Type.Straight.ordinal() * DEFAULT_HAND_VALUE;
	public static int FLUSH_DEFAULT = Type.Flush.ordinal() * DEFAULT_HAND_VALUE;
	public static int FULL_HOUSE_DEFAULT = Type.FullHouse.ordinal() * DEFAULT_HAND_VALUE;
	public static int FOUR_OF_A_KIND_DEFAULT = Type.FourOfAKind.ordinal() * DEFAULT_HAND_VALUE;
	public static int STRAIGHT_FLUSH_DEFAULT = Type.StraightFlush.ordinal() * DEFAULT_HAND_VALUE;
	public static int ROYAL_FLUSH_DEFAULT = Type.RoyalFlush.ordinal() * DEFAULT_HAND_VALUE;		
	
	private static int CARDS_PER_HAND = 5;
	private static int TYPES_OF_CARDS = 13; // A 2 3 ... J Q K
	private PlayingCard[] cards = new PlayingCard[CARDS_PER_HAND];
	private DeckOfCards deckOfCards;
	
	private int[] gameValuesInHandCountArray = new int[TYPES_OF_CARDS]; // array which stores the number of each type of card in a hand
	
	// main constructor which deals 5 cards into HandOfCards
	public HandOfCards(DeckOfCards deckOfCards){
		this.deckOfCards = deckOfCards;
		for(int i = 0; i < CARDS_PER_HAND; i++){
			cards[i] = deckOfCards.dealNext();
		}
		sort();
		
		// returns an array where each item in the array is an int
		// representing the number of that type of card in the hand
		gameValuesInHandCountArray = getGameValueCountInHand();		
	}
	
	// Extra constructor for testing specific types of hand
	public HandOfCards(PlayingCard[] cards){
		this.cards = cards;
		sort();
		gameValuesInHandCountArray = getGameValueCountInHand();		
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
			if(gameValuesInHandCountArray[i] == 4){
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
			if(gameValuesInHandCountArray[i] == 3){
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
			if(gameValuesInHandCountArray[i] == 3){
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
			if(gameValuesInHandCountArray[i] == 2){ // if pair is found we increment variable
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
			if(gameValuesInHandCountArray[i] == 2){
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
			if(gameValuesInHandCountArray[i] > 1){
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
			
			int defaultValue = ROYAL_FLUSH_DEFAULT;
			int additionalGameValueComponent = 0;  
			return defaultValue + additionalGameValueComponent;
		}
		else if(isStraightFlush()){
			// straight flush is decided by the hand with runs to the highest value
			
			int defaultValue = STRAIGHT_FLUSH_DEFAULT;
			PlayingCard highCardInStraightFlush = cards[0]; // gets the highest card in the straight flush (since hand is sorted from high to low)
			int additionalGameValueComponent = (int) Math.pow(highCardInStraightFlush.getGameValue(), 5);    
			return defaultValue + additionalGameValueComponent;
		}		
		else if(isFourOfAKind()){
			// four of a kinds are distinguished by the game value of their 4 of a kind card
			
			int defaultValue = FOUR_OF_A_KIND_DEFAULT;
			int gameValueOfFourOfAKindCard = getGameValueOfCardWithCount(4);
			int additionalGameValueComponent = (int) Math.pow(gameValueOfFourOfAKindCard, 4); 
			return defaultValue + additionalGameValueComponent;
		}		
		
		else if(isFullHouse()){ 
			// Full houses are ranked by the highest Three of a kind card, 
			// It is not possible for two players to have the same 3 of a kind, so we can simply rank the hands by the game value multiplied by a factor 
			// and ignore the pair
			
			int defaultValue = FULL_HOUSE_DEFAULT;
			int gameValueOfThreeOfAKind = getGameValueOfCardWithCount(3);
			int additionalGameValueComponent = ((int) Math.pow(factor, 3)) * gameValueOfThreeOfAKind;
			return defaultValue + additionalGameValueComponent; 
		}	
		
		else if(isFlush()){
			// flushes are ranked by highest card in flush, if there is a match then we check the next card, then the next etc.
			// To rank these properly, we multiple the game value by the cards from high to low by a factor raised to a power, which reduces as we go down the hand to
			// the lower cards, this gives us a correct additionalGameValueComponent
			
			int defaultValue = FLUSH_DEFAULT;
			int additionalGameValueComponent = 0;
			
			int power = 4;
			for (int i = 0; i < cards.length; i++) {
				additionalGameValueComponent += ((int) Math.pow(factor, power--)) * cards[i].getGameValue();
			}
             
			return defaultValue + additionalGameValueComponent;
		}		
		
		else if(isStraight()){
			// Straight's are distinguished by the game value of their highest card
			
			int defaultValue = STRAIGHT_DEFAULT;
			PlayingCard highCardInStraight = cards[0]; 
			int additionalGameValueComponent = (int) Math.pow(highCardInStraight.getGameValue(), 5);  
			return defaultValue + additionalGameValueComponent;
		}	
		
		else if(isThreeOfAKind()){
			// Three of a kind hands are ranked by game value of the three of a kind card, 
			// It is not possible for two players to have the same 3 of a kind, so we can simply rank the hands by the game value multiplied by a factor 
			
			int defaultValue = THREE_OF_A_KIND_DEFAULT;
			int gameValueOfThreeOfAKind = getGameValueOfCardWithCount(3);
			int additionalGameValueComponent = ((int) Math.pow(factor, 3)) * gameValueOfThreeOfAKind;;
			return defaultValue + additionalGameValueComponent;
		}	
		else if(isTwoPair()){
			// Two pair hands are ranked by game value of high pair, then low pair, if there is a match then we check the remaining card. 
			// To rank these properly, we multiple the game value of high pair by the factor squared, then the lower pair by the factor, 
			// then we add the game value of the remaining card				
			
			int defaultValue = TWO_PAIR_DEFAULT;
			int gameValueOfHighPairCard = 0;
			
			// loops through games value count array and gets game value of high pair
			for (int cardIndex = TYPES_OF_CARDS - 1; cardIndex >= 0; cardIndex--) {
				if(gameValuesInHandCountArray[cardIndex] == 2){
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
			
			int defaultValue = ONE_PAIR_DEFAULT;
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
			
			int defaultValue = HIGH_HAND_DEFAULT;
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
			if(gameValuesInHandCountArray[cardIndex] == countOfCardInHand){
				return cardIndex + 2; // adds two to card index as array goes from 0-12, while card game values go from 2-14
			}
		}
		return 0;
	}	
	
	
	// checks the type of hand
	public Type getHandType(){
			if(isRoyalFlush()){
				return HandOfCards.Type.RoyalFlush;
			}		
			else if(isStraightFlush()){
				return HandOfCards.Type.StraightFlush;
			}			
			else if(isFourOfAKind()){
				return HandOfCards.Type.FourOfAKind;
			}					
			else if(isFullHouse()){
				return HandOfCards.Type.FullHouse;
			}	
			else if(isFlush()){
				return HandOfCards.Type.Flush;
			}
			else if(isStraight()){
				return HandOfCards.Type.Straight;
			}	
			else if(isThreeOfAKind()){
				return HandOfCards.Type.ThreeOfAKind;
			}	
			else if(isTwoPair()){
				return HandOfCards.Type.TwoPair;
			}
			else if(isOnePair()){
				return HandOfCards.Type.OnePair;
			}
			else{
				return HandOfCards.Type.HighHand;
			}						
	}	
	
	public int getDiscardProbability(int cardPosition){
		int discardProbability = 0;
		if(cardPosition < 0 || cardPosition > 4){
			return discardProbability;	
		}
		
		Type handType = getHandType();
		if(handType == Type.HighHand || handType == Type.OnePair){
			boolean result = isBrokenStraight();
			if(result){
				int indexOfCardThatBrokeStraight = getCardThatBrokeStraight();
				
				if(indexOfCardThatBrokeStraight > 0){
					System.out.println("Card that broke straight: " + cards[indexOfCardThatBrokeStraight]);
				}
			}
		}
		
		return discardProbability;
	}
	
	private int getCardThatBrokeStraight(){
		
		int gameValueOfCardThatBrokeStraight = -1;
		int indexOfCardThatBrokeStraight = -1;
		
		// if there are two of any card type, then one of them is what broke the straight, so
		// we just need to find the card which there are two of
		for (int i = 0; i < gameValuesInHandCountArray.length; i++) {
			if(gameValuesInHandCountArray[i] == 2){
				gameValueOfCardThatBrokeStraight =  i + 2;
				break;
			}
		}
		
		int keepCards[] = {0,0,0,0,0};
		
		// otherwise, we need to check which card is the odd one out
		if(gameValueOfCardThatBrokeStraight == -1){
			// if we have an Ace in our hand, it changes the way we need to search for the card that broke the straight
			if(cards[0].getType() == "A"){
				
				for (int i = 0; i < cards.length; i++) {
					if(hasNeighbour(i, false)){
						keepCards[i] = 1;
					}
				}
				System.out.println("hasNeighbour: " + Arrays.toString(keepCards));
				int indexOfOddCard = findOddCardFromNeighbours(keepCards, false);
				System.out.println("Card which broke straight: " + cards[indexOfOddCard]);					
			}else{
				
				for (int i = 0; i < cards.length; i++) {
					if(hasNeighbour(i, false)){
						keepCards[i] = 1;
					}
				}
				System.out.println("hasNeighbour: " + Arrays.toString(keepCards));
				int indexOfOddCard = findOddCardFromNeighbours(keepCards, false);
				System.out.println("Card which broke straight: " + cards[indexOfOddCard]);				
			}
			

		}
		
		for (int i = 0; i < CARDS_PER_HAND; i++) {
			if(cards[i].getFaceValue() == gameValueOfCardThatBrokeStraight){
				
				indexOfCardThatBrokeStraight = i;
			}
		}		
		return indexOfCardThatBrokeStraight;
	}
	
	private boolean hasNeighbour(int index, boolean hasAce){
		
		if(hasAce){
			if(index > 0){
				if(cards[index-1].getFaceValue() == cards[index].getFaceValue() + 1){
					return true;
				}			
			}

			if(index < 4){
				if(cards[index].getFaceValue() == cards[index+1].getFaceValue() + 1){
					return true;
				}	
			}
			return false;			
		}else{
			if(index > 0){
				if(cards[index-1].getGameValue() == cards[index].getGameValue() + 1){
					return true;
				}			
			}

			
			if(index < 4){
				if(cards[index].getGameValue() == cards[index+1].getGameValue() + 1){
					return true;
				}	
			}
			return false;
		}
		

	}
	
	private int findOddCardFromNeighbours(int[] keepCards, boolean hasAce){
		
		int noNeighbourIndex = -1;
		int otherNoNeighbourIndex = -1;
		int cardsWithNoNeighbours = 0;
		for (int i = 0; i < keepCards.length; i++) {
			if(keepCards[i] == 0){
				if(cardsWithNoNeighbours == 0){
					noNeighbourIndex = i;
					cardsWithNoNeighbours++;
				}else{
					otherNoNeighbourIndex = i;
				}

			}
		}		
		
		if(cardsWithNoNeighbours == 1){
			return noNeighbourIndex;
		}else{
			int d1 = distanceToCardWithNeighbour(keepCards, noNeighbourIndex, hasAce);
			int d2 = distanceToCardWithNeighbour(keepCards, otherNoNeighbourIndex, hasAce);
			
			if(d1 < d2){
				return noNeighbourIndex;
			}else{
				return otherNoNeighbourIndex;
			}
			// find the card with no neighbour which is closest to a card with a neighbour
		}
		
	}
	
	private int distanceToCardWithNeighbour(int[] keepCards, int indexOfCardWithNoNeighbour, boolean hasAce){
		
		if(hasAce){
			PlayingCard cardWithNoNeighbour = cards[indexOfCardWithNoNeighbour];
			int distanceLeft = 100, distanceRight = 100;
			
			int neighbourIndex = indexOfCardWithNoNeighbour - 1;
			
			if(neighbourIndex > 0 && keepCards[neighbourIndex] != 0){
				distanceLeft = cards[neighbourIndex].getFaceValue() - cardWithNoNeighbour.getFaceValue();
			}
			
			neighbourIndex = indexOfCardWithNoNeighbour + 1;
			
			if(neighbourIndex < 5 && keepCards[neighbourIndex] != 0){
				distanceRight = cardWithNoNeighbour.getFaceValue() - cards[neighbourIndex].getFaceValue();
			}
			
			return Math.min(distanceLeft, distanceRight);			
		}else{
			PlayingCard cardWithNoNeighbour = cards[indexOfCardWithNoNeighbour];
			int distanceLeft = 100, distanceRight = 100;
			
			int neighbourIndex = indexOfCardWithNoNeighbour - 1;
			
			if(neighbourIndex > 0 && keepCards[neighbourIndex] != 0){
				distanceLeft = cards[neighbourIndex].getGameValue() - cardWithNoNeighbour.getGameValue();
			}
			
			neighbourIndex = indexOfCardWithNoNeighbour + 1;
			
			if(neighbourIndex < 5 && keepCards[neighbourIndex] != 0){
				distanceRight = cardWithNoNeighbour.getGameValue() - cards[neighbourIndex].getGameValue();
			}
			
			return Math.min(distanceLeft, distanceRight);
		}

	}
	
	
	
	
	
	
	
	private int isBustedFlush(){
		int majoritySuitIndex = -1;
		int flushBusterIndex = -1;
		boolean bustedFlush = false;
		
		// tracks the number of each suit in hand, layout: [H, D, S, C]
		int[] suitsInHand = {0,0,0,0};

		for(int i = 0; i < CARDS_PER_HAND; i++){
			PlayingCard card = cards[i];
			
			int suitIndex = card.getEnumSuit().ordinal();
			suitsInHand[suitIndex] += 1; 
			
			if(suitsInHand[suitIndex] == 4){
				bustedFlush = true;
				majoritySuitIndex = i;
			}
		}
		
		if(bustedFlush){
			PlayingCard.Suit majoritySuit = cards[majoritySuitIndex].getEnumSuit();
			for(int i = 0; i < CARDS_PER_HAND; i++){
				PlayingCard.Suit candidateSuit = cards[i].getEnumSuit();
				if(candidateSuit != majoritySuit){
					flushBusterIndex = i;
				}
			}
		}
		return flushBusterIndex;
	}
	
	private boolean isBrokenStraight(){
		int almostStraightCount = 4;
		int checkCards = 5;
		
		// gameValuesInHandCountArray is a array of size 13
		// each location in array is an int representing the number of
		// that card type in our hand, it is generated when the hand is dealt
		// this makes is easier to look for different hand types
		// e.g. if our hand was:
		// AH 9H 8H 3H 2H 
		// then gameValuesInHandCountArray would be: 
		// [1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1]  (cards listed in game value order: 2,3,4... -> ...J,Q,K,A)
		
		
		// checks if there is a Ace in the hand, if so then this code 
		// takes into account a low or high Ace when looking for a broken straight
		if(gameValuesInHandCountArray[TYPES_OF_CARDS-1] > 0){
			// special values for checking the first chunk of the array, to account for an Ace
			almostStraightCount = 3;
			checkCards = 4;
		}
		
		int i = 0;
		int j = 4;
		int index = 0;		
		
		// These loop goes through the array holding the number of each card type.
		// it scans the array from left to right checking if there is a broken loop
		while(j < 13){
			int count = 0;
			index = i;
			

			// this checks array location 0-4 in the gameValuesInHandCountArray, 
			// then it checks location 1-5, then 2-6 etc. until it finally checks location 8-12
			// it works in a sliding window style, each loop around the window slides one location to the right
			// if in any set of 5 locations, there are 3 non-zero values, then we have 4 out of 5 
			// cards of a run
			for (int k = 0; k < checkCards; k++) {
				System.out.print(index + " ");
				
				// if we find a location in array with value greater then 0, then we have that card in our hand
				// so we increment the count
				if(gameValuesInHandCountArray[index++] > 0){
					count++;
				}
			}
			System.out.print("count: " + count);
			System.out.println();
			
			// if count is equal to almost straight (3 in case of Ace in hand for the first run, 4 otherwise)
			// then we have a broken straight
			if(count == almostStraightCount){
				System.out.println(toString());
				System.out.println(Arrays.toString(gameValuesInHandCountArray));		
				System.out.println("Broken Straight");
				
				
				return true;
			}
			
			// resets count, increments i,j, 
			count = 0;
			i++;
			j++;
			// resets the almost these values back to default, as we only need them different for the first round
			// if there is an Ace
			almostStraightCount = 4;
			checkCards = 5;
		}
		
		return false;
	}
	
	public static void main(String[] args) {	
		long startTime = System.currentTimeMillis();
		
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		
		/*
		// simple test:
		
		HandOfCards hand1 = new HandOfCards(deck);
		HandOfCards hand2 = new HandOfCards(deck);
		System.out.println("Player1: " + hand1.toString() + " " + hand1.getHandType());
		System.out.println("Player2: " + hand2.toString() + " " + hand2.getHandType());
		
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
		
		
		*/
		// To Show the different hand types are sorted properly, we deal 100 random hands, sort them by their game value
		// then print them out with handtype along side, these can then be inspected to ensure they are ordered correctly
		// we can also note the same hand types are also ordered correctly
		// we can change the iterations to a much higher value in order to generate the more rare hand types
		
		System.out.println("Printing randomly dealt hands, sorted by gameValue");
		System.out.println("**************************************************");
		Map<Integer, String> results = new TreeMap<Integer, String>();
		
		deck.shuffle();
		//HandOfCards handOfCards = new HandOfCards(deck);
		HandOfCards handOfCards = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("2", 'H', 2, 2),
				new PlayingCard("3", 'C', 3, 3),
				new PlayingCard("4", 'H', 4, 4),
				new PlayingCard("8", 'C', 8, 8),
				new PlayingCard("A", 'H', 1, 14),
		});			
		
		if(!HandOfCards.checkIfHandType(handOfCards, Type.Straight)){
			int result = handOfCards.getDiscardProbability(0);	
			
		}	
		System.out.println();
		deck.shuffle();
		//HandOfCards handOfCards = new HandOfCards(deck);
		handOfCards = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("2", 'H', 2, 2),
				new PlayingCard("5", 'C', 5, 5),
				new PlayingCard("6", 'H', 6, 6),
				new PlayingCard("7", 'C', 7, 7),
				new PlayingCard("8", 'H', 8, 8),
		});			
		
		if(!HandOfCards.checkIfHandType(handOfCards, Type.Straight)){
			int result = handOfCards.getDiscardProbability(0);	
		}		
		
		
		System.out.println();
		deck.shuffle();
		//HandOfCards handOfCards = new HandOfCards(deck);
		handOfCards = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("8", 'H', 8, 8),
				new PlayingCard("6", 'C', 6, 6),
				new PlayingCard("4", 'H', 4, 4),
				new PlayingCard("3", 'C', 3, 3),
				new PlayingCard("2", 'H', 2, 2),
		});			
		
		if(!HandOfCards.checkIfHandType(handOfCards, Type.Straight)){
			int result = handOfCards.getDiscardProbability(0);	
		}		
				
		
		
		
		int iterations = 1;
		for(int i = 0; i < iterations; i++){

			
			
			/*
			if(result != -1){
				System.out.println(handOfCards.toString() + " is a busted flush, at index: " + result);
			}
			*/
			
			//results.put(handOfCards.getGameValue(), handOfCards.toString() + " " + handType);
		}
		/*
		for(Integer key: results.keySet()){
			System.out.println(results.get(key) + "  " + key);
		}
		System.out.println();		
		
		*/
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
			
			if(HandOfCards.checkIfHandType(handOfCards, handType)){
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
		return HandOfCards.checkIfHandType(handOfCards, Type.Straight);		
	}	
	
	// static method checking the type of hand
	public static boolean checkIfHandType(HandOfCards handOfCards, Type handType){
		
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
}
