import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LogAtividade {
    private Connection connection;

    public LogAtividade(Connection connection) {
        this.connection = connection;
    }

    public void logActivity(String descricao, int usuarioId) {
        String sql = "INSERT INTO tb_atividade (descricao, data_de_ocorrencia, usuario_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, descricao);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, usuarioId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarCaca(int usuarioId, int sessaoId) {
        String sql = "UPDATE pontuacoes SET cacadas = cacadas + 1, pontos = pontos + 2 WHERE usuario_id = ? AND sessao_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, sessaoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarSono(int usuarioId, int sessaoId) {
        String sql = "UPDATE pontuacoes SET sonos = sonos + 1, pontos = pontos - 1 WHERE usuario_id = ? AND sessao_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, sessaoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int calcularPontuacaoFinal(int usuarioId, int sessaoId) {
        String sql = "SELECT cacadas, sonos FROM pontuacoes WHERE usuario_id = ? AND sessao_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, sessaoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int cacadas = rs.getInt("cacadas");
                    int sonos = rs.getInt("sonos");
                    int pontos = 2 * cacadas - sonos;
                    atualizarPontuacao(usuarioId, sessaoId, pontos);
                    return pontos;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void atualizarPontuacao(int usuarioId, int sessaoId, int pontos) {
        String sql = "UPDATE pontuacoes SET pontos = ? WHERE usuario_id = ? AND sessao_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, pontos);
            pstmt.setInt(2, usuarioId);
            pstmt.setInt(3, sessaoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLogs() {
        List<String> logs = new ArrayList<>();
        String sql = "SELECT descricao, data_de_ocorrencia FROM tb_atividade ORDER BY data_de_ocorrencia DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                logs.add(rs.getTimestamp("data_de_ocorrencia") + " - " + rs.getString("descricao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão com o banco de dados", e);
            }
        }
    }
}