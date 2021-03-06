/*
 * Copyright (C) 2019 Alonso del Arte
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
package algebraics.quadratics;

import algebraics.AlgebraicDegreeOverflowException;
import algebraics.NotDivisibleException;

import java.text.DecimalFormatSymbols;

/**
 * Defines objects to represent imaginary quadratic integers, for the most part
 * symbolically rather than numerically.
 * @author Alonso del Arte
 */
public class ImaginaryQuadraticInteger extends QuadraticInteger {

    private final double numValRe;
    private final double numValIm;

    /**
     * Gives twice the real part of the imaginary quadratic integer. If the ring
     * has so-called "half-integers," this might be an odd number, otherwise it
     * should always be an even number.
     * @return Twice the real part. For example, for &minus;3/2 +
     * (5&radic;&minus;7)/2, this would be &minus;3; for &minus;3 +
     * 5&radic;&minus;7, this would be &minus;6.
     */
    public long getTwiceRealPartMult() {
        long twiceRealPartMult = this.regPartMult;
        if (this.denominator == 1) {
            twiceRealPartMult *= 2;
        }
        return twiceRealPartMult;
    }

    /**
     * Gives twice the imaginary part of the imaginary quadratic integer, before
     * multiplication by &radic;<i>d</i>. If the ring has so-called
     * "half-integers," this might be an odd number, otherwise it should always
     * be an even number.
     * @return Twice the imaginary part before multiplication by
     * &radic;<i>d</i>. For example, for &minus;3/2 + (5&radic;&minus;7)/2, this
     * would be 5; for &minus;3 + 5&radic;&minus;7, this would be 10.
     */
    public long getTwiceImagPartMult() {
        long twiceImagPartMult = this.surdPartMult;
        if (this.denominator == 1) {
            twiceImagPartMult *= 2;
        }
        return twiceImagPartMult;
    }

    /**
     * Gives the imaginary quadratic integer's distance from 0.
     * @return This distance from 0 of the imaginary quadratic integer expressed
     * as a nonnegative real double. For example, for 5/2 + (&radic;-7)/2, this
     * would be approximately 2.82842712. For a purely real positive integer,
     * just the integer itself as a double, likewise for purely real negative
     * integers this is the integer itself multiplied by -1.
     */
    @Override
    public double abs() {
        if (this.surdPartMult == 0) {
            if (this.regPartMult < 0) {
                return -this.regPartMult;
            } else {
                return this.regPartMult;
            }
        }
        if (this.regPartMult == 0 && this.quadRing.radicand == -1) {
            if (this.surdPartMult < 0) {
                return -this.surdPartMult;
            } else {
                return this.surdPartMult;
            }
        }
        double realLegSquare = this.regPartMult * this.regPartMult;
        double imagLegSquare = this.surdPartMult * this.surdPartMult * (-this.quadRing.radicand);
        double hypotenuseSquare = realLegSquare + imagLegSquare;
        if (this.denominator == 2) {
            hypotenuseSquare /= 4;
        }
        return Math.sqrt(hypotenuseSquare);
    }

    /**
     * Gets the real part of the imaginary quadratic integer. May be half an
     * integer.
     * @return The real part of the imaginary quadratic integer. For example,
     * for &minus;1/2 + (&radic;&minus;7)/2, the result should be &minus;0.5.
     */
    @Override
    public double getRealPartNumeric() {
        return this.numValRe;
    }

    /**
     * Gets the imaginary part of the imaginary quadratic integer multiplied by
     * &minus;<i>i</i>. It will most likely be the rational approximation of an
     * irrational real number.
     * @return The imaginary part of the imaginary quadratic integer multiplied
     * by &minus;<i>i</i>. For example, for &minus;1/2 + (&radic;&minus;7)/2,
     * the result should be roughly 1.32287565553229529525.
     */
    @Override
    public double getImagPartNumeric() {
        return this.numValIm;
    }

    @Override
    public double angle() {
        return Math.atan2(this.numValIm, this.numValRe);
    }

    public static ImaginaryQuadraticInteger inferStep(ImaginaryQuadraticInteger startPoint, ImaginaryQuadraticInteger endPoint) {
        if (startPoint.equals(endPoint)) {
            return new ImaginaryQuadraticInteger(0, 0, startPoint.quadRing);
        }
        int startRe = startPoint.regPartMult;
        int startIm = startPoint.surdPartMult;
        int endRe = endPoint.regPartMult;
        int endIm = endPoint.surdPartMult;
        if (startPoint.quadRing.d1mod4) {
            if (startPoint.denominator == 1) {
                startRe *= 2;
                startIm *= 2;
            }
            if (endPoint.denominator == 1) {
                endRe *= 2;
                endIm *= 2;
            }
        }
        QuadraticInteger step;
        if (startRe == endRe) {
            step = new ImaginaryQuadraticInteger(0, 1, startPoint.quadRing);
            if (startIm > endIm) {
                step = step.times(-1);
            }
        } else if (startIm == endIm) {
            step = new ImaginaryQuadraticInteger(1, 0, startPoint.quadRing);
            if (startRe > endRe) {
                step = step.times(-1);
            }
        } else {
            step = endPoint.minus(startPoint);
            boolean keepGoing = true;
            int divisor = 2;
            while (keepGoing) {
                try {
                    step = step.divides(divisor);
                    keepGoing = step.abs() > 1.0;
                    divisor = 1;
                } catch (NotDivisibleException nde) {
                    keepGoing = nde.getAbs() > 1.0;
                }
                divisor++;
            }
        }
        return (ImaginaryQuadraticInteger) step;
    }

    /**
     * Interprets a String that contains 0s, 1s, 2s and/or 3s as the
     * quater-imaginary representation of a Gaussian integer. Computer pioneer
     * Donald Knuth is the first person known to propose this system, in which
     * any Gaussian integer can be represented without the need for minus signs
     * and without the need to separate the real and imaginary parts of the
     * number.
     * @param str The String to parse. May contain spaces, which will be
     * stripped out prior to parsing. May also contain a "decimal" dot followed
     * by either "2" and zero or more zeroes, or just zeroes.
     * @return An ImaginaryQuadraticInteger object containing the Gaussian
     * integer represented by the quater-imaginary String.
     * @throws NumberFormatException If str has a "decimal" dot followed by any
     * digit other than a single 2 or a bunch of zeroes, or if it contains
     * digits other than 0, 1, 2 or 3, this runtime exception will be thrown.
     * The problematic character mentioned in the exception message may or may
     * not be the only parsing obstacle.
     */
    public static QuadraticInteger parseQuaterImaginary(String str) {
        ImaginaryQuadraticRing ringGaussian = new ImaginaryQuadraticRing(-1);
        ImaginaryQuadraticInteger base = new ImaginaryQuadraticInteger(0, 2, ringGaussian);
        QuadraticInteger currPower = new ImaginaryQuadraticInteger(1, 0, ringGaussian);
        QuadraticInteger currPowerMult;
        QuadraticInteger parsedSoFar = new ImaginaryQuadraticInteger(0, 0, ringGaussian);
        ImaginaryQuadraticInteger gaussianZero = new ImaginaryQuadraticInteger(0, 0, ringGaussian);
        str = str.replace(" ", ""); // Strip out spaces
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        int dotPlace = str.indexOf(dfs.getDecimalSeparator());
        if (dotPlace > - 1) {
            boolean keepGoing = true;
            int currFractPlace = dotPlace + 2;
            while ((currFractPlace < str.length()) && keepGoing) {
                keepGoing = (str.charAt(currFractPlace) == '0');
                currFractPlace++;
            }
            if (!keepGoing) {
                throw new NumberFormatException("'" + str.charAt(currFractPlace - 1) + "' after \"decimal\" separator is not a valid digit for the quater-imaginary representation of a Gaussian integer.");
            }
            if (str.length() == dotPlace + 1) {
                str = str + "0";
            }
            str = str.substring(0, dotPlace + 2); // Discard trailing "decimal" zeroes
        }
        String dotZeroEnding = dfs.getDecimalSeparator() + "0";
        if (str.endsWith(dotZeroEnding)) {
            str = str.substring(0, str.length() - 2);
        }
        String dotTwoEnding = dfs.getDecimalSeparator() + "2";
        if (str.endsWith(dotTwoEnding)) {
            parsedSoFar = new ImaginaryQuadraticInteger(0, -1, ringGaussian);
            str = str.substring(0, str.length() - 2);
        }
        char currDigit;
        for (int i = str.length() - 1; i > -1; i--) {
            currDigit = str.charAt(i);
            switch (currDigit) {
                case '0':
                    currPowerMult = gaussianZero;
                    break;
                case '1':
                    currPowerMult = currPower;
                    break;
                case '2':
                    currPowerMult = currPower.times(2);
                    break;
                case '3':
                    currPowerMult = currPower.times(3);
                    break;
                default:
                    String exceptionMessage = "'" + currDigit + "' is not a valid quater-imaginary digit (should be one of 0, 1, 2, 3).";
                    throw new NumberFormatException(exceptionMessage);
            }
            parsedSoFar = parsedSoFar.plus(currPowerMult);
            currPower = currPower.times(base);
        }
        return parsedSoFar;
    }

    // STUB TO FAIL THE FIRST TEST
    public static ImaginaryQuadraticInteger applyOmega(int a, int b) {
        return (ImaginaryQuadraticInteger) QuadraticInteger.applyTheta(a, b, new ImaginaryQuadraticRing(-3));
    }

    /**
     * Alternative constructor, may be used when the denominator is known to be
     * 1. For example, this constructor may be used for &minus;1 +
     * &radic;&minus;3. For &minus;1/2 + (&radic;&minus;3)/2, it will be
     * necessary to use the primary constructor. One could always construct
     * &minus;1 + &radic;&minus;3 and then use {@link
     * QuadraticInteger#divides(int)} with a divisor of 2, but that would
     * probably be too circuitous in most cases.
     * @param a The real part of the imaginary quadratic integer. For example,
     * for 5 + 4&radic;&minus;7, this parameter would be 5.
     * @param b The imaginary part of the imaginary quadratic integer divided by
     * &radic;<i>d</i>. For example, for 5 + 4&radic;&minus;7, this parameter
     * would be 4.
     * @param R The ring to which this algebraic integer belongs to. For
     * example, for 5 + &radic;&minus;7, this parameter could be <code>new
     * ImaginaryQuadraticRing(-7)</code>.
     * @throws IllegalArgumentException If R is not of the type {@link
     * ImaginaryQuadraticRing}. However, if R is of type {@link
     * RealQuadraticRing} and b = 0, an imaginary ring will be quietly
     * substituted.
     */
    public ImaginaryQuadraticInteger(int a, int b, QuadraticRing R) {
        this(a, b, R, 1);
    }

    /**
     * Primary constructor. If the denominator is known to be 1, the alternative
     * constructor may be used.
     * @param a The real part of the imaginary quadratic integer, multiplied by
     * 2 when applicable. For example, for 7/2 + (29&radic;&minus;3)/2, this
     * parameter would be 7.
     * @param b The imaginary part divided by &radic;<i>d</i> and perhaps
     * multiplied by 2 when applicable. For example, for 7/2 +
     * (29&radic;&minus;3)/2, this parameter would be 29.
     * @param R The ring to which this algebraic integer belongs to. For
     * example, for 7/2 + (29&radic;&minus;3)/2, this parameter could be
     * <code>new ImaginaryQuadraticRing(-3)</code>.
     * @param denom In most cases 1, but may be 2 if a and b have the same
     * parity and R{@link QuadraticRing#hasHalfIntegers() .hasHalfIntegers()} is
     * true. If that is the case, &minus;2 may also be used, and &minus;1 can
     * always be used; the constructor will quietly substitute 1 or 2 and
     * multiply a and b by &minus;1.
     * @throws IllegalArgumentException If denom = 2 but there is a parity
     * mismatch between a and b (that is, one is odd and the other is even), or
     * if denom = 2 but R{@link QuadraticRing#hasHalfIntegers()
     * .hasHalfIntegers()} is false. This exception may also arise if R is not
     * of the type {@link ImaginaryQuadraticRing}. However, if R is of type
     * {@link RealQuadraticRing} and b = 0, an imaginary ring will be quietly
     * substituted.
     */
    public ImaginaryQuadraticInteger(int a, int b, QuadraticRing R, int denom) {
        super(a, b, R, denom);
        if (!(R instanceof ImaginaryQuadraticRing)) {
            String exceptionMessage = R.toASCIIString() + " is not an imaginary quadratic ring as needed.";
            throw new IllegalArgumentException(exceptionMessage);
        }
        double realPart = this.regPartMult;
        if (this.denominator == 2) {
            realPart /= 2;
        }
        this.numValRe = realPart;
        double imagPartwRad = this.surdPartMult * this.quadRing.getAbsNegRadSqrt();
        if (this.denominator == 2) {
            imagPartwRad /= 2;
        }
        this.numValIm = imagPartwRad;
    }

}
