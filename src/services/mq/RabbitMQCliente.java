package services.mq;

import com.rabbitmq.client.*;

import utils.RabbitMQConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Classe que representa um cliente RabbitMQ, responsável por estabelecer a conexão
 * com o servidor RabbitMQ, enviar mensagens para uma fila específica e receber
 * mensagens de uma fila específica.
 */
public class RabbitMQCliente {

    private Connection connection;
    private Channel channel;

    /**
     * Construtor que configura a conexão com o servidor RabbitMQ utilizando
     * as configurações definidas em RabbitMQConfig.
     *
     * @throws IOException       se ocorrer um erro de E/S ao estabelecer a conexão ou criar o canal.
     * @throws TimeoutException  se ocorrer um tempo limite ao tentar estabelecer a conexão.
     */
    public RabbitMQCliente() throws IOException, TimeoutException {
        // Configura a conexão com o RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfig.HOST);
        factory.setPort(RabbitMQConfig.PORT);
        factory.setUsername(RabbitMQConfig.USERNAME);
        factory.setPassword(RabbitMQConfig.PASSWORD);
        factory.setVirtualHost(RabbitMQConfig.VIRTUAL_HOST);

        // Cria a conexão e o canal
        connection = factory.newConnection();
        channel = connection.createChannel();

        // Declara as filas e os tópicos
        channel.queueDeclare(RabbitMQConfig.FILA_BROKER, true, false, false, null);
        channel.queueDeclare(RabbitMQConfig.FILA_BOLSADEVALORES, true, false, false, null);

        channel.exchangeDeclare(RabbitMQConfig.LDAMD_EXCHANGE, BuiltinExchangeType.TOPIC);
    }

    /**
     * Envia uma mensagem para a fila especificada com a chave de roteamento dada.
     *
     * @param fila        A fila para a qual a mensagem deve ser enviada.
     * @param routingKey  A chave de roteamento para direcionar a mensagem.
     * @param mensagem    A mensagem a ser enviada para a fila.
     * @throws IOException se ocorrer um erro de E/S ao enviar a mensagem.
     */
    public void enviarMensagem(String fila, String routingKey, String mensagem) throws IOException {
        // Publica a mensagem no tópico correspondente
        channel.basicPublish(RabbitMQConfig.LDAMD_EXCHANGE, routingKey, null, mensagem.getBytes(StandardCharsets.UTF_8));
        System.out.println("Enviado '" + routingKey + "'-> '" + mensagem + "'");
    }

    /**
     * Recebe mensagens de uma fila específica usando um callback para processá-las.
     *
     * @param queueName       O nome da fila de onde as mensagens devem ser recebidas.
     * @param deliverCallback O callback para processar mensagens recebidas.
     * @param local           Indica a origem (BolsaDeValores ou outra) para configurar corretamente a fila.
     * @throws IOException se ocorrer um erro de E/S ao configurar o recebimento de mensagens.
     */
    public void receberMensagem(String queueName, DeliverCallback deliverCallback, String local) throws IOException {
        if (local.equals("BolsaDeValores")) {
            // Faz o bind da fila com o tópico correspondente
            channel.queueBind(queueName, RabbitMQConfig.LDAMD_EXCHANGE, "#");
        
            // Inicia o consumo de mensagens da fila especificada
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } else {
            // Inicia o consumo de mensagens da fila especificada
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        }
    }

    /**
     * Fecha a conexão e o canal com o servidor RabbitMQ.
     *
     * @throws IOException      se ocorrer um erro de E/S ao fechar a conexão ou o canal.
     * @throws TimeoutException se ocorrer um tempo limite ao fechar a conexão ou o canal.
     */
    public void fecharConexao() throws IOException, TimeoutException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
