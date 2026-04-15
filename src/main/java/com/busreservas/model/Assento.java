package com.busreservas.model;

import java.util.Objects;

/**
 * US04 - Controle de Assentos
 * Responsável: Felipe
 */
public class Assento {

    private final int numero;
    private boolean ocupado;
    private Passageiro passageiro;

    // US04/T1 - Criar classe Assento
    public Assento(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Número do assento deve ser positivo.");
        }
        this.numero = numero;
        this.ocupado = false;
        this.passageiro = null;
    }

    // US04/T4 - Verificar disponibilidade
    public boolean isDisponivel() {
        return !ocupado;
    }

    // US04/T5 - Reservar assento para um passageiro
    public void reservar(Passageiro passageiro) {
        if (ocupado) {
            throw new IllegalStateException("Assento " + numero + " já está ocupado.");
        }
        if (passageiro == null) {
            throw new IllegalArgumentException("Passageiro não pode ser nulo.");
        }
        this.ocupado = true;
        this.passageiro = passageiro;
    }

    // US07/T3 - Liberar assento (cancelamento)
    public void liberar() {
        this.ocupado = false;
        this.passageiro = null;
    }

    public int getNumero() { return numero; }

    public boolean isOcupado() { return ocupado; }

    public Passageiro getPassageiro() { return passageiro; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assento)) return false;
        Assento assento = (Assento) o;
        return numero == assento.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        String status = ocupado
                ? "OCUPADO(" + (passageiro != null ? passageiro.getNome() : "?") + ")"
                : "DISPONIVEL";

        return String.format("[Assento %02d | %s]", numero, status);
    }
}