# Sistema de Bolsa de Valores com RabbitMQ

Este projeto consiste no desenvolvimento de um sistema de Bolsa de Valores utilizando RabbitMQ para a comunicação entre a Bolsa de Valores e Brokers. O sistema permite que Brokers enviem operações de compra ou venda à Bolsa de Valores, que gerencia o livro de ofertas e encaminha as operações aos Brokers interessados.

## Descrição Detalhada do Trabalho

O trabalho é dividido em duas partes: uma para a Bolsa de Valores e outra para o Broker. Ambos se comunicam através de RabbitMQ utilizando filas de mensagens e tópicos.

Os requisitos do trabalho são:

1. **Configuração do RabbitMQ**: Utilize um servidor RabbitMQ configurado na nuvem, como uma máquina gratuita do tipo Little Lemur - For Development do site [https://www.cloudamqp.com/](https://www.cloudamqp.com/).

2. **Comunicação entre Broker e Bolsa de Valores**: A bolsa de valores deve ter um canal pub/sub usando tópicos para publicar atualizações no livro de ofertas e operações realizadas em uma ação. O canal deve se chamar `BOLSADEVALORES`.

3. **Filas de Mensagens**:
    - A fila `BROKER` é usada para receber operações dos Brokers.
    - A fila `BOLSADEVALORES` é usada para notificar os Brokers das operações.

4. **Threads em Java**: Utilize threads para implementar a lógica do sistema.

5. **Servidores diferentes**: O servidor do sistema deve ser disponibilizado em uma máquina diferente de localhost.

6. **Execução em ambiente Linux**: O aplicativo deve funcionar nas máquinas Linux do laboratório de redes do curso de Engenharia de Software da PUC Minas.

## Descrição da Arquitetura

- **Bolsa de Valores**: A parte da Bolsa de Valores gerencia as operações recebidas dos Brokers, armazena no livro de ofertas e publica atualizações e transações realizadas em tópicos RabbitMQ.

- **Broker**: Os Brokers enviam operações à Bolsa de Valores usando a fila `BOLSADEVALORES` e assinam tópicos para receber notificações sobre operações em que estão interessados através da fila `BROKER`.

- **Livro de Ofertas**: O Livro de Ofertas armazena ordens de compra e venda, permitindo que a Bolsa de Valores gerencie e execute transações.

## Descrição da Arquitetura RabbitMQ

- **Filas**:
    - `BOLSADEVALORES`: Fila onde a bolsa recebe operações de compra e venda dos Brokers.
    - `BROKER`: Fila onde a bolsa enviar atualizações sobre operações aos Brokers.

- **Tópicos**:
    - `compra.ativo`: Tópico de ordens de compra de um ativo específico.
    - `venda.ativo`: Tópico de ordens de venda de um ativo específico.

## Ferramentas Necessárias

- **Java Development Kit**: Versão 8 ou superior.
- **RabbitMQ**: Um servidor RabbitMQ configurado na nuvem, conforme especificado nos requisitos.

## Instruções para Executar o Trabalho

1. **Configurar o RabbitMQ**: Obtenha as credenciais e configurações do servidor RabbitMQ.

2. **Compilar o projeto**: Execute o script `compile.sh` na raiz do projeto para compilar os arquivos Java.

    ```shell
    ./scripts/compile.sh
    ```

3. **Executar a Bolsa de Valores**: Em um terminal, execute o aplicativo da Bolsa de Valores usando o script `run.sh` com o argumento `AppBolsaDeValores`.

    ```shell
    ./scripts/run.sh AppBolsaDeValores
    ```

4. **Executar o Broker**: Em um segundo terminal, execute o aplicativo do Broker usando o script `run.sh` com o argumento `AppBroker`.

    ```shell
    ./scripts/run.sh AppBroker
    ```

5. **Monitorar a execução**: Monitore os terminais para observar o funcionamento do sistema.
