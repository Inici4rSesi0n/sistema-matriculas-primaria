package infraestructura.persistencia;

import dominio.modelo.Docente;
import dominio.puerto.repositorio.RepositorioDocentes;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioDocentes implements RepositorioDocentes, Recargable {
    private static final String ARCHIVO = "docentes.bin";
    private List<Docente> lista;

    public AdaptadorRepositorioDocentes() {
        List<Docente> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Docente docente) {
        if (docente == null) throw new IllegalArgumentException("El docente no puede ser nulo");
        lista.add(docente);
        guardar();
    }

    @Override
    public Optional<Docente> buscarPorDni(String dni) {
        return lista.stream()
                .filter(d -> d.getDni().equalsIgnoreCase(dni))
                .findFirst();
    }

    @Override
    public Optional<Docente> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(d -> d.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<Docente> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Docente original, Docente actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El docente no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Docente docente) {
        lista.remove(docente);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
    @Override
    public void recargar() {
        List<Docente> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null)?new ArrayList<>(cargada):new ArrayList<>();
    }
}