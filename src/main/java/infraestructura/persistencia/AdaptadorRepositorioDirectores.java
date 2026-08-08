package infraestructura.persistencia;

import dominio.modelo.Director;
import dominio.puerto.repositorio.RepositorioDirectores;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioDirectores implements RepositorioDirectores, Recargable {
    private static final String ARCHIVO = "directores.bin";
    private List<Director> lista;

    public AdaptadorRepositorioDirectores() {
        List<Director> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Director director) {
        if (director == null) throw new IllegalArgumentException("El director no puede ser nulo");
        lista.add(director);
        guardar();
    }

    @Override
    public Optional<Director> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(d -> d.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<Director> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Director original, Director actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El director no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Director director) {
        lista.remove(director);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
    @Override
    public void recargar() {
        List<Director> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null)?new ArrayList<>(cargada):new ArrayList<>();
    }
}