╔══════════════════════════════════════════════════════╗
║     SISTEMA BUSRESERVAS - GUIA DE ENTREGA            ║
║     Grupo 4 | Estrutura de Dados em Java             ║
╚══════════════════════════════════════════════════════╝

ESTRUTURA DO ZIP:
─────────────────
1-codigo-fonte/   → Código-fonte Java completo com JavaDoc
2-javadoc/        → Documentação HTML (abrir index.html)
3-testes/         → Classe de testes com 90 casos de teste
4-executavel/     → BusReservas.jar (executável)

COMO EXECUTAR O SISTEMA:
─────────────────────────
java -jar 4-executavel/BusReservas.jar

COMO RODAR OS TESTES:
──────────────────────
java -cp 4-executavel/BusReservas.jar:3-testes/compiled com.busreservas.TesteBusReservas

COMO VER A DOCUMENTAÇÃO:
──────────────────────────
Abrir: 2-javadoc/index.html no navegador

TECNOLOGIAS:
─────────────
Linguagem: Java 21
Estrutura de dados: Fila Estática (FilaEstatica<T>)
Padrão: Camadas (model / estrutura / servico)
Testes: Manual com 90 casos de teste (90/90 passando)

USER STORIES IMPLEMENTADAS:
────────────────────────────
US00 - Controle de Perfis de Usuário     (Uanderson)
US01 - Gerenciamento de Linhas           (Arthur)
US02 - Gerenciamento de Horários         (Bianca)
US03 - Cadastro de Viagens               (Gabriel)
US04 - Controle de Assentos              (Felipe)
US05 - Reserva de Passagem               (Igor)
US06 - Lista de Espera                   (Rafael)
US07 - Cancelamento de Reserva           (Rodolfo)
US08 - Reserva de Múltiplos Assentos     (Victor)
US09 - Consulta de Informações           (Uanderson)
