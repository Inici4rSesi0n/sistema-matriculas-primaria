package infraestructura.persistencia;

import dominio.modelo.PeriodoAcademico;
import dominio.puerto.repositorio.RepositorioPeriodos;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioPeriodos implements RepositorioPeriodos, Recargable {
    private static final String ARCHIVO = "periodos.bin";
    private List<PeriodoAcademico> lista;

    public AdaptadorRepositorioPeriodos() {
        List<PeriodoAcademico> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(PeriodoAcademico periodo) {
        if (periodo == null) throw new IllegalArgumentException("El periodo académico no puede ser nulo");
        lista.add(periodo);
        guardar();
    }

    @Override
    public Optional<PeriodoAcademico> buscarPorNombre(String nombre) {
        return lista.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<PeriodoAcademico> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(PeriodoAcademico original, PeriodoAcademico actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El periodo académico no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(PeriodoAcademico periodo) {
        lista.remove(periodo);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
    @Override
    public void recargar() {
        List<PeriodoAcademico> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null)?new ArrayList<>(cargada):new ArrayList<>();
    }
}