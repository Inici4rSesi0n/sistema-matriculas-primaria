package infraestructura.log;
import dominio.puerto.externo.LoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 *
 * @author inici4rsesi0n
 */
@Component
public class Slf4jLoggerAdapter implements LoggerPort {
    private final Logger logger;
    public Slf4jLoggerAdapter() {
        this.logger = LoggerFactory.getLogger("SchoolBase");
    }
    @Override
    public void info(String mensaje) {
        logger.info(mensaje);
    }
    @Override
    public void info(String formato, Object... argumentos) {
        logger.info(formato, argumentos);
    }
    @Override
    public void warn(String mensaje) {
        logger.warn(mensaje);
    }
    @Override
    public void warn(String formato, Object... argumentos) {
        logger.warn(formato, argumentos);
    }
    @Override
    public void error(String mensaje) {
        logger.error(mensaje);
    }
    @Override
    public void error(String formato, Object... argumentos) {
        logger.error(formato, argumentos);
    }
    @Override
    public void error(String mensaje, Throwable excepcion) {
        logger.error(mensaje, excepcion);
    }
    @Override
    public void debug(String mensaje) {
        logger.debug(mensaje);
    }
    @Override
    public void debug(String formato, Object... argumentos) {
        logger.debug(formato, argumentos);
    }
    @Override
    public void trace(String mensaje) {
        logger.trace(mensaje);
    }
    @Override
    public void trace(String formato, Object... argumentos) {
        logger.trace(formato, argumentos);
    }
}