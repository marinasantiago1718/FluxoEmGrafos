# Relatório Técnico: O Problema de Fechamento de Ruas (Corte Mínimo)

## 1. Contexto do Problema e Objetivo
O problema consiste em uma situação de perseguição policial: um assaltante (Kaaleppi) acabou de roubar um banco (localizado no cruzamento `1`) e está fugindo em direção ao porto (localizado no cruzamento `n`).
Para pará-lo, a polícia deseja fechar estrategicamente o menor número possível de ruas, eliminando completamente qualquer rota viável entre o banco e o porto.

**O que precisa ser calculado:**
* O **número mínimo** de ruas que precisam ser interditadas.
* **Quais** ruas específicas devem ser fechadas para isolar o porto do banco.

---

## 2. Processamento da Entrada
A entrada do programa fornece a descrição da infraestrutura viária da cidade e deve ser lida linha por linha:

* **Primeira linha:** Contém dois inteiros, `n` e `m`.
  * `n`: O número total de cruzamentos (vértices do grafo), indexados de $1$ até $n$.
  * `m`: O número total de ruas que conectam esses cruzamentos (arestas).
* **Próximas `m` linhas:** Cada linha contém dois inteiros `a` e `b`, indicando a existência de uma rua bidirecional conectando diretamente o cruzamento `a` ao cruzamento `b`. 

---

## 3. Saída Esperada e Modelagem Teórica
A saída deve imprimir o inteiro `k` (quantidade de ruas fechadas) seguido por `k` linhas, onde cada linha indica os cruzamentos de uma rua interditada.

Essa saída é obtida mapeando o problema de fechamento de ruas no conceito de **Corte Mínimo (Min-Cut)** em uma rede de fluxo.
Pelo *Teorema do Fluxo Máximo / Corte Mínimo*, o gargalo que limita o fluxo máximo de uma rede corresponde exatamente ao conjunto mínimo de arestas que, se removidas, desconectam a origem do destino.
Portanto, o valor do fluxo máximo nos dá a quantidade `k`, e a análise do grafo residual final revela quais ruas compõem o corte.

---

## 4. Modelagem da Rede de Fluxo

### Vértices da Rede
Os vértices do grafo representam diretamente os **cruzamentos da cidade**. O conjunto de vértices é modelado como $V = \{1, 2, \dots, n\}$.
Não há necessidade de criar vértices virtuais ou adicionais, pois a topologia original da cidade mapeia perfeitamente os pontos de decisão da fuga.

### Origem (Source) e Sorvedouro (Sink)
* **Origem ($s$):** Cruzamento `1` (o Banco). Faz sentido porque é o ponto inicial da fuga, de onde o "fluxo" do assaltante se origina.
* **Sorvedouro ($t$):** Cruzamento `n` (o Porto). Faz sentido porque é o destino final e o ponto de escape do assaltante.
*  O objetivo do algoritmo é impedir que qualquer fluxo chegue aqui.

### Arestas, Direções e Capacidades
Como as ruas são de mão dupla (bidirecionais), para cada rua entre `a` e `b` informada na entrada, criamos **duas arestas direcionadas** na nossa rede de fluxo residual:
1. Uma aresta direcionada de `a` para `b` com **capacidade 1.0**.
2. Uma aresta direcionada de `b` para `a` com **capacidade 1.0**.



### Justificativa das Capacidades
Cada rua pode ser usada ou não para a fuga, e o objetivo é minimizar o *número* de ruas fechadas, independentemente de haver "trânsito" nelas.
Atribuir **capacidade unitária (1.0)** para cada sentido da rua garante que cada rua conte exatamente como $1$ no somatório do corte.
Desse modo, o fluxo máximo corresponderá fielmente ao menor número de ruas necessárias para cortar a comunicação entre $s$ e $t$.

---

## 5. Análise e Escolha do Algoritmo
O grupo optou pelo algoritmo de **Edmonds-Karp**. 

**Justificativa:** Embora o método de Ford-Fulkerson puro (usando DFS) fosse teoricamente suficiente devido às capacidades unitárias da rede ($f \le m$), a escolha pelo Edmonds-Karp traz previsibilidade algorítmica.
Ao utilizar uma Busca em Largura (**BFS**) para encontrar os caminhos aumentantes, o Edmonds-Karp garante a escolha do caminho com o menor número de arestas a cada iteração.
Isso blinda a aplicação contra caminhos ineficientes ou loops redundantes no grafo residual, mitigando riscos de *Time Limit Exceeded (TLE)* e garantindo uma complexidade de pior caso limitada estritamente a $O(V \cdot E^2)$.

---

## 6. Resolução da Instância do Enunciado

### Instância de Exemplo
* Vértices: $4$, Arestas: $5$
* Ruas: `1-2`, `1-3`, `2-3`, `3-4`, `1-4`

### Execução Passo a Passo (Edmonds-Karp)

* **Estado Inicial:** Todas as arestas com fluxo = 0.
* **Iteração 1:**
  * **BFS** encontra o caminho mais curto: $1 \rightarrow 4$.
  * **Gargalo:** $\min(1.0) = 1.0$.
  * **Atualização:** O fluxo na aresta $1 \rightarrow 4$ torna-se 1.0 (Aresta saturada). Fluxo total acumulado = 1.0.
* **Iteração 2:**
  * **BFS** encontra outro caminho mais curto: $1 \rightarrow 3 \rightarrow 4$.
  * **Gargalo:** $\min(1.0, 1.0) = 1.0$.
  * **Atualização:** O fluxo nas arestas $1 \rightarrow 3$ e $3 \rightarrow 4$ torna-se 1.0 (Ambas saturadas). Fluxo total acumulado = 2.0.
* **Iteração 3:**
  * **BFS** tenta encontrar um novo caminho. As arestas que levam ao nó destino $4$ ($1 \rightarrow 4$ e $3 \rightarrow 4$) estão com capacidade residual zero. Não há caminhos disponíveis de $1$ para $4$.
  * **Fim do algoritmo.** Fluxo Máximo = **2.0**.

---

## 7. Recuperação da Resposta Final (Extração do Corte Mínimo)

Após o término das iterações do Edmonds-Karp, a estrutura mantém os estados da última BFS realizada a partir da origem $s=1$. Essa busca visitou todos os vértices que ainda possuem capacidade residual positiva a partir de $1$.

* **Vértices Alcançáveis (Lado $S$ do corte):** $\{1, 2, 3\}$
  * *(Nota: O nó 2 é alcançado via $1 \rightarrow 2$, o nó 3 é alcançado via $1 \rightarrow 3$ ou $2 \rightarrow 3$, mas o nó 4 está isolado).*
* **Vértices Não Alcançáveis (Lado $T$ do corte):** $\{4\}$

Para descobrir quais ruas fechar, percorremos a lista de **ruas originais** fornecidas na entrada. Uma rua que conecta um vértice $u$ a um vértice $v$ deve ser fechada se, e somente se, um dos vértices foi alcançado pela BFS e o outro não.

**Verificação no exemplo:**
* Rua `1-2`: Ambos alcançáveis $\rightarrow$ Não fechar.
* Rua `1-3`: Ambos alcançáveis $\rightarrow$ Não fechar.
* Rua `2-3`: Ambos alcançáveis $\rightarrow$ Não fechar.
* Rua `3-4`: $3$ é alcançável, $4$ não é $\rightarrow$ **FECHAR!**
* Rua `1-4`: $1$ é alcançável, $4$ não é $\rightarrow$ **FECHAR!**

**Resultado impresso:**
```text
2
3 4
1 4
