package client;

import chess.*;
import ui.PreloginUI;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.lang.String;

public class ClientMain {
    static PreloginUI prelogin = new PreloginUI();

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Welcome to 240 chess. Type help to get started.\n");
        boolean request = true;
        while (request) {
            String executor = scanner.nextLine();
            if (executor.equalsIgnoreCase("help")) {
                ArrayList<String> list = prelogin.help();
                for (String s : list) {
                    System.out.printf("%s%n", s);
                }
                System.out.print("What would you like to do now?\n");
            } else if (executor.equalsIgnoreCase("quit")) {
                request = false;
            }
        }
    }
}
