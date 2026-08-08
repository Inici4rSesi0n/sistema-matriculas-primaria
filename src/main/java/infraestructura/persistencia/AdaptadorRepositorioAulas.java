package infraestructura.persistencia;
import dominio.modelo.Aula;
import dominio.puerto.repositorio.RepositorioAulas;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioAulas implements RepositorioAulas {
    private static final String ARCHIVO = "aulas.bin";
    private List<Aula> lista;

    public AdaptadorRepositorioAulas() {
        List<Aula> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Aula aula) {
        if (aula == null) throw new IllegalArgumentException("El aula no puede ser nula");
        lista.add(aula);
        guardar();
    }

    @Override
    public Optional<Aula> buscarPorNombre(String nombre) {
        return lista.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Aula> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Aula original, Aula actualizada) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El aula no existe");
        lista.set(idx, actualizada);
        guardar();
    }

    @Override
    public void eliminar(Aula aula) {
        lista.remove(aula);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}