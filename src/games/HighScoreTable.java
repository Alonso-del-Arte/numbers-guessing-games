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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Alonso del Arte
 */
public class HighScoreTable implements Serializable {

    private static final long serialVersionUID = 4549602407897643827L;

    private final ArrayList<HighScoreTableRecord> records = new ArrayList<>();

    public void addHighScore(HighScoreTableRecord score) {
        this.records.add(score);
    }

    public ArrayList<HighScoreTableRecord> getRecords() {
        return new ArrayList<>(this.records);
    }

}
