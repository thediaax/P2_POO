import java.util.Random;
import javax.swing.JOptionPane;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            if (!ControleLogin.showLoginDialog()) {
                return;
            }
            
            int usuarioId = ControleLogin.getUsuarioIdLogado();

            if (usuarioId == -1) {
                JOptionPane.showMessageDialog(null, "Erro ao obter ID do usuário logado.");
                return;
            }

            try (Connection connection = ConnectionFactory.getConnection()) {
                if (connection == null) {
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                    return;
                }

                LogAtividade logger = new LogAtividade(connection);

                while (true) {
                    String menu = "1- Jogar\n2- Consultar log completo\n3- Ver ranking\n4- Sair";
                    String escolha = JOptionPane.showInputDialog(menu);

                    if (escolha == null) {
                        JOptionPane.showMessageDialog(null, "Operação cancelada.");
                        break;
                    }

                    switch (escolha) {
                        case "1":
                            try {
                                int sessaoId = iniciarNovaSessao(usuarioId, connection);

                                if (sessaoId == -1) {
                                    JOptionPane.showMessageDialog(null, "Erro ao iniciar nova sessão.");
                                    continue;
                                }

                                var p = new Personagem(10, 0, 0, usuarioId, sessaoId, logger);
                                Random gerador = new Random();
                                if (!p.nomear()) {
                                    continue;
                                }

                                p.jogar(gerador);

                                StringBuilder output = new StringBuilder();
                                output.append("Resultado do jogo:\n");
                                output.append(p.getStatus());

                                int pontuacaoFinal = logger.calcularPontuacaoFinal(usuarioId, sessaoId);
                                output.append("\nPontuação final: ").append(pontuacaoFinal).append("\n");

                                JOptionPane.showMessageDialog(null, output.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Erro ao jogar: " + e.getMessage());
                            }
                            break;

                        case "2":
                            try {
                                List<String> logs = logger.getLogs();
                                StringBuilder logOutput = new StringBuilder("Log de Atividades:\n\n");
                                for (String log : logs) {
                                    logOutput.append(log).append("\n");
                                }
                                JOptionPane.showMessageDialog(null, logOutput.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Erro ao consultar log: " + e.getMessage());
                            }
                            break;

                        case "3":
                            try {
                                mostrarRanking(connection);
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Erro ao mostrar ranking: " + e.getMessage());
                            }
                            break;

                        case "4":
                            return;

                        default:
                            JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
                            break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro de SQL: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inicializar a aplicação: " + e.getMessage());
        }
    }

    private static int iniciarNovaSessao(int usuarioId, Connection connection) {
        String sql = "INSERT INTO pontuacoes (usuario_id, sessao_id, data_jogo) VALUES (?, ?, CURRENT_TIMESTAMP) RETURNING sessao_id";
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
            JOptionPane.showMessageDialog(null, "Erro ao iniciar nova sessão: " + e.getMessage());
        }
        return -1;
    }

    private static int gerarSessaoId() {
        return new Random().nextInt(1000000);
    }

    private static void mostrarRanking(Connection connection) {
        String sql = "SELECT u.usuario, p.pontos, p.data_jogo FROM pontuacoes p " +
                     "JOIN usuarios u ON p.usuario_id = u.id " +
                     "ORDER BY p.pontos DESC, p.data_jogo ASC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            StringBuilder rankingOutput = new StringBuilder("Ranking:\n\n");
            while (rs.next()) {
                String nome = rs.getString("usuario");
                int pontos = rs.getInt("pontos");
                String dataJogo = rs.getString("data_jogo");
                rankingOutput.append(String.format("%s - Pontuação: %d - Data: %s\n", nome, pontos, dataJogo));
            }
            JOptionPane.showMessageDialog(null, rankingOutput.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao recuperar o ranking: " + e.getMessage());
        }
    }
}
