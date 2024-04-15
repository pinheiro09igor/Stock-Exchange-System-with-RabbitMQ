package app;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import server.LivroDeOfertas;
import services.manager.Operacao;
import services.manager.TipoOperacao;

/**
 * A classe AppBroker representa a aplicação que interage com o broker.
 * Ela fornece um menu interativo para processar operações de compra e venda,
 * consultar ofertas específicas e visualizar todas as operações no Livro de
 * Ofertas.
 */
public class AppBroker {

    private static final Scanner scanner = new Scanner(System.in);
    private static Broker broker;

    /**
     * O método main é o ponto de entrada da aplicação.
     * Ele inicia o broker e apresenta um menu interativo para o usuário,
     * permitindo a realização de operações e consultas relacionadas à bolsa de
     * valores.
     *
     * @param args Argumentos de linha de comando (não usados nesta aplicação).
     */
    public static void main(String[] args) {
        try {
            System.out.println("Bem-vindo ao Broker");
            System.out.print("Digite o nome da corretora: ");
            // Ler nome do broker com scanner
            String brokerName = scanner.nextLine();

            broker = new Broker(brokerName);

            int opcao;
            do {
                opcao = exibirMenu();

                switch (opcao) {
                    case 1:
                        processarCompra(brokerName);
                        break;
                    case 2:
                        processarVenda(brokerName);
                        break;
                    case 4:
                        consultarOfertasPorAtivoETipo();
                        break;
                    case 5:
                        consultarTodasOperacoes();
                        break;
                    case 6:
                        System.out.println("Saindo do Broker...");
                        break;
                    default:
                        System.out.println("Opção inválida, por favor tente novamente.");
                }
            } while (opcao != 6);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    /**
     * Exibe um menu com opções para o usuário.
     *
     * @return A opção escolhida pelo usuário.
     */
    private static int exibirMenu() {
        System.out.println("\n1 - Comprar"
                + "\n2 - Vender"
                + "\n4 - Consultar operações de um ativo específico no Livro de Ofertas por tipo"
                + "\n5 - Consultar todas as operações no Livro de Ofertas"
                + "\n6 - Sair");
        System.out.print("Escolha uma opção: ");
        return scanner.nextInt();
    }

    /**
     * Processa uma operação de compra.
     *
     * @param brokerName O nome do broker.
     */
    private static void processarCompra(String brokerName) {
        scanner.nextLine(); // Limpar a linha após a leitura do inteiro
        Operacao operacao = obterOperacao(TipoOperacao.COMPRA, brokerName);
        try {
            broker.enviarOperacao(operacao);
            System.out.println("Operação de compra enviada com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao enviar operação de compra: " + e.getMessage());
        }
    }

    /**
     * Processa uma operação de venda.
     *
     * @param brokerName O nome do broker.
     */
    private static void processarVenda(String brokerName) {
        scanner.nextLine(); // Limpar a linha após a leitura do inteiro
        Operacao operacao = obterOperacao(TipoOperacao.VENDA, brokerName);
        try {
            broker.enviarOperacao(operacao);
            System.out.println("Operação de venda enviada com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao enviar operação de venda: " + e.getMessage());
        }
    }

    /**
     * Obtém uma operação com os dados fornecidos pelo usuário.
     *
     * @param tipoOperacao O tipo de operação (COMPRA ou VENDA).
     * @param brokerName   O nome do broker.
     * @return Uma nova instância de Operação com os dados fornecidos.
     * @return Uma nova instância de Operação com os dados fornecidos.
     */
    private static Operacao obterOperacao(TipoOperacao tipoOperacao, String brokerName) {
        System.out.print("Digite o ativo: ");
        String ativo = scanner.nextLine();
        System.out.print("Digite a quantidade: ");
        int quantidade = scanner.nextInt();
        System.out.print("Digite o valor: ");
        double valor = scanner.nextDouble();

        return new Operacao(tipoOperacao, ativo, quantidade, valor, brokerName, LocalDateTime.now());
    }

    /**
     * Consulta ofertas específicas de um ativo no Livro de Ofertas com base no tipo
     * de operação.
     */
    private static void consultarOfertasPorAtivoETipo() {
        System.out.println("Digite a ação que deseja consultar: ");
        String ativo = scanner.next();
        System.out.println("Digite o tipo da operação (1 - COMPRA ou 2 - VENDA): ");
        int tipoOperacao = scanner.nextInt();

        if (tipoOperacao == 1) {
            System.out.println("Ofertas de compra para " + ativo + ":");
            List<Operacao> ofertas = LivroDeOfertas.getInstance().consultarOfertasPorAtivoETipo(ativo,
                    TipoOperacao.COMPRA);
            ofertas.forEach(oferta -> {
                System.out.println(
                        oferta.getCorretora() + " - " + oferta.getQuantidade() + " ações por R$ " + oferta.getValor());
            });
        } else if (tipoOperacao == 2) {
            System.out.println("Ofertas de venda para " + ativo + ":");
            List<Operacao> ofertas = LivroDeOfertas.getInstance().consultarOfertasPorAtivoETipo(ativo,
                    TipoOperacao.VENDA);
            ofertas.forEach(oferta -> {
                System.out.println(
                        oferta.getCorretora() + " - " + oferta.getQuantidade() + " ações por R$ " + oferta.getValor());
            });
        } else {
            System.out.println("Tipo de operação inválido.");
        }
    }

    /**
     * Consulta todas as operações no Livro de Ofertas e as exibe no console.
     */
    private static void consultarTodasOperacoes() {
        System.out.println("Todas as operações no Livro de Ofertas:");
        List<Operacao> operacoes = LivroDeOfertas.getInstance().consultarTodasOfertas(null);
        operacoes.forEach(operacao -> {
            System.out
                    .println(operacao.getCorretora() + " - " + operacao.getTipo() + " - " + operacao.getAtivo() + " - "
                            + operacao.getQuantidade() + " ações por R$ " + operacao.getValor());
        });
    }
}
