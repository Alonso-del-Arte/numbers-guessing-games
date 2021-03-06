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

/**
 * Defines objects to represent imaginary quadratic rings.
 * @author Alonso del Arte
 */
public class ImaginaryQuadraticRing extends QuadraticRing {

    /**
     * A convenient holder for the absolute value of radicand.
     */
    protected int absRadicand;

    /**
     * Indicates that this ring is not purely real. It contains purely imaginary 
     * numbers as well as complex numbers.
     * @return Always false for an imaginary quadratic ring.
     */
    @Override
    public final boolean isPurelyReal() {
        return false;
    }

    /**
     * This function is included strictly only to simplify inheritance from 
     * {@link QuadraticRing} to {@link RealQuadraticRing}.
     * @return Nothing, ever.
     * @throws UnsupportedOperationException Always thrown, because the double 
     * primitive can't represent a purely imaginary number. If you need the 
     * square root of the radicand divided by <i>i</i>, use {@link
     * #getAbsNegRadSqrt() getAbsNegRadSqrt()} instead.
     */
    @Override
    public double getRadSqrt() {
        String exceptionMessage = "Since the radicand " + this.radicand + " is negative, this operation requires an object that can represent an imaginary number.";
        throw new UnsupportedOperationException(exceptionMessage);
    }

    @Override
    public int getAbsNegRad() {
        return this.absRadicand;
    }

    /**
     * Gives the numeric value of the square root of the radicand divided by the 
     * imaginary unit <i>i</i>.
     * @return A double with a rational approximation of the square root of the 
     * radicand divided by <i>i</i>. For example, for <b>Z</b>[&radic;&minus;2], 
     * this would be roughly 1.414; for <b>Z</b>[&radic;&minus;3], this would be 
     * roughly 1.732.
     */
    @Override
    public double getAbsNegRadSqrt() {
        return this.realRadSqrt;
    }

    /**
     * Constructs a new object representing an imaginary quadratic ring.
     * @param d A squarefree, negative integer. Examples: &minus;3, &minus;58, 
     * &minus;163.
     * @throws IllegalArgumentException If d is 0 or positive, or negative but 
     * the multiple of a nontrivial square. Examples: &minus;12, 3583.
     */
    public ImaginaryQuadraticRing(int d) {
        super(d);
        if (d > -1) {
            throw new IllegalArgumentException("Negative integer required for parameter d.");
        }
        this.d1mod4 = (d % 4 == -3);
        this.absRadicand = Math.abs(this.radicand);
        this.realRadSqrt = Math.sqrt(this.absRadicand);
    }

}