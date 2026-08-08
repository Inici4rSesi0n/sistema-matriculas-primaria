package infraestructura.persistencia;

import dominio.modelo.Secretario;
import dominio.puerto.repositorio.RepositorioSecretarios;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioSecretarios implements RepositorioSecretarios, Recargable {
    private static final String ARCHIVO = "secretarios.bin";
    private List<Secretario> lista;

    public AdaptadorRepositorioSecretarios() {
        List<Secretario> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Secretario secretario) {
        if (secretario == null) throw new IllegalArgumentException("El secretario no puede ser nulo");
        lista.add(secretario);
        guardar();
    }

    @Override
    public Optional<Secretario> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(s -> s.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<Secretario> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Secretario original, Secretario actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El secretario no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Secretario secretario) {
        lista.remove(secretario);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
    @Override
    public void recargar() {
        List<Secretario> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null)?new ArrayList<>(cargada):new ArrayList<>();
    }
}