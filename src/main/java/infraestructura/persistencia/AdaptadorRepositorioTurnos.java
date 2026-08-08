package infraestructura.persistencia;

import dominio.modelo.Turno;
import dominio.puerto.repositorio.RepositorioTurnos;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioTurnos implements RepositorioTurnos {
    private static final String ARCHIVO = "turnos.bin";
    private List<Turno> lista;

    public AdaptadorRepositorioTurnos() {
        List<Turno> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Turno turno) {
        if (turno == null) throw new IllegalArgumentException("El turno no puede ser nulo");
        lista.add(turno);
        guardar();
    }

    @Override
    public Optional<Turno> buscarPorNombre(String nombre) {
        return lista.stream()
                .filter(t -> t.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Turno> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Turno original, Turno actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El turno no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Turno turno) {
        lista.remove(turno);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}