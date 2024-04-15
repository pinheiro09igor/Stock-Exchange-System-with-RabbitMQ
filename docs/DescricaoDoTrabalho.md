# Descrição do Trabalho

Este projeto consiste em desenvolver um sistema para uma bolsa de valores fictícia, semelhante à Bovespa, utilizando RabbitMQ para comunicação entre corretores (brokers) e a bolsa de valores.

## Cenário de Operações

O sistema envolve dois atores principais: o Broker e a Bolsa de Valores. Eles se comunicam por meio de filas e tópicos em RabbitMQ, realizando operações de compra e venda de ações.

- **Corretora (Broker):** Envia ordens de compra ou venda à Bolsa de Valores por meio de filas de mensagens.
- **Bolsa de Valores:** Recebe as ordens de compra e venda dos corretores e realiza transações quando há correspondência nos preços e quantidades.

## Fluxo de Mensagens

- **Ordens de Compra:** Um broker pode enviar uma ordem de compra para a Bolsa de Valores, especificando a quantidade, o valor e a corretora que está fazendo a operação. A mensagem é enviada para o tópico `compra.<ativo>`.

- **Ordens de Venda:** Da mesma forma, um broker pode enviar uma ordem de venda para a Bolsa de Valores. A mensagem é enviada para o tópico `venda.<ativo>`.

- **Respostas da Bolsa de Valores:** A Bolsa de Valores, ao receber uma ordem de compra ou venda, verifica se existe uma correspondência com ordens em sua base de dados. Se houver uma correspondência, ela notifica o broker responsável pela ordem por meio de um tópico.

## Estrutura do Sistema

1. **Comunicação:** A comunicação entre brokers e a Bolsa de Valores é feita utilizando RabbitMQ com filas e tópicos específicos para operações.

2. **Filas:** Duas filas são utilizadas no sistema:
    - `BROKER_queue`: Usada pelos brokers para enviar ordens de compra ou venda à Bolsa de Valores.
    - `BOLSADEVALORES_queue`: Usada pela Bolsa de Valores para enviar atualizações e transações aos brokers.

3. **Tópicos:** As mensagens são publicadas e consumidas através de tópicos com o formato `operacao.ativo`, permitindo uma comunicação mais eficiente entre os atores.

## Regras de Negócio

- **Envio de Ordens:** Os brokers enviam ordens de compra ou venda para a Bolsa de Valores.
- **Assinaturas:** Brokers podem assinar tópicos relacionados aos ativos que desejam acompanhar.
- **Notificações:** A Bolsa de Valores notifica os brokers sobre operações em tópicos relacionados a ativos específicos.
- **Transações:** A Bolsa de Valores gera mensagens de transações quando há correspondência entre ordens de compra e venda para um mesmo ativo, atualizando o livro de ofertas.

## Implementação e Requisitos

- **RabbitMQ:** É necessário configurar um servidor RabbitMQ na nuvem utilizando uma conta gratuita em [CloudAMQP](https://www.cloudamqp.com/).
  
- **Execução:** O projeto é executado em dois terminais: um para a Bolsa de Valores e outro para o Broker.

- **Estrutura de Comunicação:** A estrutura de comunicação consiste em filas e tópicos com nomes específicos, como `compra.<ativo>` e `venda.<ativo>`.

- **Compilação:** Scripts estão disponíveis para a compilação (`compile.sh`) e execução (`run.sh`) do projeto.

O sistema deve ser testado em ambientes Linux e é importante seguir as recomendações de configuração do servidor RabbitMQ. Além disso, threads em Java devem ser utilizadas para garantir uma comunicação eficiente entre as entidades.
