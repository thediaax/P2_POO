import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogAtividade {
    private Connection connection;

    public LogAtividade() {
        try {
            connection = ConnectionFactory.getConnection();
            if (connection == null) {
                throw new RuntimeException("Erro ao estabelecer conexão com o banco de dados");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao estabelecer conexão com o banco de dados", e);
        }
    }

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
