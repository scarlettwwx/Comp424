package student_player;

import Saboteur.SaboteurBoard;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Board;
import boardgame.BoardState;
import student_player.StudentPlayer;
import java.lang.reflect.Array;
import java.util.*;
//import Path;

/*
 * aTiles: tiles in the path
 * OpenEnds: number of Open ends in this path
 * startsAtOrigin: 1, starts at origin.  0, starts at somewhere else
 * */

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }

    //    public final int BOARD_SIZE =  SaboteurBoardState.BOARD_SIZE;
    public static final int BOARD_SIZE =  42;
    public static int[][] aBoard;
    public static SaboteurTile[][] aTileBoard;
    public static boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];

    public MyTools(){

    }

    public static void initVisited(){
        for(int i = 0; i<BOARD_SIZE;i++){  //initialize visited array.
            for(int j=0;j<BOARD_SIZE;j++){
                visited[i][j]=false;
            }
        }

    }

    public static void printIntBoard(){
        for(int i =0; i<BOARD_SIZE; i++){
            for(int j=0;j<BOARD_SIZE;j++){
                System.out.print(aBoard[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }



    public static ArrayList<student_player.Path> searchBoard(int[][] pBoard,SaboteurTile[][] tiles){
        student_player.MyTools.aTileBoard = tiles;
        student_player.MyTools.aBoard = pBoard;
        initVisited(); //needs it as to initialize visited array everytime.
        ArrayList<student_player.Path> arr =new ArrayList<student_player.Path>();
        student_player.Path temp = new student_player.Path();
        temp.numOnes = temp.dfs(16,16);
        arr.add(temp);
//        System.out.println(temp);
        return arr;
    }



    //wenwen
    public static int openEndsWholeBoard (SaboteurBoardState boardState) {
        int num = 0;
        int[][] intBoard = boardState.getHiddenIntBoard();
        for (int i= 1; i< intBoard.length-7;i++) {
            for (int j= 0; j< intBoard.length-1;j++) {
                //check row
                if((intBoard[i][j] == -1 && intBoard[i][j+1] == 1) || (intBoard[i][j] == 1 && intBoard[i][j+1] == -1)) {
                    num++;
                }
                //check column
                if((intBoard[i-1][j] == -1 && intBoard[i][j] == 1) || (intBoard[i][j] == 1 && intBoard[i+1][j] == -1)) {
                    num++;
                }

            }
        }
        return num;
    }

    //for copy board:

    public static int openEndsWholeBoard (SaboteurBoardStateCopy boardState) {
        int num = 0;
        int[][] intBoard = boardState.getHiddenIntBoard();
        for (int i= 1; i< intBoard.length-7;i++) {
            for (int j= 0; j< intBoard.length-1;j++) {
                //check row
                if((intBoard[i][j] == -1 && intBoard[i][j+1] == 1) || (intBoard[i][j] == 1 && intBoard[i][j+1] == -1)) {
                    num++;
                }
                //check column
                if((intBoard[i-1][j] == -1 && intBoard[i][j] == 1) || (intBoard[i][j] == 1 && intBoard[i+1][j] == -1)) {
                    num++;
                }
            }
        }
        return num;
    }

    public static int pathToHiddenDis (ArrayList<student_player.Path> paths, int[] coord) {
        int total= 0;
        for (student_player.Path a: paths){
            total += a.manhattanDistance(coord[0],coord[1]);
        }
        return total;
    }

    public static int disToThreeHidden(SaboteurBoardStateCopy boardState) {
        int credit=0;
        int[] leftHidden= {36,10};
        int[] middleHidden= {36,16};
        int[] rightHidden= {36,22};

        SaboteurTile[][] tiles = boardState.getHiddenBoard();
        int[][] board =  boardState.getHiddenIntBoard();

        ArrayList<student_player.Path> temp2 = student_player.MyTools.searchBoard(board,tiles);

        if (temp2.isEmpty()) {
            return Integer.MIN_VALUE+1;
        }

        //if three hidden objectives are not visible;
        if(tiles[12][3].getIdx() == "8" && tiles[12][5].getIdx() == "8" &&tiles[12][7].getIdx() == "8") {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, middleHidden)-pathToHiddenDis(temp2, rightHidden);
//            System.out.println("3 Hidden");
        }
        //if one nugget is visible or the other two hidden1/2 are visible
        else if(tiles[12][3].getIdx().equals("nugget") || (tiles[12][5].getIdx().equals("hidden1") && tiles[12][7].getIdx().equals("hidden2")) || (tiles[12][7].getIdx().equals("hidden1") && tiles[12][5].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, leftHidden);
//            System.out.println("1st is nugget");
        }
        else if(tiles[12][5].getIdx().equals("nugget") || (tiles[12][3].getIdx().equals("hidden1") && tiles[12][7].getIdx().equals("hidden2")) || (tiles[12][7].getIdx().equals("hidden1") && tiles[12][3].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, middleHidden);
//            System.out.println("2nd is nugget");
        }
        else if(tiles[12][7].getIdx().equals("nugget") || (tiles[12][5].getIdx().equals("hidden1") && tiles[12][3].getIdx().equals("hidden2")) || (tiles[12][3].getIdx().equals("hidden1") && tiles[12][5].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, rightHidden);
//            System.out.println("3rd is nugget");
        }
        //if one hidden is visible:
        else if (tiles[12][3].getIdx().equals("hidden1") || tiles[12][3].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, middleHidden)-pathToHiddenDis(temp2, rightHidden);
//            System.out.println("1st Hidden visible");

        }
        else if (tiles[12][5].getIdx().equals("hidden1") || tiles[12][5].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, rightHidden);
//            System.out.println("2nd Hidden visible");

        }
        else if (tiles[12][7].getIdx().equals("hidden1") || tiles[12][7].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, middleHidden);
//            System.out.println("3rd Hidden visible");

        }
        int numOnes = temp2.get(0).numOnes;
//        System.out.println(credit);
        return 10*credit + numOnes;
    }

    //OVERLOADING
    public static int disToThreeHidden(SaboteurBoardState boardState) {
        int credit=0;
        int[] leftHidden= {36,10};
        int[] middleHidden= {36,16};
        int[] rightHidden= {36,22};

        SaboteurTile[][] tiles = boardState.getHiddenBoard();
        int[][] board =  boardState.getHiddenIntBoard();

        ArrayList<student_player.Path> temp2 = student_player.MyTools.searchBoard(board,tiles);

        if (temp2.isEmpty()) {
            return Integer.MIN_VALUE+1;
        }

        //if three hidden objectives are not visible;
        if(tiles[12][3].getIdx() == "8" && tiles[12][5].getIdx() == "8" &&tiles[12][7].getIdx() == "8") {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, middleHidden)-pathToHiddenDis(temp2, rightHidden);
//            System.out.println("3 Hidden");
        }
        //if one nugget is visible or the other two hidden1/2 are visible
        else if(tiles[12][3].getIdx().equals("nugget") || (tiles[12][5].getIdx().equals("hidden1") && tiles[12][7].getIdx().equals("hidden2")) || (tiles[12][7].getIdx().equals("hidden1") && tiles[12][5].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, leftHidden);
//            System.out.println("1st is nugget");
        }
        else if(tiles[12][5].getIdx().equals("nugget") || (tiles[12][3].getIdx().equals("hidden1") && tiles[12][7].getIdx().equals("hidden2")) || (tiles[12][7].getIdx().equals("hidden1") && tiles[12][3].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, middleHidden);
//            System.out.println("2nd is nugget");
        }
        else if(tiles[12][7].getIdx().equals("nugget") || (tiles[12][5].getIdx().equals("hidden1") && tiles[12][3].getIdx().equals("hidden2")) || (tiles[12][3].getIdx().equals("hidden1") && tiles[12][5].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, rightHidden);
//            System.out.println("3rd is nugget");
        }
        //if one hidden is visible:
        else if (tiles[12][3].getIdx().equals("hidden1") || tiles[12][3].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, middleHidden)-pathToHiddenDis(temp2, rightHidden);
//            System.out.println("1st Hidden visible");

        }
        else if (tiles[12][5].getIdx().equals("hidden1") || tiles[12][5].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, rightHidden);
//            System.out.println("2nd Hidden visible");

        }
        else if (tiles[12][7].getIdx().equals("hidden1") || tiles[12][7].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, middleHidden);
//            System.out.println("3rd Hidden visible");

        }
        int numOnes = temp2.get(0).numOnes;
//        System.out.println(credit);
        return 10*credit + numOnes;
    }


    /*
     * Overload to check a path's hidden distance.
     * */

    public static int disToThreeHidden(student_player.Path temp) {
        int credit=0;
        int[] leftHidden= {36,10};
        int[] middleHidden= {36,16};
        int[] rightHidden= {36,22};
        SaboteurTile[][] tiles = student_player.MyTools.aTileBoard;
        int[][] board =  student_player.MyTools.aBoard;
        ArrayList<student_player.Path> temp2= new ArrayList<student_player.Path>();
        temp2.add(temp);
//        student_player.MyTools tryy = new student_player.MyTools(board);
//        ArrayList<student_player.Path> temp2 = student_player.MyTools.searchBoard();

        //if three hidden objectives are not visible;
        if(tiles[12][3].getIdx() == "8" && tiles[12][5].getIdx() == "8" &&tiles[12][7].getIdx() == "8") {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, middleHidden)-pathToHiddenDis(temp2, rightHidden);
        }
        //if one nugget is visible or the other two hidden1/2 are visible
        else if(tiles[12][3].getIdx().equals("nugget") || (tiles[12][5].getIdx().equals("hidden1") && tiles[12][7].getIdx().equals("hidden2")) || (tiles[12][7].getIdx().equals("hidden1") && tiles[12][5].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, leftHidden);
        }
        else if(tiles[12][5].getIdx().equals("nugget") || (tiles[12][3].getIdx().equals("hidden1") && tiles[12][7].getIdx().equals("hidden2")) || (tiles[12][7].getIdx().equals("hidden1") && tiles[12][3].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, middleHidden);
        }
        else if(tiles[12][7].getIdx().equals("nugget") || (tiles[12][5].getIdx().equals("hidden1") && tiles[12][3].getIdx().equals("hidden2")) || (tiles[12][3].getIdx().equals("hidden1") && tiles[12][5].getIdx().equals("hidden2")) ) {
            credit = -pathToHiddenDis(temp2, rightHidden);
        }
        //if one hidden is visible:
        else if (tiles[12][3].getIdx().equals("hidden1") || tiles[12][3].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, middleHidden)-pathToHiddenDis(temp2, rightHidden);
        }
        else if (tiles[12][5].getIdx().equals("hidden1") || tiles[12][5].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, rightHidden);
        }
        else if (tiles[12][7].getIdx().equals("hidden1") || tiles[12][7].getIdx().equals("hidden2")) {
            credit = -pathToHiddenDis(temp2, leftHidden)-pathToHiddenDis(temp2, middleHidden);
        }
        return credit;
    }
}




class Path{
    public int numOnes;
    public int[] deepestEnd={0,0};
    public int openEnds = 0;

    public static final int[] dx = {1,0,0,-1};
    public static final int[] dy = {0,-1,1,0};

    @Override
    public String toString() {
        return "Path{" +
                "numOnes=" + numOnes +
                ", deepestEnd=" + Arrays.toString(deepestEnd) +
                ", openEnds=" + openEnds +
                '}';
    }

    public int dfs(int i, int j){
        if( i<0 || i>= student_player.MyTools.BOARD_SIZE || j<0 || j>= student_player.MyTools.BOARD_SIZE ){return 0;}
        if( student_player.MyTools.aBoard[i][j]!= 1 || student_player.MyTools.visited[i][j]){

            if ( student_player.MyTools.aBoard[i][j]== -1 ){  //if open ends
                int row = deepestEnd[0];
                int col = deepestEnd[1];
//                if (i>row){  //check if it's the deepest.
//                    deepestEnd[0]=i;
//                    deepestEnd[1]=j;
//
//                }else if(i==row){ //use disToThreeHidden to check who's the deepest end
                int[] coordinate = new int[2];
                coordinate[0]=i;
                coordinate[1]=j;
                student_player.Path temp1 = new student_player.Path();
                temp1.deepestEnd=coordinate;
                student_player.Path temp2 = new student_player.Path();
                temp2.deepestEnd=deepestEnd;
                int l1dis = student_player.MyTools.disToThreeHidden(temp1);
                int l2dis = student_player.MyTools.disToThreeHidden(temp2);
                if(l1dis > l2dis ){
                    deepestEnd[0]=i;
                    deepestEnd[1]=j;
                }

                openEnds++;
            }
            return 0;
        }else{ //if another 1 has been found
            student_player.MyTools.visited[i][j] = true;
            int ans = 1;
            for (int d=0; d<4;d++){
                ans = ans + dfs(i+dx[d],j+dy[d]);
            }
            return ans;


        }
    }


    public int manhattanDistance(int c, int d) {
        //two coordinate: (deepestEnd[0],deepestEnd[1]) (c,d)
        return Math.abs(deepestEnd[0]-c)+Math.abs(deepestEnd[1]-d);
    }

}



