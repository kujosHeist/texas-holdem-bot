package poker;

public class PokerHand {
	
	private static int DEFAULT = 1000;
	
	// defines default value for each hand type, based on their order in HandOfCards enum Type (from 0-9)
	// therefore default hand values range from 0 (for high hand) to 9000 (for royal flush) 
	public static int HIGH_HAND_DEFAULT = HandOfCards.Type.HighHand.ordinal() * DEFAULT;
	public static int ONE_PAIR_DEFAULT = HandOfCards.Type.OnePair.ordinal() * DEFAULT;
	public static int TWO_PAIR_DEFAULT = HandOfCards.Type.TwoPair.ordinal() * DEFAULT;
	public static int THREE_OF_A_KIND_DEFAULT = HandOfCards.Type.ThreeOfAKind.ordinal() * DEFAULT;
	public static int STRAIGHT_DEFAULT = HandOfCards.Type.Straight.ordinal() * DEFAULT;
	public static int FLUSH_DEFAULT = HandOfCards.Type.Flush.ordinal() * DEFAULT;
	public static int FULL_HOUSE_DEFAULT = HandOfCards.Type.FullHouse.ordinal() * DEFAULT;
	public static int FOUR_OF_A_KIND_DEFAULT = HandOfCards.Type.FourOfAKind.ordinal() * DEFAULT;
	public static int STRAIGHT_FLUSH_DEFAULT = HandOfCards.Type.StraightFlush.ordinal() * DEFAULT;
	public static int ROYAL_FLUSH_DEFAULT = HandOfCards.Type.RoyalFlush.ordinal() * DEFAULT;	
	
	public static int getDefaultHandValue(HandOfCards.Type handType){
		int value = 0;
		
		switch(handType){
			case HighHand:
				value = HIGH_HAND_DEFAULT;
				break;
			case OnePair:
				value = ONE_PAIR_DEFAULT;
				break;
			case TwoPair:
				value = TWO_PAIR_DEFAULT;
				break;
				
			case ThreeOfAKind:
				value = THREE_OF_A_KIND_DEFAULT;
				break;
				
			case Straight:
				value = STRAIGHT_DEFAULT;
				break;
				
			case Flush:
				value = FLUSH_DEFAULT;
				break;
				
			case FullHouse:
				value = FULL_HOUSE_DEFAULT;
				break;
				
			case FourOfAKind:
				value = FOUR_OF_A_KIND_DEFAULT;
				break;						
				
			case StraightFlush:
				value = STRAIGHT_FLUSH_DEFAULT;
				break;						
				
			case RoyalFlush:
				value = ROYAL_FLUSH_DEFAULT;
				break;									
		}
		return value;
	}

}
