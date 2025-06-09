package Parser;

import java.util.HashSet;
import java.util.Set;

public class Grupo {
    private Set<Pessoa> pessoas;

    public Grupo() {
        pessoas = new HashSet<>();
    }

    public void adicionarPessoa(Pessoa pessoa) {
        pessoas.add(pessoa);
        pessoa.adicionarGrupo(this);
    }

    public boolean contemPessoa(Pessoa pessoa) {
        return pessoas.contains(pessoa);
    }

    public Set<Pessoa> getPessoas() {
        return pessoas;
    }
}