import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class ControleLogin {
    private static int usuarioIdLogado = -1;

    public static boolean login(String usuario, String senha) {
        String query = "SELECT id FROM usuarios WHERE usuario = ? AND senha = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuarioIdLogado = rs.getInt("id");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
                    return false;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar login: " + e.getMessage());
            return false;
        }
    }
    public static int getUsuarioIdLogado() {
        return usuarioIdLogado;
    }


    public static boolean showLoginDialog() {
        while (true) {
            String usuario;
            String senha;
            
            do {
                usuario = JOptionPane.showInputDialog("Digite o usuário: ");
                if (usuario == null) {
                    JOptionPane.showMessageDialog(null, "Login cancelado!");
                    return false;
                } else if (usuario.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Usuário não pode estar vazio. Digite novamente");
                }
            } while (usuario.isEmpty());
    
            do {
                senha = JOptionPane.showInputDialog("Digite a senha: ");
                if (senha == null) {
                    JOptionPane.showMessageDialog(null, "Login cancelado!");
                    return false;
                } else if (senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Senha não pode estar vazia. Digite novamente.");
                }
            } while (senha.isEmpty());
    
            if (login(usuario, senha)) {
                JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos. Tente novamente.");
            }
        }
    }
}