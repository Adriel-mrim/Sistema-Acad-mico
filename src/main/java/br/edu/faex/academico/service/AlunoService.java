package br.edu.faex.academico.service;

import br.edu.faex.academico.model.Aluno;
import br.edu.faex.academico.repository.AlunoRepository;

import java.util.List;

public class AlunoService {
    private AlunoRepository repository;
    private Long proximoId = 1L;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Aluno aluno){
        if (aluno.getNome() == null || aluno.getNome().isBlank()) {
            System.out.println("O nome do aluno é obrigatório.");
            return;
        }

        if (aluno.getEmail() == null || aluno.getEmail().isBlank()) {
            System.out.println("O e-mail do aluno é obrigatório.");
            return;
        }

        if (!aluno.getEmail().contains("@")) {
            System.out.println("E-mail inválido.");
            return;
        }

        if (repository.listar().stream()
                .anyMatch(a -> a.getEmail().equalsIgnoreCase(aluno.getEmail()))) {
            System.out.println("E-mail já cadastrado.");
            return;
        }

        aluno.setId(proximoId);
        proximoId++;

        this.repository.salvar(aluno);
        System.out.println("Aluno cadastrado com sucesso.");
    }

    public List<Aluno> listar(){
        return repository.listar();
    }

    public Aluno buscarPorId(Long id){
        Aluno aluno = repository.buscarPorId(id);
        if (aluno == null){
            System.out.println("Aluno não encontrado.");
            return null;
        }
        return aluno;
    }

    public void excluir(Long id){
        Aluno aluno = repository.buscarPorId(id);
        if(aluno == null){
            System.out.println("Aluno não encontrado.");
            return;
        }
        repository.excluir(id);
        System.out.println("Aluno excluído com sucesso.");
    }

    public void atualizar(Aluno alunoEditado){
        if (alunoEditado == null || alunoEditado.getId() == null) {
            System.out.println("Dados do aluno para atualização são inválidos.");
            return;
        }

        Aluno alunoExistente = repository.buscarPorId(alunoEditado.getId());
        if (alunoExistente == null){
            System.out.println("Aluno não encontrado.");
            return;
        }

        if (alunoEditado.getNome() == null || alunoEditado.getNome().isBlank()) {
            System.out.println("O nome do aluno é obrigatório.");
            return;
        }

        if (alunoEditado.getEmail() == null || alunoEditado.getEmail().isBlank()) {
            System.out.println("O e-mail do aluno é obrigatório.");
            return;
        }

        if (!alunoEditado.getEmail().contains("@")) {
            System.out.println("E-mail inválido.");
            return;
        }

        boolean emailEmUsoPorOutro = repository.listar().stream()
                .anyMatch(a -> !a.getId().equals(alunoEditado.getId())
                        && a.getEmail().equalsIgnoreCase(alunoEditado.getEmail()));

        if (emailEmUsoPorOutro) {
            System.out.println("E-mail já cadastrado.");
            return;
        }

        repository.atualizar(alunoEditado);
        System.out.println("Aluno atualizado com sucesso.");
    }
}