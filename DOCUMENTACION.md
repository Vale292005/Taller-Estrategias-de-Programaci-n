# Programación Dinámica: Shortest Path in Grid y Problema de la Mochila

## 1. SHORTEST PATH IN GRID

### Problema
Dada una matriz m×n, encontrar el costo mínimo desde (0,0) hasta (m-1,n-1), moviéndose solo:
- **Hacia la derecha** → (i, j+1)
- **Hacia abajo** → (i+1, j)
- **En diagonal (abajo-derecha)** → (i+1, j+1)

### Relación de Recurrencia
```
dp[i][j] = matriz[i][j] + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])
```

**Interpretación:**
- `dp[i][j]` = costo mínimo para llegar a la posición (i,j)
- `matriz[i][j]` = costo de la celda actual
- `min(...)` = elegir el camino con menor costo previo

**Casos Base:**
- `dp[0][0] = matriz[0][0]`
- Primera fila: solo llegamos desde la izquierda
- Primera columna: solo llegamos desde arriba

### Complejidad

| Enfoque | Tiempo | Espacio |
|---------|--------|---------|
| Tabla 2D | O(m×n) | O(m×n) |
| Optimizado 1D | O(m×n) | **O(n)** |

### Optimización del Espacio a O(n)

En lugar de guardar una matriz completa `dp[m][n]`, usamos un array unidimensional de tamaño `n`:

1. **Inicialización**: Llenar la primera fila en el array
2. **Para cada fila i**:
   - Actualizar `dp[0]` (primera columna, solo desde arriba)
   - Para cada columna j:
     - Guardar `dp[j]` antes de actualizarlo (será diagonal para `j+1`)
     - Calcular min de arriba, izquierda, diagonal
     - Actualizar `dp[j]`

**Ventaja**: Reducimos memoria de O(m×n) a O(n), especialmente útil cuando m >> n.

### Ejemplo Paso a Paso
```
Matriz original:        Tabla DP (2D):           Array optimizado:
1 3 1                   1  4  5                  Fila 0: 1  4  5
1 5 1          →        2  7  6          →       Se reutiliza para cada fila
4 2 1                   6  8  7

Camino óptimo: (0,0) → (1,0) → (2,1) → (2,2) = 1+1+2+1 = 5
```

---

## 2. PROBLEMA DE LA MOCHILA (KNAPSACK 0/1)

### Problema
**Maximizar** la ganancia al elegir productos que caben en la mochila sin fraccionar.

**Datos:**
| Producto | Peso | Valor |
|----------|------|-------|
| A | 1 | 2 |
| B | 3 | 5 |
| C | 4 | 10 |
| D | 5 | 14 |
| E | 7 | 15 |

**Restricción**: Capacidad máxima = 8 kg

### Relación de Recurrencia
```
dp[i][w] = máximo valor usando los primeros i ítems con capacidad w

dp[i][w] = máx(
    dp[i-1][w],                        // No incluir el ítem i
    valor[i] + dp[i-1][w - peso[i]]  // Incluir el ítem i (si cabe)
)
```

**Interpretación:**
- Para cada ítem i y capacidad w, decidimos si incluirlo o no
- Si incluimos: añadimos su valor pero perdemos espacio
- Si no incluimos: perdemos el valor pero mantenemos la capacidad

### Complejidad
- **Tiempo**: O(n × W) donde n = # ítems, W = capacidad
- **Espacio**: O(n × W) para tabla 2D, O(W) optimizado

### Cómo se Construye la Tabla

La tabla `dp[i][w]` tiene:
- **Filas**: 0 a n (número de ítems + 1)
- **Columnas**: 0 a W (capacidades posibles)

**Construcción:**
```
      w=0  w=1  w=2  w=3  w=4  w=5  w=6  w=7  w=8
i=0    0    0    0    0    0    0    0    0    0   ← No hay ítems
i=A    0    2    2    2    2    2    2    2    2   ← Ítem A (peso=1, valor=2)
i=B    0    2    2    5    7    7    7    7    7   ← Agregar B
i=C    0    2    2    5   10   12   12   15   17   ← Agregar C
i=D    0    2    2    5   10   14   16   16   19   ← Agregar D
i=E    0    2    2    5   10   14   16   16   19   ← Agregar E
       ↓
    Respuesta final: dp[5][8] = 19
```

### ¿Por Qué NO Funciona el Enfoque VORAZ?

El enfoque voraz intenta elegir los mejores ítems localmente sin considerar combinaciones óptimas.

**Ejemplo de Fallo:**

1. **Ordenar por relación valor/peso** (mejor por "unidad"):
   - D: 14/5 = 2.80 ← Mejor
   - C: 10/4 = 2.50
   - E: 15/7 = 2.14
   - A: 2/1 = 2.00
   - B: 5/3 = 1.67

2. **Solución VORAZ:**
   ```
   Tomar D (peso=5, valor=14)  ← Cabe, toman. Capacidad restante: 3
   Tomar C (peso=4, valor=10)  ← NO cabe (4 > 3)
   Tomar E (peso=7, valor=15)  ← NO cabe
   Tomar A (peso=1, valor=2)   ← Cabe. Capacidad restante: 2
   
   Total: D + A = Peso=6, VALOR=16 ❌
   ```

3. **Solución ÓPTIMA (DP):**
   ```
   Tomar C (peso=4, valor=10)
   Tomar B (peso=3, valor=5)
   Tomar A (peso=1, valor=2)
   
   Total: A + B + C = Peso=8, VALOR=17 ✓ 
   ```

**Diferencia**: DP = 17 vs Voraz = 16 (**Pérdida de 1 unidad de valor**)

### ¿Por Qué el Voraz Falla?

El voraz **no considera combinaciones** de ítems más pequeños que juntos pueden ser más valiosos. En este ejemplo:
- D tiene excelente relación valor/peso, pero ocupa mucho espacio
- B+A juntos pueden llenar lo que queda mejor que nada
- La combinación A+B+C es mejor que D+A

### Reconstruir la Solución (Qué Ítems Elegir)

Para saber **cuáles ítems seleccionar**, retrotrazamos desde `dp[n][W]`:

```
Empezar en dp[5][8] = 19
Si dp[5][8] ≠ dp[4][8]: Incluir ítem E → retroceder a dp[4][8]
Si dp[4][8] ≠ dp[3][8]: Incluir ítem D → retroceder a dp[3][8]
...
```

**Resultado**: En nuestro ejemplo = B + D (peso=8, valor=19)

---

## Comparación: DP vs Voraz

| Aspecto | Programación Dinámica | Voraz |
|---------|----------------------|-------|
| **Para Knapsack** | ✓ Óptimo siempre | ✗ Falla |
| **Para Shortest Path** | ✓ Óptimo | ✓ Óptimo* |
| **Tiempo** | O(n×W) o O(m×n) | O(n log n) |
| **Espacio** | O(n×W) o O(m×n) | O(n) |
| **Facilidad** | Media (requiere tabla) | Fácil |

*En shortest path el voraz funciona solo si los pesos son no negativos.

---

## Cuándo Usar Cada Enfoque

### Programación Dinámica
✓ Problemas con **subestructura óptima** (solución óptima se compone de soluciones óptimas de subproblemas)  
✓ **Subproblemas superpuestos** (se repiten cálculos)  
✓ Cuando queremos la solución **óptima garantizada**

### Enfoque Voraz
✓ Cuando el problema tiene **propiedad voraz** (elección local = óptima global)  
✓ Cuando necesitamos **máxima eficiencia** (tiempo O(n log n) o mejor)  
✓ Ejemplos: cambio de monedas (denominaciones estándar), selección de actividades

---

## Archivos en Este Proyecto

- **ShortestPathGrid.java**: 3 implementaciones del shortest path con explicaciones
- **KnapsackProblem.java**: 3 implementaciones del knapsack con demostración de por qué falla el voraz
- **DOCUMENTACION.md**: Este archivo

## Cómo Ejecutar

```bash
# Compilar y ejecutar Shortest Path
javac ShortestPathGrid.java
java ShortestPathGrid

# Compilar y ejecutar Knapsack
javac KnapsackProblem.java
java KnapsackProblem
```

Ambos programas incluyen ejemplos ejecutables y muestran paso a paso cómo trabajan los algoritmos.
