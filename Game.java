/* Description: 21 points game where each player try to get to 21 points or as close as possible.
 * They can add bet to score more points if they win. The computer calculates probabilities and acts like a player.
 * */

import java.io.*;
import java.util.Scanner;

public class Game
{
  //declare Console variable
  static Scanner i;
  static PrintStream c;
  
  //Player objects for the human player and computer
  static Player human;
  static Player computer;
  
  public static void main (String[] args) throws IOException
  {       
    c = new PrintStream(System.out);   
    
    i = new Scanner(System.in);
    
    //The round number, the option from user input, and the index to keep track of where the last card is drawn
    int rounds, option, index = 0;
    int winner = 0; //Winner of each round
    String name; //The player's name
    //The deck of cards
    int[] deck = new int[52];
    deck = shuffle();
    
    //Prompt user for their name
    c.println("Enter player name");
    name = i.nextLine();
    
    //Make sure the player doesn't get two cards with sum bigger than 20
    while (deck[index] + deck[index + 1] > 20 || deck[index + 2] + deck[index + 3] > 20)
    {
      index++;
    }
    
    //Initialize player objects
    human = new Player(deck, index);
    computer = new Player(deck, index + 2);
    
    //Keep running until the user exits the game
    do
    {
      human.initializePoints();
      computer.initializePoints();
      
      //Prompt the user for number of rounds
      c.println("Enter the number of rounds");
      rounds = i.nextInt();
      //Arrays to keep track of each player's points each round
      int[] roundPointsHuman = new int[rounds + 1];
      int[] roundPointsComputer = new int[rounds + 1];
    
      //Each round of the game
      for (int i = 1; i <= rounds; i++)
      {
        //Clear the console and output the player's information
        c.println("\nRound " + i);
        outputHumanInfo();
        
        //Turns for both players, end when both players pass their turn
        while (true)
        {
          //Prompt user for the action they want to do
          option = input(3, 1, "\n1 - Draw a card\n2 - Increase bet\n3 - Pass");
          
          //Determine if the player wants to draw
          if (option == 1)
          {
            //Draw a card for the player
            human.draw(deck[index]);
            index++;
            outputHumanInfo();
          
            //Determine if the player has busted or reached 21 points
            if (human.getSum() > 21)
            {
              c.println(name + " has busted");
              winner = 2;
              break;
            }
            else if (human.getSum() == 21)
            {
              c.println(name + " has reached 21 points");
              winner = 1;
              break;
            }
            else
            {
              option = input(2, 1, "\n1 - End round\n2 - Increase bet");
            }
          } //End of if
          
          //Determine if the player wants to increase their bet
          if (option == 2)
          {
            //Prompt user for the number of tokens they want to add to their bet
            human.addBet(input(human.getTokens(), 1, "Enter number of tokens to add"));
          }
          
          //Determine if both players want to pass their turn
          if (option == 3 && computerPass())
          {
            //Determine the winner of the round based on the sum of each player
            if (human.getSum() > computer.getSum())
            {
              winner = 1;
              break;
            }
            else if (computer.getSum() > human.getSum())
            {
              winner = 2;
              break;
            }
            else
            {
              //When both players have the same sum, determine who have the higher bet
              if (human.getBet() > computer.getBet())
              {
                winner = 1;
                break;
              }
              else if (computer.getBet() > human.getBet())
              {
                winner = 2;
                break;
              }
              else
              {
                winner = 0;
                break;
              } //End of inner if
            } //End of middle if
          } //End of outer if
          
          //Determine if computer wants to draw
          if (computerDraw())
          {
            computer.draw(deck[index]);
            index++;
            c.println("Computer draws a card");
            outputComputerInfo();
          }
          
          //Determine if computer has busted or reached 21 points
          if (computer.getSum() > 21)
          {
            c.println("Computer has busted");
            winner = 1;
            break;
          }
          else if (computer.getSum() == 21)
          {
            c.println("Computer has reached 21 points");
            winner = 2;
            break;
          }
          
          //Determine if computer wants to add bet
          if (computerAddBet())
          {
            computer.addBet(3);
            c.println("Computer adds 3 tokens to its bet");
            outputComputerInfo();
          }
        } //End of while loop
        
        //Output the player's information and the computer's cards
        outputComputerCards();
        
        //Determine the winner of the round and give points accordingly
        if (winner == 1)
        {
          c.println(name + " is the winner");
          human.win(computer.getBet());
        }
        else if (winner == 2)
        {
          c.println("Computer is the winner");
          computer.win(human.getBet());
        }
        else
        {
          c.println("It is a draw");
          human.drawGame();
          computer.drawGame();
        }
        
        //Record each player's points for the round
        roundPointsHuman[i] = human.getRoundPoints();
        roundPointsComputer[i] = computer.getRoundPoints();
        
        //Shuffle the deck and reset the index
        deck = shuffle();
        index = 0;
        
        //Make sure the players don't get two cards with a sum bigger than 20
        while (deck[index] + deck[index + 1] > 20 || deck[index + 2] + deck[index + 3] > 20)
        {
          index++;
        }
        
        //Initialize the player objects for next round
        human.initialize(deck, index);
        computer.initialize(deck, index + 2);
        
        //Ask user if they want to end the game
        option = input(1, 0, "Enter 0 to exit or 1 to continue");
        if (option == 0)
        {
          break;
        }
      } //End of for loop
   
      //Output the results of the game
      outputResults(name, rounds, roundPointsHuman, roundPointsComputer);
      
      //Prompt user if they want to start a new game
      option = input(1, 0, "Enter 1 to start a new game or 0 to end");
    }
    while (option == 1);
        
  }//end of main - this line comment labels the closing bracket for the main method
  
  //Query method to randomly generate a deck of 52 cards
  public static int[] shuffle()
  {
    int[] deck = new int[52];
    int index;
    
    //Loop through every card from ace to king
    for (int i = 1; i <= 13; i++)
    {
      //Loop through the four cards for each number
      for (int j = 0; j < 4; j++)
      {
        //Randomly assign the card a location in the deck
        do
        {
          index = (int)Math.round(Math.random() * 51);
        }
        while (deck[index] != 0);
        
        deck[index] = i;
      }
    }
    
    return deck;
  }
  
  //Command method to output the player's information
  public static void outputHumanInfo()
  {
    int[] cards = human.getCards();
    
    c.print("Cards: ");
    
    //Output each card the player have
    for (int i = 0; i < human.getCardsCount() - 1; i++)
    {
      c.print(cards[i] + ", ");
    }
    
    c.println(cards[human.getCardsCount() - 1]);
    
    //Output the player's total, tokens, bet and points
    c.println("Total: " + human.getSum());
    c.println("Tokens: " + human.getTokens());
    c.println("Bet: " + human.getBet());
    c.println("Points: " + human.getPoints());
  }
  
  //Command method to output computer's cards
  public static void outputComputerCards()
  {
    int[] cards = computer.getCards();
    
    c.print("Computer cards: ");
    
    for (int i = 0; i < computer.getCardsCount() - 1; i++)
    {
      c.print(cards[i] + ", ");
    }
    
    c.println(cards[computer.getCardsCount() - 1]);
  }
  
  //Query method to input a valid integer between a maximum and minimum value
  public static int input(int max, int min, String prompt)
  {
    int inputInt;
    String inputString = "";
    
    //Keep asking for an input until a valid number is entered
    while (true)
    {
      try
      {
        c.println(prompt);
        inputString = i.nextLine();
        
        inputInt = Integer.parseInt(inputString);
        
        //Determine if input is out of range
        if (inputInt <= max && inputInt >= min)
        {
          break;
        }
        else
        {
          c.println(inputInt + " is out of range");
        }
      }
      catch (NumberFormatException e)
      {
        c.println(inputString + " is invalid");
      }
    }
    
    return inputInt;
  }
  
  //Query method to decide if the computer should draw a card
  public static boolean computerDraw()
  {
    double pBust;
    pBust = (double)(computer.getSum() - 8) / 13;
    
    //Determine if the probability of busting is more than 60%
    if (pBust > 0.60)
    {
      return false;
    }
    else
    {
      return true;
    }
  }
  
  //Query method to decide if the computer should increase its bet
  public static boolean computerAddBet()
  {
    double pBust;
    pBust = (double)(computer.getSum() - 8) / 13;
    
    //Determine if computer's sum is close enough to 21
    if (pBust > 0.60 && computer.getTokens() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  //Query method to determine if the computer should pass its turn
  public static boolean computerPass()
  {
    //Determine if the computer doesn't want to draw and have no tokens to bet
    if (!computerDraw() && computer.getTokens() == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  //Command method to determine the winner and output the results of each round
  public static void outputResults(String name, int rounds, int[] humanPoints, int[] computerPoints)
  {
    c.println("\n**************************");
    c.println("Game ends");
      
    //Determine the winner
    if (human.getPoints() > computer.getPoints())
    {
      c.println(name + " wins the game");
    }
    else if (computer.getPoints() > human.getPoints())
    {
      c.println("Computer wins the game");
    }
    else
    {
      c.println("It is a draw");
    }
      
    //Output the total points to both the console and file
    c.print(name);
    c.println(" - Points " + human.getPoints());
    
    c.print("Computer");
    c.println(" - Points " + computer.getPoints());
    
    //Output player points from each round to both the console and file
    for (int i = 1; i <= rounds; i++)
    {
      c.println("Round " + i);
      c.println(name + " - " + humanPoints[i]);
      c.println("Computer - " + computerPoints[i]);
    }
  }
  
  //Command method to output the computer's bet and points
  public static void outputComputerInfo()
  {
    c.println("Computer");
    c.println("Bet: " + computer.getBet());
    c.println("Points: " + computer.getPoints());
  }
  
}//end of class - this line comment labels the closing bracket for the class