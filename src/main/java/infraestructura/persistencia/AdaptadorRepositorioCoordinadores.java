package infraestructura.persistencia;
import dominio.modelo.CoordinadorAcademico;
import dominio.puerto.repositorio.RepositorioCoordinadores;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioCoordinadores implements RepositorioCoordinadores {
    private static final String ARCHIVO = "coordinadores.bin";
    private List<CoordinadorAcademico> lista;

    public AdaptadorRepositorioCoordinadores() {
        List<CoordinadorAcademico> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(CoordinadorAcademico coordinador) {
        if (coordinador == null) throw new IllegalArgumentException("El coordinador no puede ser nulo");
        lista.add(coordinador);
        guardar();
    }

    @Override
    public Optional<CoordinadorAcademico> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(c -> c.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<CoordinadorAcademico> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(CoordinadorAcademico original, CoordinadorAcademico actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El coordinador no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(CoordinadorAcademico coordinador) {
        lista.remove(coordinador);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}