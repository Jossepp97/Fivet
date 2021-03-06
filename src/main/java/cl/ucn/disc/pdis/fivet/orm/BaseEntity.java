/*
 * MIT License
 *
 * Copyright (c) 2022 José Ávalos Guzmán
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cl.ucn.disc.pdis.fivet.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * The Base Entity.
 * @author José Ávalos Guzmán.
 */
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable
public abstract class BaseEntity {

    /**
     *  The Id.
     */
    @Getter  //lombok automáticamente agrega el get
    @DatabaseField(generatedId = true) // profe: señor orm todas las clases que den de esta tendran una id, y esa id tu lo generas (el orm)
    protected Integer id;

    /**
     *  The date of creation.
     */
    @Getter
    @DatabaseField(canBeNull = false)
    protected ZonedDateTime createAt = ZonedDateTime.now();

    /**
     * The date of deletion.
     */
    @Getter
    @DatabaseField(canBeNull = true)
    protected ZonedDateTime deletedAt;
}
