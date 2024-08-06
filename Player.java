public class Player
{
  private int sum; //Sum of cards
  private int[] cards = new int[11]; //Cards of the player
  private int cardsCount; //The number of cards they have
  private int tokens;
  private int bet;
  private int points;
  private int roundPoints; //The point they get for each round
  
  //Constructor
  public Player(int[] deck, int index)
  {
    this.cards[0] = deck[index];
    this.cards[1] = deck[index + 1];
    this.sum = this.cards[0] + this.cards[1];
    this.cardsCount = 2;
    this.tokens = 3;
    this.points = 0;
    this.roundPoints = 0;
  }
  
  //Player's two possible actions during their turn
  public void draw(int nextCard)
  {
    this.cards[cardsCount] = nextCard;
    this.sum += nextCard;
    cardsCount++;
  }
  
  public void addBet(int tokens)
  {
    this.bet += tokens;
    this.tokens -= tokens;
  }
  
  //Initialize the attributes
  public void initialize(int[] deck, int index)
  {
    this.cards[0] = deck[index];
    this.cards[1] = deck[index + 1];
    this.sum = this.cards[0] + this.cards[1];
    this.cardsCount = 2;
    this.tokens = 3;
    this.bet = 0;
    this.roundPoints = 0;
  }
  
  //Methods to return attributes
  public int[] getCards()
  {
    return this.cards;
  }
  
  public int getSum()
  {
    return this.sum;
  }
  
  public int getTokens()
  {
    return this.tokens;
  }
  
  public int getBet()
  {
    return this.bet;
  }
  
  public int getPoints()
  {
    return this.points;
  }
  
  public int getCardsCount()
  {
    return this.cardsCount;
  }
  
  public int getRoundPoints()
  {
    return this.roundPoints;
  }
  
  //Updates when player wins the game or when it is a draw
  public void win(int bet)
  {
    this.roundPoints += 2;
    this.roundPoints += (this.bet + bet);
    this.points += this.roundPoints;
  }
  
  public void drawGame()
  {
    this.points++;
  }

  public void initializePoints()
  {
    this.points = 0;
  }
} //End of class