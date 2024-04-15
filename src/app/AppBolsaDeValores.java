package app;

/**
 * A classe AppBolsaDeValores é o ponto de entrada da aplicação da Bolsa de Valores.
 * Ela contém o método main, que inicia uma nova instância de BolsaDeValores.
 */
public class AppBolsaDeValores {
    
    /**
     * O método main é o ponto de entrada da aplicação.
     * Ele cria uma nova instância de BolsaDeValores e lida com quaisquer exceções
     * que possam ocorrer durante a inicialização.
     *
     * @param args Argumentos de linha de comando (não usados nesta aplicação).
     */
    public static void main(String[] args) {
        try {
            // Cria uma nova instância de BolsaDeValores
            new BolsaDeValores();
        } catch (Exception e) {
            // Trate possíveis exceções durante a inicialização da Bolsa de Valores
            System.err.println("Erro ao inicializar a Bolsa de Valores: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
