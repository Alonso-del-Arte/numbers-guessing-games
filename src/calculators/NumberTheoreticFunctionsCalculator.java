/*
 * Copyright (C) 2020 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package calculators;

import algebraics.AlgebraicDegreeOverflowException;
import algebraics.AlgebraicInteger;
import algebraics.IntegerRing;
import algebraics.NonEuclideanDomainException;
import algebraics.NonUniqueFactorizationDomainException;
import algebraics.NotDivisibleException;
import algebraics.UnsupportedNumberDomainException;
import algebraics.quadratics.ImaginaryQuadraticInteger;
import algebraics.quadratics.ImaginaryQuadraticRing;
import algebraics.quadratics.QuadraticInteger;
import algebraics.quadratics.QuadraticRing;
import algebraics.quadratics.RealQuadraticInteger;
import algebraics.quadratics.RealQuadraticRing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A collection of number theoretic functions, including basic primality testing
 * and the Euclidean GCD algorithm.
 * @author Alonso del Arte
 */
public class NumberTheoreticFunctionsCalculator {

    /**
     * The only twenty-one values of <i>d</i> such that
     * <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub> is a unique factorization
     * domain and a norm-Euclidean domain. These numbers are listed in <a
     * href="http://oeis.org/A048981">Sloane's A048981</a>.
     */
    public static final int[] NORM_EUCLIDEAN_QUADRATIC_RINGS_D = {-11, -7, -3, -2, -1, 2, 3, 5, 6, 7, 11, 13, 17, 19, 21, 29, 33, 37, 41, 57, 73};

    /**
     * The only five negative values <i>d</i> such that
     * <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub> is a norm-Euclidean domain.
     * These numbers, &minus;11, &minus;7, &minus;3, &minus;2, &minus;1, are the
     * first five terms listed in <a href="http://oeis.org/A048981">Sloane's
     * A048981</a>. These numbers correspond to <b>Z</b>[<i>i</i>],
     * <b>Z</b>[&radic;&minus;2], <b>Z</b>[&omega;],
     * <i>O</i><sub><b>Q</b>(&radic;&minus;7)</sub> and
     * <i>O</i><sub><b>Q</b>(&radic;&minus;11)</sub>.
     */
    public static final int[] NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D = {-11, -7, -3, -2, -1};

    /**
     * The sixteen positive values <i>d</i> such that
     * <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub> is a norm-Euclidean domain.
     * These numbers are listed in <a href="http://oeis.org/A048981">Sloane's
     * A048981</a> (together with five negative values). They correspond to
     * <b>Z</b>[&radic;2], <b>Z</b>[&radic;3], <b>Z</b>[&radic;&phi;],
     * <b>Z</b>[&radic;6], <b>Z</b>[&radic;7], <b>Z</b>[&radic;11],
     * <i>O</i><sub><b>Q</b>(&radic;13)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;17)</sub>, <b>Z</b>[&radic;19],
     * <i>O</i><sub><b>Q</b>(&radic;21)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;29)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;33)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;37)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;41)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;57)</sub> and
     * <i>O</i><sub><b>Q</b>(&radic;73)</sub>.
     */
    public static final int[] NORM_EUCLIDEAN_QUADRATIC_REAL_RINGS_D = {2, 3, 5, 6, 7, 11, 13, 17, 19, 21, 29, 33, 37, 41, 57, 73};

    /**
     * There are the only nine negative numbers d such that the ring of
     * algebraic integers of <b>Q</b>(&radic;d) is a unique factorization domain
     * (though not necessarily Euclidean). These numbers are, in descending
     * order: -1, -2, -3, -7, -11, -19, -43, -67, -163 (in this constant array,
     * they are in the reverse order). These correspond to <b>Z</b>[i],
     * <b>Z</b>[&radic;-2], <b>Z</b>[&omega;],
     * <i>O</i><sub><b>Q</b>(&radic;-7)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-11)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-19)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-43)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-67)</sub> and
     * <i>O</i><sub><b>Q</b>(&radic;-163)</sub>.
     */
    public static final int[] HEEGNER_NUMBERS = {-163, -67, -43, -19, -11, -7, -3, -2, -1};

    /**
     * The ring of Gaussian integers, <b>Z</b>[<i>i</i>].
     */
    public static final ImaginaryQuadraticRing RING_GAUSSIAN = new ImaginaryQuadraticRing(-1);

    /**
     * The imaginary unit <i>i</i> = &radic;-1.
     */
    public static final ImaginaryQuadraticInteger IMAG_UNIT_I = new ImaginaryQuadraticInteger(0, 1, RING_GAUSSIAN, 1);

    /**
     * The additive inverse of the imaginary unit, -<i>i</i> = -&radic;-1.
     */
    public static final QuadraticInteger IMAG_UNIT_NEG_I = IMAG_UNIT_I.times(-1);

    /**
     * The ring of Eisenstein integers, <b>Z</b>[&omega;].
     */
    public static final ImaginaryQuadraticRing RING_EISENSTEIN = new ImaginaryQuadraticRing(-3);

    /**
     * A complex cubic root of unity, &omega; = -1/2 + (&radic;-3)/2, a number
     * such that &omega;<sup>3</sup> = 1.
     */
    public static final ImaginaryQuadraticInteger COMPLEX_CUBIC_ROOT_OF_UNITY = new ImaginaryQuadraticInteger(-1, 1, RING_EISENSTEIN, 2);

    /**
     * <b>Z</b>[&phi;], the ring of algebraic integers of <b>Q</b>(&radic;5).
     */
    public static final RealQuadraticRing RING_ZPHI = new RealQuadraticRing(5);

    /**
     * The number 1/2 + (&radic;5)/2 &asymp; 1.618, the fundamental unit of
     * <b>Z</b>[&phi;].
     */
    public static final RealQuadraticInteger GOLDEN_RATIO = new RealQuadraticInteger(1, 1, RING_ZPHI, 2);

    /**
     * Determines the prime factors of a given number. Uses simple trial
     * division with only basic optimization.
     * @param num The integer for which to determine prime factors of.
     * @return A list of the prime factors, with some factors repeated as
     * needed. For example, given num = 44100, the resulting list should be 2,
     * 2, 3, 3, 5, 5, 7, 7. The factorization of 0 is given as just 0. For a
     * negative number, the factorization starts with -1 followed by the
     * factorization of its positive counterpart. For example, given num =
     * -44100, the resulting list should be -1, 2, 2, 3, 3, 5, 5, 7, 7.
     */
    public static List<Integer> primeFactors(int num) {
        int n = num;
        List<Integer> factors = new ArrayList<>();
        if (n == 0) {
            factors.add(0);
        } else {
            if (n < 0) {
                n *= (-1);
                factors.add(-1);
            }
            while (n % 2 == 0) {
                factors.add(2); // Treating 2 as a special case
                n /= 2;
            }
            for (int i = 3; i <= n; i += 2) {
                while (n % i == 0) {
                    factors.add(i);
                    n /= i;
                }
            }
        }
        return factors;
    }

    /**
     * Determines whether a given purely real number is prime or not. The
     * numbers 0, &minus;1, 1, &minus;2, 2 are treated as special cases (the
     * first three are not prime, the last two are). For all others, the
     * function searches only for the least positive prime factor. If the least
     * positive prime factor is found to be unequal to the absolute value of the
     * number, the function returns false. This function is open to
     * optimization.
     * @param num The number to be tested for primality. Examples: &minus;29,
     * 30, &minus;42, 43.
     * @return True if the number is prime (even if negative), false otherwise.
     * For example, &minus;2 and 47 should both return true, &minus;25, 0 and 91
     * should all return false.
     */
    public static boolean isPrime(int num) {
        switch (num) {
            case -1:
            case 0:
            case 1:
                return false;
            case -2:
            case 2:
                return true;
            default:
                if (num % 2 == 0) {
                    return false;
                } else {
                    boolean primeFlag = true;
                    int potentialFactor = 3;
                    double numSqrt = Math.sqrt(Math.abs(num));
                    while (primeFlag && potentialFactor <= numSqrt) {
                        primeFlag = (num % potentialFactor != 0);
                        potentialFactor += 2;
                    }
                    return primeFlag;
                }
        }
    }

    /**
     * Determines whether a given purely real number is prime or not.
     * @param num The number to be tested for primality.
     * @return True if the number is prime, false otherwise.
     */
    public static boolean isPrime(long num) {
        if (num == -1 || num == 0 || num == 1) {
            return false;
        }
        if (num == -2 || num == 2) {
            return true;
        }
        if (num % 2 == 0) {
            return false;
        } else {
            boolean primeFlag = true;
            long potentialFactor = 3;
            double numSqrt = Math.sqrt(Math.abs(num));
            while (primeFlag && potentialFactor <= numSqrt) {
                primeFlag = (num % potentialFactor != 0);
                potentialFactor += 2;
            }
            return primeFlag;
        }
    }

    /**
     * Determines whether a given algebraic integer is prime or not. It is not
     * enough for the number to be irreducible, it must be prime and not a unit,
     * meaning that if <i>p</i> divides <i>ab</i> for any combination of
     * <i>a</i> and <i>b</i> from the same ring as <i>p</i>, then either
     * <i>p</i> divides <i>a</i> or it divides <i>b</i>, maybe both.
     * @param num The number to be tested for primality. Examples: 1 +
     * &radic;&minus;2, 2 + &radic;10.
     * @return True if the number is prime, false if a unit or composite. For
     * example, 1 + &radic;&minus;2 is prime, while 2 + &radic;10 is irreducible
     * but not prime.
     * @throws ArithmeticException If a norm computation error occurs (this is a
     * runtime exception), such as, for example, if the norm of an imaginary
     * quadratic integer is erroneously said to be negative.
     * @throws UnsupportedNumberDomainException Thrown when called upon a number
     * from a type of ring that is not fully supported yet. For example, as of
     * 2018, this program does not really have support for cubic integers, so
     * asking if 3 &minus; &#8731;2 is prime would probably trigger this
     * exception.
     */
    public static boolean isPrime(AlgebraicInteger num) {
        if (isPrime(num.norm())) {
            return true;
        }
        if (num instanceof ImaginaryQuadraticInteger) {
            if (num.norm() < 0) {
                String exceptionMessage = "Overflow has occurred for the computation of the norm of " + num.toASCIIString();
                throw  new ArithmeticException(exceptionMessage);
            }
        }
        if (num instanceof ImaginaryQuadraticInteger || num instanceof RealQuadraticInteger) {
            QuadraticInteger n = (QuadraticInteger) num;
            int radic = n.getRing().getRadicand();
            int surdPart = n.getSurdPartMult();
            if (radic == -1 && n.getRegPartMult() == 0) {
                if (isPrime(surdPart)) {
                    return ((Math.abs(surdPart)) % 4 == 3);
                } else {
                    return false;
                }
            }
            if (radic == -3 && surdPart != 0) {
                QuadraticInteger pureReal = n.times(COMPLEX_CUBIC_ROOT_OF_UNITY);
                if (pureReal.getSurdPartMult() != 0) {
                    pureReal = pureReal.times(COMPLEX_CUBIC_ROOT_OF_UNITY);
                }
                if (pureReal.getSurdPartMult() == 0) {
                    if (pureReal.getRegPartMult() < 0) {
                        pureReal = pureReal.times(-1);
                    }
                    if (isPrime(pureReal.getRegPartMult())) {
                        return (pureReal.getRegPartMult() % 3 == 2);
                    } else {
                        return false;
                    }
                }
            }
            if (surdPart == 0) {
                int absRegPartMult = Math.abs(n.getRegPartMult());
                if (absRegPartMult == 2) {
                    byte symbol = symbolKronecker(radic, 2);
                    if (radic % 4 == -1 || radic % 4 == 3) {
                        symbol = 0;
                    }
                    return (symbol == -1);
                }
                if (isPrime(absRegPartMult)) {
                    switch (radic) {
//                        case -163:
//                        case -67:
//                        case -43:
//                        case -19:
//                        case -11:
//                        case -7:
//                            return (symbolLegendre(num.imagQuadRing.negRad, absRealPartMult) == -1);
                        case -3:
                            return (absRegPartMult % 3 == 2);
                        case -2:
                            return (absRegPartMult % 8 == 5 || absRegPartMult % 8 == 7);
                        case -1:
                            return (absRegPartMult % 4 == 3);
//                        cases 2, 3, 5, 6, 7, 11, 13, 17, 19, 21, 29, 33, 37, 41, 57, 73:
                        default:
                            return (symbolLegendre(radic, absRegPartMult) == -1);
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        String exceptionMessage = "The domain of the number " + num.toASCIIString() + " is not yet supported.";
        throw new UnsupportedNumberDomainException(exceptionMessage, num);
    }

    /**
     * The Legendre symbol, a number theoretic function which tells if a given
     * number is a quadratic residue modulo an odd prime. There is no overflow
     * checking, but hopefully that's only a problem for numbers that are very
     * close to {@link Integer#MIN_VALUE} or {@link Integer#MAX_VALUE}.
     * @param a The number to test for being a quadratic residue modulo an odd
     * prime. For example, 10.
     * @param p The odd prime to test a for being a quadratic residue modulo of.
     * For example, 7. This parameter may be negative; the function will quietly
     * change it to a positive number; this behavior is not guaranteed for
     * future versions of this program.
     * @return &minus;1 if a is quadratic residue modulo p, 0 if gcd(a, p) > 1, 1 if a
     * is a quadratic residue modulo p. An example of each: Legendre(10, 7) = &minus;1
     * since there are no solutions to <i>x</i><sup>2</sup> = 10 mod 7; Legendre(10, 5) = 0 since
     * 10 is a multiple of 5; and Legendre(10, 3) = 1 since <i>x</i><sup>2</sup> = 10 mod 3 does
     * have solutions, such as <i>x</i> = 4.
     * @throws IllegalArgumentException If p is not an odd prime. Note that this
     * is a runtime exception.
     */
    public static byte symbolLegendre(int a, int p) {
        if (!isPrime(p)) {
            throw new IllegalArgumentException(p + " is not a prime number. Consider using the Jacobi symbol instead.");
        }
        if (p == -2 || p == 2) {
            throw new IllegalArgumentException(p + " is not an odd prime. Consider using the Kronecker symbol instead.");
        }
        if (euclideanGCD(a, p) > 1) {
            return 0;
        }
        final int oddPrime = Math.abs(p); // Making sure p is positive
        final int exponent = (oddPrime - 1)/2;
        final int modStop = oddPrime - 2;
        int adjA = a;
        if (adjA > (oddPrime - 1)) {
            adjA %= oddPrime;
        }
        if (adjA == (oddPrime - 1)) {
            adjA = -1;
        }
        while (adjA < -1) {
            adjA += oddPrime;
        }
        int power = adjA;
        for (int i = 1; i < exponent; i++) {
            power *= adjA;
            while (power > modStop) {
                power -= oddPrime;
            }
            while (power < -1) {
                power += oddPrime;
            }
        }
        return (byte) power;
    }

    /**
     * The Jacobi symbol, a number theoretic function. This implementation is
     * almost entirely dependent on the Legendre symbol.
     * @param n Parameter n, for example, 8.
     * @param m Parameter m, for example, 15.
     * @return The result, for example, 1.
     * @throws IllegalArgumentException If m is even or negative (or both). Note
     * that this is a runtime exception.
     */
    public static byte symbolJacobi(int n, int m) {
        if (m % 2 == 0) {
            throw new IllegalArgumentException(m + " is not an odd number. Consider using the Kronecker symbol instead.");        }
        if (m < 0) {
            throw new IllegalArgumentException(m + " is not a positive number. Consider using the Kronecker symbol instead.");
        }
        if (m == 1) {
            return 1;
        }
        if (euclideanGCD(n, m) > 1) {
            return 0;
        }
        List<Integer> mFactors = primeFactors(m);
        List<Integer> symbols = new ArrayList<>();
        int curr;
        for (Integer mFactor : mFactors) {
            curr = symbolLegendre(n, mFactor);
            symbols.add(curr);
        }
        int symbol = symbols.stream().reduce(1, (a, b) -> a * b);
        return (byte) symbol;
    }

    private static byte symbolKroneckerNegOne(int n) {
        if (n < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    private static byte symbolKroneckerTwo(int n) {
        int nMod8 = n % 8;
        switch (nMod8) {
            case -7:
            case -1:
            case 1:
            case 7:
                return 1;
            case -5:
            case -3:
            case 3:
            case 5:
                return -1;
            default:
                return 0;
        }
    }

    /**
     * The Kronecker symbol, a number theoretic function. This implementation
     * relies in great part on the Legendre symbol, but not at all on the Jacobi
     * symbol.
     * @param n Parameter n, for example, 3.
     * @param m Parameter m, for example, 2.
     * @return The result, for example, &minus;1.
     */
    public static byte symbolKronecker(int n, int m) {
        if (euclideanGCD(n, m) > 1) {
            return 0;
        }
        if (m == 1) {
            return 1;
        }
        if (m == 0) {
            if (n == -1 || n == 1) {
                return 1;
            } else {
                return 0;
            }
        }
        List<Integer> mFactors = primeFactors(m);
        int currMFactorIndex = 0;
        int kindaOmega = mFactors.size();
        byte symbol = 1;
        if (mFactors.get(currMFactorIndex) == -1) {
            symbol *= symbolKroneckerNegOne(n);
            currMFactorIndex++;
        }
        int currFactor;
        boolean keepGoing = true; // Keep going with Kronecker(n, 2)?
        while (currMFactorIndex < kindaOmega && keepGoing) {
            currFactor = mFactors.get(currMFactorIndex);
            keepGoing = (currFactor == 2);
            if (keepGoing) {
                symbol *= symbolKroneckerTwo(n);
                currMFactorIndex++;
            }
        }
        while (currMFactorIndex < kindaOmega) {
            currFactor = mFactors.get(currMFactorIndex);
            symbol *= symbolLegendre(n, currFactor);
            currMFactorIndex++;
        }
        return symbol;
    }

    /**
     * Sorts a list of algebraic integers in ascending order by the absolute
     * value of the norm. Do note that integers of the same norm might come back
     * in the same order relative to each other that they were originally in.
     * The sorting is done with a rudimentary bubble sort algorithm, so do not
     * use this if speed or efficiency are required.
     * @param listAlgInt A list of algebraic integers, which may be in any order
     * whatsoever. For example: &minus;1 + <i>i</i>, 4 + <i>i</i>,
     * &minus;<i>i</i>, 1 &minus; <i>i</i>. The algebraic integers need not come
     * from the same ring and there is no checking here of whether or not that
     * is the case.
     * @return A list of the algebraic integers sorted by norm. For example:
     * &minus;<i>i</i>, &minus;1 + <i>i</i>, 1 &minus; <i>i</i>, 4 + <i>i</i>.
     * Note that there is no checking of norm overflows, so, for example,
     * imaginary quadratic integer objects with erroneously negative norms would
     * be erroneously sorted before units.
     */
    public static List<AlgebraicInteger> sortListAlgebraicIntegersByNorm(List<AlgebraicInteger> listAlgInt) {
        boolean swapFlag;
        AlgebraicInteger a, b;
        List<AlgebraicInteger> nums = new ArrayList<>();
        listAlgInt.forEach((ai) -> {
            nums.add(ai);
        });
        int opLen = listAlgInt.size() - 1;
        if (opLen > 0) {
            do {
                swapFlag = false;
                for (int counter = 0; counter < opLen; counter++) {
                    a = nums.get(counter);
                    b = nums.get(counter + 1);
                    if (Math.abs(a.norm()) > Math.abs(b.norm())) {
                        nums.set(counter, b);
                        nums.set(counter + 1, a);
                        swapFlag = true;
                    }
                }
                a = nums.get(opLen);
                b = nums.get(0);
                if (Math.abs(a.norm()) < Math.abs(b.norm())) {
                    nums.set(opLen, b);
                    nums.set(0, a);
                    swapFlag = true;
                }
            } while (swapFlag);
        }
        return nums;
    }

    /**
     * Computes the prime factors, and unit factors when applicable, of an
     * algebraic integer from a unique factorization domain (UFD).
     * @param num The algebraic integer to find the prime factors of. For
     * example, &minus;4 + 3&radic;(&minus;19).
     * @return A list of algebraic integers, with the first possibly being a
     * unit, the rest should be primes. For example, &minus;1, 5/2 &minus;
     * &radic;(&minus;19)/2, 7/2 &minus; &radic;(&minus;19)/2, which multiply to
     * &minus;4 + 3&radic;(&minus;19). The only time this should return 0 is in
     * a list by itself when the input number is 0.
     * @throws NonUniqueFactorizationDomainException This checked exception will
     * be thrown if this function is called upon to compute the prime factors of
     * a number from a non-UFD, even if a complete factorization into primes is
     * possible in the given domain, e.g., 5 and 41 in
     * <b>Z</b>[(&radic;&minus;5)]. However, as of 2018, this exception will be
     * erroneously thrown for numbers from real quadratic integer rings that are
     * UFDs but not norm-Euclidean. This is a compromise while I work on getting
     * the timed <b>Z</b>[(&radic;103)] fundamental unit test to pass.
     * @throws UnsupportedNumberDomainException Thrown when called upon a number
     * from a type of ring that is not fully supported yet. For example, as of
     * 2018, this program hardly has any support for cubic integers, so asking
     * for the prime factorization of &minus;15 + 2&#8731;2 +
     * (&#8731;2)<sup>2</sup> would probably trigger this exception.
     */
    public static List<AlgebraicInteger> primeFactors(AlgebraicInteger num) throws NonUniqueFactorizationDomainException {
        if (num instanceof QuadraticInteger) {
            QuadraticInteger number = (QuadraticInteger) num;
            QuadraticInteger unity = number.minus(number).plus(1);
            int d = number.getRing().getRadicand();
            boolean notUFDFlag = true;
            if ((d > -164) && (d < 0)) {
                for (int heegNum : HEEGNER_NUMBERS) {
                    if (d == heegNum) {
                        notUFDFlag = false;
                    }
                }
            }
            if (d > 0){
                for (int normEuclRealD : NORM_EUCLIDEAN_QUADRATIC_REAL_RINGS_D) {
                    if (d == normEuclRealD) {
                        notUFDFlag = false;
                    }
                }
            }
            if (notUFDFlag) {
                String exceptionMessage = number.getRing().toASCIIString() + " is not a unique factorization domain.";
                throw new NonUniqueFactorizationDomainException(exceptionMessage, num);
            }
            if ((d < 0) && (num.norm() < 0)) {
                String exceptionMessage = "A norm computation error occurred for " + num.toASCIIString() + ", which should not have norm " + num.norm();
                throw new ArithmeticException(exceptionMessage);
            }
            List<AlgebraicInteger> factors = new ArrayList<>();
            if (Math.abs(number.norm()) < 2) {
                factors.add(number);
                return factors;
            }
            if (isPrime(number)) {
                factors.add(unity);
                factors.add(number);
                number = unity; // Prime divided by itself is 1
            } else {
                QuadraticInteger testDivisor = unity.plus(1); // This should be 2
                boolean keepGoing = true;
                if (isPrime(testDivisor)) {
                    while (number.norm() % 4 == 0) {
                        try {
                            number = number.divides(testDivisor);
                            factors.add(testDivisor);
                        } catch (NotDivisibleException nde) {
                            keepGoing = ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                        }
                    }
                }
                testDivisor = testDivisor.plus(1);
                while (Math.abs(number.norm()) > Math.abs(testDivisor.norm()) && keepGoing) {
                    if (isPrime(testDivisor)) {
                        while (number.norm() % testDivisor.norm() == 0) {
                            try {
                                number = number.divides(testDivisor);
                                factors.add(testDivisor);
                            } catch (NotDivisibleException nde) {
                                if (d < 0) {
                                    keepGoing = ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                                } else {
                                    keepGoing = (Math.abs(testDivisor.norm()) <= Math.abs(number.norm()));
                                }
                            }
                        }
                    }
                    testDivisor = testDivisor.plus(2);
                }
                int testDivRegPartMult = 0;
                int testDivSurdPartMult = 2; // These will be divided by 2 regardless of d = 1 mod 4 or not
                if (number.getRing().hasHalfIntegers()) {
                    testDivRegPartMult = 1;
                    testDivSurdPartMult = 1;
                }
                boolean withinRange;
                while (Math.abs(number.norm()) > 1) {
                    if (d < 0) {
                        testDivisor = new ImaginaryQuadraticInteger(testDivRegPartMult, testDivSurdPartMult, number.getRing(), 2);
                    } else {
                        testDivisor = new RealQuadraticInteger(testDivRegPartMult, testDivSurdPartMult, number.getRing(), 2);
                    }
                    if (d < 0) {
                        withinRange = (testDivisor.norm() <= number.norm());
                    } else {
                        withinRange = (testDivisor.norm() <= Math.abs(number.norm()));
                    }
                    if (isPrime(testDivisor)) {
                        while (number.norm() % testDivisor.norm() == 0) {
                            try {
                                number = number.divides(testDivisor.conjugate());
                                factors.add(testDivisor.conjugate());
                            } catch (NotDivisibleException nde) {
                                // withinRange = withinRange && ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                            }
                            try {
                                number = number.divides(testDivisor);
                                factors.add(testDivisor);
                            } catch (NotDivisibleException nde) {
                                // withinRange = withinRange && ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                            }
                        }
                    }
                    if (withinRange) {
                        testDivRegPartMult += 2;
                    } else {
                        if (!number.getRing().hasHalfIntegers()) {
                            testDivRegPartMult = 0;
                            testDivSurdPartMult += 2;
                        } else {
                            if (testDivSurdPartMult % 2 == 0) {
                                testDivRegPartMult = 1;
                            } else {
                                testDivRegPartMult = 0;
                            }
                            testDivSurdPartMult++;
                        }
                    }
                }
                factors.add(number); // This should be a unit, most likely -1 or 1
            }
            factors = sortListAlgebraicIntegersByNorm(factors);
            QuadraticInteger currFac;
            QuadraticInteger currFirstUnit = number;
            for (int i = 1; i < factors.size(); i++) {
                currFac = (QuadraticInteger) factors.get(i);
                if (currFac.getRegPartMult() < 0 || ((currFac.getRegPartMult() == 0 && currFac.getSurdPartMult() < 0))) {
                    factors.set(i, currFac.times(-1));
                    currFirstUnit = currFirstUnit.times(-1);
                    factors.set(0, currFirstUnit);
                }
            }
            if (unity.equals(factors.get(0))) {
                factors.remove(0);
            }
            return factors;
        }
        throw new UnsupportedNumberDomainException("At this time only ImaginaryQuadraticInteger and RealQuadraticInteger are supported for this factorization operation.", num);
    }

    /**
     * Determines whether a given number is irreducible, not necessarily prime.
     * @param num The number for which to make the determination. For example, 1
     * + &radic;&minus;5.
     * @return True if num is irreducible, false if not. For example, 1 +
     * &radic;&minus;5 is famously irreducible but not prime. Also, units are
     * considered irreducible by this function.
     * @throws ArithmeticException If a norm computation error occurs (this is a
     * runtime exception), such as, for example, if the norm of an imaginary
     * quadratic integer is erroneously said to be negative.
     * @throws UnsupportedNumberDomainException Thrown when called upon a number
     * from a type of ring that is not fully supported yet. For example, as of
     * 2018, this program does not really have support for cubic integers, so
     * asking if 3 &minus; &#8731;2 is irreducible would probably trigger this
     * exception.
     * @throws NullPointerException If <code>num</code> is null. The exception
     * message will be "Null is not an algebraic integer, neither reducible nor
     * irreducible".
     */
    public static boolean isIrreducible(AlgebraicInteger num) {
        if (num instanceof ImaginaryQuadraticInteger || num instanceof RealQuadraticInteger) {
            if (num instanceof ImaginaryQuadraticInteger && num.norm() < 0) {
                String exceptionMessage = "Overflow has occurred for the computation of the norm of " + num.toASCIIString();
                throw new ArithmeticException(exceptionMessage);
            }
            if (isPrime(num.norm())) {
                return true;
            } else {
                if (Math.abs(num.norm()) < 2) {
                    return true;
                } else {
                    QuadraticInteger number = (QuadraticInteger) num;
                    QuadraticRing r = number.getRing();
                    int d = r.getRadicand();
                    boolean normEuclD = false;
                    for (int euclD : NORM_EUCLIDEAN_QUADRATIC_RINGS_D) {
                        if (d == euclD) {
                            normEuclD = true;
                        }
                    }
                    if (normEuclD) {
                        return isPrime(number);
                    } else {
                        boolean withinRange = true;
                        boolean presumedIrreducible = true;
                        QuadraticInteger testDivisor, currDivision;
                        int testDivRegPartMult = 4;
                        int testDivSurdPartMult = 0;
                        boolean testDivisorChanged;
                        while (withinRange && presumedIrreducible) {
                            if (d < 0) {
                                testDivisor = new ImaginaryQuadraticInteger(testDivRegPartMult, testDivSurdPartMult, r, 2);
                            } else {
                                testDivisor = new RealQuadraticInteger(testDivRegPartMult, testDivSurdPartMult, r, 2);
                            }
                            testDivisorChanged = false;
                            withinRange = (Math.abs(testDivisor.norm()) < Math.abs(number.norm()));
                            while (withinRange && presumedIrreducible) {
                                if (Math.abs(testDivisor.norm()) == 1) {
                                    withinRange = false;
                                } else {
                                    try {
                                        currDivision = number.divides(testDivisor);
                                        if (Math.abs(currDivision.norm()) > 1) {
                                            presumedIrreducible = false;
                                        } else {
                                            withinRange = false;
                                        }
                                    } catch (NotDivisibleException nde) {
                                        withinRange = (Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1);
                                        testDivRegPartMult += 2;
                                        if (d < 0) {
                                            testDivisor = new ImaginaryQuadraticInteger(testDivRegPartMult, testDivSurdPartMult, r, 2);
                                        } else {
                                            testDivisor = new RealQuadraticInteger(testDivRegPartMult, testDivSurdPartMult, r, 2);
                                        }
                                        testDivisorChanged = true;
                                    }
                                }
                            }
                            if (!r.hasHalfIntegers()) {
                                testDivRegPartMult = 0;
                                testDivSurdPartMult += 2;
                            } else {
                                if (testDivSurdPartMult % 2 == 0) {
                                    testDivRegPartMult = 1;
                                } else {
                                    testDivRegPartMult = 0;
                                }
                                testDivSurdPartMult++;
                            }
                            if (testDivisorChanged) {
                                withinRange = true;
                            }
                        }
                        return presumedIrreducible;
                    }
                }
            }
        }
        if (num == null) {
            String excMsg = "Null is not an algebraic integer, neither reducible nor irreducible";
            throw new NullPointerException(excMsg);
        }
        String exceptionMessage = "The domain of the number " + num.toASCIIString() + " is not yet supported.";
        throw new UnsupportedNumberDomainException(exceptionMessage, num);
    }

    /**
     * Tests whether one algebraic integer is divisible by another.
     * @param a The number to test for divisibility by another. For example,
     * &radic;14.
     * @param b The number to test as a potential divisor of the first number.
     * For example, 4 + &radic;14.
     * @return True if a is divisible by b, false otherwise. For example, it
     * should return true that &radic;14 is indeed divisible by 4 + &radic;14,
     * false for &radic;14 being divisible by 3 + &radic;14 (it's not
     * divisible). Sure to be false if b is 0 regardless of what a is, sure to
     * be true is a is 0 and b is any nonzero number.
     * @throws AlgebraicDegreeOverflowException If the two numbers are from
     * different rings, even if one or both have zero "surd" parts, this runtime
     * exception will be thrown. For example, is 1 + &radic;&minus;2 divisible
     * by &radic;3? Or how about is 9 from <b>Z</b>[&radic;15] divisible by 3
     * from <b>Z</b>[&radic;&minus;10]? The former can't be resolved in a
     * quadratic ring, the latter can, though the choice of ring could have
     * unexpected consequences for subsequent calculations, so this exception is
     * thrown just the same.
     * @throws UnsupportedNumberDomainException Thrown when called upon numbers
     * from a type of ring that is not fully supported yet. For example, as of
     * 2018, this program does not really have support for cubic integers, so
     * asking if 35 + 5&#8731;2 is divisible by 3 &minus; &#8731;2 would
     * probably trigger this exception.
     */
    public static boolean isDivisibleBy(AlgebraicInteger a, AlgebraicInteger b) {
        if (!a.getRing().equals(b.getRing())) {
            String exceptionMessage = "Ring mismatch: " + a.toASCIIString() + " is from " + a.getRing().toASCIIString() + " but " + b.toASCIIString() + " is from " + b.getRing().toASCIIString() + ".";
            int deg;
            if (a.algebraicDegree() > b.algebraicDegree()) {
                deg = a.algebraicDegree();
            } else {
                deg = b.algebraicDegree();
            }
            throw new AlgebraicDegreeOverflowException(exceptionMessage, deg, a, b);
        }
        if (b.norm() == 0) {
            return false;
        }
        if (a.norm() == 0) {
            return true;
        }
        if (a instanceof QuadraticInteger) {
            QuadraticInteger divA = (QuadraticInteger) a;
            QuadraticInteger divB = (QuadraticInteger) b;
            try {
                divA.divides(divB);
                return true;
            } catch (NotDivisibleException nde) {
                return false;
            }
        }
        String exceptionMessage = "Testing divisibility in the domain of the number " + a.toASCIIString() + " is not yet supported.";
        throw new UnsupportedNumberDomainException(exceptionMessage, a, b);
    }

    /**
     * Determines whether a given number is squarefree or not. The original
     * implementation depended on {@link #primeFactors(int) primeFactors(int)}.
     * For version 0.95, this was optimized to try the number modulo 4, and if
     * it's not divisible by 4, to try dividing it by odd squares. Although this
     * includes odd squares like 9 and 81, it still makes for a performance
     * improvement over relying on primeFactors(int).
     * @param num The number to be tested for being squarefree.
     * @return True if the number is squarefree, false otherwise. For example,
     * &minus;3 and 7 should each return true, &minus;4, 0 and 25 should each
     * return false. Note that 1 is considered to be squarefree. Therefore, for
     * num = 1, this function should return true.
     */
    public static boolean isSquareFree(int num) {
        switch (num) {
            case -1:
            case 1:
                return true;
            case 0:
                return false;
            default:
                boolean noDupFactorFound = (num % 4 != 0);
                if (noDupFactorFound) {
                    double threshold = Math.sqrt(Math.abs(num));
                    int currRoot = 3;
                    int currSquare;
                    do {
                        currSquare = currRoot * currRoot;
                        noDupFactorFound = (num % currSquare != 0);
                        currRoot += 2;
                    } while (noDupFactorFound && currRoot <= threshold);
                }
                return noDupFactorFound;
        }
    }

    /**
     * Gives the squarefree kernel of an integer. The current implementation
     * works by obtaining the prime factorization of the number, deleting
     * duplicate factors and then multiplying the factors that remain.
     * @param num The number for which to find the squarefree kernel of. May be
     * negative. For example, &minus;392.
     * @return The squarefree kernel of the given number. Should be positive for
     * positive inputs and negative for negative inputs. For example, the kernel
     * of &minus;392 is &minus;14. The current implementation returns 0 for an
     * input of 0, but this is not guaranteed for future implementations.
     */
    public static int kernel(int num) {
        List<Integer> factors = primeFactors(num);
        for (int i = factors.size() - 1; i > 0; i--) {
            if (factors.get(i).equals(factors.get(i - 1))) {
                factors.remove(i);
            }
        }
        int product = 1;
        product = factors.stream().map((factor)
                -> factor).reduce(product, (accumulator, _item)
                -> accumulator * _item);
        return product;
    }

    /**
     * Computes the M&ouml;bius function &mu; for a given integer.
     * @param num The integer for which to compute the M&ouml;bius function.
     * @return 1 if num is squarefree with an even number of prime factors,
     * &minus;1 if num is squarefree with an odd number of prime factors, 0 if
     * num is not squarefree. Since &minus;1 is a unit, not a prime,
     * &mu;(&minus;<i>n</i>) = &mu;(<i>n</i>). For example, &mu;(31) = &minus;1,
     * &mu;(32) = 0 and &mu;(33) = 1.
     */
    public static byte moebiusMu(int num) {
        switch (num) {
            case -1:
            case 1:
                return 1;
            default:
                if (isSquareFree(num)) {
                    List<Integer> prFacts = primeFactors(num);
                    if (prFacts.get(0) == -1) {
                        prFacts.remove(0);
                    }
                    if (prFacts.size() % 2 == 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }
        }
    }

    /**
     * Computes the greatest common divisor (GCD) of two purely real integers by
     * using the Euclidean algorithm.
     * @param a One of the two integers. May be negative, need not be greater
     * than the other.
     * @param b One of the two integers. May be negative, need not be smaller
     * than the other.
     * @return The GCD as an integer.
     * If one of a or b is 0 and the other is nonzero, the result will be the
     * nonzero number.
     * If both a and b are 0, then the result will be 0, which is perhaps
     * technically wrong, but I think it's good enough for the purpose here.
     */
    public static int euclideanGCD(int a, int b) {
        int currA, currB, currRemainder;
        if (a < b) {
            currA = b;
            currB = a;
        } else {
            currA = a;
            currB = b;
        }
        while (currB != 0) {
            currRemainder = currA % currB;
            currA = currB;
            currB = currRemainder;
        }
        if (currA < 0) {
            currA *= -1;
        }
        return currA;
    }

    /**
     * Computes the greatest common divisor (GCD) of two purely real integers by
     * using the Euclidean algorithm.
     * @param a One of the two integers. May be negative, need not be greater
     * than the other.
     * @param b One of the two integers. May be negative, need not be smaller
     * than the other.
     * @return The GCD as an integer. If one of a or b is 0 and the other is
     * nonzero, the result will be the nonzero number. If both a and b are 0,
     * then the result will be 0, which is perhaps technically wrong, but I
     * think it's good enough for the purpose here.
     */
    public static long euclideanGCD(long a, long b) {
        long currA, currB, currRemainder;
        if (a < b) {
            currA = b;
            currB = a;
        } else {
            currA = a;
            currB = b;
        }
        while (currB != 0) {
            currRemainder = currA % currB;
            currA = currB;
            currB = currRemainder;
        }
        if (currA < 0) {
            currA *= -1;
        }
        return currA;
    }

    /**
     * Computes the greatest common divisor (GCD) of two algebraic integers by
     * using the Euclidean algorithm.
     * @param a One of the two algebraic integers. Need not have greater norm
     * than the other algebraic integer.
     * @param b One of the two algebraic integers. Need not have smaller norm
     * than the other algebraic integer.
     * @return The GCD of the two algebraic integers.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come
     * from different rings of the same degree, the GCD might be a number from a
     * ring of greater degree.
     * @throws NonEuclideanDomainException In the case of quadratic integers, if
     * they come from a ring <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub> for
     * <i>d</i> other than those listed in {@link
     * #NORM_EUCLIDEAN_QUADRATIC_RINGS_D NORM_EUCLIDEAN_QUADRATIC_RINGS_D}, this
     * function assumes the Euclidean GCD algorithm with the absolute value of
     * the norm function will fail without even trying, and throws this checked
     * exception. However, for some pairs drawn from a non-Euclidean domain, the
     * Euclidean GCD algorithm might nevertheless work. For this reason, the
     * exception provides {@link
     * NonEuclideanDomainException#tryEuclideanGCDAnyway()
     * tryEuclideanGCDAnyway()}.
     * @throws UnsupportedNumberDomainException If the algebraic integers come
     * from a domain for which this function has not been defined yet, this
     * runtime exception will be thrown. For example, this function might have
     * been programmed for imaginary quadratic integers but not yet programmed
     * for real quadratic integers, so the latter will trigger this exception.
     */
    public static AlgebraicInteger euclideanGCD(AlgebraicInteger a, AlgebraicInteger b) throws NonEuclideanDomainException {
        if (a instanceof QuadraticInteger && b instanceof QuadraticInteger) {
            QuadraticInteger currA = (QuadraticInteger) a;
            QuadraticInteger currB = (QuadraticInteger) b;
            if (!currA.getRing().equals(currB.getRing())) {
                if (currA.getSurdPartMult() == 0 || currB.getSurdPartMult() == 0) {
                    if (currA.getSurdPartMult() == 0 && currB.getSurdPartMult() != 0) {
                        if (currB instanceof ImaginaryQuadraticInteger) {
                            currA = new ImaginaryQuadraticInteger(currA.getRegPartMult(), 0, currB.getRing());
                        } else {
                            if (currB instanceof RealQuadraticInteger) {
                                currA = new RealQuadraticInteger(currA.getRegPartMult(), 0, currB.getRing());
                            } else {
                                String exceptionMessage = currB.getClass().getName() + " is not currently supported for this Euclidean GCD operation.";
                                throw new UnsupportedNumberDomainException(exceptionMessage, currB.getRing());
                            }
                        }
                    }
                    if (currB.getSurdPartMult() == 0 && currA.getSurdPartMult() != 0) {
                        if (currA instanceof ImaginaryQuadraticInteger) {
                            currB = new ImaginaryQuadraticInteger(currB.getRegPartMult(), 0, currA.getRing());
                        } else {
                            if (currA instanceof RealQuadraticInteger) {
                                currB = new RealQuadraticInteger(currB.getRegPartMult(), 0, currA.getRing());
                            } else {
                                String exceptionMessage = currA.getClass().getName() + " is not currently supported for this Euclidean GCD operation.";
                                throw new UnsupportedNumberDomainException(exceptionMessage, currA.getRing());
                            }
                        }
                    }
                } else {
                    String exceptionMessage = a.toASCIIString() + " is from " + currA.getRing().toASCIIString() + " but " + b.toASCIIString() + " is from " + currB.getRing().toASCIIString() + "\"";
                    throw new AlgebraicDegreeOverflowException(exceptionMessage, 2, a, b);
                }
            }
            boolean nonNormEuclFlag = true;
            int counter = 0;
            int radic = currA.getRing().getRadicand();
            while (nonNormEuclFlag && (counter < NORM_EUCLIDEAN_QUADRATIC_RINGS_D.length)) {
                nonNormEuclFlag = (radic != NORM_EUCLIDEAN_QUADRATIC_RINGS_D[counter]);
                counter++;
            }
            if (nonNormEuclFlag) {
                String exceptionMessage = currA.getRing().toASCIIString() + " is not a norm-Euclidean domain.";
                throw new NonEuclideanDomainException(exceptionMessage, currA, currB);
            }
            if (Math.abs(currA.norm()) < Math.abs(currB.norm())) {
                QuadraticInteger swapper = currA;
                currA = currB;
                currB = swapper;
            }
            QuadraticInteger tempMultiple, currRemainder;
            AlgebraicInteger[] bounds;
            while (currB.norm() != 0) {
                try {
                    tempMultiple = currA.divides(currB);
                    tempMultiple = tempMultiple.times(currB);
                    currRemainder = currA.minus(tempMultiple);
                } catch (NotDivisibleException nde) {
                    bounds = nde.getBoundingIntegers();
                    boolean notFound;
                    counter = 0;
                    QuadraticInteger holder;
                    do {
                        holder = (QuadraticInteger) bounds[counter];
                        // TODO: REMOVE NEXT LINE AFTER DEBUGGING
//                        System.out.println("bounds[" + counter + "] = " + holder.toASCIIString());
                        tempMultiple = holder.times(currB);
                        currRemainder = currA.minus(tempMultiple);
                        notFound = Math.abs(currRemainder.norm()) >= Math.abs(currB.norm());
                        counter++;
                    } while (notFound);
                }
                currA = currB;
                // TODO: REMOVE NEXT LINE AFTER DEBUGGING
//                System.out.println("currA = " + currA.toASCIIString());
                currB = currRemainder;
            }
            if (radic == -1 && currA.getRegPartMult() == 0) {
                currA = currA.times(IMAG_UNIT_NEG_I);
            }
            if (currA.getRegPartMult() < 0) {
                currA = currA.times(-1);
            }
            return currA;
        }
        throw new UnsupportedNumberDomainException("Not supported yet, sorry", a, b);
    }

    /**
     * Computes the greatest common divisor (GCD) of an algebraic integer which
     * may or may not be of degree 1 or 0, passed in as an AlgebraicInteger, and
     * an algebraic integer of degree 1 or 0 passed in as an int.
     * @param a An algebraic integer from any supported domain in this program.
     * For example, 3&radic;&minus;2.
     * @param b An algebraic integer of degree 1 or 0. For example, 4.
     * @return The GCD. For example, &radic;&minus;2.
     * @throws NonEuclideanDomainException In the case of quadratic integers, if
     * they come from a ring <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub> for
     * <i>d</i> other than those listed in {@link
     * #NORM_EUCLIDEAN_QUADRATIC_RINGS_D NORM_EUCLIDEAN_QUADRATIC_RINGS_D}, this
     * function assumes the Euclidean GCD algorithm with the absolute value of
     * the norm function will fail without even trying, and throws this checked
     * exception. However, for some pairs drawn from a non-Euclidean domain, the
     * Euclidean GCD algorithm might nevertheless work. For this reason, the
     * exception provides {@link
     * NonEuclideanDomainException#tryEuclideanGCDAnyway()
     * tryEuclideanGCDAnyway()}.
     * @throws UnsupportedNumberDomainException If parameter a comes from a
     * domain for which this function has not been defined yet, this runtime
     * exception will be thrown. For example, this function might have been
     * programmed for imaginary quadratic integers but not yet programmed for
     * real quadratic integers, so the latter will trigger this exception.
     */
    public static AlgebraicInteger euclideanGCD(AlgebraicInteger a, int b) throws NonEuclideanDomainException {
        if (a instanceof QuadraticInteger) {
            QuadraticInteger wrappedB = new ImaginaryQuadraticInteger(b, 0, RING_GAUSSIAN);
            return euclideanGCD(a, wrappedB);
        }
        throw new UnsupportedNumberDomainException("Not supported yet, sorry", a);
    }

    /**
     * Computes the greatest common divisor (GCD) of an algebraic integer which
     * may or may not be of degree 1 or 0, passed in as an AlgebraicInteger, and
     * an algebraic integer of degree 1 or 0 passed in as an int.
     * @param a An algebraic integer of degree 1 or 0. For example, 4.
     * @param b An algebraic integer from any supported domain in this program.
     * For example, 3&radic;&minus;2.
     * @return The GCD. For example, &radic;&minus;2.
     * @throws NonEuclideanDomainException In the case of quadratic integers, if
     * they come from a ring <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub> for
     * <i>d</i> other than those listed in {@link
     * #NORM_EUCLIDEAN_QUADRATIC_RINGS_D NORM_EUCLIDEAN_QUADRATIC_RINGS_D}, this
     * function assumes the Euclidean GCD algorithm with the absolute value of
     * the norm function will fail without even trying, and throws this checked
     * exception. However, for some pairs drawn from a non-Euclidean domain, the
     * Euclidean GCD algorithm might nevertheless work. For this reason, the
     * exception provides {@link
     * NonEuclideanDomainException#tryEuclideanGCDAnyway()
     * tryEuclideanGCDAnyway()}.
     * @throws UnsupportedNumberDomainException If parameter b comes from a
     * domain for which this function has not been defined yet, this runtime
     * exception will be thrown. For example, this function might have been
     * programmed for imaginary quadratic integers but not yet programmed for
     * real quadratic integers, so the latter will trigger this exception.
     */
    public static AlgebraicInteger euclideanGCD(int a, AlgebraicInteger b) throws NonEuclideanDomainException {
        if (b instanceof QuadraticInteger) {
            QuadraticInteger wrappedA = new ImaginaryQuadraticInteger(a, 0, RING_GAUSSIAN);
            return euclideanGCD(wrappedA, b);
        }
        throw new UnsupportedNumberDomainException("Not supported yet, sorry", b);
    }

    /**
     * Gives the fundamental unit of a ring that has infinitely many units. In a
     * domain of purely real numbers, the fundamental unit is the smallest unit
     * greater than 1. It may have norm &minus;1 or 1. At least for now, this
     * uses a brute force algorithm that may in some cases be unacceptably slow.
     * @param ring The ring of algebraic integers for which to find the
     * fundamental unit.
     * @return The fundamental unit. For example, for <b>Z</b>[&radic;2], this
     * would be 1 + &radic;2; for <b>Z</b>[&radic;3] this would be 2 + &radic;3.
     * @throws ArithmeticException Since computations are performed using 32-bit
     * and 64-bit integers, an overflow could occur, especially if the
     * fundamental unit is much larger than 2<sup>31</sup>.
     * @throws IllegalArgumentException If called upon with a supported domain
     * known to not have infinitely many units, such as an imaginary quadratic
     * integer ring, this runtime exception will be thrown.
     * @throws UnsupportedNumberDomainException If called upon with a number
     * domain for which support has not been fully fleshed out yet, such as a
     * simple cubic integer ring.
     * @throws NullPointerException If <code>ring</code> is null. The exception
     * message will be "Null ring has no fundamental unit".
     */
    public static AlgebraicInteger fundamentalUnit(IntegerRing ring) {
        if (ring instanceof ImaginaryQuadraticRing) {
            String exceptionMessage = "Since " + ring.toASCIIString() + " has a finite unit group, there is no fundamental unit.";
            throw new IllegalArgumentException(exceptionMessage);
        }
        if (ring instanceof RealQuadraticRing) {
            RealQuadraticRing r = (RealQuadraticRing) ring;
            QuadraticInteger potentialUnit;
            int d = r.getRadicand();
            long xd;
            long trialRegNeg;
            int trialSurd = 1;
            boolean notFoundYet = true;
            do {
                xd = trialSurd * trialSurd * d;
                trialRegNeg = (long) Math.floor(Math.sqrt(xd));
                potentialUnit = new RealQuadraticInteger((int) trialRegNeg, trialSurd, r);
                if (potentialUnit.norm() == -1) {
                    notFoundYet = false;
                }
                if (notFoundYet) {
                    potentialUnit = potentialUnit.plus(1);
                    if (potentialUnit.norm() == 1) {
                        notFoundYet = false;
                    }
                }
                trialSurd++;
            } while ((trialRegNeg < Integer.MAX_VALUE) && notFoundYet);
            if (r.hasHalfIntegers()) {
                double threshold = potentialUnit.abs();
                double currAbs;
                QuadraticInteger potentialHalfUnit;
                trialSurd = 1;
                do {
                    xd = trialSurd * trialSurd * d;
                    trialRegNeg = (long) Math.floor(Math.sqrt(xd - 4));
                    trialRegNeg += ((trialRegNeg % 2) - 1); // Make sure it's odd
                    potentialHalfUnit = new RealQuadraticInteger((int) trialRegNeg, trialSurd, r, 2);
                    if (potentialHalfUnit.norm() == -1) {
                        return potentialHalfUnit;
                    } else {
                        currAbs = potentialHalfUnit.abs();
                    }
                    potentialHalfUnit = potentialHalfUnit.plus(1); // Add 2/2 to "regular" part
                    if (potentialHalfUnit.norm() == 1) {
                        return potentialHalfUnit;
                    }
                    trialSurd += 2;
                } while (currAbs < threshold);
            }
            if (notFoundYet) {
                String exceptionMessage = "Overflow occurred, fundamental unit is greater than " + potentialUnit.toASCIIString() + ".";
                throw new ArithmeticException(exceptionMessage);
            } else {
                return potentialUnit;
            }
        }
        if (ring == null) {
            String excMsg = "Null ring has no fundamental unit";
            throw new NullPointerException(excMsg);
        }
        String exceptionMessage = "Fundamental unit function not yet supported for " + ring.toASCIIString();
        throw new UnsupportedNumberDomainException(exceptionMessage, ring);
    }

    /**
     * Places an algebraic integer in a sector of the complex plane considered
     * preferable for the canonical form of a prime factorization. This is done
     * by multiplying the number by a suitable unit, so that its norm remains
     * the same.
     * @param num The number which may or may not be in the primary sector and
     * needs to be placed there. For example, 1 &minus; 10<i>i</i>, which has a
     * norm of 101 and an angle of approximately &minus;1.47 radians (roughly
     * &minus;84 degrees).
     * @return An algebraic integer placed in the primary sector, having the
     * same norm as num but a different angle (unless num was already in the
     * primary sector to begin with). For example, for 1 &minus; 10<i>i</i>,
     * this would be 10 + <i>i</i>, which also has a norm of 101 but an angle of
     * approximately 0.09966865249 (roughly 5.71 degrees).
     * @throws UnsupportedNumberDomainException Thrown when called upon a number
     * from a type of ring that is not fully supported yet. For example, as of
     * 2018, this program has limited support for quartic integers such as those
     * of the ring of algebraic integers of <b>Q</b>(&zeta;<sub>8</sub>), so
     * asking to place &minus;1 + (&zeta;<sub>8</sub>)<sup>3</sup> would
     * probably trigger this exception.
     */
    public static AlgebraicInteger placeInPrimarySector(AlgebraicInteger num) {
        if (num.abs() == 0) {
            return num;
        }
        if (num instanceof QuadraticInteger) {
            QuadraticInteger n = (QuadraticInteger) num;
            QuadraticRing r = n.getRing();
            int d = r.getRadicand();
            double minAngle, maxAngle;
            QuadraticInteger turnUnit;
            switch (d) {
                case -1:
                    minAngle = -0.7853981633974483; // -45 degrees
                    maxAngle = 0.7853981633974483; // 45 degrees
                    turnUnit = IMAG_UNIT_NEG_I;
                    break;
                case -3:
                    minAngle = -0.5235987755982988730771; // -30 degrees
                    maxAngle = 0.5235987755982988730771; // 30 degrees
                    turnUnit = new ImaginaryQuadraticInteger(1, -1, r, 2); // -omega
                    break;
                default:
                    if ((n.getRegPartMult() < 0) || ((n.getRegPartMult() == 0) && (n.getSurdPartMult() < 0))) {
                        return n.times(-1);
                    } else {
                        return n;
                    }
            }
            while ((n.angle() <= minAngle) || (n.angle() > maxAngle)) {
                n = n.times(turnUnit);
            }
            return n;
        }
        String exceptionMessage = "Place in primary sector function not yet supported for " + num.getRing().toASCIIString();
        throw new UnsupportedNumberDomainException(exceptionMessage, num);
    }

    /**
     * Divides the non-trivial units out of an algebraic integer, so as to place
     * a number close to 0 but to the right and/or above 1 on the complex plane.
     * This enables the determination of whether two numbers with the same norm
     * belong to the same principal ideal. The number returned by this function
     * can still be divided by units, but that should place the number to the
     * left and/or below 1.
     * @param num The number out of which to divide units. For example, 2 +
     * &radic;2, which has norm 2.
     * @return A number which can be factorized into primes without need for
     * nontrivial units. For example, for 2 + &radic;2, this would be &radic;2,
     * which has norm &minus;2 (since &radic;2 times the fundamental unit 1 +
     * &radic;2, which has norm &minus;1, is 2 + &radic;2).
     * @throws UnsupportedNumberDomainException If called upon to divide the
     * units out of an algebraic integer from a type of ring not yet supported
     * by this function. As of 2018, real and imaginary quadratic rings are
     * supported, but none of higher algebraic degree.
     */
    public static AlgebraicInteger divideOutUnits(AlgebraicInteger num) {
        if (num.norm() == 0) {
            return num;
        }
        if (num instanceof ImaginaryQuadraticInteger) {
            return placeInPrimarySector(num);
        }
        if (num instanceof RealQuadraticInteger) {
            QuadraticInteger unit = (QuadraticInteger) fundamentalUnit(num.getRing());
            QuadraticInteger n;
            if (num.getRealPartNumeric() < 0.0) {
                n = ((QuadraticInteger) num).times(-1);
            } else {
                n = (QuadraticInteger) num;
            }
            while (n.getRealPartNumeric() < 1.0) {
                n = n.times(unit);
            }
            QuadraticInteger nextN;
            do {
                try {
                    nextN = n.divides(unit);
                    if (nextN.getRealPartNumeric() > 1.0) {
                        n = nextN;
                    }
                } catch (NotDivisibleException nde) {
                    System.err.println("NotDivisibleException should not have happened dividing by a unit: \"" + nde.getMessage() + "\"");
                    throw new RuntimeException(nde); // Rethrow wrapped in RuntimeException
                }
            } while (n.equals(nextN));
            return n;
        }
        String exceptionMessage = "Place in primary section function not yet supported for " + num.getRing().toASCIIString() + ".";
        throw new UnsupportedNumberDomainException(exceptionMessage, num);
    }

    /**
     * Wraps the purely real, rational unit 1 into an implementation of {@link
     * AlgebraicInteger}.
     * @param ring The ring of algebraic integers from which to get 1.
     * @return An object implementing AlgebraicInteger representing the number
     * with real part 1 and imaginary part 0 in the given ring.
     * @throws UnsupportedNumberDomainException If called upon with a type of
     * ring for which support has not been fleshed out yet.
     */
    public static AlgebraicInteger getOneInRing(IntegerRing ring) {
        if (ring instanceof QuadraticRing) {
            QuadraticRing r = (QuadraticRing) ring;
            if (r instanceof ImaginaryQuadraticRing) {
                return new ImaginaryQuadraticInteger(1, 0, r);
            }
            if (r instanceof RealQuadraticRing) {
                return new RealQuadraticInteger(1, 0, r);
            }
        }
        if (ring == null) {
            String excMsg = "Null ring does not contain the number 1";
            throw new NullPointerException(excMsg);
        }
        String exceptionMessage = "1 from given ring function not yet supported for " + ring.toASCIIString() + ".";
        throw new UnsupportedNumberDomainException(exceptionMessage, ring);
    }

    private static short w(int d) {
        switch (d) {
            case -3:
                return 6;
            case -1:
                return 4;
            default:
                return 2;
        }
    }

    /**
     * Gives the class number of a ring of algebraic integers.
     * @param ring The ring for which for which to compute the class number. For
     * example, <b>Z</b>[&radic;2], <b>Z</b>[&radic;&minus;5].
     * @return A positive integer with the class number, should be 1 for unique
     * factorization domains, 2 or greater for non-unique factorization domains.
     * For example, the class number of <b>Z</b>[&radic;2] is 1, the class
     * number of <b>Z</b>[&radic;&minus;5] is 2. A return of 0 or a negative
     * integer could indicate either an arithmetic overflow problem or a mistake
     * on the programmer's part.
     * @throws UnsupportedNumberDomainException If called upon for a type of
     * ring which is not yet supported by this function. For example, as of
     * 2018, this function supports real and imaginary quadratic rings but no
     * kind of cubic ring, so asking this function to compute the class number
     * of <b>Z</b>[&#8731;2] should trigger this exception.
     * @throws NullPointerException If <code>ring</code> is null. The exception
     * message will be "Null ring does not have class number".
     */
    public static int fieldClassNumber(IntegerRing ring) {
        if (ring instanceof QuadraticRing) {
            QuadraticRing r = (QuadraticRing) ring;
            int d = r.getRadicand();
            if (!r.hasHalfIntegers()) {
                d *= 4;
            }
            double sumMult, interNum;
            if (d < 0) {
                sumMult = (double) w(d) / (2.0 * d);
                short kronSum = 0;
                if (d == -4) {
                    kronSum = -2;
                }
                for (short i = 1; i < Math.abs(d); i++) {
                    kronSum += symbolKronecker(d, i) * i;
                }
                interNum = sumMult * kronSum;
                return (int) Math.round(interNum);
            } else {
                sumMult = fundamentalUnit(r).getRealPartNumeric();
                sumMult = 2 * Math.log(sumMult);
                sumMult = -1.0 / sumMult;
                double indKron;
                double kronSum = 0.0;
                for (int i = 1; i < d; i++) {
                    indKron = Math.log(Math.sin((Math.PI * i)/d));
                    indKron *= symbolKronecker(d, i);
                    kronSum += indKron;
                }
                interNum = sumMult * kronSum;
                return (int) Math.round(interNum);
            }
        }
        if (ring == null) {
            String excMsg = "Null ring does not have class number";
            throw new NullPointerException(excMsg);
        }
        String exceptionMessage = "Class number function not yet supported for " + ring.toASCIIString();
        throw new UnsupportedNumberDomainException(exceptionMessage, ring);
    }

    /**
     * Provides a pseudorandom positive squarefree integer.
     * @param bound The lowest number desired (but may use a negative integer).
     * For example, for a pseudorandom squarefree number between 1 and 97, you
     * can pass -100 or 100.
     * @return A pseudorandom positive squarefree integer. For example, given a
     * bound of 100, this might return 91.
     */
    public static int randomSquarefreeNumber(int bound) {
        if (bound < 0) {
            bound *= -1;
        }
        Random ranNumGen = new Random();
        int randomNumber = ranNumGen.nextInt(bound);
        while (!isSquareFree(randomNumber)) {
            randomNumber++;
        }
        return randomNumber;
    }

}
