package com.busreservas;

import com.busreservas.estrutura.FilaEstatica;
import com.busreservas.model.*;
import com.busreservas.servico.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Classe de testes do sistema BusReservas.
 *
 * <p>Testa todas as regras de negócio das User Stories US00 a US09,
 * incluindo cenários de sucesso e cenários de erro esperados.</p>
 *
 * @author Grupo 4
 * @version 1.0
 */
public class TesteBusReservas {

    // Contadores de resultado
    private static int passou = 0;
    private static int falhou = 0;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         TESTES DO SISTEMA BUSRESERVAS            ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        testarFilaEstatica();
        testarPerfisDeAcesso();
        testarCadastroLinhas();
        testarHorarios();
        testarViagens();
        testarReservas();
        testarListaEspera();
        testarCancelamento();
        testarMultiplosAssentos();
        testarConsultas();

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.printf( "║  RESULTADO FINAL: %d passou | %d falhou           ║%n", passou, falhou);
        System.out.println("╚══════════════════════════════════════════════════╝");

        if (falhou > 0) System.exit(1);
    }

    // ── helpers ──────────────────────────────────────────────────────────

    static void titulo(String s) {
        System.out.println("\n── " + s + " " + "─".repeat(Math.max(0, 46 - s.length())));
    }

    static void ok(String descricao) {
        System.out.println("  [PASSOU] " + descricao);
        passou++;
    }

    static void falha(String descricao) {
        System.out.println("  [FALHOU] " + descricao);
        falhou++;
    }

    static void assertVerdadeiro(boolean condicao, String msg) {
        if (condicao) ok(msg); else falha(msg);
    }

    static void assertFalso(boolean condicao, String msg) {
        if (!condicao) ok(msg); else falha(msg);
    }

    static void assertIgual(Object esperado, Object obtido, String msg) {
        boolean igual = (esperado == null) ? (obtido == null) : esperado.equals(obtido);
        if (igual) ok(msg);
        else falha(msg + " | Esperado: " + esperado + " | Obtido: " + obtido);
    }

    static void assertLanca(Runnable acao, Class<? extends Exception> tipo, String msg) {
        try {
            acao.run();
            falha(msg + " (excecao nao lancada)");
        } catch (Exception e) {
            if (tipo.isInstance(e)) ok(msg);
            else falha(msg + " (tipo errado: " + e.getClass().getSimpleName() + ")");
        }
    }

    // ── TESTES DE FILA ESTÁTICA ──────────────────────────────────────────

    static void testarFilaEstatica() {
        titulo("US06 - FilaEstatica<T>");

        FilaEstatica<String> fila = new FilaEstatica<>(3);

        assertVerdadeiro(fila.isEmpty(), "Fila recém-criada deve estar vazia");
        assertFalso(fila.isFull(), "Fila recém-criada não deve estar cheia");
        assertIgual(0, fila.getTamanho(), "Tamanho inicial deve ser 0");

        assertVerdadeiro(fila.enqueue("A"), "enqueue(A) deve retornar true");
        assertVerdadeiro(fila.enqueue("B"), "enqueue(B) deve retornar true");
        assertVerdadeiro(fila.enqueue("C"), "enqueue(C) deve retornar true");
        assertFalso(fila.enqueue("D"), "enqueue quando cheia deve retornar false");
        assertVerdadeiro(fila.isFull(), "Fila deve estar cheia após 3 inserções");
        assertIgual(3, fila.getTamanho(), "Tamanho deve ser 3");

        assertIgual("A", fila.peek(), "peek() deve retornar A sem remover");
        assertIgual(3, fila.getTamanho(), "Tamanho não deve mudar após peek");

        assertIgual("A", fila.dequeue(), "dequeue() deve retornar A (FIFO)");
        assertIgual("B", fila.dequeue(), "dequeue() deve retornar B (FIFO)");
        assertIgual(1, fila.getTamanho(), "Tamanho deve ser 1 após 2 dequeues");

        assertVerdadeiro(fila.enqueue("D"), "enqueue após dequeue deve funcionar (circular)");
        assertIgual("C", fila.dequeue(), "dequeue() deve retornar C");
        assertIgual("D", fila.dequeue(), "dequeue() deve retornar D");
        assertVerdadeiro(fila.isEmpty(), "Fila deve estar vazia após todos dequeues");
        assertIgual(null, fila.dequeue(), "dequeue em fila vazia deve retornar null");

        // Teste de posição
        FilaEstatica<String> fila2 = new FilaEstatica<>(5);
        fila2.enqueue("X");
        fila2.enqueue("Y");
        fila2.enqueue("Z");
        assertIgual(1, fila2.getPosicao("X"), "Posição de X deve ser 1");
        assertIgual(2, fila2.getPosicao("Y"), "Posição de Y deve ser 2");
        assertIgual(3, fila2.getPosicao("Z"), "Posição de Z deve ser 3");
        assertIgual(-1, fila2.getPosicao("W"), "Posição de W (inexistente) deve ser -1");

        // Teste de remoção por elemento
        assertVerdadeiro(fila2.remover("Y"), "remover(Y) deve retornar true");
        assertIgual(2, fila2.getTamanho(), "Tamanho após remover Y deve ser 2");
        assertIgual("X", fila2.dequeue(), "Após remover Y, dequeue deve retornar X");
        assertIgual("Z", fila2.dequeue(), "Após remover Y, próximo deve ser Z");

        // Capacidade inválida
        assertLanca(() -> new FilaEstatica<>(0), IllegalArgumentException.class,
                "Capacidade 0 deve lançar IllegalArgumentException");
    }

    // ── TESTES DE PERFIS ─────────────────────────────────────────────────

    static void testarPerfisDeAcesso() {
        titulo("US00 - Perfis de Acesso");

        Usuario admin = new Usuario(1, "Admin", "a@a.com", "123", PerfilUsuario.ADMINISTRADOR);
        Usuario atendente = new Usuario(2, "Atend", "b@b.com", "123", PerfilUsuario.ATENDENTE);
        Passageiro passageiro = new Passageiro(3, "Pass", "c@c.com", "123", "000.000.000-00", "77999990000");

        assertVerdadeiro(admin.podeGerenciarLinhas(), "Admin pode gerenciar linhas");
        assertFalso(atendente.podeGerenciarLinhas(), "Atendente não pode gerenciar linhas");
        assertFalso(passageiro.podeGerenciarLinhas(), "Passageiro não pode gerenciar linhas");

        assertVerdadeiro(atendente.podeReservarPassagem(), "Atendente pode reservar");
        assertVerdadeiro(passageiro.podeReservarPassagem(), "Passageiro pode reservar");
        assertFalso(admin.podeReservarPassagem(), "Admin não pode reservar diretamente");

        assertVerdadeiro(atendente.podeReservarMultiplosAssentos(), "Atendente pode reservar múltiplos");
        assertFalso(passageiro.podeReservarMultiplosAssentos(), "Passageiro não pode reservar múltiplos");

        assertVerdadeiro(admin.podeCancelarReserva(), "Admin pode cancelar");
        assertVerdadeiro(passageiro.podeCancelarReserva(), "Passageiro pode cancelar");

        // Autenticação
        ServicoUsuario su = new ServicoUsuario();
        assertLanca(() -> su.autenticar("errado@email.com", "wrong"),
                SecurityException.class, "Login inválido deve lançar SecurityException");
    }

    // ── TESTES DE LINHAS ─────────────────────────────────────────────────

    static void testarCadastroLinhas() {
        titulo("US01 - Cadastro de Linhas");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        Usuario admin = su.autenticar("admin@bus.com", "admin123");
        Usuario atendente = su.autenticar("joao@bus.com", "atend123");

        Rota rota = sl.cadastrarRota(admin, "Brasilia", "Sao Paulo", 1015.0, 720);
        LinhaOnibus linha = sl.cadastrarLinha(admin, "TST-001", "Teste", rota, 100.0);

        assertVerdadeiro(linha != null, "Linha deve ser criada com sucesso");
        assertIgual("TST-001", linha.getCodigo(), "Código deve ser TST-001");

        assertLanca(() -> sl.cadastrarLinha(admin, "TST-001", "Dup", rota, 50.0),
                IllegalArgumentException.class, "Código duplicado deve lançar exceção");

        assertLanca(() -> sl.cadastrarLinha(atendente, "TST-002", "X", rota, 50.0),
                SecurityException.class, "Atendente não pode cadastrar linha");

        assertLanca(() -> sl.cadastrarLinha(admin, "", "X", rota, 50.0),
                IllegalArgumentException.class, "Código vazio deve lançar exceção");
    }

    // ── TESTES DE HORARIOS ───────────────────────────────────────────────

    static void testarHorarios() {
        titulo("US02 - Horários");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        Usuario admin = su.autenticar("admin@bus.com", "admin123");

        Rota rota = sl.cadastrarRota(admin, "A", "B", 100.0, 60);
        LinhaOnibus linha = sl.cadastrarLinha(admin, "HOR-001", "Teste", rota, 50.0);

        Horario h = sl.adicionarHorario(admin, linha, "08:00");
        assertVerdadeiro(h != null, "Horário 08:00 deve ser adicionado");
        assertIgual("08:00", h.getHora(), "Hora deve ser 08:00");

        assertLanca(() -> sl.adicionarHorario(admin, linha, "08:00"),
                IllegalArgumentException.class, "Horário duplicado deve lançar exceção");

        assertLanca(() -> new Horario(99, "25:00"),
                IllegalArgumentException.class, "Hora 25:00 é inválida");

        assertLanca(() -> new Horario(99, "12:60"),
                IllegalArgumentException.class, "Minuto 60 é inválido");

        assertLanca(() -> new Horario(99, "abc"),
                IllegalArgumentException.class, "Formato inválido deve lançar exceção");
    }

    // ── TESTES DE VIAGENS ────────────────────────────────────────────────

    static void testarViagens() {
        titulo("US03/US04 - Viagens e Assentos");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        ServicoViagem sv = new ServicoViagem();
        Usuario admin = su.autenticar("admin@bus.com", "admin123");

        Rota rota = sl.cadastrarRota(admin, "X", "Y", 200.0, 120);
        LinhaOnibus linha = sl.cadastrarLinha(admin, "VIA-001", "Teste", rota, 80.0);
        Horario h = sl.adicionarHorario(admin, linha, "10:00");

        LocalDate amanha = LocalDate.now().plusDays(1);
        Viagem v = sv.cadastrarViagem(admin, linha, h, amanha, 5);

        assertVerdadeiro(v != null, "Viagem deve ser criada");
        assertIgual(5, v.getCapacidadeTotal(), "Capacidade deve ser 5");
        assertIgual(5, v.getQuantidadeAssentosDisponiveis(), "Todos assentos devem estar livres");
        assertFalso(v.isLotada(), "Viagem não deve estar lotada");

        // Duplicata
        assertLanca(() -> sv.cadastrarViagem(admin, linha, h, amanha, 5),
                IllegalArgumentException.class, "Viagem duplicada deve lançar exceção");

        // Data passada
        assertLanca(() -> sv.cadastrarViagem(admin, linha, h, LocalDate.now().minusDays(1), 5),
                IllegalArgumentException.class, "Data passada deve lançar exceção");

        // Assento inválido
        assertIgual(null, v.buscarAssento(99), "Assento 99 não existe");
        assertVerdadeiro(v.buscarAssento(1).isDisponivel(), "Assento 1 deve estar disponível");
    }

    // ── TESTES DE RESERVAS ───────────────────────────────────────────────

    static void testarReservas() {
        titulo("US05 - Reserva de Passagem");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        ServicoViagem sv = new ServicoViagem();
        ServicoReserva sr = new ServicoReserva();
        Usuario admin = su.autenticar("admin@bus.com", "admin123");

        Rota rota = sl.cadastrarRota(admin, "P", "Q", 300.0, 180);
        LinhaOnibus linha = sl.cadastrarLinha(admin, "RES-001", "Teste", rota, 120.0);
        Horario h = sl.adicionarHorario(admin, linha, "14:00");
        Viagem v = sv.cadastrarViagem(admin, linha, h, LocalDate.now().plusDays(2), 3);

        Passageiro p1 = su.cadastrarPassageiro("João", "joao@t.com", "123", "001.001.001-01", "77900000001");
        Passageiro p2 = su.cadastrarPassageiro("Maria", "maria@t.com", "123", "002.002.002-02", "77900000002");

        Bilhete b1 = sr.reservar(p1, v, 1);
        assertVerdadeiro(b1 != null, "Bilhete deve ser gerado após reserva");
        assertVerdadeiro(b1.getCodigoBilhete().startsWith("BLT-"), "Código do bilhete deve começar com BLT-");
        assertFalso(v.buscarAssento(1).isDisponivel(), "Assento 1 deve estar ocupado");
        assertIgual(2, v.getQuantidadeAssentosDisponiveis(), "Devem restar 2 assentos");

        // Assento já ocupado
        assertLanca(() -> sr.reservar(p2, v, 1),
                IllegalStateException.class, "Reservar assento ocupado deve lançar exceção");

        // Assento inexistente
        assertLanca(() -> sr.reservar(p2, v, 99),
                IllegalArgumentException.class, "Assento 99 inexistente deve lançar exceção");

        // Reserva do p2 no assento 2
        Bilhete b2 = sr.reservar(p2, v, 2);
        assertVerdadeiro(b2 != null, "p2 deve conseguir reservar assento 2");
        assertIgual(1, (int) p2.getMinhasReservas().size(),
                "p2 deve ter 1 reserva");
    }

    // ── TESTES DE LISTA DE ESPERA ─────────────────────────────────────────

    static void testarListaEspera() {
        titulo("US06 - Lista de Espera");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        ServicoViagem sv = new ServicoViagem();
        ServicoReserva sr = new ServicoReserva();
        Usuario admin = su.autenticar("admin@bus.com", "admin123");

        Rota rota = sl.cadastrarRota(admin, "L", "M", 100.0, 60);
        LinhaOnibus linha = sl.cadastrarLinha(admin, "ESP-001", "Teste", rota, 50.0);
        Horario h = sl.adicionarHorario(admin, linha, "16:00");
        Viagem v = sv.cadastrarViagem(admin, linha, h, LocalDate.now().plusDays(3), 1);

        Passageiro p1 = su.cadastrarPassageiro("Ana", "ana@t.com", "123", "003.003.003-03", "77900000003");
        Passageiro p2 = su.cadastrarPassageiro("Carlos", "carlos@t.com", "123", "004.004.004-04", "77900000004");
        Passageiro p3 = su.cadastrarPassageiro("Diana", "diana@t.com", "123", "005.005.005-05", "77900000005");

        // Reserva o único assento
        Bilhete b1 = sr.reservar(p1, v, 1);
        assertVerdadeiro(b1 != null, "p1 deve reservar o único assento");
        assertVerdadeiro(v.isLotada(), "Viagem deve estar lotada");

        // p2 e p3 vão para a lista de espera
        Bilhete b2 = sr.reservar(p2, v, 1);
        assertIgual(null, b2, "p2 deve ir para lista de espera (bilhete null)");

        Bilhete b3 = sr.reservar(p3, v, 1);
        assertIgual(null, b3, "p3 deve ir para lista de espera (bilhete null)");

        ListaEspera lista = v.getListaEspera();
        assertIgual(2, lista.getTamanho(), "Lista de espera deve ter 2 entradas");
        assertIgual(1, lista.getPosicao(p2), "p2 deve estar na posição 1");
        assertIgual(2, lista.getPosicao(p3), "p3 deve estar na posição 2");
    }

    // ── TESTES DE CANCELAMENTO ────────────────────────────────────────────

    static void testarCancelamento() {
        titulo("US07 - Cancelamento e Promoção Automática");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        ServicoViagem sv = new ServicoViagem();
        ServicoReserva sr = new ServicoReserva();
        Usuario admin = su.autenticar("admin@bus.com", "admin123");

        Rota rota = sl.cadastrarRota(admin, "C", "D", 150.0, 90);
        LinhaOnibus linha = sl.cadastrarLinha(admin, "CAN-001", "Teste", rota, 70.0);
        Horario h = sl.adicionarHorario(admin, linha, "18:00");
        Viagem v = sv.cadastrarViagem(admin, linha, h, LocalDate.now().plusDays(4), 1);

        Passageiro p1 = su.cadastrarPassageiro("Eva", "eva@t.com", "123", "006.006.006-06", "77900000006");
        Passageiro p2 = su.cadastrarPassageiro("Fábio", "fabio@t.com", "123", "007.007.007-07", "77900000007");

        Bilhete b1 = sr.reservar(p1, v, 1);
        sr.reservar(p2, v, 1); // vai para espera

        assertVerdadeiro(v.isLotada(), "Viagem deve estar lotada");
        assertIgual(1, v.getListaEspera().getTamanho(), "Lista deve ter 1 entrada");

        // Cancelar reserva de p1 — p2 deve ser promovido automaticamente
        boolean cancelou = sr.cancelar(p1, b1.getReserva().getId());
        assertVerdadeiro(cancelou, "Cancelamento deve retornar true");
        assertIgual(Reserva.StatusReserva.CANCELADA, b1.getReserva().getStatus(),
                "Status da reserva deve ser CANCELADA");
        assertVerdadeiro(v.buscarAssento(1).isDisponivel() == false,
                "Assento deve ter sido promovido para p2");
        assertIgual(0, v.getListaEspera().getTamanho(),
                "Lista de espera deve estar vazia após promoção");

        // Tentativa de cancelar reserva de outro passageiro
        Passageiro p3 = su.cadastrarPassageiro("Gabi", "gabi@t.com", "123", "008.008.008-08", "77900000008");
        assertLanca(() -> sr.cancelar(p3, b1.getReserva().getId()),
                SecurityException.class, "Cancelar reserva de outro deve lançar SecurityException");
    }

    // ── TESTES DE MÚLTIPLOS ASSENTOS ─────────────────────────────────────

    static void testarMultiplosAssentos() {
        titulo("US08 - Reserva de Múltiplos Assentos");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        ServicoViagem sv = new ServicoViagem();
        ServicoReserva sr = new ServicoReserva();
        Usuario admin = su.autenticar("admin@bus.com", "admin123");
        Usuario atendente = su.autenticar("joao@bus.com", "atend123");

        Rota rota = sl.cadastrarRota(admin, "E", "F", 200.0, 120);
        LinhaOnibus linha = sl.cadastrarLinha(admin, "MUL-001", "Teste", rota, 90.0);
        Horario h = sl.adicionarHorario(admin, linha, "20:00");
        Viagem v = sv.cadastrarViagem(admin, linha, h, LocalDate.now().plusDays(5), 5);

        Passageiro p = su.cadastrarPassageiro("Hélio", "helio@t.com", "123", "009.009.009-09", "77900000009");

        List<Integer> assentos = Arrays.asList(1, 2, 3);
        List<Bilhete> bilhetes = sr.reservarMultiplos(atendente, p, v, assentos);

        assertIgual(3, bilhetes.size(), "Devem ser gerados 3 bilhetes");
        assertIgual(2, v.getQuantidadeAssentosDisponiveis(), "Devem restar 2 assentos");

        // Passageiro tentando reservar múltiplos (sem permissão)
        assertLanca(() -> sr.reservarMultiplos(p, p, v, Arrays.asList(4, 5)),
                SecurityException.class, "Passageiro não pode reservar múltiplos");

        // Mais assentos do que disponíveis
        assertLanca(() -> sr.reservarMultiplos(atendente, p, v, Arrays.asList(4, 5, 6)),
                IllegalStateException.class, "Solicitar mais que disponível deve lançar exceção");
    }

    // ── TESTES DE CONSULTAS ───────────────────────────────────────────────

    static void testarConsultas() {
        titulo("US09 - Consultas e Filtros");

        ServicoUsuario su = new ServicoUsuario();
        ServicoLinha sl = new ServicoLinha();
        ServicoViagem sv = new ServicoViagem();
        ServicoConsulta sc = new ServicoConsulta(sv);
        Usuario admin = su.autenticar("admin@bus.com", "admin123");

        Rota r1 = sl.cadastrarRota(admin, "G", "Sao Paulo", 300.0, 180);
        Rota r2 = sl.cadastrarRota(admin, "H", "Rio de Janeiro", 400.0, 240);

        LinhaOnibus l1 = sl.cadastrarLinha(admin, "CON-001", "G-SP", r1, 110.0);
        LinhaOnibus l2 = sl.cadastrarLinha(admin, "CON-002", "H-RJ", r2, 130.0);

        Horario h1 = sl.adicionarHorario(admin, l1, "07:00");
        Horario h2 = sl.adicionarHorario(admin, l2, "09:00");

        LocalDate d1 = LocalDate.now().plusDays(10);
        LocalDate d2 = LocalDate.now().plusDays(11);

        sv.cadastrarViagem(admin, l1, h1, d1, 10);
        sv.cadastrarViagem(admin, l2, h2, d2, 10);
        sv.cadastrarViagem(admin, l1, h1, d2, 10);

        List<Viagem> porDestino = sv.filtrarPorDestino("Sao Paulo");
        assertIgual(2, porDestino.size(), "Filtro por 'Sao Paulo' deve retornar 2 viagens");

        List<Viagem> porData = sv.filtrarPorData(d1);
        assertIgual(1, porData.size(), "Filtro por d1 deve retornar 1 viagem");

        List<Viagem> porLinha = sv.filtrarPorLinha("CON-001");
        assertIgual(2, porLinha.size(), "Filtro por CON-001 deve retornar 2 viagens");

        List<Viagem> porHorario = sv.filtrarPorHorario(7, 0);
        assertIgual(2, porHorario.size(), "Filtro por 07:00 deve retornar 2 viagens");

        // Filtro sem resultados
        List<Viagem> semResultado = sv.filtrarPorDestino("Manaus");
        assertIgual(0, semResultado.size(), "Filtro por 'Manaus' deve retornar 0 viagens");

        // Consulta sem permissão
        Usuario semPerfil = new Usuario(99, "X", "x@x.com", "x", null);
        assertLanca(() -> sc.listarViagensDisponiveis(semPerfil),
                Exception.class, "Consulta sem perfil deve lançar exceção");
    }
}
