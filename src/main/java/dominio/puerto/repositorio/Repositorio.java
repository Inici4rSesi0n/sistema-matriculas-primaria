package dominio.puerto.repositorio;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
public interface Repositorio<T> {
    void agregar(T entidad);
    List<T> listarTodos();
    void actualizar(T original, T actualizada);
    void eliminar(T entidad);
}