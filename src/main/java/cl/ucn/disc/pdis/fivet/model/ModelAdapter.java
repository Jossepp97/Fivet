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

package cl.ucn.disc.pdis.fivet.model;

import cl.ucn.disc.pdis.fivet.grpc.*;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Model.
 *
 * @author José Ávalos Guzmán.
 */
@UtilityClass
public final class ModelAdapter {

    /**
     * Build a persona java of gRPC.
     *
     * @param persona to use
     * @return PersonaEntity
     */
    public static PersonaEntity build(final Persona persona) {
        return PersonaEntity.newBuilder()
                .setNombre(persona.getNombre())
                .setDireccion(persona.getDireccion())
                .setPassword(persona.getPassword())
                .setEmail(persona.getEmail())
                .setRut(persona.getRut())
                .build();
    }

    /**
     * Build a persona from PersonaEntity.
     *
     * @param personaEntity to use
     * @return Persona
     */
    public static Persona build(final PersonaEntity personaEntity) {
        return Persona.builder()
                .nombre(personaEntity.getNombre())
                .direccion(personaEntity.getDireccion())
                .password(personaEntity.getPassword())
                .email(personaEntity.getEmail())
                .rut(personaEntity.getRut())
                .build();
    }

    /**
     * Build a control java of gRPC.
     *
     * @param controlEntity to use
     * @return Control
     */
    public static Control build(final ControlEntity controlEntity) {
        return Control.builder()
                .fecha(build(controlEntity.getFecha()))
                .diagnostico(controlEntity.getDiagnostico())
                .temperatura(controlEntity.getTemperatura())
                .peso(controlEntity.getPeso())
                .altura(controlEntity.getAltura())
                .veterinario(build(controlEntity.getVeterinario()))
                .build();
    }

    /**
     * Build a persona java of gRPC.
     *
     * @param control to use
     * @return ControlEntity
     */
    public static ControlEntity build(final Control control) {
        return ControlEntity.newBuilder()
                .setFecha(control.getFecha().toString())
                .setDiagnostico(control.getDiagnostico())
                .setTemperatura(control.getTemperatura())
                .setPeso(control.getPeso())
                .setAltura(control.getAltura())
                .setVeterinario(build(control.getVeterinario()))
                .build();
    }

    /**
     * Build a String to ZonedDateTime.
     *
     * @param fecha to use
     * @return ZonedDateTime
     */
    public static ZonedDateTime build(final String fecha) {
        return ZonedDateTime.parse(fecha, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    /**
     * Build a String to LocalDate.
     *
     * @param fechaNacimiento to use
     * @return LocalDate
     */
    public static LocalDate buildLocalDate(final String fechaNacimiento) {
        return LocalDate.parse(fechaNacimiento, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Build a FichaMedica to sexo.
     *
     * @param sexoEntity to use
     * @return Sexo
     */
    public static FichaMedica.Sexo build(final SexoEntity sexoEntity) {
        return FichaMedica.Sexo.valueOf(sexoEntity.name());
    }

    /**
     * Build a sexo.
     *
     * @param sexo to use
     * @return SexoEntity
     */
    public static SexoEntity build(final FichaMedica.Sexo sexo) {
        return SexoEntity.valueOf(sexo.name());
    }

    /**
     * Build a FichaMedica gRPC from FichaMedicaEntity.
     * @param fichaMedicaEntity to use
     * @return FichaMedica
     */
    public static FichaMedica build(final FichaMedicaEntity fichaMedicaEntity) {
        return FichaMedica.builder()
                .numeroFicha(fichaMedicaEntity.getNumeroFicha())
                .nombrePaciente(fichaMedicaEntity.getNombrePaciente())
                .especie(fichaMedicaEntity.getEspecie())
                .fechaNacimiento(buildLocalDate(fichaMedicaEntity.getFechaNacimiento()))
                .raza(fichaMedicaEntity.getRaza())
                .sexo(build(fichaMedicaEntity.getSexo()))
                .color(fichaMedicaEntity.getColor())
                .tipo(fichaMedicaEntity.getTipo())
                .duenio(build(fichaMedicaEntity.getDuenio()))
                .controles(buildControles(fichaMedicaEntity.getControlesList()))
                .build();
    }

    /**
     * Build a fichaMedicaEntity from fichaMedica.
     * @param fichaMedica to use
     * @return FichaMedicaEntity
     */
    public static FichaMedicaEntity build(final FichaMedica fichaMedica) {
        return FichaMedicaEntity.newBuilder()
                .setNumeroFicha(fichaMedica.getNumeroFicha())
                .setNombrePaciente(fichaMedica.getNombrePaciente())
                .setEspecie(fichaMedica.getEspecie())
                .setFechaNacimiento(fichaMedica.getFechaNacimiento().toString())
                .setRaza(fichaMedica.getRaza())
                .setSexo(build(fichaMedica.getSexo()))
                .setColor(fichaMedica.getColor())
                .setTipo(fichaMedica.getTipo())
                .setDuenio(build(fichaMedica.getDuenio()))
                .addAllControles(buildControlEntities(fichaMedica.getControles()))
                .build();
    }

    /**
     * Build a Collection of controlEntity.
     * @param controles to use
     * @return Collection<ControlEntity>
     */
    public static Collection<ControlEntity> buildControlEntities(final Collection<Control> controles) {
        List<ControlEntity> controlEntities = new ArrayList<>();
        for (Control control : controles) {
            controlEntities.add(build(control));
        }
        return controlEntities;
    }

    /**
     * Build a controlEntity of Collection.
     * @param controlEntities to use
     * @return Collection<Control>
     */
    public static Collection<Control> buildControles(final Collection<ControlEntity> controlEntities) {
        List<Control> controles = new ArrayList<>();
        for (ControlEntity controlEntity : controlEntities) {
            controles.add(build(controlEntity));
        }
        return controles;
    }
}