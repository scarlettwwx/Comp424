package student_player;

import Saboteur.SaboteurBoard;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Board;
import boardgame.Move;

import java.lang.reflect.Array;
import java.util.*;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public static final int DEFAULT_SEARCH_DEPTH = 2;
    public static final int FIRST_STEP_SEARCH_DEPTH = 3;
    public static final int DEFAULT_TIMEOUT = 1930;
    public static final int FIRST_MOVE_TIMEOUT = 29930;

    public StudentPlayer() {
        super("260769099");
    }

    private class MoveValue {

        public double returnValue;
        public Move returnMove;

        public MoveValue(double returnValue) {
            this.returnValue = returnValue;
        }

        public MoveValue(double returnValue, Move returnMove) {
            this.returnValue = returnValue;
            this.returnMove = returnMove;
        }
    }

    private class StateMove{
        public Move move;
        public SaboteurBoardState boardState;
        public double eval;

        public StateMove(Move move, SaboteurBoardState boardState) {
            this.move = move;
            this.boardState = boardState;
            this.eval = evaluate(boardState); //to implement
        }
    }

    public static double minimax(int depth, SaboteurBoardStateCopy board, double alpha, double beta, boolean isMax){
        double score;
        ArrayList<SaboteurMove> allMoves = board.getAllLegalMoves();
        //if leaf node:
        if(depth == 0 || board.getWinner() == board.AGENT_ID || board.getWinner() == (1-board.AGENT_ID) || board.getWinner()==Board.DRAW){
            double result =  evaluate(board);
            System.out.println("RESULTS:");
            System.out.println(result);
            return result;
        }
        if(isMax){
            for(Iterator<SaboteurMove> a = allMoves.iterator(); a.hasNext();){
                SaboteurMove move = a.next();
                SaboteurBoardStateCopy child = new SaboteurBoardStateCopy(board);
                child.processMove(move);
                score = minimax(depth-1, child, alpha,beta,false);
                if(score> alpha){
                    alpha = score;
                }
                if(alpha>=beta){
                    break;
                }
            }
            return alpha;
        }else{
            for(Iterator<SaboteurMove> a = allMoves.iterator(); a.hasNext();){
                SaboteurMove move = a.next();
                SaboteurBoardStateCopy child = new SaboteurBoardStateCopy(board);
                child.processMove(move);
                score = minimax(depth-1, child, alpha,beta,true);
                if(score < beta){
                    beta = score;
                }
                if(alpha>=beta){
                    break;
                }
            }
            return beta;
        }
    }




    //1. evaluate on boardState
    public static double evaluate(SaboteurBoardState pBoard){
        int player_id = pBoard.getTurnPlayer();
        //needs to verify again
        if(pBoard.getWinner() == player_id){ return Integer.MAX_VALUE -1;}  //first player is us?
        else if(pBoard.getWinner() == 1-player_id){ return Integer.MIN_VALUE + 1;}  //second player
        else if(pBoard.getWinner() == Board.DRAW){ return 0.0;} //a tie

        boolean exist = player_id==0;
        double score1 = 3* MyTools.disToThreeHidden(pBoard);
        double score2 = MyTools.openEndsWholeBoard(pBoard);

        SaboteurTile[][] tiles= pBoard.getHiddenBoard();
        int numOfHidden = 0;
        int nugget= 0;
        int i =0;

        while(i<3){
            int j = 3 + i*2;
            //if the first card is hidden1 or 2.
            if(tiles[12][j].getIdx().equals("hidden1") || tiles[12][j].getIdx().equals("hidden2") ){
                numOfHidden++;
            }else if(tiles[12][j].getIdx().equals("nugget")){
                nugget++;
            }
            i++;
        }

        double score3 = numOfHidden*10 + nugget*30; //check hidden cards

        double score4 = pBoard.getNbMalus(pBoard.getTurnPlayer());

        return score1+score2+score3+score4;
    }

    //2. evaluate on the copy board:
    public static double evaluate(SaboteurBoardStateCopy pBoard){
        //needs to verify again
        int player_id = pBoard.getTurnPlayer();
        if(pBoard.getWinner() == player_id){ return Integer.MAX_VALUE -1;}  //first player is us?
        else if(pBoard.getWinner() == 1-player_id){ return Integer.MIN_VALUE + 1;}  //second player
        else if(pBoard.getWinner() == Board.DRAW){ return 0.0;} //a tie

        boolean exist = player_id==0;
        double score1 = 3*MyTools.disToThreeHidden(pBoard);
        double score2 = MyTools.openEndsWholeBoard(pBoard);
        SaboteurTile[][] tiles= pBoard.getHiddenBoard();
        int numOfHidden = 0;
        int nugget= 0;
        int i =0;
        while(i<3){
            int j = 3 + i*2;
            //if the first card is hidden1 or 2.
            if(tiles[12][j].getIdx().equals("hidden1") || tiles[12][j].getIdx().equals("hidden2") ){
                numOfHidden++;
            }else if(tiles[12][j].getIdx().equals("nugget")){
                nugget++;
            }
            i++;
        }
        double score3 = numOfHidden*10 + nugget*30; //check hidden cards

        double score4 = pBoard.getNbMalus(pBoard.getTurnPlayer());

        return score1+score2+score3+score4;
    }

      public static int evalMove (SaboteurMove sm) {
    		int[] posi=sm.getPosPlayed(); 
    		int disToEntrance = (posi[0]+posi[1]-10); // 往entrance之上放减一点分
    		int cardScore=0;
    		String name = sm.getCardPlayed().getName();
    		
    		//开放形tile加分：
    		String[] opening = {"0","0_flip", "5","5_flip","6","6_flip","7","7_flip","8","8_flip", "9","9_flip","10","10_flip"};
         for(String o : opening){
             if(o.equals(name)) {
            	 	cardScore += 1;
            	 	break;
             }
          }
         
         if(name.equals("Malus")) {
        	 	return 100;
         }
         
         if(name.equals("Map")) {
      	 	return 500;
          }
         
         if(name.equals("Bonus")) {
     	 	return 1000;
         }
         
         if(name.equals("Drop")) {
      	 	return -1000;
          }
         
    		return disToEntrance+cardScore;
      }


    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState board) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        MyTools.getSomething();
//        MyTools p1= new MyTools();
        Move myMove = null;
        // Is random the best you can do?
        ArrayList<Double> scoreOFMoves = new ArrayList<>();
        int i=0;

        double max=Integer.MIN_VALUE;
        for (SaboteurMove m : board.getAllLegalMoves()) {
            //System.out.println("Chose the move: "+ m.toPrettyString());
            SaboteurBoardStateCopy sbsc = new SaboteurBoardStateCopy(board);

            if (!sbsc.isLegal(m)) {
                System.out.println("illegal");
                continue;
            }
            sbsc.processMove(m);
            double curr = student_player.StudentPlayer.evaluate(sbsc);
            if(max< student_player.StudentPlayer.evaluate(sbsc)){
                max = curr;
                System.out.println(max);
                myMove = m;
            };

        }
        return myMove;

    }

    public static void main(String args[]){
        SaboteurBoardState newboard = new SaboteurBoardState();
        student_player.StudentPlayer a = new student_player.StudentPlayer();
        newboard.processMove( newboard.getRandomMove());
        double max = Integer.MIN_VALUE;
        SaboteurMove bestmove = null;
        for(SaboteurMove temp: newboard.getAllLegalMoves()){
            System.out.println(temp.toPrettyString());
            SaboteurBoardStateCopy copy  = new SaboteurBoardStateCopy(newboard);
//            copy.processMove(temp);
            System.out.println("Minimax results:");
            double curr = student_player.StudentPlayer.minimax(1,copy,Integer.MIN_VALUE,Integer.MAX_VALUE,true);
            System.out.println(curr);
            if(curr>max){
                max = curr;
                bestmove = temp;
            }
        }
        System.out.println();

        System.out.println(bestmove.toPrettyString());
        System.out.println(newboard.getAllLegalMoves().size());


    }

}
