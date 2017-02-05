package poker;

import java.util.ArrayList;
import java.util.Random;

public class DeckOfCards {

	private PlayingCard[] deck = new PlayingCard[52];
	private ArrayList<PlayingCard> discardedCards = new ArrayList<PlayingCard>();
	
	private int nextCardIndex = deck.length - 1; 
	
	public void reset(){
		
	}	
	
	public void shuffle(){
		Random randrom = new Random();
		for(int i = 0 ; i < deck.length * 2; i++){
			int index1 = randrom.nextInt(52);
			int index2 = randrom.nextInt(52);
			
			// swap card positions in deck
			PlayingCard card = deck[index1];
			deck[index1] = deck[index2];
			deck[index2] = card;
		}
	}
	
	public PlayingCard dealNext(){
		return deck[nextCardIndex--];
	}
	
	public void returnCard(PlayingCard discarded){
		discardedCards.add(discarded);
	}
	
	public DeckOfCards(){	
		char[] suits = {PlayingCard.HEARTS, PlayingCard.DIAMONDS, PlayingCard.SPADES, PlayingCard.CLUBS};
		String[] types = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
		
		int counter = 0;
		for(int i = 0; i < suits.length; i++){
			for(int j = 0; j < types.length; j++){
				int gameValue = types[j] == "A" ? 14 : j + 1;
				deck[counter++] = new PlayingCard(types[j], suits[i], j + 1, gameValue);
				
			}
		}		
		
	}
	
	public static void main(String[] args) {
		DeckOfCards deckOfCards = new DeckOfCards();
		
		deckOfCards.shuffle();
		
		for(int i = 0; i < 52; i++){
			System.out.println(deckOfCards.dealNext());
		}
	}
	
}