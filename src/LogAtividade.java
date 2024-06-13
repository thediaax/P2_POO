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

<<<<<<< HEAD
    public Connection getConnection() {
        return connection;
    }

    public void logActivity(int usuarioId, String descricao) {
        if (connection == null) {
            throw new RuntimeException("Conexão não estabelecida");
        }
        String sql = "INSERT INTO tb_atividade (usuario_id, descricao, data_de_ocorrencia) VALUES (?, ?, NOW())";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, descricao);
=======
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

    public void registrarAtividade(int usuarioId, int sessaoId, String descricao) {
        String sql = "INSERT INTO atividades (usuario_id, sessao_id, descricao, data_hora) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, sessaoId);
            pstmt.setString(3, descricao);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
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
>>>>>>> 19b45de46f01d5632a962ca85206b2f3f2181cd6
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLogs() {
        List<String> logs = new ArrayList<>();
        String sql = "SELECT u.usuario, a.descricao, a.data_de_ocorrencia " +
                     "FROM tb_atividade a " +
                     "INNER JOIN usuarios u ON a.usuario_id = u.id " +
                     "ORDER BY a.data_de_ocorrencia DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                logs.add(rs.getString("data_de_ocorrencia") + " - " +
                         rs.getString("usuario") + " - " +
                         rs.getString("descricao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public Connection getConnection() {
        return connection;
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
