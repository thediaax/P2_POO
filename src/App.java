import java.util.Random;
import javax.swing.JOptionPane;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

            LogAtividade logger = new LogAtividade(connection);

            while (true) {
                String menu = "1- Jogar\n2- Consultar log completo\n3- Sair";
                String escolha = JOptionPane.showInputDialog(menu);

                if (escolha == null) {
                    JOptionPane.showMessageDialog(null, "Operação cancelada.");
                    continue;
                }

                switch (escolha) {
                    case "1":
                        // Iniciar uma nova sessão
                        int sessaoId = iniciarNovaSessao(usuarioId, connection);

                        // Verifica se a sessão foi criada com sucesso
                        if (sessaoId == -1) {
                            JOptionPane.showMessageDialog(null, "Erro ao iniciar nova sessão.");
                            continue;
                        }

                        var p = new Personagem(10, 0, 0, usuarioId, sessaoId, logger);
                        Random gerador = new Random();
                        p.nome = JOptionPane.showInputDialog("Nome do personagem: ");

                        StringBuilder output = new StringBuilder();
                        output.append(p.toString()).append("\n\n");

                        for (int i = 0; i < 10; i++) {
                            if (p.energia > 0) {
                                int oQueFazer = gerador.nextInt(3);
                                switch (oQueFazer) {
                                    case 0:
                                        p.cacar(output);
                                        break;
                                    case 1:
                                        p.comer(output);
                                        break;
                                    case 2:
                                        p.dormir(output);
                                        break;
                                }
                            }
                        }

                        // Calcular e registrar a pontuação final
                        int pontuacaoFinal = logger.calcularPontuacaoFinal(usuarioId, sessaoId);
                        output.append("\nPontuação final: ").append(pontuacaoFinal).append("\n");

                        JOptionPane.showMessageDialog(null, output.toString());
                        break;

                    case "2":
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

    private static int iniciarNovaSessao(int usuarioId, Connection connection) {
        String sql = "INSERT INTO pontuacoes (usuario_id, sessao_id) VALUES (?, ?) RETURNING sessao_id";
        int sessaoId = gerarSessaoId();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, sessaoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("sessao_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static int gerarSessaoId() {
        // Gerar um ID de sessão aleatório ou baseado em lógica específica
        return new Random().nextInt(1000000);
    }
}
