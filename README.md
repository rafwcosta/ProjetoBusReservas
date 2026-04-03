# Sistema de Reserva de Passagens de Ônibus

## Descrição

Este projeto implementa um **sistema de reserva de passagens de ônibus** desenvolvido em Java.
O objetivo é simular o funcionamento de um sistema de transporte rodoviário, permitindo o gerenciamento de passageiros, viagens, assentos e reservas.

O projeto foi desenvolvido para fins acadêmicos, aplicando conceitos de:

* Estrutura de Dados
* Programação Orientada a Objetos
* Organização de sistemas em camadas

---

# Tecnologias Utilizadas

* Java
* Programação Orientada a Objetos
* Estruturas de Dados (Fila Estática)

---

# Estrutura do Projeto

O projeto está organizado no pacote principal:

com.busreservas

Dentro dele existem subpacotes responsáveis por diferentes partes do sistema.

## Pacote model

Contém as entidades principais do sistema.

Classes:

* `Usuario` → representa um usuário do sistema
* `PerfilUsuario` → define o tipo de usuário
* `Passageiro` → representa um passageiro
* `LinhaOnibus` → representa uma linha de transporte
* `Rota` → define origem e destino da viagem
* `Horario` → define data e horário da viagem
* `Assento` → representa um assento do ônibus
* `Reserva` → representa uma reserva de passagem
* `Bilhete` → representa o bilhete da viagem
* `Viagem` → representa uma viagem específica
* `ListaEspera` → representa a lista de passageiros aguardando vaga
* `Sistema` → classe central que controla o sistema

---

## Pacote estrutura

Contém a implementação de estruturas de dados utilizadas pelo sistema.

### FilaEstatica

Implementação de uma fila baseada em vetor que segue o modelo:

FIFO — First In, First Out.

Essa estrutura é utilizada para controlar a **fila de espera de passageiros quando os assentos da viagem estão ocupados**.

---

## Pacote servico

Contém as classes responsáveis pela lógica de negócio do sistema.

Classes:

* `ServicoReserva` → gerencia reservas
* `ServicoLinha` → gerencia linhas de ônibus
* `ServicoViagem` → gerencia viagens
* `ServicoUsuario` → gerencia usuários
* `ServicoConsulta` → realiza consultas no sistema

Essas classes separam a lógica de funcionamento das entidades do sistema.

---

## Classe Principal

A classe principal do sistema é:

Main.java

Localização:

com.busreservas.Main

Essa classe executa o sistema e inicia as operações principais.

---

# Funcionalidades do Sistema

O sistema permite:

* Cadastro de usuários
* Cadastro de passageiros
* Criação de linhas de ônibus
* Criação de viagens
* Reserva de assentos
* Cancelamento de reservas
* Controle de assentos disponíveis
* Controle de lista de espera
* Emissão de bilhetes

---

# Estruturas de Dados Utilizadas

O projeto utiliza:

* Vetores
* Fila estática
* Controle de índices
* Estrutura FIFO para lista de espera

---

# Objetivos do Projeto

O principal objetivo deste projeto é aplicar na prática:

* conceitos de **Programação Orientada a Objetos**
* implementação de **estruturas de dados**
* organização de código em **camadas de responsabilidade**
* simulação de um sistema real de reservas

---

# Possíveis Melhorias Futuras

Algumas melhorias que podem ser implementadas:

* Interface gráfica
* Persistência em banco de dados
* API para integração com outros sistemas
* Sistema de autenticação de usuários
* Relatórios de viagens e reservas

---

# Sobre

Projeto desenvolvido para fins acadêmicos na disciplina de Estrutura de Dados.
