package br.edu.faex.academico.repository;

import br.edu.faex.academico.config.Conexao;
import br.edu.faex.academico.model.Curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CursoRepository {

    // INSERT quando id == null, UPDATE quando o id já existe
    public void salvar(Curso curso) {
        if (curso.getId() == null) {
            String sql = "INSERT INTO curso (nome, modalidade) VALUES (?, ?)";

            try (Connection conn = Conexao.conectar();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, curso.getNome());
                ps.setString(2, curso.getModalidade());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        curso.setId(rs.getLong(1));
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao salvar curso: " + e.getMessage(), e);
            }
        } else {
            atualizar(curso);
        }
    }

    public void atualizar(Curso curso) {
        String sql = "UPDATE curso SET nome = ?, modalidade = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, curso.getNome());
            ps.setString(2, curso.getModalidade());
            ps.setLong(3, curso.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar curso: " + e.getMessage(), e);
        }
    }

    public List<Curso> listar() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id, nome, modalidade FROM curso ORDER BY id";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getString("nome"),
                        rs.getString("modalidade"));
                curso.setId(rs.getLong("id"));
                cursos.add(curso);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cursos: " + e.getMessage(), e);
        }
        return cursos;
    }

    public Curso buscarPorId(Long id) {
        String sql = "SELECT id, nome, modalidade FROM curso WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Curso curso = new Curso(
                            rs.getString("nome"),
                            rs.getString("modalidade"));
                    curso.setId(rs.getLong("id"));
                    return curso;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curso: " + e.getMessage(), e);
        }
        return null;
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM curso WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir curso: " + e.getMessage(), e);
        }
    }
}