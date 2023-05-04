package model;

import java.util.Arrays;

public class TicTacToe {
    private static char x = 'X';
    private static char o = 'O';
    private static char empty = 'E';
    private boolean gameOver = false;
    private boolean backtracking = false;
    private char winner;
    public int moveCount = 0;

    public char[][] board;

    public TicTacToe(){
        this.board = makeBoard();
    }

    /*
     * Second constructor for use by the minimax algorithm
     */
    public TicTacToe(char[][] oBoard, int moves){
        char[][] nBoard = new char[3][3];
        for(int xi = 0; xi < 3; xi++){
            for(int y = 0; y < 3; y++){
                nBoard[xi][y] = oBoard[xi][y];
            }
        }
        this.board = nBoard;
        this.moveCount = moves;
        this.backtracking = true;
    }

    /**
     * Constructs blank board
     * @return char[][] of the board
     */
    public char[][] makeBoard(){
        char[][] nBoard = new char[3][3];
        for(int xi = 0; xi < 3; xi++){
            for(int y = 0; y < 3; y++){
                nBoard[xi][y] = empty;
            }
        }
        return nBoard;
    }

    private boolean checkWin(){ return(!(getWin() == empty)); }

    public boolean over(){ return gameOver;}

    /**
     * Makes move for the current player.
     * @param xi row
     * @param y col
     */
    public void move(int xi, int y){
        char turn = getTurn();
        if(isValid(xi, y)){
            board[xi][y] = turn;
            moveCount++;
        }

        if(checkWin()){ 
            gameOver = true;
            winner = getWin();
            endGame();
        } else if(full()){
            gameOver = true;
            winner = empty;
            endGame();
        }
    }

    /**
     * Calculates the current turn using the move count.
     * @return char player
     */
    public char getTurn(){ if(moveCount % 2 == 0){ return x; } else { return o; }}

    /**
     * 
     * @param xi row
     * @param y col
     * @return boolean regarding whether the move was valid
     */
    private boolean isValid(int xi, int y){
        if(gameOver){
            return false;
        } else if(board[xi][y] == empty){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Checks all possible win combinations for a winner.
     * @return char of winner
     */
    public char getWin(){
        for(int xi = 0; xi < 3; xi++){
            if(board[xi][0] == empty){
                continue;
            }
            if(board[xi][0] == board[xi][1] && board[xi][0] == board[xi][2]){
                return board[xi][0];
            }
        }

        for(int y = 0; y < 3; y++){
            if(board[0][y] == empty){
                continue;
            }
            if(board[0][y] == board[1][y] && board[0][y] == board[2][y]){
                return board[0][y];
            }
        }

        if(!(board[0][0] == empty || board[0][2] == empty)){
            if(board[0][0] == board[1][1] && board[0][0] == board[2][2]){ return board[0][0]; }
            if(board[2][0] == board[1][1] && board[2][0] == board[0][2]){ return board[2][0]; }
        }

        return empty;
    }

    /**
     * End game sequence.
     */
    private void endGame(){
        if(backtracking){ return; }
        if(!(winner == 'E')){ System.out.println("The winner is: " + winner + "!"); }
        else {System.out.println("It's a draw!");}
    }

    /**
     * Checks if board is full for determining if there is a draw.
     * @return
     */
    private boolean full(){
        for(char[] row : board){
            for(char a : row){
                if(a == empty){ return false; }
            }
        }
        return true;
    }

    /**
     * Prints board
     */
    public void printBoard(){
        System.out.println("Turn: " + getTurn());
        for(int i = 0; i < 3; i++){
            System.out.println(Arrays.toString(board[i]));
        }
        System.out.println("");
    }

    /**
     * Used by the minimax algorithm
     * @return 
     */
    public int utility(){
        if(getWin() == x){
            return 1;
        } else if (getWin() == o){
            return -1;
        } else if (full()){ 
            return 0;
        } else { return 5; }
    }

    /**
     * Used by the minimax algorithm to find all possible moves.
     * @return int[][] of all moves
     */
    private int[][] getMoves(){
        int[][] moves = new int[9][2];
        int next = 0;

        for(int xi = 0; xi < 3; xi++){
            for(int y = 0; y < 3; y++){
                if(board[xi][y] == empty){
                    int[] a = {xi, y};
                    moves[next] = a;
                    next++;
                } 
            }
        }

        int[][] finalmoves = new int[next][2];
        for(int i = 0; i < next; i++){
            finalmoves[i] = moves[i];
        }
        return finalmoves;
    }

    /**
     * Finds the most optimal move.
     * @return
     */
    public int[] minimax(){
        int[] b = null;

        if(full()){ return b; }

        int[] winInOne = checkWinMove(getTurn());
        if(!(winInOne == null)){ return winInOne; }

        if(getTurn() == x){
            double optimalScore = Double.NEGATIVE_INFINITY;
            int[][] pMoves = getMoves();
            for(int[] move : pMoves){
                TicTacToe newGame = new TicTacToe(this.board, moveCount);
                newGame.move(move[0], move[1]);
                double score = minimaxHelper(newGame, newGame.getTurn());
                if(score > optimalScore){
                    optimalScore = score;
                    b = move;
                }
            }

        } else {
            double optimalScore = Double.POSITIVE_INFINITY;
            int[][] pMoves = getMoves();
            for(int[] move : pMoves){
                TicTacToe newGame = new TicTacToe(this.board, moveCount);
                newGame.move(move[0], move[1]);
                double score = minimaxHelper(newGame, newGame.getTurn());
                if(score < optimalScore){
                    optimalScore = score;
                    b = move;
                }
            }
        }

        return b;
    }

    /**
     * Recursive minimax algorithm to find value of move
     * @param game
     * @param user
     * @return doulbe of move score
     */
    public double minimaxHelper(TicTacToe game, char user){
        if(game.utility() != 5){
            return game.utility();
        }

        if(user == x){
            double optimal = Double.NEGATIVE_INFINITY;
            int[][] pMoves = getMoves();
            for(int[] move : pMoves){
                TicTacToe newGame = new TicTacToe(game.board, game.moveCount);
                newGame.move(move[0], move[1]);
                double score = newGame.minimaxHelper(newGame, newGame.getTurn());
                if(score > optimal){
                    optimal = score;
                }
            }
            return optimal;

        } else {
            double optimal = Double.POSITIVE_INFINITY;
            int[][] pMoves = game.getMoves();
            for(int[] move : pMoves){
                TicTacToe newGame = new TicTacToe(game.board, game.moveCount);
                newGame.move(move[0], move[1]);
                double score = newGame.minimaxHelper(newGame, newGame.getTurn());
                if(score < optimal){
                    optimal = score;
                }
            }
            return optimal;
        }
    }

    /**
     * This prevents the AI from choosing a move that will lead to a win, 
     * but maybe not in one move when it is possible to do so.
     * @param user
     * @return winning move
     */
    public int[] checkWinMove(char user){
        int[][] pMoves = getMoves();
        for (int[] move : pMoves) {
            TicTacToe newGame = new TicTacToe(this.board, this.moveCount);
            newGame.move(move[0], move[1]);
            if(newGame.getWin() == user){
                return move;
            }
        }

        return null;
    }

    /**
     * Handles making an AI Move.
     */
    public void aiMove(){
        int[] move = this.minimax();
        this.move(move[0], move[1]);
    }
}
