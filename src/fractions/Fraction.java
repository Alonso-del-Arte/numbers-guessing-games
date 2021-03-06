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
package fractions;

import calculators.NumberTheoreticFunctionsCalculator;

import java.io.Serializable;

/**
 * Defines objects to represent fractions symbolically rather than numerically.
 * For example, to represent one half as 1/2 rather than 0.5.
 * @author Alonso del Arte
 */
public class Fraction implements Comparable<Fraction>, Serializable {

    private static final long serialVersionUID = 4547844323164568371L;

    private final long numerator;
    private final long denominator;

    private final double numericVal;

    protected static final int HASH_SEP = 65536;

    /**
     * Gives the numerator of the fraction. It may or may not match the
     * numerator the constructor was given.
     * @return The numerator of the fraction in lowest terms, regardless of how
     * the fraction was constructed. For example, if the fraction was
     * constructed as <sup>2</sup>&frasl;<sub>&minus;4</sub>, the fraction will
     * be expressed as <sup>&minus;1</sup>&frasl;<sub>2</sub> and this function
     * will return &minus;1, not 2.
     */
    public long getNumerator() {
        return this.numerator;
    }

    /**
     * Gives the denominator of the fraction. It may or may not match the
     * denominator the constructor was given.
     * @return The denominator of the fraction in lowest terms and as a positive
     * number, regardless of how the fraction was constructed. For example, if
     * the fraction was constructed as <sup>2</sup>&frasl;<sub>&minus;4</sub>,
     * the fraction will be expressed as <sup>&minus;1</sup>&frasl;<sub>2</sub>
     * and this function will return 2, not &minus;4.
     */
    public long getDenominator() {
        return this.denominator;
    }

    /**
     * Adds a fraction to this fraction.
     * @param addend The fraction to add. For example,
     * <sup>1</sup>&frasl;<sub>7</sub>.
     * @return A new <code>Fraction</code> object with the sum. For example, if
     * this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the addend is
     * <sup>1</sup>&frasl;<sub>7</sub>, the result will be
     * <sup>9</sup>&frasl;<sub>14</sub>.
     */
    public Fraction plus(Fraction addend) {
        long interNumerA = this.numerator * addend.denominator;
        long interNumerB = addend.numerator * this.denominator;
        long newNumer = interNumerA + interNumerB;
        long newDenom = this.denominator * addend.denominator;
        return new Fraction(newNumer, newDenom);
    }

    /**
     * Adds an integer to this fraction.
     * @param addend The integer to add. For example, 3.
     * @return A new <code>Fraction</code> object with the sum. For example, if
     * this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the integer addend
     * is 3, the result will be <sup>7</sup>&frasl;<sub>2</sub>.
     */
    public Fraction plus(int addend) {
        long newNumer = this.numerator + addend * this.denominator;
        return new Fraction(newNumer, this.denominator);
    }

    /**
     * Subtracts a fraction from this fraction.
     * @param subtrahend The fraction to subtract. For example,
     * <sup>1</sup>&frasl;<sub>7</sub>.
     * @return A new <code>Fraction</code> object with the subtraction.
     * For example, if this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the
     * subtrahend is <sup>1</sup>&frasl;<sub>7</sub>, the result will be
     * <sup>5</sup>&frasl;<sub>14</sub>.
     */
    public Fraction minus(Fraction subtrahend) {
        return this.plus(subtrahend.negate());
    }

    /**
     * Subtracts an integer from this fraction.
     * @param subtrahend The integer to subtract. For example, 3.
     * @return A new <code>Fraction</code> object with the subtraction. For
     * example, if this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the
     * integer subtrahend is 3, the result will be
     * &minus;<sup>5</sup>&frasl;<sub>2</sub>.
     */
    public Fraction minus(int subtrahend) {
        return this.plus(-subtrahend);
    }

    /**
     * Multiplies this fraction by another fraction.
     * @param multiplicand The fraction to multiply by. For example,
     * <sup>1</sup>&frasl;<sub>7</sub>.
     * @return A new <code>Fraction</code> object with the product. For example,
     * if this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the multiplicand
     * is <sup>1</sup>&frasl;<sub>7</sub>, the result will be
     * <sup>1</sup>&frasl;<sub>14</sub>.
     */
    public Fraction times(Fraction multiplicand) {
        long newNumer = this.numerator * multiplicand.numerator;
        long newDenom = this.denominator * multiplicand.denominator;
        return new Fraction(newNumer, newDenom);
    }

    /**
     * Multiplies this fraction by an integer.
     * @param multiplicand The integer to multiply by. For example, 3.
     * @return A new <code>Fraction</code> object with the product. For example,
     * if this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the multiplicand
     * is 3, the result will be <sup>3</sup>&frasl;<sub>2</sub>.
     */
    public Fraction times(int multiplicand) {
        return new Fraction(this.numerator * multiplicand, this.denominator);
    }

    /**
     * Divides this fraction by another fraction.
     * @param divisor The fraction to divide by. For example,
     * <sup>1</sup>&frasl;<sub>7</sub>.
     * @return A new <code>Fraction</code> object with the division. For
     * example, if this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the
     * divisor is <sup>1</sup>&frasl;<sub>7</sub>, the result will be
     * <sup>7</sup>&frasl;<sub>2</sub>.
     * @throws IllegalArgumentException If the divisor is 0, this runtime
     * exception will be thrown.
     */
    public Fraction dividedBy(Fraction divisor) {
        if (divisor.numerator == 0) {
            String exceptionMessage = "Dividing " + this.toString() + " by 0 results in an indeterminate number.";
            throw new IllegalArgumentException(exceptionMessage);
        }
        return this.times(divisor.reciprocal());
    }

    /**
     * Divides this fraction by an integer.
     * @param divisor The integer to divide by. For example, 3.
     * @return A new <code>Fraction</code> object with the division. For
     * example, if this fraction is <sup>1</sup>&frasl;<sub>2</sub> and the
     * divisor is 3, the result will be <sup>1</sup>&frasl;<sub>6</sub>.
     * @throws IllegalArgumentException If the divisor is 0, this runtime
     * exception will be thrown.
     */
    public Fraction dividedBy(int divisor) {
        if (divisor == 0) {
            String exceptionMessage = "Dividing " + this.toString() + " by 0 results in an indeterminate number.";
            throw new IllegalArgumentException(exceptionMessage);
        }
        return new Fraction(this.numerator, this.denominator * divisor);
    }

    /**
     * Multiplies this fraction by &minus;1.
     * @return A new <code>Fraction</code> object with the negation. For
     * example, if this fraction is <sup>1</sup>&frasl;<sub>2</sub>, the result
     * will be &minus;<sup>1</sup>&frasl;<sub>2</sub>.
     */
    public Fraction negate() {
        return new Fraction(-this.numerator, this.denominator);
    }

    /**
     * Gives the reciprocal of this fraction. If this fraction is a unit
     * fraction, the reciprocal will be arithmetically equal to an integer.
     * @return A new <code>Fraction</code> object with the reciprocal. For
     * example, if this fraction is <sup>1</sup>&frasl;<sub>2</sub>, the result
     * will be 2; if this fraction is <sup>7</sup>&frasl;<sub>2</sub>, the
     * result will be <sup>2</sup>&frasl;<sub>7</sub>.
     * @throws IllegalArgumentException If this fraction is 0, this runtime
     * exception will be thrown with the exception detail message "Denominator 0
     * is not allowed."
     */
    public Fraction reciprocal() {
        return new Fraction(this.denominator, this.numerator);
    }

    /**
     * Gives a text representation of this fraction, using ASCII characters
     * only.
     * @return A <code>String</code> with the numerator, followed by the
     * character "/" and then the denominator. However, if this fraction is an
     * integer, the characters "/1" will be omitted. If this fraction is
     * negative, the "-" character will be used instead of "&minus;" (the latter
     * is not an ASCII character). For example, if this fraction is
     * &minus;<sup>1</sup>&frasl;<sub>2</sub>, the result will be "-1/2". If
     * this fraction is 3, the result will be just "3".
     */
    @Override
    public String toString() {
        if (this.denominator == 1) {
            return Long.toString(this.numerator);
        } else {
            return (this.numerator + "/" + this.denominator);
        }
    }

    /**
     * Gives a representation of this fraction as a String suitable for use in
     * an HTML document. The output placed in the context of an HTML document
     * viewed in a Web browser should render with the numerator towards the top
     * left and the denominator towards the bottom right of the space allotted
     * for the text.
     * @return A <code>String</code> with the numerator set as an HTML
     * superscript, followed by the fraction slash character entity and then the
     * denominator set as an HTML subscript. However, if this fraction is an
     * integer, the "&nbsp;&frasl;<sub>1</sub>" part will be omitted and the
     * integer will be presented with no special formatting markup. If the
     * fraction is negative, the String will start with "&minus;" rather than
     * "-". For example, if this fraction is
     * &minus;<sup>1</sup>&frasl;<sub>2</sub>, the result will be
     * "<sup>&minus;1</sup>&frasl;<sub>2</sub>".
     */
    public String toHTMLString() {
        if (this.denominator == 1) {
            return Long.toString(this.numerator);
        } else {
            String fractStr = "<sup>";
            if (this.numerator < 0) {
                fractStr = fractStr + "&minus;" + Math.abs(this.numerator);
            } else {
                fractStr = fractStr + this.numerator;
            }
            fractStr = fractStr + "</sup>&frasl;<sub>" + this.denominator + "</sub>";
            return fractStr;
        }
    }

    /**
     * Gives a representation of this fraction suitable for use in a TeX
     * document.
     * @return A <code>String</code> starting with "\frac{", followed by the
     * numerator, then "}{", then the denominator and lastly "}". For example,
     * if this fraction is &minus;<sup>1</sup>&frasl;<sub>2</sub>, the result
     * will be "\frac{-1}{2}". However, if this fraction is an integer, the
     * output will be the same as {@link #toString()}.
     */
    public String toTeXString() {
        if (this.denominator == 1) {
            return Long.toString(this.numerator);
        } else {
            return ("\\frac{" + this.numerator + "}{" + this.denominator + "}");
        }
    }

    /**
     * Gives a hash code for the fraction. This is guaranteed to be the same for
     * two fractions that are arithmetically equal. It is likely but not
     * guaranteed to be different for two fractions that are arithmetically
     * unequal. Also, it is likely but not guaranteed that the sign of the hash
     * code will match the sign of the fraction.
     * @return A 32-bit integer hash code. For example, if this fraction is
     * &minus;<sup>9</sup>&frasl;<sub>14</sub>, the result might be
     * &minus;589810.
     */
    @Override
    public int hashCode() {
        long numerHash = this.numerator % HASH_SEP;
        long denomHash = this.denominator % HASH_SEP;
        return (int) (numerHash * HASH_SEP + denomHash);
    }

    /**
     * Determines if this <code>Fraction</code> object is equal to another
     * object.
     * @param obj The object to be tested for equality.
     * @return True if both objects are of the <code>Fraction</code> class and
     * they represent the same arithmetical fraction (regardless of what
     * numerators and denominators were used at the time of construction), false
     * otherwise. For example, <sup>3</sup>&frasl;<sub>4</sub> and
     * <sup>9</sup>&frasl;<sub>12</sub> should be found to be equal.
     * <sup>3</sup>&frasl;<sub>4</sub> and <sup>3</sup>&frasl;<sub>5</sub>
     * should not be found to be equal. A <code>Fraction</code> object should
     * not be found to be equal to an {@link Integer} object even if their
     * values are arithmetically equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fraction other = (Fraction) obj;
        if (this.numerator != other.numerator) {
            return false;
        }
        return (this.denominator == other.denominator);
    }

    /**
     * Compares this fraction to another fraction for order. Returns a negative
     * integer, zero, or a positive integer as this fraction is less than, equal
     * to, or greater than the other fraction. This enables sorting with
     * <code>java.util.Collections#sort(java.util.List)</code> without need for
     * a comparator.
     * @param other The fraction to compare to. Examples:
     * &minus;<sup>1</sup>&frasl;<sub>2</sub>, <sup>5</sup>&frasl;<sub>3</sub>,
     * <sup>22</sup>&frasl;<sub>7</sub>.
     * @return &minus;1 or any other negative integer if the compared fraction
     * is greater than this fraction, 0 if they are equal, 1 or any other
     * positive integer if the compared fraction is less than this fraction. For
     * example, if this fraction is <sup>5</sup>&frasl;<sub>3</sub>, compared to
     * <sup>22</sup>&frasl;<sub>7</sub> the result would most
     * likely be &minus;1; compared to <sup>5</sup>&frasl;<sub>3</sub> the
     * result would certainly be 0, and compared to
     * &minus;<sup>1</sup>&frasl;<sub>2</sub> the result would most likely be 1.
     */
    @Override
    public int compareTo(Fraction other) {
        Fraction diff = this.minus(other);
        if (diff.numerator < 0) {
            return -1;
        }
        if (diff.numerator > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * Gives a numeric approximation of the value of this fraction. This is
     * likely to be precise if the denominator is a small power of 2.
     * @return A floating point approximation of the value of this fraction. For
     * example, if this fraction is <sup>1</sup>&frasl;<sub>7</sub>, the result
     * might be something like 0.14285714285714285714285714285714. If this
     * fraction is <sup>6</sup>&frasl;<sub>13</sub>, the result might be
     * something like 0.46153846153846153846153846153846.
     */
    public double getNumericApproximation() {
        return this.numericVal;
    }

    /**
     * Parses a <code>String</code> into a <code>Fraction</code> object.
     * @param s The <code>String</code> to parse. It may contain spaces. For
     * example, "22 / 7".
     * @return A <code>Fraction</code> object. For example, <code>new
     * Fraction(22, 7)</code>.
     * @throws NumberFormatException If <code>s</code> contains characters other
     * than digits, spaces, "-" (may occur once or twice, but only preceding
     * digits) or "/" (may only occur once or not at all).
     */
    public static Fraction parseFract(String s) {
        s = s.replace(" ", "");
        int slashIndex = s.indexOf('/');
        if (slashIndex == -1) {
            long numer = Long.parseLong(s);
            return new Fraction(numer);
        }
        long numer = Long.parseLong(s.substring(0, slashIndex));
        long denom = Long.parseLong(s.substring(slashIndex + 1));
        return new Fraction(numer, denom);
    }

    /**
     * Implicit denominator constructor for integers. Denominator is filled in
     * as 1.
     * @param numer The numerator of the fraction. For example, 7. Then the
     * fraction <sup>7</sup>&frasl;<sub>1</sub> is arithmetically equal to the
     * integer 7.
     */
    public Fraction(long numer) {
        this.numerator = numer;
        this.numericVal = numer;
        this.denominator = 1;
    }

    /**
     * This should be considered the default constructor. It makes sure the
     * fraction is expressed in lowest terms and that the denominator is
     * positive.
     * @param numer The numerator of the fraction. For example, 2.
     * @param denom The denominator of the fraction. It must not be 0. Other
     * than that, there are no requirements for the denominator. It need not be
     * coprime to the numerator, and it may be negative. The constructor will
     * make sure the fraction is in lowest terms, and that it has a positive
     * denominator. For example, &minus;4.
     */
    public Fraction(long numer, long denom) {
        if (denom == 0) {
            String exceptionMessage = "Denominator 0 is not allowed.";
            throw new IllegalArgumentException(exceptionMessage);
        }
        long gcdNumDen = NumberTheoreticFunctionsCalculator.euclideanGCD(numer, denom);
        if (denom < 0) {
            gcdNumDen *= -1;
        }
        this.numerator = numer / gcdNumDen;
        this.denominator = denom / gcdNumDen;
        this.numericVal = ((double) this.numerator / (double) this.denominator);
    }

}
