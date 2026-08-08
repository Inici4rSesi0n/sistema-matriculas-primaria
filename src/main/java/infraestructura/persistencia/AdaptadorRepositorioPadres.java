package infraestructura.persistencia;

import dominio.modelo.Padre;
import dominio.puerto.repositorio.RepositorioPadres;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioPadres implements RepositorioPadres, Recargable {
    private static final String ARCHIVO = "padres.bin";
    private List<Padre> lista;

    public AdaptadorRepositorioPadres() {
        List<Padre> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Padre padre) {
        if (padre == null) throw new IllegalArgumentException("El padre no puede ser nulo");
        lista.add(padre);
        guardar();
    }

    @Override
    public Optional<Padre> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(p -> p.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<Padre> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Padre original, Padre actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El padre no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Padre padre) {
        lista.remove(padre);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
    @Override
    public void recargar() {
        List<Padre> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null)?new ArrayList<>(cargada):new ArrayList<>();
    }
}