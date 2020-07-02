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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alonso del Arte
 */
public class NumberGuessingGameTest {

    @Test
    public void testGetUserName() {
        System.out.println("getUserName");
        String expected = "Jim";
        InputStream mockStream = new ByteArrayInputStream(expected.getBytes());
        Scanner input = new Scanner(mockStream);
        String actual = NumberGuessingGame.getUserName(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOtherUserName() {
        String expected = "Aabirah";
        InputStream mockStream = new ByteArrayInputStream(expected.getBytes());
        Scanner input = new Scanner(mockStream);
        String actual = NumberGuessingGame.getUserName(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testChooseNumber() {
        System.out.println("chooseNumber");
        int min = -100;
        int max = 100;
        String lowEndMsg = "Pseudorandom number should be greater than " + min
                + ", a(";
        String highEndMsg = "Pseudorandom number should be less than " + max
                + ", a(";
        int num;
        for (int i = 0; i < 1000; i++) {
            num = NumberGuessingGame.chooseNumber(min, max);
            assert num >= min : (lowEndMsg + i + ") = " + num);
            assert num <= max : (highEndMsg + i + ") = " + num);
        }
    }

    @Test
    public void testNumberDistribution() {
        HashSet<Integer> numbers = new HashSet<>();
        int min = -500;
        int max = 500;
        int span = max - min;
        int num;
        for (int i = 0; i < span; i++) {
            num = NumberGuessingGame.chooseNumber(min, max);
            numbers.add(num);
        }
        int expLen = span * 3 / 5;
        int actLen = numbers.size();
        String msg = "Set should have at least " + expLen
                + " distinct elements (it has " + actLen
                + " distinct elements)";
        assert expLen < actLen : msg;
    }

    @Test
    public void testGetNumberGuess() {
        System.out.println("getNumberGuess");
        int expected = 1729;
        String numStr = Integer.toString(expected);
        InputStream mockStream = new ByteArrayInputStream(numStr.getBytes());
        Scanner input = new Scanner(mockStream);
        int actual = NumberGuessingGame.getNumberGuess(input);
        System.out.println();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOtherNumberGuess() {
        int expected = 28347;
        String numStr = Integer.toString(expected);
        InputStream mockStream = new ByteArrayInputStream(numStr.getBytes());
        Scanner input = new Scanner(mockStream);
        int actual = NumberGuessingGame.getNumberGuess(input);
        System.out.println();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNumberGuessPropagatesException() {
        String invalidNumber = "Not an actual number";
        InputStream mockStream
                = new ByteArrayInputStream(invalidNumber.getBytes());
        Scanner input = new Scanner(mockStream);
        try {
            int num = NumberGuessingGame.getNumberGuess(input);
            System.out.println();
            String msg = "Trying to parse number from \"" + invalidNumber
                    + "\" should have caused an exception, not given result "
                    + num;
            fail(msg);
        } catch (NumberFormatException nfe) {
            System.out.println("Trying to parse number from \"" + invalidNumber
                    + "\" correctly caused NumberFormatException");
            System.out.println("\"" + nfe.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName()
                    + " is the wrong exception to throw for trying to parse \""
                    + invalidNumber + "\" as a number";
            fail(msg);
        }
    }

    /**
     * Test of scoreGame method, of class NumberGuessingGame. The formula for 
     * scoring the game is (<i>MG</i> - <i>g</i>) * &delta;, where <i>MG</i> is 
     * the maximum number of guesses the player may make, <i>g</i> is how many  
     * guesses the player made prior to getting it right, and &delta; is the 
     * absolute value of the difference between the prior guess and the right 
     * number, or, in the case the player got it right on the first guess, the 
     * maximum number that the number could be.
     * <p>With the right number being 50 out of a range from 1 to 100, and five 
     * guesses allowed, the following five scenarios are tested:</p>
     * <ol>
     *   <li>The player gets it right on the first guess. The score should be 
     *   500.</li>
     *   <li>The player guesses 100 and then 50. The score should be 200.</li>
     *   <li>The player guesses 100, 75 and then 50. The score should be 
     *   75.</li>
     *   <li>The player guesses 100, 75, 60 and then 50. The score should be 
     *   20.</li>
     *   <li>The player guesses 100, 75, 60, 49 and then 50. The score should 
     *   be 1.</li>
     * </ol>
     */
    @Test
    public void testScoreGame() {
        System.out.println("scoreGame");
        int[] guesses = {50, 100, 75, 60, 49};
        int[] expected = {500, 200, 75, 20, 1};
        int[] actual = new int[5];
        for (int i = 0; i < 5; i++) {
            actual[i] = NumberGuessingGame.scoreGame(50, guesses[i], i);
        }
        assertArrayEquals(expected, actual);
    }

    /*[TEMP JAVADOC DISABLE]*
     * Test of main method, of class NumberGuessingGame.
     */
//    @Test
//    public void testMain() {
//        System.out.println("main");
////        String[] args = null;
////        NumberGuessingGame.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
