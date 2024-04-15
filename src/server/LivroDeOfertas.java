package server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import services.manager.Operacao;
import services.manager.TipoOperacao;

/**
 * A classe LivroDeOfertas é uma implementação concreta da interface ILivroDeOfertas.
 * Ela gerencia um livro de ofertas para um mercado financeiro, permitindo adicionar,
 * remover e consultar ofertas de ativos específicos.
 * 
 * A classe segue o padrão Singleton para garantir que apenas uma instância única
 * exista durante a execução do programa.
 */
public class LivroDeOfertas implements ILivroDeOfertas {
    private static LivroDeOfertas instance;
    private final List<Operacao> ofertas;

    /**
     * Construtor privado para evitar a criação direta de instâncias.
     * Inicializa a lista de ofertas.
     */
    private LivroDeOfertas() {
        super();
        ofertas = new ArrayList<>();
    }

    /**
     * Método para obter a instância única da classe LivroDeOfertas.
     *
     * @return A instância única de LivroDeOfertas.
     */
    public static synchronized LivroDeOfertas getInstance() {
        if (instance == null) {
            instance = new LivroDeOfertas();
        }
        return instance;
    }

    /**
     * Adiciona uma oferta a um ativo.
     *
     * @param oferta A oferta a ser adicionada.
     */
    @Override
    public void adicionarOferta(Operacao oferta) {
        synchronized (this) {
            ofertas.add(oferta);
        }
    }

    /**
     * Remove uma oferta de um ativo.
     *
     * @param operacao A operação ou oferta a ser removida.
     */
    @Override
    public void removerOferta(Operacao operacao) {
        synchronized (this) {
            ofertas.remove(operacao);
        }
    }

    /**
     * Consulta as ofertas de um ativo específico de acordo com o tipo de operação (COMPRA ou VENDA).
     *
     * @param ativo O ativo para o qual as ofertas devem ser consultadas.
     * @param tipoOperacao O tipo de operação para filtrar as ofertas (COMPRA ou VENDA).
     * @return Uma lista de ofertas que correspondem ao ativo e ao tipo de operação especificados.
     */
    @Override
    public List<Operacao> consultarOfertasPorAtivoETipo(String ativo, TipoOperacao tipoOperacao) {
        // Filtra as ofertas de acordo com o ativo e o tipo de operação
        synchronized (this) {
            return ofertas.stream()
                .filter(oferta -> oferta.getAtivo().equals(ativo) && oferta.getTipo() == tipoOperacao)
                .collect(Collectors.toList());
        }
    }

    /**
     * Consulta as ofertas de um ativo específico, independentemente do tipo de operação.
     *
     * @param ativo O ativo para o qual as ofertas devem ser consultadas.
     * @return Uma lista de ofertas que correspondem ao ativo especificado.
     */
    @Override
    public List<Operacao> consultarOfertasPorAtivo(String ativo) {
        // Filtra as ofertas de acordo com o ativo
        synchronized (this) {
            return ofertas.stream()
                .filter(oferta -> oferta.getAtivo().equals(ativo))
                .collect(Collectors.toList());
        }
    }

    /**
     * Consulta todas as ofertas presentes no livro de ofertas, com uma opção para filtrar por tipo de operação.
     *
     * @param tipoOperacao O tipo de operação para filtrar as ofertas (COMPRA, VENDA ou null para todas as ofertas).
     * @return Uma lista de todas as ofertas no livro de ofertas, filtradas pelo tipo de operação especificado.
     */
    @Override
    public List<Operacao> consultarTodasOfertas(TipoOperacao tipoOperacao) {
        synchronized (this) {
            // Retorna a lista de todas as ofertas.
            return ofertas;
        }
    }
}
