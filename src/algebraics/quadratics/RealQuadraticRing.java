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
 * Defines objects to represent real quadratic rings.
 * @author Alonso del Arte
 */
public class RealQuadraticRing extends QuadraticRing {

    /**
     * Indicates that this ring is purely real. All the numbers of this ring are
     * on the real number line.
     * @return Always true for a real quadratic ring.
     */
    @Override
    public final boolean isPurelyReal() {
        return true;
    }

    /**
     * Gives the numeric value of the square root of the radicand.
     * @return A double with a rational approximation of the square root of the
     * radicand. For example, for <b>Z</b>[&radic;2], this would be roughly
     * 1.414; for <b>Z</b>[&radic;3], this would be roughly 1.732.
     */
    @Override
    public double getRadSqrt() {
        return this.realRadSqrt;
    }

    /**
     * This function is included merely to simplify the inheritance structure of
     * {@link QuadraticRing} to {@link ImaginaryQuadraticRing}.
     * @return The same number as {@link QuadraticRing#getRadicand()}.
     */
    @Override
    public int getAbsNegRad() {
        return this.radicand;
    }

    /**
     * This function is included merely to simplify the inheritance structure of
     * {@link QuadraticRing} to {@link ImaginaryQuadraticRing}.
     * @return The same number as {@link QuadraticRing#getRadSqrt()}.
     */
    @Override
    public double getAbsNegRadSqrt() {
        return this.realRadSqrt;
    }

    /**
     * Constructs a new object representing a real quadratic ring.
     * @param d A squarefree, positive integer greater than 1. Examples: 5, 21,
     * 1729.
     * @throws IllegalArgumentException If d is negative, 0 or 1, or positive
     * but the multiple of a nontrivial square. Examples: &minus;7, 28.
     */
    public RealQuadraticRing(int d) {
        super(d);
        if (d < 1) {
            throw new IllegalArgumentException("Positive integer required for parameter d.");
        }
        if (d == 1) {
            throw new IllegalArgumentException("Sorry, O_(Q(sqrt(1))) is not supported. Did you mean Z[i]?");
        }
        this.d1mod4 = (d % 4 == 1);
        this.realRadSqrt = Math.sqrt(this.radicand);
    }

}
