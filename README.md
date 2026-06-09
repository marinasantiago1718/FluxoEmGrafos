## Nome do Problema
Police Chase

## Link do Problema
[https://cses.fi/problemset/task/1695](https://cses.fi/problemset/task/1695)

## Integrantes do Grupo
- João Miguel Drumond
- Marina Maia
- Tales Pimentel

## Linguagem Utilizada
- Java

---

## Como Executar a Solução
Abra o terminal na pasta do projeto, compile o código e execute a classe principal passando o nome do arquivo da pasta entrada como argumento:

```bash
# Passo 1: Compilar o código Java
javac Main.java

# Passo 2: Executar a classe compilada passando o arquivo de entrada
java Main < entrada_do_problema.txt
```

---

## Modelagem do Grafo

O grafo não direcionado foi modelado como uma rede de fluxo em que o vértice 1 
representa a origem s e o vértice n representa o destino t. Cada rua da cidade, 
originalmente representada por uma aresta não direcionada entre os vértices A e B, 
foi convertida em dois arcos direcionados opostos (A→B e B→A), ambos com capacidade unitária. 
Para cada arco inserido, também foi criada automaticamente uma aresta reversa residual com capacidade inicial zero, 
conforme exigido pela implementação do algoritmo de Edmonds-Karp. 
Dessa forma, o grafo residual pode ser atualizado a cada aumento de fluxo, 
permitindo o redirecionamento de fluxo por caminhos alternativos quando necessário. 
Como todas as capacidades são iguais a 1, o valor do fluxo máximo obtido corresponde, pelo teorema fluxo máximo–corte mínimo, 
ao número mínimo de ruas que devem ser bloqueadas para separar a origem do destino.

---

## Definição de Origem, Sorvedouro, Vértices, Arestas e Capacidades

ORIGEM (s):
   - Representação no problema: O ponto de partida do ladrão.
   - Definição na rede: É o vértice 1, onde o fluxo inicia.

SORVEDOURO (t):
   - Representação no problema: O ponto de fuga da cidade.
   - Definição na rede: É o vértice n, onde o fluxo é drenado.

VÉRTICES (V):
   - Representação no problema: Os cruzamentos/interseções da cidade.
   - Definição na rede: São os nós do grafo (de 1 a n) que transmitem o fluxo.

ARESTAS (E):
   - Representação no problema: As ruas bidirecionais que ligam os cruzamentos.
   - Definição na rede: Arcos direcionados e opostos (A -> B e B -> A) com arestas reversas.

CAPACIDADES (c):
   - Representação no problema: O peso unitário para bloquear cada rua (custo uniforme).
   - Definição na rede: Valor fixo de 1 para arcos reais e 0 para arestas reversas.

---

## Algoritmo Utilizado

Algoritmo de Edmonds Karp (Menor Caminho Aumentador)

---

## Papel Grafo Residual

O grafo residual permite reajustar decisões de fluxo tomadas em iterações anteriores, 
disponibilizando arestas reversas pelas quais parte do fluxo pode ser 
redirecionada quando um caminho aumentador melhor é encontrado.

- Mecanismo de Refluxo: Sempre que um caminho é usado, o algoritmo reduz a 
   capacidade da aresta direta (rua original) e aumenta a da aresta reversa 
   (virtual). Isso permite que fluxos futuros trafeguem no sentido oposto, o que 
   equivale matematicamente a "cancelar" ou "desfazer" uma rota anterior mal 
   escolhida, redirecionando o fluxo corretamente.

- Critério de Parada e Corte: O algoritmo para quando não existem mais caminhos 
   com capacidade acima de zero no grafo residual. Após a paragem, as arestas 
   residuais que ficaram completamente saturadas (capacidade igual a 0) e que 
   conectam a parte alcançável da rede à parte isolada indicam com precisão as 
   ruas que a polícia deve fechar (Corte Mínimo).

  ---

  ## Conversão do Resultado em Resposta

A conversão do fluxo máximo na resposta final do problema baseia-se no Teorema 
do Fluxo Máximo / Corte Mínimo e é feita em dois passos:

- Definição da Quantidade: Como cada rua tem capacidade 1, o valor total do 
   fluxo máximo final é numericamente idêntico à quantidade mínima de ruas que 
   a polícia precisa fechar.

- Identificação da Ruas: Uma BFS final parte do vértice 1 e caminha apenas por 
   arestas com capacidade acima de zero (e.cap > 0), dividindo a cidade em dois 
   lados: o lado alcançável (Grupo S) e o lado isolado (Grupo T). O código então 
   compara a lista de ruas originais e seleciona aquelas que conectam um vértice 
   do Grupo S a um vértice do Grupo T. Essas são as ruas saturadas que devem 
   ser exibidas na resposta.

  ---

  ## Corte Mínimo e Reconstrução de caminhos

O problema não utiliza emparelhamento, focando-se no corte mínimo e na sua 
reconstrução através de duas etapas:

- Tratamento do Corte (separação em S e T): Uma BFS final parte do vértice 1 e
  marca como visitados todos os nós alcançáveis por arestas com capacidade residual
  acima de zero (e.cap > 0). Isto divide o grafo em dois grupos: o Grupo S (alcançável) e o Grupo T (isolado).

- Reconstrução da Fronteira: O código percorre todas as ruas originais armazenadas na entrada e
  verifica em qual lado da partição cada extremidade ficou após a BFS residual. Quando uma extremidade
  pertence ao conjunto S (alcançável) e a outra ao conjunto T (não alcançável), a rua atravessa o corte
  mínimo e é incluída na resposta. Pelo teorema fluxo máximo–corte mínimo, essas ruas correspondem às conexões
  saturadas que separam a origem do destino e representam as vias que devem ser bloqueadas.

---

## Análise das Complexidades

- Complexidade de Tempo: O(V * E²)
   - Cada Busca em Largura (BFS) custa O(E) para encontrar o caminho mais curto.
   - O algoritmo de Edmonds-Karp garante que são feitas, no máximo, O(V * E) 
     buscas antes que a rede fique completamente saturada.
   - A multiplicação do custo de cada busca pelo número de iterações resulta em 
     O(V * E²), que domina o tempo total de execução. O pós-processamento do 
     corte mínimo é linear O(V + E) e não afeta este limite.

- Complexidade de Espaço: O(V + E)
   - O grafo residual utiliza uma lista de adjacências que armazena todos os 
     vértices e as arestas (incluindo as reversas), ocupando espaço O(V + E).
   - As estruturas auxiliares da BFS (vetores de visitados, pais e arestas pais) 
     e a lista para guardar as ruas originais consomem espaço linear O(V) e O(E).

---

## Casos Especiais

- GRAFO JÁ DESCONEXO:
   A primeira BFS falha imediatamente por não haver conexão entre 1 e n. O fluxo 
   máximo final é 0. Nenhuma rua é saturada, logo, nenhuma rua é impressa. O 
   ladrão já está isolado desde o começo.

- ARESTA DIRETA (1 -> N):
   Por usar BFS, o algoritmo sempre escolhe o caminho com menos arestas.
   A aresta direta será saturada na primeira iteração, mas sua presença no
   corte mínimo final dependerá da estrutura residual obtida após o término do algoritmo.

- MÚLTIPLAS SOLUÇÕES:
   Ocorre quando existem diferentes conjuntos de ruas capazes de separar a origem
   do destino com o mesmo custo mínimo. Nesses casos, o valor do fluxo máximo
   (e consequentemente do corte mínimo) será o mesmo para todas as soluções ótimas.
   Entretanto, o conjunto específico de ruas retornado pode variar de acordo com a ordem
   de exploração das arestas durante as buscas em largura (BFS) realizadas pelo algoritmo.
   Ainda assim, qualquer conjunto identificado dessa forma constitui um corte mínimo válido,
   o que é suficiente para atender aos requisitos do problema.

---

## Aceppted
