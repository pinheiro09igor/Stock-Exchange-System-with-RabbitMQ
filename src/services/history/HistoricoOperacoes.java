package services.history;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import services.manager.Operacao;

/**
 * A classe HistoricoOperacoes gerencia o histórico de operações realizadas em um mercado financeiro.
 * Ela mantém um histórico das operações em um arquivo, permitindo consultas ao histórico.
 * 
 * A classe segue o padrão Singleton para garantir que haja apenas uma instância durante a execução do programa.
 */
public class HistoricoOperacoes {

    private static HistoricoOperacoes instance;
    
    // Lista para armazenar o histórico de operações
    private final List<Operacao> historico;
    
    // Buffer para acumular operações em lote
    private final List<Operacao> buffer;
    
    // Caminho padrão para o arquivo de histórico
    private static final String ARQUIVO_HISTORICO = "HistoricoTransacao.txt";
    
    // Tamanho máximo padrão do lote antes de escrever no arquivo
    private static final int TAMANHO_LOTE_PADRAO = 100;
    
    /**
     * Construtor privado para evitar a criação direta de instâncias.
     * Inicializa a lista de histórico e o buffer de operações.
     */
    private HistoricoOperacoes() {
        this.historico = new ArrayList<>();
        this.buffer = new ArrayList<>();
        carregarHistorico(); // Carrega o histórico a partir do arquivo
    }
    
    /**
     * Método para obter a instância única da classe HistoricoOperacoes.
     *
     * @return A instância única de HistoricoOperacoes.
     */
    public static synchronized HistoricoOperacoes getInstance() {
        if (instance == null) {
            instance = new HistoricoOperacoes();
        }
        return instance;
    }
    
    /**
     * Registra uma operação no histórico, adicionando-a ao buffer.
     * Se o buffer atingir ou ultrapassar o tamanho máximo do lote, as operações são salvas em lote.
     *
     * @param operacao A operação a ser registrada.
     */
    public synchronized void registrarOperacao(Operacao operacao) {
        buffer.add(operacao); // Adiciona a operação ao buffer
        
        // Se o buffer atingir ou ultrapassar o tamanho máximo do lote, salva as operações
        if (buffer.size() >= TAMANHO_LOTE_PADRAO) {
            salvarLote();
        }
    }
    
    /**
     * Consulta o histórico de operações.
     *
     * @return Uma lista contendo o histórico de operações.
     */
    public synchronized List<Operacao> consultarHistorico() {
        return new ArrayList<>(historico);
    }
    
    /**
     * Salva as operações acumuladas no buffer em um arquivo de histórico.
     */
    private synchronized void salvarLote() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_HISTORICO, true))) {
            // Escreve todas as operações do buffer ao final do arquivo
            for (Operacao operacao : buffer) {
                oos.writeObject(operacao);
            }
            // Limpa o buffer após a escrita
            buffer.clear();
        } catch (IOException e) {
            System.err.println("Erro ao salvar o lote no histórico: " + e.getMessage());
            // Lidar com exceções adequadamente
        }
    }
    
    /**
     * Carrega o histórico de operações a partir de um arquivo.
     */
    private synchronized void carregarHistorico() {
        File file = new File(ARQUIVO_HISTORICO);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                historico.clear();
                Operacao operacao;
                while ((operacao = (Operacao) ois.readObject()) != null) {
                    historico.add(operacao);
                }
            } catch (EOFException e) {
                // Fim do arquivo alcançado, o que é esperado
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar o histórico: " + e.getMessage());
                // Lidar com exceções adequadamente
            }
        }
    }
}
