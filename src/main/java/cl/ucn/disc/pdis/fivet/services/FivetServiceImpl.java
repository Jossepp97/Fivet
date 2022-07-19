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

package cl.ucn.disc.pdis.fivet.services;

import cl.ucn.disc.pdis.fivet.model.FichaMedica;
import cl.ucn.disc.pdis.fivet.model.Persona;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * The Fivet Implementation
 */
@Slf4j
public class FivetServiceImpl extends FivetServiceGrpc.FivetServiceImplBase {

    /**
     * The Controller
     */
    private final FivetController fivetController;

    /**
     * The Fivet Service
     *
     * @param databaseUrl to use
     */
    public FivetServiceImpl(String databaseUrl) {
        this.fivetController = new FivetControllerImpl(databaseUrl);
    }



    /**
     * Authenticate
     *
     * @param request credentials of Persona
     * @param responseObserver StreamObserver of Persona
     */
    @Override
    public void authenticate(AuthenticateReq request, StreamObserver<PersonaReply> responseObserver) {
        Optional<Persona> persona2 = this.fivetController.retrieveByLogin(
                request.getLogin());
        if (persona2.isPresent()) {
            responseObserver.onNext(PersonaReply.newBuilder()
                    .setRut(persona2.get().getRut())
                    .setNombre(persona2.get().getNombre())
                    .setEmail(persona2.get().getEmail())
                    .setDireccion(persona2.get().getDireccion())
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(PersonaReply.newBuilder()
                    .setRut("199682954")
                    .setNombre("Jose Avalos Guzman")
                    .setEmail("jose.avalos01@alumnos.ucn.cl")
                    .setDireccion("las palmeras 35")
                    .build());
            responseObserver.onCompleted();
        }
    }

    public void addPersona(AddPersonaReq request, StreamObserver<PersonaReply> responseObserver){

    }

    public void addControl(AddControlReq request, StreamObserver<FichaMedicaReply> responseObserver){

    }

    public void retrieveFicha(retrieveFichaMedicaReq request, StreamObserver<FichaMedicaReply> responseObserver){

    }

    public void searchFicha(searchFichaMedicaReq request, StreamObserver<FichaMedicaReply> responseObserver){

    }

    public void addFichaMedica(addFichaMedicaReq request, StreamObserver<FichaMedicaReply> responseObserver){

    }

}