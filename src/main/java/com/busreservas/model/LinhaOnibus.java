package com.busreservas.model;

import java.util.ArrayList;
import java.util.List;

public class LinhaOnibus {

    private int id;
    private String codigo;
    private String nome;
    private Rota rota;
    private List<Horario> horarios;
    private double valorPassagem;

    public LinhaOnibus(int id, String codigo, String nome, Rota rota, double valorPassagem) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo da linha nao pode ser vazio.");
        }
        if (rota == null) {
            throw new IllegalArgumentException("Rota nao pode ser nula.");
        }
        this.id = id;
        this.codigo = codigo.trim().toUpperCase();
        this.nome = nome;
        this.rota = rota;
        this.valorPassagem = valorPassagem;
        this.horarios = new ArrayList<>();
    }

    public void adicionarHorario(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("Horario nao pode ser nulo.");
        }
        for (Horario h : horarios) {
            if (h.getHora().equals(horario.getHora())) {
                throw new IllegalArgumentException("Ja existe um horario " +
                        horario.getHora() + " nesta linha.");
            }
        }
        horarios.add(horario);
    }

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
    public void setValorPassagem(double v) { this.valorPassagem = v; }

    @Override
    public String toString() {
        return String.format("[Linha] %s | %s | %s | R$ %.2f | %d horario(s)",
                codigo, nome, rota, valorPassagem, horarios.size());
    }
}
