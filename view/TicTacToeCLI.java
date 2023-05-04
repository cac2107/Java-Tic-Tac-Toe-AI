package view;
import java.util.Scanner;

import model.TicTacToe;

public class TicTacToeCLI {
    public static void main(String[] args){
        TicTacToe board = new TicTacToe();
        char ai;

        try(Scanner scanner = new Scanner(System.in)){
            System.out.print("What move should the AI play as? (X or O): ");
            String aiString = scanner.nextLine().toUpperCase();
            ai = aiString.toCharArray()[0];
            if(!(ai == 'X') && !(ai == 'O')){
                System.out.println("Invalid character... autosetting to X");
                ai = 'X';
            }

            while(!board.over()){

                if(board.getTurn() == ai){
                    board.printBoard();
                    System.out.println("AI making move");
                    board.aiMove();
                }

                board.printBoard();
                if(board.over()) { break; }
                System.out.print("Enter move coords (0 0): ");
                String move = scanner.nextLine();
                String[] moveArr = move.split(" ");
                int[] moveCoords = {Integer.parseInt(moveArr[0]), Integer.parseInt(moveArr[1])};
                board.move(moveCoords[0], moveCoords[1]);
            }
            scanner.close();
        }
    }
}
