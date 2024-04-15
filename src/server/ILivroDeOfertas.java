package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import services.manager.Operacao;
import services.manager.TipoOperacao;

/**
 * A interface ILivroDeOfertas define os métodos necessários para interagir com um livro de ofertas
 * em um mercado financeiro. Os métodos incluem adicionar, remover e consultar ofertas de ativos.
 */
public interface ILivroDeOfertas extends Remote {
    /**
     * Adiciona uma oferta a um ativo.
     *
     * @param oferta A oferta a ser adicionada.
     * @throws RemoteException Em caso de erro de comunicação remota.
     */
    void adicionarOferta(Operacao oferta) throws RemoteException;

    /**
     * Remove uma oferta de um ativo.
     *
     * @param operacao A operação ou oferta a ser removida.
     * @throws RemoteException Em caso de erro de comunicação remota.
     */
    void removerOferta(Operacao operacao) throws RemoteException;

    /**
     * Consulta as ofertas de um ativo específico de acordo com o tipo de operação (COMPRA ou VENDA).
     *
     * @param ativo O ativo para o qual as ofertas devem ser consultadas.
     * @param tipoOperacao O tipo de operação para filtrar as ofertas (COMPRA ou VENDA).
     * @return Uma lista de ofertas que correspondem ao ativo e ao tipo de operação especificados.
     * @throws RemoteException Em caso de erro de comunicação remota.
     */
    List<Operacao> consultarOfertasPorAtivoETipo(String ativo, TipoOperacao tipoOperacao) throws RemoteException;

    /**
     * Consulta as ofertas de um ativo específico, independentemente do tipo de operação.
     *
     * @param ativo O ativo para o qual as ofertas devem ser consultadas.
     * @return Uma lista de ofertas que correspondem ao ativo especificado.
     * @throws RemoteException Em caso de erro de comunicação remota.
     */
    List<Operacao> consultarOfertasPorAtivo(String ativo) throws RemoteException;

    /**
     * Consulta todas as ofertas presentes no livro de ofertas, com uma opção para filtrar por tipo de operação.
     *
     * @param tipoOperacao O tipo de operação para filtrar as ofertas (COMPRA, VENDA ou null para todas as ofertas).
     * @return Uma lista de todas as ofertas no livro de ofertas, filtradas pelo tipo de operação especificado.
     * @throws RemoteException Em caso de erro de comunicação remota.
     */
    List<Operacao> consultarTodasOfertas(TipoOperacao tipoOperacao) throws RemoteException;
}
