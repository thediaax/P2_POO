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

    public List<String> getLogs() {
        List<String> logs = new ArrayList<>();
        String sql = "SELECT descricao, data_de_ocorrencia FROM tb_atividade ORDER BY data_de_ocorrencia DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                logs.add(rs.getString("data_de_ocorrencia") + " - " + rs.getString("descricao"));
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
