package infraestructura.persistencia;

import dominio.modelo.Matricula;
import dominio.puerto.repositorio.RepositorioMatriculas;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioMatriculas implements RepositorioMatriculas, Recargable {
    private static final String ARCHIVO = "matriculas.bin";
    private List<Matricula> lista;

    public AdaptadorRepositorioMatriculas() {
        List<Matricula> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Matricula matricula) {
        if (matricula == null) throw new IllegalArgumentException("La matrícula no puede ser nula");
        lista.add(matricula);
        guardar();
    }

    @Override
    public Optional<Matricula> buscarPorEstudianteYPeriodo(String codigoEstudiante, String nombrePeriodo) {
        return lista.stream()
                .filter(m -> m.getEstudiante() != null && m.getPeriodo() != null)
                .filter(m -> m.getEstudiante().getCodigo().equalsIgnoreCase(codigoEstudiante)
                        && m.getPeriodo().getNombre().equalsIgnoreCase(nombrePeriodo))
                .findFirst();
    }

    @Override
    public List<Matricula> buscarPorGrupo(String nombreGrupo) {
        return lista.stream()
                .filter(m -> m.getGrupo() != null && m.getGrupo().getNombre().equalsIgnoreCase(nombreGrupo))
                .collect(Collectors.toList());
    }

    @Override
    public List<Matricula> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Matricula original, Matricula actualizada) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("La matrícula no existe");
        lista.set(idx, actualizada);
        guardar();
    }

    @Override
    public void eliminar(Matricula matricula) {
        lista.remove(matricula);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
    @Override
    public void recargar() {
        List<Matricula> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null)?new ArrayList<>(cargada):new ArrayList<>();
    }
}