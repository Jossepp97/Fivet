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

package cl.ucn.disc.pdis.fivet.services;

import cl.ucn.disc.pdis.fivet.model.Control;
import cl.ucn.disc.pdis.fivet.model.FichaMedica;
import cl.ucn.disc.pdis.fivet.model.Persona;
import cl.ucn.disc.pdis.fivet.orm.DAO;
import cl.ucn.disc.pdis.fivet.orm.ORMLiteDAO;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 *  The Fivet Controller Implementation.
 *  @author José Ávalos Guzmán.
 */
@Slf4j
public class FivetControllerImpl implements FivetController {

    /**
     * The dao of persona objects.
     */
    private final DAO<Persona> daoPersona;

    /**
     * The dao of ficha medica.
     */
    private final DAO<FichaMedica> daoFichaMedica;

    /**
     * The Hasher.
     */
    private static final PasswordEncoder PASSWORD_ENCODER =
            new Argon2PasswordEncoder();

    /**
     * The FivetControllerImpl Constructor.
     * @param dbUrl to connect
     */
    @SneakyThrows
    public FivetControllerImpl(String dbUrl) {
        ConnectionSource cs = new JdbcConnectionSource(dbUrl);
        this.daoPersona = new ORMLiteDAO<>(cs, Persona.class);
        this.daoFichaMedica = new ORMLiteDAO<>(cs, FichaMedica.class);
        this.daoPersona.dropAndCreateTable();
        this.daoFichaMedica.dropAndCreateTable();
        DAO<Control> controlDAO = new ORMLiteDAO<>(cs, Control.class);
        controlDAO.dropAndCreateTable();
    }

    /**
     * Retrieve a Persona.
     * @param login with rut or email.
     *
     * @return a Optional Persona
     */
    @Override
    public Optional<Persona> retrieveByLogin(String login) {
        Optional<Persona> persona = this.daoPersona.get("rut", login);

        if (persona.isEmpty()) {
            persona = this.daoPersona.get("email", login);
        }
        if (persona.isEmpty()) {
            return Optional.empty();
        }
        return persona;
    }

    /**
     * Authentication of a person in the system.
     * @param login    the login account.
     *
     * @param password the password of the user.
     * @return a Persona.
     */
    @Override
    public Optional<Persona> authenticate(String login, String password) {

        Optional<Persona> persona = this.daoPersona.get("rut", login);

        if (persona.isEmpty()) {
            persona = this.daoPersona.get("email", login);
        }
        if (persona.isEmpty()) {
            return Optional.empty();
        }

        if (PASSWORD_ENCODER.matches(password, persona.get().getPassword())) {
            return persona;
        }

        log.warn("Incorrect password");
        return Optional.empty();
    }

    /**
     * Add a persona in the data base.
     *
     * @param persona  to add
     * @param password to hash
     */
    @Override
    public void add(@NonNull Persona persona, @NonNull String password) {
        // Hash password
        persona.setPassword(PASSWORD_ENCODER.encode(password));
        // Save the persona
        this.daoPersona.save(persona);
    }

    /**
     * Delete a Persona.
     * @param id to delete
     */
    @Override
    public void delete(Integer id) {
        // Delete
        this.daoPersona.delete(id);
    }

    /**
     * Add a persona to the backend.
     *
     * @param persona to add
     */
    @Override
    public void addPersona(Persona persona) {
        this.daoPersona.save(persona);
    }

    /**
     * Add a control to a FichaMedica.
     *
     * @param control           to add
     * @param numeroFichaMedica to attach
     */
    @Override
    public void addControl(Control control, int numeroFichaMedica) {

    }

    /**
     * Add a FichaMedica into the backend.
     *
     * @param fichaMedica to add
     */
    @Override
    public void addFichaMedica(FichaMedica fichaMedica) {
        this.daoFichaMedica.save(fichaMedica);
    }

    /**
     * Retrieve a FichaMedica from backend.
     *
     * @param numeroFichaMedica to retrieve.
     * @return a FichaMedica.
     */
    @Override
    public Optional<FichaMedica> retrieveFichaMedica(int numeroFichaMedica) {
        Optional<FichaMedica> fichaMedica = this.daoFichaMedica.get("numero", numeroFichaMedica);
        if (fichaMedica.isEmpty()) {
            return Optional.empty();
        } else {
            return fichaMedica;
        }
    }
}



