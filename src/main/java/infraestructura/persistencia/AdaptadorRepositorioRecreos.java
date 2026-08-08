package infraestructura.persistencia;

import dominio.modelo.Recreo;
import dominio.puerto.repositorio.RepositorioRecreos;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioRecreos implements RepositorioRecreos, Recargable {
    private static final String ARCHIVO = "recreos.bin";
    private List<Recreo> lista;

    public AdaptadorRepositorioRecreos() {
        List<Recreo> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Recreo recreo) {
        if (recreo == null) throw new IllegalArgumentException("El recreo no puede ser nulo");
        lista.add(recreo);
        guardar();
    }

    @Override
    public Optional<Recreo> buscarPorDescripcion(String descripcion) {
        return lista.stream()
                .filter(r -> r.getDescripcion().equalsIgnoreCase(descripcion))
                .findFirst();
    }

    @Override
    public List<Recreo> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Recreo original, Recreo actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El recreo no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Recreo recreo) {
        lista.remove(recreo);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
    @Override
    public void recargar() {
        List<Recreo> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null)?new ArrayList<>(cargada):new ArrayList<>();
    }
}