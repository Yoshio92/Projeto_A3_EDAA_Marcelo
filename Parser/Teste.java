package Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Teste {
    public static void main(String[] args) {
        File file = new File("entrada.txt");
        Parser parser = new Parser(file);

         //estrutura HashMap para colocar e mapear todas as pessoas presentes, garantindo não haver pessoas repetidas
        Map<String, Pessoa> pessoasMap = new HashMap<>();
        //lista usada para colocar todos os grupos criados a partir do recebido da entrada
        List<Grupo> grupos = new ArrayList<>();
        //estrutura HashMap para colocar e mapear todas as filas criadas, garantindo não haver filas repetidas
        Map<String, FilaBrasileira> filas = new LinkedHashMap<>();

        try (PrintWriter writer = new PrintWriter(new FileWriter("saida.txt"))) {

            while (parser.hasNext()) {
                String line = parser.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\s+");

                switch (tokens[0]) {
                    case "grupo:": {
                        Grupo grupo = new Grupo();
                        for (int i = 1; i < tokens.length; i++) {
                            String nome = tokens[i];
                            Pessoa p = pessoasMap.getOrDefault(nome, new Pessoa(nome));
                            grupo.adicionarPessoa(p);
                            pessoasMap.put(nome, p);
                        }
                        grupos.add(grupo);
                        break;
                    }

                    case "existe:": {
                        String nome = tokens[1];
                        if (pessoasMap.containsKey(nome)) {
                            writer.println(" [" + nome + "] existe!");
                        } else {
                            writer.println(" [" + nome + "] NÃO existe!");
                        }
                        break;
                    }

                    case "conhece:": {
                        String nome1 = tokens[1];
                        String nome2 = tokens[2];
                        Pessoa p1 = pessoasMap.get(nome1);
                        Pessoa p2 = pessoasMap.get(nome2);
                        //verificação se as pessoas existem e se elas se conhecem
                        boolean conhece = p1 != null && p2 != null && p1.conhece(p2);
                        writer.println(" [" + nome1 + "] " + (conhece ? "conhece" : "NÃO conhece") + " [" + nome2 + "]");
                        break;
                    }

                    case "criaFila:": {
                        for (int i = 1; i < tokens.length; i++) {
                            String id = tokens[i];
                            filas.put(id, new FilaBrasileira(id));
                        }
                        break;
                    }

                    case "atendeFila:": {
                        for (int i = 1; i < tokens.length; i++) {
                            FilaBrasileira fila = filas.get(tokens[i]);
                            if (fila != null) {
                                fila.atender();
                            }
                        }
                        break;
                    }

                    case "chegou:": {
                        for (int i = 1; i < tokens.length; i++) {
                            String nome = tokens[i];
                            Pessoa nova = pessoasMap.getOrDefault(nome, new Pessoa(nome));
                            pessoasMap.putIfAbsent(nome, nova);
                            
                            FilaBrasileira melhorFila = null;
                            FilaBrasileira melhorFilaComConhecido = null;
                            int melhorPosicao = Integer.MAX_VALUE;
                            int melhorPosicaoComConhecido = Integer.MAX_VALUE;
                            boolean temConhecido = false;
                            
                            // o método abaixo garante que a pessoa será colocada na melhor posição possível
                            // para ela, de preferência atrás de seu conhecido se houver empate ao invés de uma
                            // fila sem conhecidos
                            for (FilaBrasileira fila : filas.values()) {
                                List<Pessoa> atual = fila.getFila();
                                int posicaoInsercao = -1;
                    
                                for (int j = 0; j < atual.size(); j++) {
                                    if (nova.conhece(atual.get(j))) {
                                        posicaoInsercao = j + 1;
                                        temConhecido = true;
                                    }
                                }
                                boolean conheceAlguem = (posicaoInsercao > 0);
                                if(atual.size() == 0 ) {
                                    posicaoInsercao = 0;
                                }
                                
                    
                                int posFinal = (posicaoInsercao >= 0) ? posicaoInsercao : atual.size();


                                if (posFinal <= melhorPosicao) {
                                    // Melhor posição encontrada
                                    melhorPosicao = posFinal;
                                    melhorFila = fila;
                                    
                                } 
                                if (conheceAlguem) {
                                    // Melhor posição encontrada com um conhecido
                                    if(posFinal <= melhorPosicaoComConhecido){
                                        melhorPosicaoComConhecido = posFinal;
                                        melhorFilaComConhecido = fila;
                                    } 
                                }
                                
                            }
                            if(temConhecido){
                                if (melhorPosicaoComConhecido <= melhorPosicao && melhorFilaComConhecido != null){
                                    melhorFilaComConhecido.inserir(nova, melhorPosicaoComConhecido);
                                } else if (melhorFila != null) {
                                    melhorFila.inserir(nova, melhorPosicao);
                                }
                            } else if (melhorFila != null) {
                                melhorFila.inserir(nova, melhorPosicao);
                            }
                        }
                        break;
                    }

                    case "desiste:": {
                        for (int i = 1; i < tokens.length; i++) {
                            String nome = tokens[i];
                            Pessoa p = pessoasMap.get(nome);
                            if (p != null) {
                                for (FilaBrasileira fila : filas.values()) {
                                    fila.desistir(p);
                                }
                            }
                        }
                        break;
                    }

                    case "imprime:": {
                        for (FilaBrasileira fila : filas.values()) {
                            writer.println(fila.toString());
                        }
                        break;
                    }

                    default:
                        System.out.println("Comando desconhecido: " + tokens[0]);
                }
            }

            System.out.println("Saída gerada no arquivo saida.txt");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
