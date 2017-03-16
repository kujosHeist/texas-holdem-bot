package poker;

import java.util.ArrayList;
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
	
	public static int CARDS_PER_HAND = 5;
	private static int HIGH_CARD_INDEX = 0;
	
	public static int KEEP = 0;
	public static int DISCARD = 100;
	
	private static int TYPES_OF_CARD = 13; // A 2 3 ... J Q K
	private static int FALSE = -1;
	private PlayingCard[] cards = new PlayingCard[CARDS_PER_HAND];
	private DeckOfCards deckOfCards;
	
	private Integer indexOfFlushBuster;
	private Integer indexOfStraightBreaker;
	
	private int[] gameValuesInHandCountArray = new int[TYPES_OF_CARD]; // array which stores the number of each type of card in a hand
	
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
	
	
	// returns a card from the hand
	public PlayingCard returnCard(int index){
		PlayingCard card = cards[index];
		cards[index] = null;
		return card;
		
	}
	
	public PlayingCard getCard(int index){
		return cards[index];
	}
	
	// based on bubble sort, this method orders the cards from highest to lowest based on game value
	public void sort(){
		boolean sorted = false;   
		
		// keeps looping until cards in in correct order
		while (!sorted){
			sorted= true; 
			PlayingCard temp;   
			
			for(int i=0;  i < cards.length-1;  i++){
				
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
		for (int i = 0; i < TYPES_OF_CARD; i++) {
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
		
		for (int i = 0; i < TYPES_OF_CARD; i++) {
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
		
		for (int i = 0; i < TYPES_OF_CARD; i++) {
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
		
		for (int i = 0; i < TYPES_OF_CARD; i++) {
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
		
		for (int i = 0; i < TYPES_OF_CARD; i++) {
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
	
		for (int i = 0; i < TYPES_OF_CARD; i++) {
			
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
		int[] gameValuesCountInHandArray = new int[TYPES_OF_CARD];
		
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
			for (int cardIndex = TYPES_OF_CARD - 1; cardIndex >= 0; cardIndex--) {
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
		for (int cardIndex = 0; cardIndex < TYPES_OF_CARD; cardIndex++) {
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
		int discardProbability = DISCARD;
		
		// If invalid card position is invalid
		if(cardPosition < 0 || cardPosition > 4){ 
			return discardProbability;	
		}
		
		
		Type handType = getHandType();
		
		if(handType == Type.HighHand || handType == Type.OnePair){
			// if we have a highhand or single pair, then we call a method to check for a busted flush or broken straight,
			// and returns the discarded probability based on this
			discardProbability = discardHighHandOrOnePair(cardPosition, handType);
		}else if(handType == Type.TwoPair){
			// if we have a two pair, we check if the odd card is the cardPosition
			ArrayList<Integer> indexes = getIndexesOfSingleCards();
			if(indexes.contains(cardPosition)){
				return DISCARD;
			}else{
				return KEEP;
			}
		}else if(handType == Type.ThreeOfAKind){
			// If we have 3 of a kind, we check if one of the two odd cards are in cardPosition
			ArrayList<Integer> indexes = getIndexesOfSingleCards();
			if(indexes.contains(cardPosition)){
				return DISCARD;
			}else{
				return KEEP;
			}
		}else{ 
			// if we have a straight, flush, full house, four of kind, straight flush or royal flush,
			// then we don't have to swap any card, so we return 0
			// (we could swap the odd card in 4 of a kind, but there is no point, as a four of a kind can 
			// only be beaten by a higher four of a kind or else a straight/royal flush
			discardProbability = 0;
		}
		
		return discardProbability;
	}
	
	private int discardHighHandOrOnePair(int cardPosition, Type handType){
		
		// checks for busted flush or broken straight, returns the index of the offending card
		int bustedFlushIndex = isBustedFlush();
		int brokenStraightIndex = isBrokenStraight();
		
		if(bustedFlushIndex != FALSE){
			if(bustedFlushIndex == cardPosition){
				return DISCARD;	
			}else{
				return KEEP;
			}
			
		}else if(brokenStraightIndex != FALSE){
			if(brokenStraightIndex == cardPosition){
				return DISCARD;	
			}else{
				return KEEP;
			}
			
		}else if(handType == Type.OnePair){
			// if we have one pair, we discard any the 3 odd cards if they are in cardPosition
			ArrayList<Integer> indexes = getIndexesOfSingleCards();
			
			if(indexes.contains(cardPosition)){
				return DISCARD;
			}else{
				return KEEP;
			}
		}else{ 
			
			// we have a high hand card, so we discard any card which is not in the top two highest in our hand
			if(cardPosition != HIGH_CARD_INDEX && cardPosition != HIGH_CARD_INDEX+1){
				return DISCARD;
			}else{
				return KEEP;
			}
			
		}		
	}
	
	// returns first occurence of a card with matching gameValue in the hand
	// only called when you know a card is in the hand
	private int getIndexOfCardInHand(int gameValue){
		int index = 0;
		for (int i = 0; i < CARDS_PER_HAND; i++) {
			if(cards[i].getGameValue() == gameValue){
				index = i;
				break;
			}
		}
		return index;
	}
	
	
	// returns a list of indexes of cards which only occurance once in the hand
	private ArrayList<Integer> getIndexesOfSingleCards(){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		for (int i = 0; i < TYPES_OF_CARD; i++) {
			if(gameValuesInHandCountArray[i] == 1){
				int gameValue = i + 2;
				int index = getIndexOfCardInHand(gameValue);
				indexes.add(index);
			}
		}
		return indexes;
	}
	
	
	// this method checks for a hand which is one short of a flush, and returns the index of the 
	// offending card if it is. Otherwise it returns static variable FALSE
	private int isBustedFlush(){
		
		// index of flush buster is stored in an instance variable, so that it only needs to be calculated once per hand
		if(indexOfFlushBuster != null){
			return indexOfFlushBuster;
		}
		
		
		int bustedIndex = FALSE;
		int majoritySuitIndex = FALSE;
		boolean bustedFlush = false;
		
		// tracks the number of each suit in hand, layout: [H, D, S, C]
		int[] suitsInHand = {0,0,0,0};

		// loops through the hand and stores the number of each suit
		for(int i = 0; i < CARDS_PER_HAND; i++){
			PlayingCard card = cards[i];
			
			// getEnumSuit.ordinal() returns an int between 0 and 3, representing the suits in order:  [H, D, S, C]
			int suitIndex = card.getEnumSuit().ordinal();
			// increments that suit
			suitsInHand[suitIndex] += 1; 
			
			// if we have 4 of one suit, then we have a busted flush (this method is only called on hands which are not a flush)
			// we then store an example index of the majority suit
			if(suitsInHand[suitIndex] == 4){
				bustedFlush = true;
				majoritySuitIndex = i;
				break;
			}
		}
		
		
		if(bustedFlush){
			
			// loops through hand checking for a card which is not majority suit, stores its index
			PlayingCard.Suit majoritySuit = cards[majoritySuitIndex].getEnumSuit();
			for(int i = 0; i < CARDS_PER_HAND; i++){
				PlayingCard.Suit candidateSuit = cards[i].getEnumSuit();
				if(candidateSuit != majoritySuit){
					bustedIndex = i;
				}
			}
		}
		indexOfFlushBuster = bustedIndex;
		return bustedIndex;
	}
	
	
	// this method checks for a hand which is one short of a straight, and returns the index of the 
	// offending card if it is. Otherwise it returns static variable FALSE 
	private int isBrokenStraight(){
		
		// index of straight breaker is stored in an instance variable, so that it only needs to be calculated once per hand
		if(indexOfStraightBreaker != null){
			return indexOfStraightBreaker;
		}		
		
		int brokenIndex = FALSE;
		int almostStraightCount = 4;  // number of cards which have to be in proximity to make a broken straight
		int checkCards = 5; // proximity, or window size, i.e. 4 cards have to there in a window of 5 cards
		
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
		if(hasAce()){
			// special values for checking the first chunk of the array, to account for an Ace
			almostStraightCount = 3;
			checkCards = 4;
		}
		
		int i = 0;
		int j = 4;
		int index = 0;		
		
		// These loops go through the array holding the number of each card type.
		// it scans the array from left to right checking if there is a broken loop
		while(j < TYPES_OF_CARD){
			
			
			int cards_in_proximity = 0;
			index = i;

			// this checks array location 0-4 in the gameValuesInHandCountArray, 
			// then it checks location 1-5, then 2-6 etc. until it finally checks location 8-12
			// it works in a sliding window style. After each loop the window slides one location to the right
			// if in any "window" of 5 card locations, there are 4 non-zero values, then we have 4 out of 5 
			// cards of a straight
			for (int k = 0; k < checkCards; k++) {
				
				// if we find a location in array with value greater then 0, then we have that card in our hand
				// so we increment the cards_in_proximity count
				if(gameValuesInHandCountArray[index++] > 0){
					cards_in_proximity++;
				}
			}
			
			// if we have 4 cards out of 5 for a straight (or 3 of 4 in case of Ace, and when checking first 4 positions)
			// then we have a broken straight
			if(cards_in_proximity == almostStraightCount){	
				// gets index of card that broke straight, stores, then returns it
				brokenIndex = getCardWhichBrokeStraight(i, j);
				indexOfStraightBreaker = brokenIndex;
				return brokenIndex;
			}
			
			// resets cards_in_proximity, increments i,j, for next to check next window of 5 cards 
			cards_in_proximity = 0;
			i++;
			j++;
			// resets these values back to default, as we only need them different for the first round/window
			// if there is an Ace in the deck
			almostStraightCount = 4;
			checkCards = 5;
		}
		// if we reach here then there was no broken straight
		indexOfStraightBreaker = FALSE;
		return FALSE;
	}
	
	private int getCardWhichBrokeStraight(int startWindow, int endWindow){
		
		int brokeIndex = FALSE;	
		// first check for 2 of a kind, if there is, return this card as it is breaking a straight
		for (int i = 0; i < TYPES_OF_CARD; i++) {
			if(gameValuesInHandCountArray[i] == 2){
				brokeIndex = i;
			}
		}
		
		// then we check for any card outside and to the left of our window, if we find a card then it broke the straight
		if(brokeIndex == FALSE){
			if(startWindow > 0){
				for (int i = startWindow - 1; i >= 0; i--) {
					if(gameValuesInHandCountArray[i] > 0){
						brokeIndex = i;
						break;
					}
				}				
			}
				
			// then we check for any card outside and to the right of our window, if we find a card then it broke the straight			
			if(endWindow < 12){
				for (int i = endWindow + 1; i < TYPES_OF_CARD; i++) {
					if(gameValuesInHandCountArray[i] > 0){
						brokeIndex = i;
						break;
					}
				}				
			}			
		}

		// we get the index of the card we need to swap, by adding 2 since game gameValuesInHandCountArray indexes run from 0-12, instead of 2-14 
		int gameValue = brokeIndex + 2;
		int indexOfCardToSwap = getIndexOfCardInHand(gameValue);
		
		return indexOfCardToSwap;
	}
	
	// if there is an ace in the hand, then it is at highest position
	public boolean hasAce(){
		return cards[HIGH_CARD_INDEX].getType() == "A"; 
	}
	
	public static void main(String[] args) {	
		long startTime = System.currentTimeMillis();
		
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		
		//////////////////////////////////////////
		// Main Test
		/////////////////////////////////////////
		// deals 10 hands, and shows which cards to discard
		HandOfCards handOfCards;
		int iterations = 1000;
		for(int i = 0; i < iterations; i++){
			deck.shuffle();
			handOfCards = new HandOfCards(deck);
			
			System.out.println(handOfCards.toString());
			System.out.println(handOfCards.getHandType());
			
			for (int cardPosition = 0; cardPosition < handOfCards.CARDS_PER_HAND; cardPosition++) {
				int discardProbability = handOfCards.getDiscardProbability(cardPosition);
				
				
				System.out.println(handOfCards.cards[cardPosition] + " - "  + discardProbability);
			}
			System.out.println();				
			
		}
		
		
		//////////////////////////////////////////
		// Special Tests
		/////////////////////////////////////////		
		
		
		// checks that it detects a broken straight, and discards the correct card
		HandOfCards handOfCards2 = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("10", 'C', 10, 10),
				new PlayingCard("9", 'C', 9, 9),
				new PlayingCard("8", 'H', 8, 8),
				new PlayingCard("7", 'S', 7, 7),
				new PlayingCard("2", 'S', 2, 2)
		});
		
		System.out.println(handOfCards2.toString());
		System.out.println("Checking for Broken Straight");
		
		for (int cardPosition = 0; cardPosition < handOfCards2.CARDS_PER_HAND; cardPosition++) {
			int discardProbability = handOfCards2.getDiscardProbability(cardPosition);
			// prints out the probability of discarding each card, in this case the card which broke the straight is the only one to discard
			System.out.println(handOfCards2.cards[cardPosition] + " - "  + discardProbability);
		}
		System.out.println();		
		
		
		
		
		// checks that it detects a busted flush, and discards the correct card
		handOfCards2 = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("10", 'C', 10, 10),
				new PlayingCard("9", 'C', 9, 9),
				new PlayingCard("8", 'H', 8, 8),
				new PlayingCard("7", 'C', 3, 3),
				new PlayingCard("2", 'C', 2, 2)
		});
		
		System.out.println(handOfCards2.toString());
		System.out.println("Checking for Busted Flush");
		
		for (int cardPosition = 0; cardPosition < handOfCards2.CARDS_PER_HAND; cardPosition++) {
			int discardProbability = handOfCards2.getDiscardProbability(cardPosition);
			// prints out the probability of discarding each card, in this case the card which busted the flush is the only one to discard
			System.out.println(handOfCards2.cards[cardPosition] + " - "  + discardProbability);
		}
		System.out.println();			
		
		
		// checks that it detects a two pair, and discards the correct card
		handOfCards2 = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("10", 'C', 10, 10),
				new PlayingCard("10", 'D', 10, 10),
				new PlayingCard("8", 'H', 8, 8),
				new PlayingCard("8", 'C', 8, 8),
				new PlayingCard("2", 'C', 2, 2)
		});
		
		System.out.println(handOfCards2.toString());
		System.out.println(handOfCards2.getHandType());
		
		for (int cardPosition = 0; cardPosition < handOfCards2.CARDS_PER_HAND; cardPosition++) {
			int discardProbability = handOfCards2.getDiscardProbability(cardPosition);
			// prints out the probability of discarding each card, in this case the card which busted the flush is the only one to discard
			System.out.println(handOfCards2.cards[cardPosition] + " - "  + discardProbability);
		}
		System.out.println();	
		
		
		
		
		
		// checks that it detects a Three of a kind, and discards the correct cards
		handOfCards2 = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("10", 'C', 10, 10),
				new PlayingCard("10", 'D', 10, 10),
				new PlayingCard("10", 'H', 10, 10),
				new PlayingCard("8", 'C', 8, 8),
				new PlayingCard("2", 'C', 2, 2)
		});
		
		System.out.println(handOfCards2.toString());
		System.out.println(handOfCards2.getHandType());
		
		for (int cardPosition = 0; cardPosition < handOfCards2.CARDS_PER_HAND; cardPosition++) {
			int discardProbability = handOfCards2.getDiscardProbability(cardPosition);
			// prints out the probability of discarding each card, in this case the card which busted the flush is the only one to discard
			System.out.println(handOfCards2.cards[cardPosition] + " - "  + discardProbability);
		}
		System.out.println();	
		
		
		// checks that it detects a Full house, and discards the correct cards
		handOfCards2 = new HandOfCards(
				new PlayingCard[]{
				new PlayingCard("10", 'C', 10, 10),
				new PlayingCard("10", 'D', 10, 10),
				new PlayingCard("10", 'H', 10, 10),
				new PlayingCard("8", 'C', 8, 8),
				new PlayingCard("8", 'D', 8, 8)
		});
		
		System.out.println(handOfCards2.toString());
		System.out.println(handOfCards2.getHandType());
		
		for (int cardPosition = 0; cardPosition < handOfCards2.CARDS_PER_HAND; cardPosition++) {
			int discardProbability = handOfCards2.getDiscardProbability(cardPosition);
			// prints out the probability of discarding each card, in this case the card which busted the flush is the only one to discard
			System.out.println(handOfCards2.cards[cardPosition] + " - "  + discardProbability);
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

	public void receiveCard(PlayingCard card) {
		for (int i = 0; i < cards.length; i++) {
			if(cards[i] == null){
				cards[i] = card;
			}
		}
	}		
}
