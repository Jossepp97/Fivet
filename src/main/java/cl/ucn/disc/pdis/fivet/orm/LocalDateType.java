/*
 * MIT License
 *
 * Copyright (c) 2022 José Ávalos Guzmán  <jose.avalos01@alumnos.ucn.cl>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * The ZonedDateTime to Database VARCHAR converter.
 *
 * @author José Ávalos Guzmán.
 */
@Slf4j
public final class LocalDateType extends BaseDataType {

    /**
     * Singleton!.
     */
    public static final LocalDateType INSTANCE = new LocalDateType();

    /**
     * The formatter (ZonedDateTime -- String).
     */
    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * The Size of the String: 2022-04-08T18:10:56.1160769-04:00[America/santiago]
     */
    private static final int DEFAULT_WIDTH = 10;


    /**
     * The private constructor.
     */
    private LocalDateType() {

        super(SqlType.STRING, new Class<?>[]{LocalDate.class});
    }

    /**
     * Get default width.
     * @return the size of the database field
     */
    @Override
    public int getDefaultWidth() {

        return DEFAULT_WIDTH;
    }


    /**
     * Convert a default string object and return the appropriate argument to a SQL insert or update statement.
     *
     * @return Result object to insert if the field is not specified or null if none.
     */
    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        log.debug("parseDefaultString: {} -> {}", fieldType, defaultStr);
        return defaultStr;
    }

    /**
     * The Java to SQL converter
     */
    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        LocalDate ld = (LocalDate) javaObject;
        if (ld == null) {
            return null;
        }
        return DTF.format(ld);
    }

    /**
     * Return the SQL argument object extracted from the results associated with column in position columnPos. For
     * example, if the type is a date-long then this will return a long value or null.
     *
     * @throws SQLException If there is a problem accessing the results data.
     */
    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    /**
     * The SQL to Java converter
     */
    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        if (sqlArg == null) {
            return null;
        }
        return LocalDate.parse((String) sqlArg, DTF);
    }
}
