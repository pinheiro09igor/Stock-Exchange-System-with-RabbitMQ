package utils;

/**
 * Classe utilitária responsável por armazenar configurações relacionadas à
 * conexão com o servidor RabbitMQ, incluindo o endereço do servidor,
 * credenciais, e nomes de filas e tópicos.
 */
public class RabbitMQConfig {
    // Configurações do servidor RabbitMQ
    public static final String HOST = "jackal.rmq.cloudamqp.com"; // Endereço do servidor RabbitMQ
    public static final int PORT = 5672; // Porta do servidor RabbitMQ
    public static final String USERNAME = "bzrycdvq"; // Nome de usuário para autenticação
    public static final String PASSWORD = "cvr5DrQ_exfC7cDMN7IU0l2tf11JS3k4"; // Senha para autenticação
    public static final String VIRTUAL_HOST = "bzrycdvq"; // Virtual Host

    // Configurações de filas
    public static final String FILA_BROKER = "BROKER_queue"; // Nome da fila para o Broker
    public static final String FILA_BOLSADEVALORES = "BOLSADEVALORES_queue"; // Nome da fila para a Bolsa de Valores

    // Configurações de tópicos
    public static final String TOPICO_COMPRA = "compra"; // Tópico para operações de compra
    public static final String TOPICO_VENDA = "venda"; // Tópico para operações de venda
    
    // Exchange
    public static final String LDAMD_EXCHANGE = "ldamd_exchange"; // Nome da exchange
}
