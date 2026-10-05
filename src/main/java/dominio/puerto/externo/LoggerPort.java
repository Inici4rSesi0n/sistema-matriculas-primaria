package dominio.puerto.externo;

/**
 * Puerto de logging del sistema.
 * Define las operaciones de registro categorizadas por propósito.
 *
 * Categorías:
 *   - General (info/warn/error/debug/trace): sistema en general.
 *   - audit: operaciones críticas de negocio.
 *   - eventBus: trazabilidad de eventos entre controladores.
 *   - ui: navegación y refrescos (solo desarrollo).
 *
 * @author inici4rsesi0n
 */
public interface LoggerPort {

    // ============ General ============

    void info(String mensaje);
    void info(String formato, Object... argumentos);
    void warn(String mensaje);
    void warn(String formato, Object... argumentos);
    void error(String mensaje);
    void error(String formato, Object... argumentos);
    void error(String mensaje, Throwable excepcion);
    void debug(String mensaje);
    void debug(String formato, Object... argumentos);
    void trace(String mensaje);
    void trace(String formato, Object... argumentos);

    // ============ Audit (negocio) ============

    void audit(String mensaje);
    void audit(String formato, Object... argumentos);

    // ============ EventBus (trazabilidad) ============

    void eventBus(String mensaje);
    void eventBus(String formato, Object... argumentos);

    // ============ UI (navegación) ============

    void ui(String mensaje);
    void ui(String formato, Object... argumentos);
}
