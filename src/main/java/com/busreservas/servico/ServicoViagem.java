package com.busreservas.servico;

import com.busreservas.model.Horario;
import com.busreservas.model.LinhaOnibus;
import com.busreservas.model.Usuario;
import com.busreservas.model.Viagem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * US03 - Serviço de Viagens
 * Responsável: Gabriel
 */
public class ServicoViagem {

    private final List<Viagem> viagens;
    private final Set<String> chavesViagens; // US03 - Impedir duplicatas
    private int proximoId = 1;

    public ServicoViagem() {
        this.viagens = new ArrayList<>();
        this.chavesViagens = new HashSet<>();
    }

    // US03/T4 - Cadastrar viagem
    public Viagem cadastrarViagem(Usuario usuario, LinhaOnibus linha, Horario horario, LocalDate data, int capacidade) {
        if (!usuario.podeCadastrarViagens()) {
            throw new SecurityException("Usuário sem permissão para cadastrar viagens.");
        }
        // US03/T5 - Validar dados
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é possível cadastrar viagem para data passada.");
        }
        if (capacidade <= 0 || capacidade > 60) {
            throw new IllegalArgumentException("Capacidade deve ser entre 1 e 60 assentos.");
        }

        // Verificar viagem duplicada
        Viagem temp = new Viagem(0, linha, horario, data, capacidade);
        if (chavesViagens.contains(temp.getChaveUnica())) {
            throw new IllegalArgumentException("Já existe uma viagem cadastrada para esta linha, " +
                    "data e horário: " + temp.getChaveUnica());
        }

        Viagem viagem = new Viagem(proximoId++, linha, horario, data, capacidade);
        viagens.add(viagem);
        chavesViagens.add(viagem.getChaveUnica());
        System.out.println("Viagem cadastrada: " + viagem);
        return viagem;
    }

    // US09/T1 - Listar todas as viagens disponíveis
    public List<Viagem> listarViagensDisponiveis() {
        List<Viagem> disponiveis = new ArrayList<>();
        for (Viagem v : viagens) {
            if (v.getStatus() == Viagem.StatusViagem.DISPONIVEL ||
                    v.getStatus() == Viagem.StatusViagem.LOTADA) {
                disponiveis.add(v);
            }
        }
        return disponiveis;
    }

    // US09/T2 - Filtrar por data
    public List<Viagem> filtrarPorData(LocalDate data) {
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagens) {
            if (v.getData().equals(data)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // US09/T2 - Filtrar por destino
    public List<Viagem> filtrarPorDestino(String destino) {
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagens) {
            if (v.getLinha().getRota().getCidadeDestino()
                    .equalsIgnoreCase(destino)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // US09/T2 - Filtrar por linha
    public List<Viagem> filtrarPorLinha(String codigoLinha) {
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagens) {
            if (v.getLinha().getCodigo().equalsIgnoreCase(codigoLinha)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // US09/T2 - Filtrar por horário de partida (hora exata)
    public List<Viagem> filtrarPorHorario(int hora, int minuto) {
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagens) {
            Horario h = v.getHorario();
            if (h.getHora().equals(String.format("%02d:%02d", hora, minuto))) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public Viagem buscarPorId(int id) {
        for (Viagem v : viagens) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    // US03/T6 - Filtrar por status (DISPONIVEL, LOTADA, CANCELADA, ENCERRADA)
    public List<Viagem> filtrarPorStatus(Viagem.StatusViagem status) {
        if (status == null) throw new IllegalArgumentException("Status nao pode ser nulo.");
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagens) {
            if (v.getStatus() == status) resultado.add(v);
        }
        return resultado;
    }

    // US03/T6 - Filtrar por cidade de origem
    public List<Viagem> filtrarPorOrigem(String origem) {
        if (origem == null || origem.isBlank()) {
            throw new IllegalArgumentException("Cidade de origem nao pode ser vazia.");
        }
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagens) {
            if (v.getLinha().getRota().getCidadeOrigem().equalsIgnoreCase(origem)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Viagem> listarTodas() {
        return new ArrayList<>(viagens);
    }
}
