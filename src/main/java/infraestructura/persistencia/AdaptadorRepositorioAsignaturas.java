package infraestructura.persistencia;
import dominio.modelo.Asignatura;
import dominio.puerto.repositorio.RepositorioAsignaturas;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioAsignaturas implements RepositorioAsignaturas {
    private static final String ARCHIVO = "asignaturas.bin";
    private List<Asignatura> lista;

    public AdaptadorRepositorioAsignaturas() {
        List<Asignatura> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Asignatura asignatura) {
        if (asignatura == null) throw new IllegalArgumentException("La asignatura no puede ser nula");
        lista.add(asignatura);
        guardar();
    }

    @Override
    public Optional<Asignatura> buscarPorNombre(String nombre) {
        return lista.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Asignatura> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Asignatura original, Asignatura actualizada) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("La asignatura no existe");
        lista.set(idx, actualizada);
        guardar();
    }

    @Override
    public void eliminar(Asignatura asignatura) {
        lista.remove(asignatura);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}