package poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PokerPlayer {

	
	public static int MAX_CARDS_TO_DISCARD = 3;
	
	public static int MAX_PROBABILITY = 100;
	
	private DeckOfCards deckOfCards;
	
	private HandOfCards handOfCards;
	
	public PokerPlayer(DeckOfCards deckOfCards){
		this.deckOfCards = deckOfCards;
		this.handOfCards = new HandOfCards(deckOfCards);
	}
	
	public int discard(){

		ArrayList<Integer> cardsToDiscardIndexArray = new ArrayList<Integer>();
		HashMap<Integer, Integer> possibleDiscards = new HashMap<Integer, Integer>();		
		
		boolean noDefiniteCardsToDiscard = true; 

		for(int index = 0; index < handOfCards.CARDS_PER_HAND; index++){
			int discardProbability = handOfCards.getDiscardProbability(index);
			
			if(discardProbability == handOfCards.KEEP){
				continue;
			}else if(discardProbability == handOfCards.DISCARD && cardsToDiscardIndexArray.size() < MAX_CARDS_TO_DISCARD){
				cardsToDiscardIndexArray.add(index);
				noDefiniteCardsToDiscard = false;
			}else{
				possibleDiscards.put(index, discardProbability);
			}
		}
		
		Random random = new Random();
		
		if(noDefiniteCardsToDiscard && cardsToDiscardIndexArray.size() < MAX_CARDS_TO_DISCARD){
			
			for(Integer index: possibleDiscards.keySet()){
				int probabilityMultiplier = MAX_CARDS_TO_DISCARD - cardsToDiscardIndexArray.size(); 
				
				int discardProbability = possibleDiscards.get(index) * probabilityMultiplier;
				
				int discardThreshold = random.nextInt(MAX_PROBABILITY);
				
				if(discardProbability <= discardThreshold){
					cardsToDiscardIndexArray.add(index);
				}
				
				if(cardsToDiscardIndexArray.size() == MAX_CARDS_TO_DISCARD){
					break;
				}
				
			}
		}
		
		
		// swap out cards
		System.out.print("Dicarding cards: ");
		for(Integer cardIndex: cardsToDiscardIndexArray){
			PlayingCard card = handOfCards.returnCard(cardIndex);
			System.out.print(card.toString() + " ");
			deckOfCards.returnCard(card);
			handOfCards.receiveCard(deckOfCards.dealNext());
		}
		System.out.println();
		handOfCards.sort();
		
		return cardsToDiscardIndexArray.size();
	}
	
	public static void main(String[] args) {
		DeckOfCards deckOfCards = new DeckOfCards();
		deckOfCards.shuffle();
		
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
