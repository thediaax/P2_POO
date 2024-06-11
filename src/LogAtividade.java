import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogAtividade {
    private Connection connection;

    public LogAtividade() {
        try {
            connection = ConnectionFactory.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logActivity(String descricao) {
        String sql = "INSERT INTO tb_atividade (descricao, data_de_ocorrencia) VALUES (?, NOW())";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, descricao);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
