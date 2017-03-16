package poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PokerPlayer {

	
	public static int MAX_CARDS_TO_DISCARD = 3;
	public static int MAX_PROBABILITY = 100;
	
	private DeckOfCards deckOfCards;
	private HandOfCards handOfCards;
	
	// constructor receives deck of cards and deals a hand for the player
	public PokerPlayer(DeckOfCards deckOfCards){
		this.deckOfCards = deckOfCards;
		this.handOfCards = new HandOfCards(deckOfCards);
	}
	
	// this method chooses which cards to discard, returns these to the deck, and receives replacement cards for the hand
	public int discard(){
		
		// keeps track of the indexes of the cards to discard
		ArrayList<Integer> cardsToDiscardIndexArray = new ArrayList<Integer>();
		
		// keeps track of possible cards to discard
		HashMap<Integer, Integer> possibleDiscards = new HashMap<Integer, Integer>();		
		
		// if the players hand has a discard probability of 100 (static variable DISCARD) for a card, then
		// the hand should only return those cards with 100, and ignore any other card probabilities
		// this is to allow the attempt to fill a broken straight etc
		boolean noDefiniteCardsToDiscard = true; 

		for(int index = 0; index < handOfCards.CARDS_PER_HAND; index++){
			
			// gets discard probability
			int discardProbability = handOfCards.getDiscardProbability(index);
			
			// if prob is 0 (static variable KEEP), continue as this is a card we want to keep no matter what
			if(discardProbability == handOfCards.KEEP){
				continue;
			// if prob is 100 (static variable DISCARD), then we discard, as this is a card which may be blocking a run or flush
			}else if(discardProbability == handOfCards.DISCARD && cardsToDiscardIndexArray.size() < MAX_CARDS_TO_DISCARD){
				cardsToDiscardIndexArray.add(index);
				noDefiniteCardsToDiscard = false; // we alway set flag to ignore all other non 100 probabilities
			}else{
				// any value in between 0 and 100 is stored for later evaluation
				possibleDiscards.put(index, discardProbability);
			}
		}
		
		Random random = new Random();
		
		// if we haven't discarded max cards and we aren't trying to fill a special hand, then we check other cards for discarding
		if(noDefiniteCardsToDiscard && cardsToDiscardIndexArray.size() < MAX_CARDS_TO_DISCARD){
			
			// loop through cards with prob between 0 and 100
			for(Integer index: possibleDiscards.keySet()){
				// used to multiply the prob. of dicarding the card, the less cards we have already discarded, the higher this value
				// therefore if we have dicarded no cards so far, then the odds are very high that we discard at least one card
				// multiplier decreases as the more cards are discarded
				int probabilityMultiplier = MAX_CARDS_TO_DISCARD - cardsToDiscardIndexArray.size(); 
				
				// multiplies discard probability of the card by probability multiplier
				int discardProbability = possibleDiscards.get(index) * probabilityMultiplier;
				
				
				// gets a random value between 0 and 100
				int discardThreshold = random.nextInt(MAX_PROBABILITY);
				
				// discards card if our discard probability is less than the random threshold
				if(discardProbability <= discardThreshold){
					cardsToDiscardIndexArray.add(index);
				}
				
				// breaks from loop if we have reach max cards to discard, otherwise checks the next possible card
				if(cardsToDiscardIndexArray.size() == MAX_CARDS_TO_DISCARD){
					break;
				}
				
			}
		}
		
		
		// goes through cards we have decided to discard, returns them to the deck, and replaces them with another card
		System.out.print("Dicarding cards: ");
		for(Integer cardIndex: cardsToDiscardIndexArray){
			PlayingCard card = handOfCards.returnCard(cardIndex);
			System.out.print(card.toString() + " ");
			deckOfCards.returnCard(card);
			handOfCards.receiveCard(deckOfCards.dealNext());
		}
		System.out.println();

		// sorts our hand again
		handOfCards.sort();
		
		return cardsToDiscardIndexArray.size();
	}
	
	public static void main(String[] args) {
		DeckOfCards deckOfCards = new DeckOfCards();
		deckOfCards.shuffle();
		
		
		// deals out a hand to 5 players, prints the hands, discards some cards, then prints the resulting hand
		int numberOfPlayers = 5;
		for (int i = 0; i < numberOfPlayers; i++) {
			PokerPlayer player = new PokerPlayer(deckOfCards);
			System.out.println("Player " + (i + 1));
			System.out.println(player.handOfCards.toString());
			player.discard();
			System.out.println(player.handOfCards.toString());	
			System.out.println();
		}

	}
}
