import java.util.*;

public class Table {
    
    public Scanner keyboard;
    public String board;
    public int leftHead;
    public int rightHead;
    public Player player1, player2;
    public ArrayList<Domino> dominoes;
    public int dominoesLeft;
    
    //domino class
    public class Domino{
        public int left;
        public int right;
        public boolean available;
        
        //constructor
        public Domino(int l, int r){
            left = l;
            right = r;
            available = true;
        }
        
        @Override
        public String toString(){
            return ("[" + left + "|" + right + "]");
        }
        
        //swaps domino heads
        public void swap(){
            int buffer = right;
            right = left;
            left = buffer;
        }
    }
    //player class
    public class Player{
        public int handSize;
        public ArrayList<Domino> hand;
        //constructor
        public Player(){
            hand = new ArrayList<Domino>(0);
        }
        //prints dominoes in hand in a readable fashion
        public String printHand(){
            String out;
            
            if (handSize==0){
                return "empty";
            }
            else{
                out = hand.get(0).toString();
                for (int i = 1; i<handSize; i++){
                    out += ", " + hand.get(i).toString();
                }
                return out;
            } 
        }
        //draws a domino and updates data
        public void draw(){
            dominoesLeft--;
            Random rand = new Random();
            int randomNum;
            int buffer = handSize;
            while (buffer == handSize){
                randomNum = rand.nextInt(28);
                if (dominoes.get(randomNum).available){
                    handSize++;
                    hand.add(dominoes.get(randomNum));
                    dominoes.get(randomNum).available = false;
                }
            }
        }
        //handles player turn
        public boolean play(){
            boolean flag = false;
            int drew = 0;
            while (dominoesLeft > 0){
                //check all dominos in hand
                for (int i = 0; i<handSize; i++){
                    //if sequences checks both domino heads to both board heads
                    if (hand.get(i).left == leftHead){
                        flag = true;
                        handSize--;
                        hand.get(i).swap();
                        board = hand.get(i).toString() + board;
                        System.out.println(hand.get(i).toString() + " played on left train (1)");
                        leftHead = hand.get(i).left;
                        hand.remove(i);
                        break;
                    }
                    else if (hand.get(i).right == leftHead){
                        flag = true;
                        handSize--;
                        board = hand.get(i).toString() + board;
                        System.out.println(hand.get(i).toString() + " played on left train (2)");
                        leftHead = hand.get(i).left;
                        hand.remove(i);
                        break;
                    }
                    else if (hand.get(i).left == rightHead){
                        flag = true;
                        handSize--;
                        board = board + hand.get(i).toString();
                        System.out.println(hand.get(i).toString() + " played on right train (3)");
                        rightHead = hand.get(i).right;
                        hand.remove(i);
                        break;
                    }
                    else if (hand.get(i).right == rightHead){
                        flag = true;
                        handSize--;
                        hand.get(i).swap();
                        board = board + hand.get(i).toString();
                        System.out.println(hand.get(i).toString() + " played on right train (4)");
                        rightHead = hand.get(i).right;
                        hand.remove(i);
                        break;
                    }
                }
                if (!flag){
                    draw();
                }
                else if (flag){
                    System.out.println("player drew "+drew+" dominoes this turn");
                    return !flag;
                }
                
                drew++;
            }
            if (!flag){
                System.out.println("player drew "+drew+" dominoes this turn");
                System.out.println("player cannot play and cannot draw. player passes");
            }
            return !flag;
            
        }
    }
    
    // initializes objects, creates dominoes, and randomly distributes dominoes
    public void API(){
        System.out.println("initializing game");
        
        Random random = new Random();
        int randomNum;
        
        dominoesLeft = 8;
        
        player1 = new Player();
        player2 = new Player();
        
        player1.handSize = 0;
        player2.handSize = 0;
        
        dominoes = new ArrayList<Domino>(0);
        
        keyboard = new Scanner(System.in);
        
        System.out.println("creating dominoes");
        
        for (int i = 0; i<7; i++){
            for (int j = i; j<7; j++){
                dominoes.add(new Domino(i,j));
            }
        }
        
        System.out.println("randomly selecting players hands");
        
        while (player1.handSize < 10){
            randomNum = random.nextInt(28);
            if (dominoes.get(randomNum).available){
                player1.hand.add(dominoes.get(randomNum));
                player1.handSize++;
                dominoes.get(randomNum).available = false;
            }
        }
        
        while (player2.handSize < 10){
            randomNum = random.nextInt(28);
            if (dominoes.get(randomNum).available){
                player2.hand.add(dominoes.get(randomNum));
                player2.handSize++;
                dominoes.get(randomNum).available = false;
            }
        }
        
        System.out.println("game ready");
    }
    
    //method for handling first turn
    public void firstTurn(){
        
        //flag checks if a double is found in a player's hand
        boolean flag = false;
        //seek used in searching for a double domino
        int seek = 6;
        //until double is found
        while (!flag){
            //check player 1 hand for double, play if there            
            for (int i = 0; i<player1.handSize; i++){
                if (player1.hand.get(i).left == seek && player1.hand.get(i).right == seek){
                    flag = true;
                    player1.hand.remove(i);
                    player1.handSize--;
                    System.out.println("player 1 has highest double ["+seek+"|"+seek+"], placing to start game");
                    break;
                }
            }
            if (flag) break;
            //check player 2 hand for double, play if there   
            for (int i = 0; i<player2.handSize; i++){
                if (player2.hand.get(i).left == seek && player2.hand.get(i).right == seek){
                    flag = true;
                    player2.hand.remove(i);
                    player2.handSize--;
                    System.out.println("player 2 has highest double ["+seek+"|"+seek+"], placing to start game");
                    break;
                }
            }
            //update board
            board = "["+seek+"|"+seek+"]";
            leftHead = seek;
            rightHead = seek;
            
            seek--;
            // if neither player has a double, both draw another domino. Then a double is guaranteed
            if (!flag && seek<0){
                System.out.println("no player has double, drawing another domino");
                player1.draw();
                player2.draw();
                seek = 6;
            }
        }
        
        
    }
    
    //main game loop method
    public void runGame(){
        
        //create flags for game termination
        boolean pass1 = false;
        boolean pass2 = false;
        boolean winner = false;
        boolean draw = false;
        //create flags for turn organization
        int turn = 2;
        boolean player1Turn = true;
        String input;
        //determine who goes first
        if (player1.handSize<player2.handSize) player1Turn = false;
        
        //main game loop
        while (!winner && !draw){
            
            //create data visualization
            System.out.println("----------------------------------------------");
            System.out.println("round: " + turn/2);
            System.out.println(board);
            System.out.println("player 1 has " + player1.handSize +
                               " dominoes, player 2 has " + player2.handSize + " dominoes");
            
            System.out.println("dominoes left to draw: " + dominoesLeft);
            
            //ask user to continue or terminate
            if (player1Turn)
                System.out.print("player 1's turn, continue? (y/n) ");
            
            else 
                System.out.print("player 2's turn, continue? (y/n) ");
            
            input = keyboard.next();
            //user termination condition
            if (input.equals("n")){
                System.out.println("user terminated game");
                return;
            }
            //correct player takes their turn
            if (player1Turn) 
                pass1 = player1.play();
            else 
                pass2 = player2.play();
            
            //change turn organization flags
            turn++;
            player1Turn = !player1Turn;
            
            //check termination conditions
            if (player1.handSize == 0 || player2.handSize == 0) winner = true;
            if (pass1 && pass2) draw = true;
            
            if (turn > 30){
                System.out.println("infinite loop detected, terminating");
                return;
            }//if
        } //end main game loop
        //check which type of termination
        if (winner){
            //display winner
            if (player1.handSize==0)
                System.out.println("---------------------------------------------\n"+
                                 "player 1 wins!");
            else System.out.println("---------------------------------------------\n"+
                                 "player 2 wins!");
        }
        
        //display tie
        else System.out.println("---------------------------------------------\n"+
                              "Both players cannot play, game draw");
        
        //display end data
        System.out.println("end board:");
        System.out.println(board);
        System.out.print("player 1 hand:" + player1.printHand());
        System.out.println("player 2 hand:" + player2.printHand());
    }
}
