# MercadoLibre Perú — QA Avanzado 🇵🇪
## Karate DSL + Selenium | Nivel Avanzado

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Maven** — Gestión de dependencias
- **Selenium WebDriver** — Automatización de UI
- **Karate DSL** — Testing de APIs REST
- **JUnit 5** — Framework de testing
- **WebDriverManager** — Gestión automática de drivers

---

## ⚡ Setup en IntelliJ IDEA (5 minutos)

### 1. Abrir el proyecto
- `File → Open` → seleccionar la carpeta `meli-qa-avanzado`
- IntelliJ detecta automáticamente que es un proyecto Maven

### 2. Importar dependencias
- Esperar a que aparezca el popup "Maven project found" → clic en **Load**
- O ir a la pestaña **Maven** (panel derecho) → clic en el ícono 🔄 Reload

### 3. Verificar Java 17+
- `File → Project Structure → SDK` → debe ser Java 17 o superior
- Si no tienes Java 17: `File → Project Structure → SDKs → +` → descargar

### 4. ¡Listo! Ejecutar tests

---

## 🧪 Estructura del proyecto

```
meli-qa-avanzado/
├── pom.xml                                          ← Dependencias Maven
└── src/test/
    ├── java/pe/meli/
    │   ├── pages/
    │   │   └── SearchPage.java                      ← Page Object Model
    │   └── tests/
    │       ├── karate/
    │       │   └── KarateRunnerTest.java             ← Runner de todos los Karate tests
    │       └── selenium/
    │           ├── BaseTest.java                     ← Setup/teardown del WebDriver
    │           ├── Ejercicio5_POMTest.java           ← Tests con Page Object
    │           └── Ejercicio6_FlujoCompletoTest.java ← Flujos encadenados
    └── resources/
        ├── karate-config.js                         ← Config global de Karate
        └── karate/meli/
            ├── 01_busqueda_schema.feature            ← Schema Validation
            ├── 02_chaining.feature                   ← API Chaining
            ├── 03_reusable_outline.feature           ← Reusable + Data-Driven
            ├── 04_negative_testing.feature           ← Negative Testing
            └── util_busqueda.feature                 ← Feature reutilizable (@ignore)
```

---

## ▶️ Cómo ejecutar

### Opción A: Un test específico (recomendado para practicar)
1. Abrir el archivo que quieres ejecutar (ej: `01_busqueda_schema.feature`)
2. Clic en el ícono ▶️ verde junto a un `Scenario:`
3. O abrir `KarateRunnerTest.java` → clic ▶️ junto al método deseado

### Opción B: Todos los tests
- Clic derecho sobre `KarateRunnerTest.java` → **Run 'KarateRunnerTest'**
- Clic derecho sobre `Ejercicio5_POMTest.java` → **Run 'Ejercicio5_POMTest'**

### Opción C: Terminal Maven
```bash
# Solo Karate
mvn test -Dtest=KarateRunnerTest

# Solo Selenium
mvn test -Dtest=Ejercicio5_POMTest,Ejercicio6_FlujoCompletoTest

# Todos
mvn test
```

---

## 📚 Conceptos nuevos en este nivel

| # | Feature | Concepto clave |
|---|---------|---------------|
| 01 | Schema Validation | `match each`, `#string`, `#number`, `#regex` |
| 02 | API Chaining | Guardar respuesta → usarla en siguiente llamada |
| 03 | Reusable + Outline | `@ignore`, `call read()`, `Scenario Outline` |
| 04 | Negative Testing | Validar 404, query vacía, headers |
| 05 | Page Object Model | Separar localizadores de la lógica de tests |
| 06 | Flujo completo | Encadenar pasos, `assertAll`, comparar datos |

---

## 🔧 Solución de problemas comunes

**"ChromeDriver no encontrado"**
→ WebDriverManager lo descarga automático. Verifica que tengas internet al primer run.

**"Element not interactable" en Selenium**
→ Agrega `--headless=new` en BaseTest si el browser no abre bien en tu PC.
→ O aumenta el timeout en `WebDriverWait(driver, Duration.ofSeconds(20))`

**Tests de Karate fallan con timeout**
→ Verifica tu conexión. La API de ML puede tener límite de rate.
→ Karate reintenta automáticamente, pero puedes agregar `* configure retry = {count: 2, interval: 3000}` en el Background.

**"Cannot resolve symbol 'Karate'"**
→ Clic derecho en `pom.xml` → Maven → Reload Project
