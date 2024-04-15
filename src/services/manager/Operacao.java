package services.manager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A classe Operacao representa uma operação financeira realizada em um mercado de valores.
 * Uma operação pode ser do tipo compra ou venda, envolvendo um ativo específico.
 * 
 * Cada operação é identificada por um ID único e possui informações sobre a corretora, ativo,
 * quantidade de ações negociadas, valor por ação e a data e hora da operação.
 */
public class Operacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id; // Identificador único da operação
    private final TipoOperacao tipo; // Tipo de operação (COMPRA ou VENDA)
    private final String ativo; // Ativo negociado
    private int quantidade; // Quantidade de ações negociadas
    private final double valor; // Valor por ação
    private final String corretora; // Corretora responsável pela operação
    private final LocalDateTime dataHora; // Data e hora da operação

    /**
     * Construtor para criar uma nova operação.
     *
     * @param tipo        Tipo de operação (COMPRA ou VENDA).
     * @param ativo       Ativo negociado.
     * @param quantidade  Quantidade de ações negociadas.
     * @param valor       Valor por ação.
     * @param corretora   Corretora responsável pela operação.
     * @param dataHora    Data e hora da operação.
     */
    public Operacao(TipoOperacao tipo, String ativo, int quantidade, double valor, String corretora, LocalDateTime dataHora) {
        this.id = UUID.randomUUID().toString(); // Gera um ID único
        this.tipo = tipo;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.valor = valor;
        this.corretora = corretora;
        this.dataHora = dataHora;
    }

    /**
     * Obtém o tipo de operação (COMPRA ou VENDA).
     *
     * @return O tipo de operação.
     */
    public TipoOperacao getTipo() {
        return tipo;
    }

    /**
     * Obtém o ativo negociado.
     *
     * @return O ativo negociado.
     */
    public String getAtivo() {
        return ativo;
    }

    /**
     * Obtém a quantidade de ações negociadas.
     *
     * @return A quantidade de ações negociadas.
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Obtém o valor por ação.
     *
     * @return O valor por ação.
     */
    public double getValor() {
        return valor;
    }

    /**
     * Obtém a corretora responsável pela operação.
     *
     * @return A corretora responsável.
     */
    public String getCorretora() {
        return corretora;
    }

    /**
     * Define a quantidade de ações negociadas.
     *
     * @param quantidade A nova quantidade de ações negociadas.
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna uma representação de string da operação, incluindo tipo, ativo,
     * quantidade, valor, corretora e data/hora da operação.
     *
     * @return Uma string representando a operação.
     */
    @Override
    public String toString() {
        return String.format("%s - %s: %d ações a R$%.2f pela corretora %s em %s",
                tipo, ativo, quantidade, valor, corretora, dataHora);
    }
}
