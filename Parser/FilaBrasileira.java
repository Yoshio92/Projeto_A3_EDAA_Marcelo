package Parser;

import java.util.*;

public class FilaBrasileira {
    private String id;
    private LinkedList<Pessoa> fila;

    public FilaBrasileira(String id) {
        this.id = id;
        this.fila = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public List<Pessoa> getFila() {
        return fila;
    }

    public void atender() {
        if (!fila.isEmpty()) {
            fila.removeFirst();
        }
    }

    public void desistir(Pessoa pessoa) {
        fila.remove(pessoa);
    }

    public void inserir(Pessoa pessoa, int posicao) {
        if (posicao < 0 || posicao > fila.size()) {
            fila.add(pessoa); // pessoa será colocada no final da fila
        } else {
            fila.add(posicao, pessoa);
        }
    }

    public boolean contem(Pessoa p) {
        return fila.contains(p);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(id).append(" [ ");
        for (int i = 0; i < fila.size(); i++) {
            if(i == fila.size() - 1){
                sb.append(fila.get(i).getNome());
            } else {
                sb.append(fila.get(i).getNome()).append(", ");
            }
        }
        if (!fila.isEmpty()) sb.append(" ");
        sb.append("]");
        return sb.toString();
    }
}