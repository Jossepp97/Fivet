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

package services;


import cl.ucn.disc.pdis.fivet.model.Persona;
import cl.ucn.disc.pdis.fivet.orm.ZonedDateTimeType;
import cl.ucn.disc.pdis.fivet.services.FivetController;
import cl.ucn.disc.pdis.fivet.services.FivetControllerImpl;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Testing the Test Fivet Controller
 *
 * @author Jose Avalos Guzman
 */
@Slf4j
public final class TestFivetController {

    @SneakyThrows
    @Test
    @DisplayName("The Main Test Fivet Controller")
    void theMain() {
        log.debug("Starting the MainTest...");

        log.debug("Registering the ZonedDateTimeType...");
        DataPersisterManager.registerDataPersisters(ZonedDateTimeType.INSTANCE);

        String databaseUrl = "jdbc:sqlite:fivet.db";

        log.debug("Building the Connection, using: {}", databaseUrl);
        @Cleanup
        ConnectionSource cs = new JdbcConnectionSource(databaseUrl);

        log.debug("Dropping the tables...");
        // Drop the database
        TableUtils.dropTable(cs, Persona.class, true);

        log.debug("Creating the tables...");
        // Create the database
        TableUtils.createTable(cs, Persona.class);

        // The Controller
        FivetController fivetController = new FivetControllerImpl(databaseUrl);

        // Add Persona
        log.debug("Add persona...");
        {
            Persona persona = Persona.builder()
                    .rut("194920377")
                    .nombre("Jose Avalos")
                    .email("jose@gmail.com")
                    .direccion("pasaje chicorita 333")
                    .build();
            fivetController.add(persona, "1234");
            log.debug("To db: {}", ToStringBuilder.reflectionToString(persona, ToStringStyle.MULTI_LINE_STYLE));
        }

        // Log in...
        {
            // Using rut as login
            String login = "194920377";
            String pass = "1234";

            Optional<Persona> persona = fivetController.authenticate(login, pass);
            Assertions.assertTrue(persona.isPresent(), "The persona was Empty");

            // Using email as login
            login = "jose@gmail.com";
            pass = "1234";

            Optional<Persona> persona2 = fivetController.authenticate(login, pass);
            Assertions.assertTrue(persona2.isPresent(), "The persona was Empty");

            // A person that not exists in the controller
            login = "0";
            pass = "1234";
            persona = fivetController.authenticate(login, pass);
            Assertions.assertFalse(persona.isPresent(), "The persona was not Empty");

            // With incorrect password
            login = "194920377";
            pass = "incorrect";
            persona = fivetController.authenticate(login, pass);
            Assertions.assertFalse(persona.isPresent(), "The persona was not Empty");

        }


        {
            // Delete a person by id
            Persona persona = fivetController.retrieveByLogin("194920377").orElseThrow();
            fivetController.delete(persona.getId());

        }

        // Drop the database
        TableUtils.dropTable(cs, Persona.class, true);

        log.debug("Done.");
    }
}
