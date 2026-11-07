package br.edu.faex.academico;

import br.edu.faex.academico.controller.AlunoController;
import br.edu.faex.academico.model.Aluno;
import br.edu.faex.academico.repository.AlunoRepository;
import br.edu.faex.academico.service.AlunoService;

import java.util.List;

public class Main {
    static void main() {
        Aluno aluno1 = new Aluno("Aleandro Lima", "aleandro.lima2@faex.edu.br");
        Aluno aluno2 = new Aluno("Maria Lima", "maria.lima2@faex.edu.br");
        Aluno aluno3 = new Aluno("José Lima", "jose.lima2@faex.edu.br");

        AlunoRepository repository = new AlunoRepository();
        AlunoService service = new AlunoService(repository);
        AlunoController controller = new AlunoController(service);
        //Cadastro de Alunos BD
        controller.cadastrar(aluno1);
        controller.cadastrar(aluno2);
        controller.cadastrar(aluno3);

        // Listar Alunos BD
        System.out.println("\n===== LISTA DE ALUNOS =====");

        List<Aluno> alunos = controller.listar();

        for (Aluno aluno : alunos) {
            System.out.println("ID: " + aluno.getId());
            System.out.println("Nome: " + aluno.getNome());
            System.out.println("E-mail: " + aluno.getEmail());
            System.out.println("Ativo: " + aluno.isAtivo());
            System.out.println("--------------------------");
        }

        Aluno aluno = controller.buscarPorId(1L);
        if (aluno != null) {
            System.out.println("Aluno encontrado!");
            System.out.println("ID: " + aluno.getId());
            System.out.println("Nome: " + aluno.getNome());
            System.out.println("E-mail: " + aluno.getEmail());
        } else {
            System.out.println("Aluno não encontrado!");
        }
    }
}
