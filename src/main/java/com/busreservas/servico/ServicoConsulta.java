package com.busreservas.servico;

import com.busreservas.model.Assento;
import com.busreservas.model.Usuario;
import com.busreservas.model.Viagem;
import java.time.LocalDate;
import java.util.List;

/**
 * US09 - Serviço de Consultas de Informações
 * Responsável: Uanderson
 */
public class ServicoConsulta {

    private final ServicoViagem servicoViagem;

    public ServicoConsulta(ServicoViagem servicoViagem) {
        this.servicoViagem = servicoViagem;
    }

    // US09/T1 - Listar viagens disponíveis
    public void listarViagensDisponiveis(Usuario usuario) {
        if (!usuario.podeConsultarViagens()) {
            throw new SecurityException("Usuário sem permissão para consultar viagens.");
        }
        List<Viagem> viagens = servicoViagem.listarViagensDisponiveis();
        if (viagens.isEmpty()) {
            System.out.println("Nenhuma viagem disponível no momento.");
            return;
        }
        System.out.println("=== Viagens Disponíveis ===");
        for (Viagem v : viagens) {
            System.out.println(v);
        }
    }

    // US09/T2 - Filtrar por data
    public void listarPorData(Usuario usuario, LocalDate data) {
        if (!usuario.podeConsultarViagens()) {
            throw new SecurityException("Usuário sem permissão para consultar viagens.");
        }
        List<Viagem> viagens = servicoViagem.filtrarPorData(data);
        System.out.println("=== Viagens em " + data.toString() + " ===");
        if (viagens.isEmpty()) {
            System.out.println("Nenhuma viagem encontrada para esta data.");
            return;
        }
        for (Viagem v : viagens) {
            System.out.println(v);
        }
    }

    // US09/T2 - Filtrar por destino
    public void listarPorDestino(Usuario usuario, String destino) {
        if (!usuario.podeConsultarViagens()) {
            throw new SecurityException("Usuário sem permissão para consultar viagens.");
        }
        List<Viagem> viagens = servicoViagem.filtrarPorDestino(destino);
        System.out.println("=== Viagens com destino: " + destino + " ===");
        if (viagens.isEmpty()) {
            System.out.println("Nenhuma viagem encontrada para este destino.");
            return;
        }
        for (Viagem v : viagens) {
            System.out.println(v);
        }
    }

    // US09/T2 - Filtrar por linha
    public void listarPorLinha(Usuario usuario, String codigoLinha) {
        if (!usuario.podeConsultarViagens()) {
            throw new SecurityException("Usuário sem permissão para consultar viagens.");
        }
        List<Viagem> viagens = servicoViagem.filtrarPorLinha(codigoLinha);
        System.out.println("=== Viagens da linha: " + codigoLinha + " ===");
        if (viagens.isEmpty()) {
            System.out.println("Nenhuma viagem encontrada para esta linha.");
            return;
        }
        for (Viagem v : viagens) {
            System.out.println(v);
        }
    }

    // US09/T2 - Filtrar por horário de partida
    public void listarPorHorario(Usuario usuario, int hora, int minuto) {
        if (!usuario.podeConsultarViagens()) {
            throw new SecurityException("Usuário sem permissão para consultar viagens.");
        }
        List<Viagem> viagens = servicoViagem.filtrarPorHorario(hora, minuto);
        System.out.printf("=== Viagens com partida às %02d:%02d ===%n", hora, minuto);
        if (viagens.isEmpty()) {
            System.out.println("Nenhuma viagem encontrada para este horário.");
            return;
        }
        for (Viagem v : viagens) {
            System.out.println(v);
        }
    }

    // US09/T3 - Exibir detalhes completos de uma viagem
    public void exibirDetalhesViagem(Usuario usuario, int viagemId) {
        if (!usuario.podeConsultarViagens()) {
            throw new SecurityException("Usuário sem permissão para consultar viagens.");
        }
        Viagem viagem = servicoViagem.buscarPorId(viagemId);
        if (viagem == null) {
            System.out.println("Viagem #" + viagemId + " não encontrada.");
            return;
        }

        System.out.println("========================================");
        System.out.println("       DETALHES DA VIAGEM #" + viagemId);
        System.out.println("========================================");
        System.out.println("Linha:      " + viagem.getLinha().getCodigo() +
                " - " + viagem.getLinha().getNome());
        System.out.println("Origem:     " + viagem.getLinha().getRota().getCidadeOrigem());
        System.out.println("Destino:    " + viagem.getLinha().getRota().getCidadeDestino());
        System.out.println("Data:       " + viagem.getDataFormatada());
        System.out.println("Partida:    " + viagem.getHorario().getHora());
        System.out.println("Capacidade: " + viagem.getCapacidadeTotal() + " assentos");
        System.out.println("Disponíveis:" + viagem.getQuantidadeAssentosDisponiveis() + " assentos");
        System.out.println("Status:     " + viagem.getStatus());
        System.out.println("Valor:      R$ " + String.format("%.2f", viagem.getLinha().getValorPassagem()));
        System.out.println("----------------------------------------");
        System.out.println("MAPA DE ASSENTOS:");

        List<Assento> assentos = viagem.getAssentos();
        for (int i = 0; i < assentos.size(); i++) {
            System.out.print(assentos.get(i) + "  ");
            if ((i + 1) % 4 == 0) System.out.println();
        }
        System.out.println();
        System.out.println("========================================");
    }
}
