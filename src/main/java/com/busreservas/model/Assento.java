package com.busreservas.model;

/**
 * US04 - Entidade Assento
 * Responsável: Felipe
 *
 * Atributos conforme diagrama de entidades:
 *   - numero
 *   - ocupado (boolean)
 *   - passageiro (referência ao Passageiro que ocupou)
 */
public class Assento {

    private int numero;
    private boolean ocupado;
    private Passageiro passageiro; // US04 - referência direta ao passageiro

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
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }

    public Passageiro getPassageiro() { return passageiro; }
    public void setPassageiro(Passageiro passageiro) { this.passageiro = passageiro; }

    @Override
    public String toString() {
        String status = ocupado
                ? "OCUPADO(" + (passageiro != null ? passageiro.getNome() : "?") + ")"
                : "DISPONIVEL";
        return String.format("[Assento %02d | %s]", numero, status);
    }
}
