package com.busreservas.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe central do sistema — conforme diagrama de entidades
 *
 * Atributos conforme diagrama:
 *   - linhas: List
 *   - viagens: List
 *   - passageiros: List
 */
public class Sistema {

    private final List<LinhaOnibus> linhas;
    private final List<Viagem> viagens;
    private final List<Passageiro> passageiros;

    public Sistema() {
        this.linhas = new ArrayList<>();
        this.viagens = new ArrayList<>();
        this.passageiros = new ArrayList<>();
    }

    // ===== LINHAS =====
    public void adicionarLinha(LinhaOnibus linha) {
        linhas.add(linha);
    }

    public boolean removerLinha(int id) {
        return linhas.removeIf(l -> l.getId() == id);
    }

    public LinhaOnibus buscarLinhaPorCodigo(String codigo) {
        for (LinhaOnibus l : linhas) {
            if (l.getCodigo().equalsIgnoreCase(codigo)) return l;
        }
        return null;
    }

    public List<LinhaOnibus> getLinhas() { return new ArrayList<>(linhas); }

    // ===== VIAGENS =====
    public void adicionarViagem(Viagem viagem) {
        viagens.add(viagem);
    }

    public boolean removerViagem(int id) {
        return viagens.removeIf(v -> v.getId() == id);
    }

    public Viagem buscarViagemPorId(int id) {
        for (Viagem v : viagens) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    public List<Viagem> getViagens() { return new ArrayList<>(viagens); }

    // ===== PASSAGEIROS =====
    public void adicionarPassageiro(Passageiro passageiro) {
        passageiros.add(passageiro);
    }

    public Passageiro buscarPassageiroPorId(int id) {
        for (Passageiro p : passageiros) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public Passageiro buscarPassageiroPorCpf(String cpf) {
        for (Passageiro p : passageiros) {
            if (p.getCpf().equals(cpf)) return p;
        }
        return null;
    }

    public List<Passageiro> getPassageiros() { return new ArrayList<>(passageiros); }

    @Override
    public String toString() {
        return String.format("[Sistema] Linhas: %d | Viagens: %d | Passageiros: %d",
                linhas.size(), viagens.size(), passageiros.size());
    }
}
