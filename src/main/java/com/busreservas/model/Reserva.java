package com.busreservas.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * US05 - Entidade Reserva
 * Responsável: Igor
 */
public class Reserva {

    public enum StatusReserva {
        CONFIRMADA,
        CANCELADA,
        LISTA_ESPERA
    }

    private int id;
    private Passageiro passageiro;
    private Viagem viagem;
    private Assento assento;
    private LocalDateTime dataReserva;
    private StatusReserva status;

    public Reserva(int id, Passageiro passageiro, Viagem viagem, Assento assento) {
        if (passageiro == null) throw new IllegalArgumentException("Passageiro não pode ser nulo.");
        if (viagem == null) throw new IllegalArgumentException("Viagem não pode ser nula.");
        if (assento == null) throw new IllegalArgumentException("Assento não pode ser nulo.");

        this.id = id;
        this.passageiro = passageiro;
        this.viagem = viagem;
        this.assento = assento;
        this.dataReserva = LocalDateTime.now();
        this.status = StatusReserva.CONFIRMADA;
    }

    // US07/T2 - Cancelar reserva
    public void cancelar() {
        if (this.status == StatusReserva.CANCELADA) {
            throw new IllegalStateException("Reserva já está cancelada.");
        }
        this.status = StatusReserva.CANCELADA;
        // US07/T3 - Liberar assento
        this.assento.liberar();
    }

    public boolean isConfirmada() {
        return status == StatusReserva.CONFIRMADA;
    }

    public String getDataReservaFormatada() {
        return dataReserva.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Passageiro getPassageiro() { return passageiro; }
    public Viagem getViagem() { return viagem; }
    public Assento getAssento() { return assento; }

    public LocalDateTime getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDateTime dataReserva) { this.dataReserva = dataReserva; }

    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[Reserva #%d] Passageiro: %s | Viagem: #%d | Linha: %s | " +
                        "Data: %s | Assento: %d | Status: %s",
                id, passageiro.getNome(), viagem.getId(),
                viagem.getLinha().getCodigo(),
                viagem.getDataFormatada(), assento.getNumero(), status);
    }
}
