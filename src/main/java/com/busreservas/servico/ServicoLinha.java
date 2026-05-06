package com.busreservas.servico;

import com.busreservas.model.Horario;
import com.busreservas.model.LinhaOnibus;
import com.busreservas.model.Rota;
import com.busreservas.model.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 * US01 + US02 - Serviço de Linhas e Horários
 * Responsável: Arthur (US01) e Bianca (US02)
 */
public class ServicoLinha {

    private final List<LinhaOnibus> linhas;
    private final List<Rota> rotas;
    private int proximoIdLinha = 1;
    private int proximoIdRota = 1;
    private int proximoIdHorario = 1;

    public ServicoLinha() {
        this.linhas = new ArrayList<>();
        this.rotas = new ArrayList<>();
    }

    // US01/T4 - Cadastrar linha
    public LinhaOnibus cadastrarLinha(Usuario usuario, String codigo, String nome, Rota rota, double valorPassagem) {
        // US01/T5 - Validar dados
        if (!usuario.podeGerenciarLinhas()) {
            throw new SecurityException("Usuário sem permissão para cadastrar linhas.");
        }
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da linha é obrigatório.");
        }
        // Verificar código duplicado
        for (LinhaOnibus l : linhas) {
            if (l.getCodigo().equalsIgnoreCase(codigo.trim())) {
                throw new IllegalArgumentException("Já existe uma linha com o código: " + codigo);
            }
        }

        LinhaOnibus nova = new LinhaOnibus(proximoIdLinha++, codigo, nome, rota, valorPassagem);
        linhas.add(nova);
        System.out.println("Linha cadastrada: " + nova);
        return nova;
    }

    // US01/T6 - Listar linhas
    public List<LinhaOnibus> listarLinhas() {
        return new ArrayList<>(linhas);
    }

    // US01/T6 - Atualizar linha
    public boolean atualizarLinha(Usuario usuario, int idLinha, String novoNome, double novoValor) {
        if (!usuario.podeGerenciarLinhas()) {
            throw new SecurityException("Usuário sem permissão para atualizar linhas.");
        }
        for (LinhaOnibus l : linhas) {
            if (l.getId() == idLinha) {
                l.setNome(novoNome);
                l.setValorPassagem(novoValor);
                return true;
            }
        }
        return false;
    }

    // US01/T6 - Remover linha
    public boolean removerLinha(Usuario usuario, int idLinha) {
        if (!usuario.podeGerenciarLinhas()) {
            throw new SecurityException("Usuário sem permissão para remover linhas.");
        }
        return linhas.removeIf(l -> l.getId() == idLinha);
    }

    public LinhaOnibus buscarLinhaPorCodigo(String codigo) {
        for (LinhaOnibus l : linhas) {
            if (l.getCodigo().equalsIgnoreCase(codigo)) {
                return l;
            }
        }
        return null;
    }

    public LinhaOnibus buscarLinhaPorId(int id) {
        for (LinhaOnibus l : linhas) {
            if (l.getId() == id) return l;
        }
        return null;
    }

    // US01/T2 - Cadastrar rota
    public Rota cadastrarRota(Usuario usuario, String origem, String destino,
                               double distancia, int duracaoMinutos) {
        if (!usuario.podeGerenciarLinhas()) {
            throw new SecurityException("Usuário sem permissão para cadastrar rotas.");
        }
        Rota rota = new Rota(proximoIdRota++, origem, destino, distancia, duracaoMinutos);
        rotas.add(rota);
        return rota;
    }

    // US02/T2 e T4 - Adicionar horário a uma linha
    public Horario adicionarHorario(Usuario usuario, LinhaOnibus linha, String hora) {
        if (!usuario.podeGerenciarHorarios()) {
            throw new SecurityException("Usuário sem permissão para gerenciar horários.");
        }
        Horario horario = new Horario(proximoIdHorario++, hora);
        linha.adicionarHorario(horario); // Valida duplicatas internamente
        System.out.println("Horário adicionado à linha " + linha.getCodigo() + ": " + horario);
        return horario;
    }

    // US02/T4 - Remover horário
    public boolean removerHorario(Usuario usuario, LinhaOnibus linha, int horarioId) {
        if (!usuario.podeGerenciarHorarios()) {
            throw new SecurityException("Usuário sem permissão para remover horários.");
        }
        return linha.removerHorario(horarioId);
    }

    public List<Rota> listarRotas() {
        return new ArrayList<>(rotas);
    }
}
