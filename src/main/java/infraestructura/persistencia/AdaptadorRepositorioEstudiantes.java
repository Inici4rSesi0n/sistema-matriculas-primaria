package infraestructura.persistencia;

import dominio.modelo.Estudiante;
import dominio.puerto.repositorio.RepositorioEstudiantes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioEstudiantes implements RepositorioEstudiantes {
    private static final String ARCHIVO = "estudiantes.bin";
    private List<Estudiante> lista;

    public AdaptadorRepositorioEstudiantes() {
        List<Estudiante> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Estudiante estudiante) {
        if (estudiante == null) throw new IllegalArgumentException("El estudiante no puede ser nulo");
        lista.add(estudiante);
        guardar();
    }

    @Override
    public Optional<Estudiante> buscarPorDni(String dni) {
        return lista.stream()
                .filter(e -> e.getDni().equalsIgnoreCase(dni))
                .findFirst();
    }

    @Override
    public Optional<Estudiante> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(e -> e.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<Estudiante> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Estudiante original, Estudiante actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El estudiante no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Estudiante estudiante) {
        lista.remove(estudiante);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}