package com.busreservas.model;

import java.util.ArrayList;
import java.util.List;

/**
 * US01 - Entidade LinhaOnibus
 * Responsável: Bianca
 */
public class LinhaOnibus {

    private String codigo;
    private String origem;
    private String destino;

    // Lista de horários
    private FilaEstatica<Horario> horarios;

    public LinhaOnibus(String codigo, String origem, String destino, int capacidadeHorarios) {
        this.codigo = codigo;
        this.origem = origem;
        this.destino = destino;
        this.horarios = new FilaEstatica<>(capacidadeHorarios);
    }

    // Adicionar horário
    public boolean adicionarHorario(String horario) {
        if (!Horario.isValido(horario)) {
            return false;
        }
        return horarios.enqueue(new Horario(horario));
    }

    // Remover horário
    public boolean removerHorario(String horario) {
        return horarios.remover(new Horario(horario));
    }

    // Listar horários
    public Horario[] listarHorarios() {
        return horarios.toArray();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    @Override
    public String toString() {
        return "Linha " + codigo + 
               " | " + origem + " -> " + destino +
               "\nHorários: " + horarios;
    }
}
