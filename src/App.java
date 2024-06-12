import java.util.Random;
import javax.swing.JOptionPane;
import java.util.List;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        if (!ControleLogin.showLoginDialog()) {
            return;
        }

        // Obtém o ID do usuário logado
        int usuarioId = ControleLogin.getUsuarioIdLogado();

        // Verifica se o ID do usuário é válido
        if (usuarioId == -1) {
            JOptionPane.showMessageDialog(null, "Erro ao obter ID do usuário logado.");
            return;
        }

        // Conexão com o banco de dados
        try (Connection connection = ConnectionFactory.getConnection()) {
            if (connection == null) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                return;
            }

            while (true) {
                String menu = "1- Jogar\n2- Consultar log completo\n3- Sair";
                String escolha = JOptionPane.showInputDialog(menu);

                if (escolha == null) {
                    JOptionPane.showMessageDialog(null, "Operação cancelada.");
                    continue;
                }

                LogAtividade logger = new LogAtividade(connection);

                switch (escolha) {
                    case "1":
                        var p = new Personagem();
                        Random gerador = new Random();
                        p.nome = JOptionPane.showInputDialog("Nome do personagem: ");

                        if (logger.getConnection() == null) {
                            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                            continue;
                        }

                        StringBuilder output = new StringBuilder();
                        output.append(p.toString()).append("\n\n");

                        for (int i = 0; i < 10; i++) {
                            if (p.energia > 0) {
                                int oQueFazer = gerador.nextInt(3);
                                switch (oQueFazer) {
                                    case 0:
                                        p.cacar();
                                        logger.logActivity("caçar", usuarioId);
                                        output.append(p.nome).append(" caçou\n");
                                        break;
                                    case 1:
                                        p.comer();
                                        logger.logActivity("comer", usuarioId);
                                        output.append(p.nome).append(" comeu\n");
                                        break;
                                    case 2:
                                        p.dormir();
                                        logger.logActivity("dormir", usuarioId);
                                        output.append(p.nome).append(" dormiu\n");
                                        break;
                                }
                            }
                        }

                        JOptionPane.showMessageDialog(null, output.toString());
                        break;

                    case "2":
                        if (logger.getConnection() == null) {
                            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                            continue;
                        }

                        List<String> logs = logger.getLogs();
                        StringBuilder logOutput = new StringBuilder("Log de Atividades:\n\n");
                        for (String log : logs) {
                            logOutput.append(log).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, logOutput.toString());
                        break;

                    case "3":
                        return;

                    default:
                        JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
                        break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }
}
