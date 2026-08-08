package infraestructura.persistencia;

import dominio.modelo.Administrador;
import dominio.puerto.repositorio.RepositorioAdministradores;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioAdministradores implements RepositorioAdministradores {
    private static final String ARCHIVO = "administradores.bin";
    private List<Administrador> lista;

    public AdaptadorRepositorioAdministradores() {
        List<Administrador> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Administrador administrador) {
        if (administrador == null) throw new IllegalArgumentException("El administrador no puede ser nulo");
        lista.add(administrador);
        guardar();
    }

    @Override
    public Optional<Administrador> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(a -> a.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<Administrador> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Administrador original, Administrador actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El administrador no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Administrador administrador) {
        lista.remove(administrador);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}