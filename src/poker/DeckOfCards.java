
package poker;

import java.util.ArrayList;																					  
import java.util.Random;																					  	

public class DeckOfCards {
	private char[] suits = {PlayingCard.CLUBS, PlayingCard.DIAMONDS, PlayingCard.HEARTS, PlayingCard.SPADES}; // array to hold each suit. 
    private String[] types = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};			  // array to hold type e.g. A for Ace.
    
    public int DECK_SIZE = 52;
    
	private PlayingCard[] deck = new PlayingCard[DECK_SIZE]; 														  // array to store the deck of cards.
	private int nextCardIndex = deck.length - 1;									                          // index variable to keep track of next card to be dealt, initially set to last card in array	
	private ArrayList<PlayingCard> discardedCards = new ArrayList<PlayingCard>(); 							  // ArrayList to hold discarded cards
	
	
	public DeckOfCards(){ 										//Constructor
		int counter = 0;										
	    
	    for (int i = 0; i < suits.length; i++){
	    	for	(int j = 0; j < types.length; j++){
	    		int gameValue = j+1; 					
	    		if (types[j] == "A" ){							// if statement for when encountering "A" in String array types
	    			gameValue = 14;								// assign the value of 14 to "A" (OTHERWISE "A" would be assigned the value of 1)
	    		}
	    		PlayingCard card = new PlayingCard(types[j], suits[i],j+1 ,gameValue);
	    		deck[counter++] = card;   			   		
	    	}
	    }	
	}
	
	public void shuffle(){										
		Random random = new Random();							
		for(int i = 0; i < deck.length * deck.length; i++){		
			
			// this code randomly chooses two cards from the deck and swaps their position
			int index1 = random.nextInt(deck.length);			
			int index2 = random.nextInt(deck.length);			
			PlayingCard card1 = deck[index1];						
			PlayingCard card2 = deck[index2];						
			deck[index1] = card2;								
			deck[index2] = card1;								
		}
	}

	// this method resets the deck to its original form, by setting the next card index to the 
	// last card in the deck, it then creates a new discardedCards array list to remove any cards that were there
	public void reset(){										
		nextCardIndex = deck.length-1;							
		discardedCards = new ArrayList <PlayingCard>();			
	}
    	
	// deals next card from the deck based on the nextCardIndex pointer,
	// then decrements the pointer, original card remains in deck array but can't be dealt again
	// includes a check that pointer has not gone below zero, returning null if it has
	public synchronized PlayingCard dealNext(){								
		if(nextCardIndex < 0){									
			return null;										
		}else{													
			return deck[nextCardIndex--];						
		}
	}

	// when a card is returned, it is stored in the discardedCards array list
	public void returnCard(PlayingCard discarded){	
		if(discarded != null){
			discardedCards.add(discarded);	
		}
	}

	public static void main(String[] args) {					
		DeckOfCards deckOfCards = new DeckOfCards();			
		deckOfCards.shuffle();									
		
		// Test to see what happens when all cards are dealt
		// This loop tries to deal 53 cards, and prints out an error message when there are none left 
		for(int i = 0; i < 53; i++){							
			PlayingCard card = deckOfCards.dealNext();			
			if(card != null){									
				System.out.println(card);						
			}else{												
				System.out.println("ERROR: No more Cards");		
				break;		// exits the loop
			}
		}
		System.out.println();
		
		// then we reset the deck and shuffle and try do the same again,
		// to show that the cards have all been put back in the deck
		deckOfCards.reset();		
		deckOfCards.shuffle();
		
		for(int i = 0; i < 60; i++){							
			PlayingCard card = deckOfCards.dealNext();				
			if(card != null){									
				System.out.println(card);						
			}else{													
				System.out.println("ERROR: No more Cards");		
				break;		// exits the loop
			}
		}
		System.out.println();
		
		deckOfCards.reset();
		deckOfCards.shuffle();
		
		// Now we test if a card is returned to the deck, that it is not dealt again
		
		PlayingCard testCard = deckOfCards.dealNext();			// deals a card from deck
		deckOfCards.returnCard(testCard);						// retuns the card to the deck
		System.out.println("Returned card: " + testCard); 
		
		// then we deal the rest of the cards from the deck and check if the testCard gets dealt
		boolean found = false;		
		for(int i = 0; i < 53; i++){
			PlayingCard card = deckOfCards.dealNext();
			
			if(card != null){									
				if(card.toString() == testCard.toString()){			// checks is the card the same as our test card
					found = true;									// if it is we set boolean to true	
				}				
			}else{													
				System.out.println("ERROR: No more Cards");		
				break;		// exits the loop
			}			
		}
		
		// prints out result of test, if found is true, then we dealt the discarded card again, which is an error
		if(found){														
			System.out.println("Error discarded card dealt again ");	
		}else{															
			// otherwise we didn't deal discarded card again, which is what we want
			System.out.println("Success: Discarded card not dealt again ");		
		}
	}

}

