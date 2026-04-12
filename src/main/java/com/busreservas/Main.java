package com.busreservas;

import com.busreservas.model.*;
import com.busreservas.servico.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Sistema de Reservas de Passagen Interestaduais por Onibus
 * Demonstracao completa de todas as User Stories
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("  SISTEMA DE RESERVAS DE PASSAGENS INTERESTADUAIS");
        System.out.println("=======================================================\n");

        // Classe central do sistema (conforme diagrama de entidades)       
        Sistema sistema = new Sistema();

        // Inicializar servicos
        ServicoUsuario servicoUsuario = new ServicoUsuario();
        ServicoLinha servicoLinha = new ServicoLinha();
        ServicoViagem servicoViagem = new ServicoViagem();
        ServicoReserva servicoReserva = new ServicoReserva();
        ServicoConsulta servicoConsulta = new ServicoConsulta(servicoViagem);

        // =====================================================
        separador("US00 - CONTROLE DE PERFIS DE USUARIO");
        // =====================================================

        Usuario admin = servicoUsuario.autenticar("admin@bus.com", "admin123");

        servicoUsuario.exibirPermissoes(PerfilUsuario.ADMINISTRADOR);
        System.out.println();
        servicoUsuario.exibirPermissoes(PerfilUsuario.ATENDENTE);
        System.out.println();
        servicoUsuario.exibirPermissoes(PerfilUsuario.PASSAGEIRO);

        Usuario atendente = servicoUsuario.cadastrarUsuario(admin, "Atendente Maria",
                "maria@bus.com", "atend456", PerfilUsuario.ATENDENTE);

        Passageiro passageiro1 = servicoUsuario.cadastrarPassageiro(
                "Carlos Silva", "carlos@email.com", "123456", "123.456.789-00", "77999990001");
        Passageiro passageiro2 = servicoUsuario.cadastrarPassageiro(
                "Ana Souza", "ana@email.com", "654321", "987.654.321-00", "77999990002");
        Passageiro passageiro3 = servicoUsuario.cadastrarPassageiro(
                "Pedro Lima", "pedro@email.com", "111222", "111.222.333-00", "77999990003");

        // Registrar passageiros no sistema central
        sistema.adicionarPassageiro(passageiro1);
        sistema.adicionarPassageiro(passageiro2);
        sistema.adicionarPassageiro(passageiro3);

        // =====================================================
        separador("US01 - GERENCIAMENTO DE LINHAS");
        // =====================================================

        Rota rotaBSB_SP  = servicoLinha.cadastrarRota(admin, "Brasilia", "Sao Paulo", 1015.0, 720);
        Rota rotaSSA_RJ  = servicoLinha.cadastrarRota(admin, "Salvador", "Rio de Janeiro", 1650.0, 960);
        Rota rotaVCA_SP  = servicoLinha.cadastrarRota(admin, "Vitoria da Conquista", "Sao Paulo", 1400.0, 900);

        LinhaOnibus linhaBSB = servicoLinha.cadastrarLinha(admin, "BSB-001", "Brasilia - Sao Paulo", rotaBSB_SP, 189.90);
        LinhaOnibus linhaSSA = servicoLinha.cadastrarLinha(admin, "SSA-001", "Salvador - Rio de Janeiro", rotaSSA_RJ, 249.90);
        LinhaOnibus linhaVCA = servicoLinha.cadastrarLinha(admin, "VCA-001", "Vitoria da Conquista - Sao Paulo", rotaVCA_SP, 210.00);

        // Registrar linhas no sistema central
        sistema.adicionarLinha(linhaBSB);
        sistema.adicionarLinha(linhaSSA);
        sistema.adicionarLinha(linhaVCA);

        System.out.println("\nLinhas cadastradas:");
        for (LinhaOnibus l : servicoLinha.listarLinhas()) System.out.println(l);

        // Teste de codigo duplicado
        try {
            servicoLinha.cadastrarLinha(admin, "BSB-001", "Duplicada", rotaBSB_SP, 100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("[BLOQUEADO] " + e.getMessage());
        }

        // =====================================================
        separador("US02 - GERENCIAMENTO DE HORARIOS");
        // =====================================================

        // Novo formato: apenas String "HH:mm" conforme diagrama (Horario tem so campo 'hora')
        Horario h1 = servicoLinha.adicionarHorario(admin, linhaBSB, "06:00");
        Horario h2 = servicoLinha.adicionarHorario(admin, linhaBSB, "14:00");
        Horario h3 = servicoLinha.adicionarHorario(admin, linhaSSA, "08:30");
        Horario h4 = servicoLinha.adicionarHorario(admin, linhaVCA, "07:00");

        // Teste de horario duplicado
        try {
            servicoLinha.adicionarHorario(admin, linhaBSB, "06:00");
        } catch (IllegalArgumentException e) {
            System.out.println("[BLOQUEADO] " + e.getMessage());
        }

        // Teste de formato invalido
        try {
            servicoLinha.adicionarHorario(admin, linhaBSB, "25:99");
        } catch (IllegalArgumentException e) {
            System.out.println("[BLOQUEADO] " + e.getMessage());
        }

        // =====================================================
        separador("US03 - CADASTRO DE VIAGENS");
        // =====================================================

        LocalDate amanha   = LocalDate.now().plusDays(1);
        LocalDate doisDias = LocalDate.now().plusDays(2);

        Viagem v1 = servicoViagem.cadastrarViagem(admin, linhaBSB, h1, amanha, 4); // 4 assentos p/ testar lotacao
        Viagem v2 = servicoViagem.cadastrarViagem(admin, linhaBSB, h2, amanha, 40);
        Viagem v3 = servicoViagem.cadastrarViagem(admin, linhaSSA, h3, doisDias, 44);
        Viagem v4 = servicoViagem.cadastrarViagem(admin, linhaVCA, h4, amanha, 44);

        // Registrar viagens no sistema central
        sistema.adicionarViagem(v1);
        sistema.adicionarViagem(v2);
        sistema.adicionarViagem(v3);
        sistema.adicionarViagem(v4);

        // Teste de viagem duplicada
        try {
            servicoViagem.cadastrarViagem(admin, linhaBSB, h1, amanha, 40);
        } catch (IllegalArgumentException e) {
            System.out.println("[BLOQUEADO] " + e.getMessage());
        }

        System.out.println("\nEstado do Sistema central:");
        System.out.println(sistema);

        // =====================================================
        separador("US09 - CONSULTA DE INFORMACOES");
        // =====================================================

        servicoConsulta.listarViagensDisponiveis(passageiro1);
        System.out.println();
        servicoConsulta.listarPorData(passageiro1, amanha);
        System.out.println();
        servicoConsulta.listarPorDestino(admin, "Sao Paulo");
        System.out.println();
        servicoConsulta.listarPorLinha(atendente, "BSB-001");
        System.out.println();
        servicoConsulta.listarPorHorario(admin, 6, 0);
        System.out.println();
        servicoConsulta.exibirDetalhesViagem(passageiro1, v1.getId());

        // =====================================================
        separador("US05 - RESERVA DE PASSAGEM");
        // =====================================================

        Bilhete b1 = servicoReserva.reservar(passageiro1, v1, 1);
        @SuppressWarnings("unused")
        Bilhete b2 = servicoReserva.reservar(passageiro2, v1, 2);

        // Assento ja ocupado
        try {
            servicoReserva.reservar(passageiro3, v1, 1);
        } catch (IllegalStateException e) {
            System.out.println("[BLOQUEADO] " + e.getMessage());
        }

        // =====================================================
        separador("US06 - LISTA DE ESPERA");
        // =====================================================

        // Lotar v1 (tem 4 assentos)
        servicoReserva.reservar(passageiro3, v1, 3);
        servicoReserva.reservar(passageiro1, v1, 4);

        // Viagem lotada — proximos vao para lista de espera
        servicoReserva.reservar(passageiro2, v1, 1);
        servicoReserva.reservar(passageiro3, v1, 2);

        servicoReserva.exibirListaEspera(v1.getId());

        // =====================================================
        separador("US07 - CANCELAMENTO DE RESERVA");
        // =====================================================

        System.out.println("Cancelando reserva de " + passageiro1.getNome() + " (assento 1)...");
        servicoReserva.cancelar(passageiro1, b1.getReserva().getId());

        System.out.println("\nLista de espera apos cancelamento:");
        servicoReserva.exibirListaEspera(v1.getId());

        // =====================================================
        separador("US08 - RESERVA DE MULTIPLOS ASSENTOS");
        // =====================================================

        List<Integer> assentosGrupo = Arrays.asList(1, 2, 3);
        List<Bilhete> bilhetesGrupo = servicoReserva.reservarMultiplos(
                atendente, passageiro3, v2, assentosGrupo);
        System.out.println(bilhetesGrupo.size() + " bilhete(s) gerado(s) para o grupo.");
        for (Bilhete b : bilhetesGrupo) System.out.println(b);

        // Passageiro tentando reservar multiplos (sem permissao)
        try {
            servicoReserva.reservarMultiplos(passageiro1, passageiro1, v3, Arrays.asList(1, 2));
        } catch (SecurityException e) {
            System.out.println("[BLOQUEADO] " + e.getMessage());
        }

        // =====================================================
        separador("RESUMO FINAL - RESERVAS POR PASSAGEIRO");
        // =====================================================

        servicoReserva.exibirReservasPassageiro(passageiro1);
        System.out.println();
        servicoReserva.exibirReservasPassageiro(passageiro2);
        System.out.println();
        servicoReserva.exibirReservasPassageiro(passageiro3);

        separador("SISTEMA ENCERRADO COM SUCESSO");
    }

    private static void separador(String titulo) {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("  " + titulo);
        System.out.println("=".repeat(55) + "\n");
    }
}
