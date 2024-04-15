package app;

import com.rabbitmq.client.DeliverCallback;
import services.manager.Operacao;
import services.mq.RabbitMQCliente;
import utils.RabbitMQConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A classe Broker representa uma corretora que interage com a Bolsa de Valores para enviar operações
 * e processar respostas de operações enviadas.
 */
public class Broker {

    private RabbitMQCliente rabbitMQCliente;
    private String nomeBroker;

    /**
     * Cria uma instância de Broker com o nome da corretora especificado.
     *
     * @param nomeBroker O nome da corretora.
     * @throws IOException Em caso de erro de comunicação com o RabbitMQ.
     * @throws Exception Em caso de outros erros ao inicializar os componentes.
     */
    public Broker(String nomeBroker) throws IOException, Exception {
        // Inicializa o cliente RabbitMQ
        rabbitMQCliente = new RabbitMQCliente();

        // Define o nome do Broker
        this.nomeBroker = nomeBroker;

        // Configura o recebimento de mensagens da fila FILA_BROKER
        configurarRecebimentoMensagens();
    }

    /**
     * Configura o recebimento de mensagens da fila FILA_BROKER para processar respostas.
     *
     * @throws IOException Em caso de erro de comunicação com o RabbitMQ.
     */
    private void configurarRecebimentoMensagens() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String routingKey = delivery.getEnvelope().getRoutingKey();

            // Processa a mensagem recebida com base na chave de roteamento
            processarResposta(routingKey, mensagem);
        };

        // Recebe mensagens da fila FILA_BROKER
        rabbitMQCliente.receberMensagem(RabbitMQConfig.FILA_BROKER, deliverCallback, "Broker");
        System.out.println("[" + nomeBroker + "] Recebendo mensagens da fila " + RabbitMQConfig.FILA_BROKER);
    }

    /**
     * Processa respostas recebidas da Bolsa de Valores com base na chave de roteamento.
     *
     * @param routingKey A chave de roteamento da mensagem.
     * @param mensagem A mensagem recebida.
     */
    private void processarResposta(String routingKey, String mensagem) {
        System.out.println("[" + nomeBroker + "] Resposta recebida: " + routingKey + " - " + mensagem);
        // Adicione lógica adicional para lidar com a resposta recebida, se necessário
    }

    /**
     * Envia uma operação para a Bolsa de Valores.
     *
     * @param operacao A operação a ser enviada.
     * @throws IOException Em caso de erro ao enviar a operação.
     */
    public void enviarOperacao(Operacao operacao) throws IOException {
        String tipoOperacao = operacao.getTipo().name();
        String ativo = operacao.getAtivo();
        String routingKey = tipoOperacao + "." + ativo;

        // Constrói a mensagem a partir dos dados da operação
        String mensagem = construirMensagem(operacao);

        System.out.println("\n\nMensagem: " + mensagem + "\n\n");

        // Envia a mensagem para a fila FILA_BOLSADEVALORES
        rabbitMQCliente.enviarMensagem(
                RabbitMQConfig.FILA_BOLSADEVALORES,
                routingKey,
                mensagem);

        //System.out.println("[" + nomeBroker + "] Enviada operação: " + routingKey + " - " + mensagem);
    }

    /**
     * Constrói uma mensagem a partir dos dados da operação.
     *
     * @param operacao A operação para a qual a mensagem será construída.
     * @return Uma string representando a mensagem da operação no formato "<ativo>,<quantidade>,<valor>,<corretora>".
     */
    private String construirMensagem(Operacao operacao) {
        // Construir a mensagem com base no layout: "<ativo>,<quantidade>,<valor>,<corretora>"
        return operacao.getAtivo() + "-" +
                operacao.getQuantidade() + "-" +
                operacao.getValor() + "-" +
                operacao.getCorretora();
    }
}
