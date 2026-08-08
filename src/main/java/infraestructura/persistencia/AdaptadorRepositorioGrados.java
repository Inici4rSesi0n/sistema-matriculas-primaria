package infraestructura.persistencia;

import dominio.modelo.Grado;
import dominio.puerto.repositorio.RepositorioGrados;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioGrados implements RepositorioGrados {
    private static final String ARCHIVO = "grados.bin";
    private List<Grado> lista;

    public AdaptadorRepositorioGrados() {
        List<Grado> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Grado grado) {
        if (grado == null) throw new IllegalArgumentException("El grado no puede ser nulo");
        lista.add(grado);
        guardar();
    }

    @Override
    public Optional<Grado> buscarPorNombre(String nombre) {
        return lista.stream()
                .filter(g -> g.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Grado> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Grado original, Grado actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El grado no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Grado grado) {
        lista.remove(grado);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}