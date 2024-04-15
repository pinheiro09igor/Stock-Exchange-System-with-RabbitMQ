package app;

import com.rabbitmq.client.DeliverCallback;
import services.manager.Operacao;
import services.manager.TipoOperacao;
import services.mq.RabbitMQCliente;
import server.LivroDeOfertas;
import services.history.HistoricoOperacoes;
import utils.RabbitMQConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A classe BolsaDeValores representa o sistema que lida com operações de compra e venda de ativos financeiros.
 * Ela interage com o RabbitMQ para receber operações, processar transações no Livro de Ofertas e registrar o
 * histórico de operações realizadas.
 */
public class BolsaDeValores {

    private RabbitMQCliente rabbitMQCliente;
    private LivroDeOfertas livroDeOfertas;
    private HistoricoOperacoes historicoOperacoes;

    /**
     * Cria uma instância de BolsaDeValores e configura o recebimento de operações pelo RabbitMQ.
     *
     * @throws IOException Em caso de erro de comunicação com o RabbitMQ.
     * @throws Exception Em caso de outros erros ao inicializar os componentes.
     */
    public BolsaDeValores() throws IOException, Exception {
        rabbitMQCliente = new RabbitMQCliente();
        livroDeOfertas = LivroDeOfertas.getInstance();
        historicoOperacoes = HistoricoOperacoes.getInstance();

        configurarRecebimentoOperacoes();
    }

    /**
     * Configura o recebimento de operações pelo RabbitMQ.
     *
     * @throws IOException Em caso de erro de comunicação com o RabbitMQ.
     */
    private void configurarRecebimentoOperacoes() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String routingKey = delivery.getEnvelope().getRoutingKey();

            System.out.println("[BolsaDeValores] Operação recebida: " + routingKey + " - " + mensagem);

            TipoOperacao tipoOperacao = determinarTipoOperacao(routingKey);
            try {
                Operacao operacao = converterMensagemParaOperacao(mensagem, tipoOperacao);
                processarOperacao(operacao, tipoOperacao);
            } catch (Exception e) {
                System.err.println("[BolsaDeValores] Erro ao processar operação: " + e.getMessage());
            }
        };

        rabbitMQCliente.receberMensagem(RabbitMQConfig.FILA_BOLSADEVALORES, deliverCallback, "BolsaDeValores");
        System.out.println("[BolsaDeValores] Ouvindo operações na fila " + RabbitMQConfig.FILA_BOLSADEVALORES);
    }

    /**
     * Determina o tipo de operação (COMPRA ou VENDA) com base na chave de roteamento.
     *
     * @param routingKey A chave de roteamento recebida com a mensagem.
     * @return O tipo de operação correspondente à chave de roteamento.
     */
    private TipoOperacao determinarTipoOperacao(String routingKey) {
        if (routingKey.startsWith("COMPRA.")) {
            return TipoOperacao.COMPRA;
        } else if (routingKey.startsWith("VENDA.")) {
            return TipoOperacao.VENDA;
        } else {
            throw new IllegalArgumentException("Chave de roteamento inválida: " + routingKey);
        }
    }

    /**
     * Converte uma mensagem recebida em uma operação de compra ou venda.
     *
     * @param mensagem A mensagem recebida.
     * @param tipoOperacao O tipo de operação (COMPRA ou VENDA).
     * @return Uma instância de Operação criada com base na mensagem.
     * @throws Exception Se a mensagem não estiver no formato esperado ou contiver valores inválidos.
     */
    private Operacao converterMensagemParaOperacao(String mensagem, TipoOperacao tipoOperacao) throws Exception {
        String[] campos = mensagem.split("-");
        if (campos.length != 4) {
            throw new IllegalArgumentException("Mensagem inválida, formato esperado: 'ativo-quantidade-valor-corretora'");
        }
        String ativo = campos[0];
        int quantidade;
        double valor;
        
        try {
            quantidade = Integer.parseInt(campos[1]);
            valor = Double.parseDouble(campos[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantidade ou valor inválidos na mensagem: " + mensagem);
        }
        
        String corretora = campos[3];

        return new Operacao(tipoOperacao, ativo, quantidade, valor, corretora, LocalDateTime.now());
    }

    /**
     * Processa a operação recebida e negocia com ofertas opostas no Livro de Ofertas.
     *
     * @param operacao A operação recebida.
     * @param tipoOperacao O tipo de operação recebida (COMPRA ou VENDA).
     * @throws IOException Em caso de erro ao adicionar ou remover ofertas no Livro de Ofertas.
     */
    private void processarOperacao(Operacao operacao, TipoOperacao tipoOperacao) throws IOException {
        TipoOperacao tipoOperacaoOposto = tipoOperacao == TipoOperacao.COMPRA ? TipoOperacao.VENDA : TipoOperacao.COMPRA;
        processarTransacao(operacao, tipoOperacaoOposto);
    }

    /**
     * Processa a transação de uma operação negociando com ofertas opostas.
     *
     * @param operacao A operação recebida.
     * @param tipoOperacaoOposto O tipo de operação oposta (COMPRA ou VENDA).
     * @throws IOException Em caso de erro ao adicionar ou remover ofertas no Livro de Ofertas.
     */
    private void processarTransacao(Operacao operacao, TipoOperacao tipoOperacaoOposto) throws IOException {
        List<Operacao> ofertasOpostas = livroDeOfertas.consultarOfertasPorAtivoETipo(operacao.getAtivo(), tipoOperacaoOposto);

        for (Operacao oferta : ofertasOpostas) {
            boolean podeNegociar = (tipoOperacaoOposto == TipoOperacao.COMPRA && oferta.getValor() >= operacao.getValor()) ||
                                   (tipoOperacaoOposto == TipoOperacao.VENDA && oferta.getValor() <= operacao.getValor());

            if (podeNegociar) {
                int quantidadeNegociada = Math.min(operacao.getQuantidade(), oferta.getQuantidade());

                operacao.setQuantidade(operacao.getQuantidade() - quantidadeNegociada);
                oferta.setQuantidade(oferta.getQuantidade() - quantidadeNegociada);

                Operacao transacao = new Operacao(
                    operacao.getTipo(),
                    operacao.getAtivo(),
                    quantidadeNegociada,
                    oferta.getValor(),
                    operacao.getCorretora(),
                    LocalDateTime.now()
                );

                historicoOperacoes.registrarOperacao(transacao);

                if (oferta.getQuantidade() == 0) {
                    livroDeOfertas.removerOferta(oferta);
                }
                if (operacao.getQuantidade() == 0) {
                    break;
                }
            }
        }

        if (operacao.getQuantidade() > 0) {
            livroDeOfertas.adicionarOferta(operacao);
        }
    }
}
