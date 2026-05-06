package com.busreservas.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Passageiro
 * Estende Usuario com funcionalidades específicas de passageiro
 */
public class Passageiro extends Usuario {

    private String cpf;
    private String telefone;
    private List<Reserva> minhasReservas;

    public Passageiro(int id, String nome, String email, String senha, String cpf, String telefone) {
        super(id, nome, email, senha, PerfilUsuario.PASSAGEIRO);
        this.cpf = cpf;
        this.telefone = telefone;
        this.minhasReservas = new ArrayList<>();
    }

    public void adicionarReserva(Reserva reserva) {
        minhasReservas.add(reserva);
    }

    public void removerReserva(Reserva reserva) {
        minhasReservas.remove(reserva);
    }

    public List<Reserva> getMinhasReservas() {
        return new ArrayList<>(minhasReservas);
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return String.format("[Passageiro] ID: %d | Nome: %s | CPF: %s | Tel: %s",
                getId(), getNome(), cpf, telefone);
    }
}
