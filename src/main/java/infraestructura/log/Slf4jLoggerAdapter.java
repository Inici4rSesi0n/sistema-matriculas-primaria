package infraestructura.log;

import dominio.puerto.externo.LoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adaptador de logging basado en SLF4J.
 * Enruta cada categoría del puerto a un logger jerárquico distinto.
 *
 * @author inici4rsesi0n
 */
@Component
public class Slf4jLoggerAdapter implements LoggerPort {

    private static final String ROOT_NAME = "WiredAcademy";
    private static final String AUDIT_NAME = "WiredAcademy.AUDIT";
    private static final String EVENTBUS_NAME = "WiredAcademy.EVENTBUS";
    private static final String UI_NAME = "WiredAcademy.UI";

    private final Logger root;
    private final Logger audit;
    private final Logger eventBus;
    private final Logger ui;

    public Slf4jLoggerAdapter() {
        this.root = LoggerFactory.getLogger(ROOT_NAME);
        this.audit = LoggerFactory.getLogger(AUDIT_NAME);
        this.eventBus = LoggerFactory.getLogger(EVENTBUS_NAME);
        this.ui = LoggerFactory.getLogger(UI_NAME);
    }

    // ============ General ============

    @Override public void info(String mensaje) { root.info(mensaje); }
    @Override public void info(String formato, Object... argumentos) { root.info(formato, argumentos); }

    @Override public void warn(String mensaje) { root.warn(mensaje); }
    @Override public void warn(String formato, Object... argumentos) { root.warn(formato, argumentos); }

    @Override public void error(String mensaje) { root.error(mensaje); }
    @Override public void error(String formato, Object... argumentos) { root.error(formato, argumentos); }
    @Override public void error(String mensaje, Throwable excepcion) { root.error(mensaje, excepcion); }

    @Override public void debug(String mensaje) { root.debug(mensaje); }
    @Override public void debug(String formato, Object... argumentos) { root.debug(formato, argumentos); }

    @Override public void trace(String mensaje) { root.trace(mensaje); }
    @Override public void trace(String formato, Object... argumentos) { root.trace(formato, argumentos); }

    // ============ Audit ============

    @Override public void audit(String mensaje) { audit.info(mensaje); }
    @Override public void audit(String formato, Object... argumentos) { audit.info(formato, argumentos); }

    // ============ EventBus ============

    @Override public void eventBus(String mensaje) { eventBus.debug(mensaje); }
    @Override public void eventBus(String formato, Object... argumentos) { eventBus.debug(formato, argumentos); }

    // ============ UI ============

    @Override public void ui(String mensaje) { ui.trace(mensaje); }
    @Override public void ui(String formato, Object... argumentos) { ui.trace(formato, argumentos); }
}
