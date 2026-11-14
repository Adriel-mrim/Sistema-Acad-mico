package br.edu.faex.academico.repository;

import br.edu.faex.academico.config.Conexao;
import br.edu.faex.academico.model.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfessorRepository {

    public void salvar(Professor professor) {
        if (professor.getId() == null) {
            String sql = "INSERT INTO professor (nome, email) VALUES (?, ?)";

            try (Connection conn = Conexao.conectar();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, professor.getNome());
                ps.setString(2, professor.getEmail());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        professor.setId(rs.getLong(1));
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao salvar professor: " + e.getMessage(), e);
            }
        } else {
            atualizar(professor);
        }
    }

    public void atualizar(Professor professor) {
        String sql = "UPDATE professor SET nome = ?, email = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, professor.getNome());
            ps.setString(2, professor.getEmail());
            ps.setLong(3, professor.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar professor: " + e.getMessage(), e);
        }
    }

    public List<Professor> listar() {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT id, nome, email FROM professor ORDER BY id";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Professor professor = new Professor(
                        rs.getString("nome"),
                        rs.getString("email"));
                professor.setId(rs.getLong("id"));
                professores.add(professor);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar professores: " + e.getMessage(), e);
        }
        return professores;
    }

    public Professor buscarPorId(Long id) {
        String sql = "SELECT id, nome, email FROM professor WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Professor professor = new Professor(
                            rs.getString("nome"),
                            rs.getString("email"));
                    professor.setId(rs.getLong("id"));
                    return professor;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar professor: " + e.getMessage(), e);
        }
        return null;
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM professor WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir professor: " + e.getMessage(), e);
        }
    }
}