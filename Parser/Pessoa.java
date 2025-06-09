package Parser;

import java.util.HashSet;
import java.util.Set;

public class Pessoa {
    private String nome;
    private Set<Grupo> grupos;

    public Pessoa(String nome) {
        this.nome = nome;
        this.grupos = new HashSet<>();
    }

    public String getNome() {
        return nome;
    }

    public Set<Grupo> getGrupos() {
        return grupos;
    }

    public void adicionarGrupo(Grupo grupo) {
        this.grupos.add(grupo);
    }

    // Método para verificar se conhece outra pessoa
    public boolean conhece(Pessoa outraPessoa) {
        for (Grupo g : grupos) {
            if (g.contemPessoa(outraPessoa)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + nome + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa)) return false;
        Pessoa p = (Pessoa) o;
        return nome.equals(p.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
