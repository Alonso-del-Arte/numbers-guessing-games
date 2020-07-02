/*
 * Copyright (C) 2020 Alonso del Arte
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package games;

import java.util.Scanner;

/**
 * The number guessing game specified by the Java 3 Week 1 class document. The
 * requirements given by the instructors are that:
 * <ul>
 *   <li>Greeting message that greets user by name</li>
 *   <li>Application randomly generate an integer to guess between 0 and
 *   100</li>
 *   <ul>
 *     <li>Do not disclose the winning number until the end</li>
 *   </ul>
 *   <li>When the user guesses the number</li>
 *   <ul>
 *     <li>Application should not accept non-integers</li>
 *     <li>Application prints personalized winning message if input number
 *     matches random number</li>
 *     <li>Application tells user if they are too high or too low  if they guess
 *     the wrong number</li>
 *     <ul>
 *       <li>Application only allows the user up to 5 guesses</li>
 *       <li>After five chances the user loses </li>
 *       <li>Disclose answer at the end</li>
 *     </ul>
 *   </ul>
 *   <li>Application gives the option to play again or quit</li>
 * </ul>
 * As I'm understanding these requirements, a player is not penalized for
 * guessing a letter or other non-numeric character. However, it is my
 * understanding that the player is penalized for guessing outside the specified
 * range (e.g., 5 million when the range is 1 to 100.
 * @author Alonso del Arte
 */
public class NumberGuessingGame extends GameWithLeaderBoard {

    public static final int MAXIMUM_GUESSES = 5;
    public static final int MINIMUM_NUMBER = 1;
    public static final int MAXIMUM_NUMBER = 100;

    private static String playerName;

    private static final String NUMBER_QUERY = "Please enter an integer between "
            + MINIMUM_NUMBER + " and " + MAXIMUM_NUMBER + ": ";

    private static HighScoreTable table;

    @Override
    long gameID() {
        return 4549604001330510643L;
    }

    public static int chooseNumber(int min, int max) {
        int span = max - min;
        double rnd = Math.random() * span;
        return (int) Math.floor(rnd) + min;
    }

    // STUB
    public static int getNumberGuess(Scanner input) {
        System.out.print(NUMBER_QUERY);
        String guess = input.nextLine();
        return Integer.parseInt(guess);
    }

    void playRound(Scanner input) {
        System.out.println();
        boolean notGuessedYet = true;
        int guessCount = 0;
        int number = chooseNumber(MINIMUM_NUMBER, MAXIMUM_NUMBER);
        int prevGuess = number - MAXIMUM_NUMBER;
        int guessedNumber;
        while (notGuessedYet && guessCount < MAXIMUM_GUESSES) {
            try {
                guessedNumber = getNumberGuess(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, didn't catch that, "
                        + nfe.getMessage());
                System.out.println("Maybe try again...");
                continue;
            }
            if (guessedNumber == number) {
                notGuessedYet = false;
                int score = scoreGame(number, prevGuess, guessCount);
                System.out.println("That's right! You win!");
                System.out.println("With a score of " + score + ".");
                updateScoreBoard(score);
            } else {
                if (guessedNumber < number) {
                    System.out.println("Too low.");
                } else {
                    System.out.println("Too high.");
                }
            }
            prevGuess = guessedNumber;
            guessCount++;
        }
        if (notGuessedYet) {
            System.out.println("Sorry, you ran out of guesses.");
            System.out.println("The number was " + number);
        }
    }

    @Override
    void playRound() {
        this.playRound(new Scanner(System.in));
    }

    static int scoreGame(int rightGuess, int priorGuess, int guessOrd) {
        int delta;
        if (guessOrd == 0) {
            delta = MAXIMUM_NUMBER;
        } else {
            delta = Math.abs(rightGuess - priorGuess);
        }
        return (MAXIMUM_GUESSES - guessOrd) * delta;
    }

    private static void updateScoreBoard(int score) {
        // TODO: Implement
    }

    public static void main(String[] args) {
        greetUser();
        NumberGuessingGame game = new NumberGuessingGame();
        try (Scanner scan = new Scanner(System.in)) {
            playerName = getUserName(scan);
            boolean keepPlaying = true;
            while (keepPlaying) {
                game.playRound(scan);
                keepPlaying = askToPlayAgain(scan);
            }
        }
    }

}
