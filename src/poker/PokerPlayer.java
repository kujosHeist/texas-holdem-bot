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

		ArrayList<PlayingCard> cardsDiscarded = new ArrayList<PlayingCard>();
		HashMap<Integer, Integer> possibleDiscards = new HashMap<Integer, Integer>();		
		
		boolean noDefiniteCardsToDiscard = true; 

		for(int index = 0; index < handOfCards.CARDS_PER_HAND; index++){
			int discardProbability = handOfCards.getDiscardProbability(index);
			
			if(discardProbability == handOfCards.KEEP){
				continue;
			}else if(discardProbability == handOfCards.DISCARD && cardsDiscarded.size() < MAX_CARDS_TO_DISCARD){
				
				PlayingCard discardCard = handOfCards.returnCard(index);
				deckOfCards.returnCard(discardCard);
				cardsDiscarded.add(discardCard);
				
				noDefiniteCardsToDiscard = false;
			}else{
				possibleDiscards.put(index, discardProbability);
			}
		}
		
		Random random = new Random();
		
		if(noDefiniteCardsToDiscard && cardsDiscarded.size() < MAX_CARDS_TO_DISCARD){
			
			for(Integer index: possibleDiscards.keySet()){
				int probabilityMultiplier = MAX_CARDS_TO_DISCARD - cardsDiscarded.size(); 
				
				int discardProbability = possibleDiscards.get(index) * probabilityMultiplier;
				
				int discardThreshold = random.nextInt(MAX_PROBABILITY);
				
				if(discardProbability <= discardThreshold){
					PlayingCard discardCard = handOfCards.returnCard(index);
					deckOfCards.returnCard(discardCard);
					cardsDiscarded.add(discardCard);
				}
				
				if(cardsDiscarded.size() == MAX_CARDS_TO_DISCARD){
					break;
				}
				
			}
		}
		
		System.out.print("Dicarding cards: ");
		for(PlayingCard card: cardsDiscarded){
			System.out.print(card.toString() + " ");
		}
		System.out.println();
		
		for (int i = 0; i < cardsDiscarded.size(); i++) {
			PlayingCard card = deckOfCards.dealNext();
			handOfCards.receiveCard(card);
		}
		
		handOfCards.sort();
		
		return cardsDiscarded.size();
	}
	

}
