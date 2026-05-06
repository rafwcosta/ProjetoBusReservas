package com.busreservas.model;

import java.util.ArrayList;
import java.util.List;

/**
 * US01 - Entidade LinhaOnibus
 * Responsável: Arthur
 */
public class LinhaOnibus {

    private int id;
    private String codigo;       // Ex: "BSB-001"
    private String nome;         // Ex: "Brasília - São Paulo"
    private Rota rota;
    private List<Horario> horarios;
    private double valorPassagem;

    public LinhaOnibus(int id, String codigo, String nome, Rota rota, double valorPassagem) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da linha não pode ser vazio.");
        }
        if (rota == null) {
            throw new IllegalArgumentException("Rota não pode ser nula.");
        }
        this.id = id;
        this.codigo = codigo.trim().toUpperCase();
        this.nome = nome;
        this.rota = rota;
        this.valorPassagem = valorPassagem;
        this.horarios = new ArrayList<>();
    }

    // US02/T2 - Associar horários à LinhaOnibus
    public void adicionarHorario(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("Horário não pode ser nulo.");
        }
        // Evitar horários duplicados
        for (Horario h : horarios) {
            if (h.getHora().equals(horario.getHora())) {
                throw new IllegalArgumentException("Já existe um horário de partida " +
                        horario.getHora() + " nesta linha.");
            }
        }
        horarios.add(horario);
    }

    // US02/T4 - Remover horário
    public boolean removerHorario(int horarioId) {
        return horarios.removeIf(h -> h.getId() == horarioId);
    }

    public List<Horario> getHorarios() {
        return new ArrayList<>(horarios);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Rota getRota() { return rota; }
    public void setRota(Rota rota) { this.rota = rota; }

    public double getValorPassagem() { return valorPassagem; }
    public void setValorPassagem(double valorPassagem) { this.valorPassagem = valorPassagem; }

    @Override
    public String toString() {
        return String.format("[Linha] %s | %s | %s | R$ %.2f | %d horário(s)",
                codigo, nome, rota, valorPassagem, horarios.size());
    }
}
