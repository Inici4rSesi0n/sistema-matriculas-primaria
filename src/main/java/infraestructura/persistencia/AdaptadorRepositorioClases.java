package infraestructura.persistencia;
import dominio.modelo.Clase;
import dominio.puerto.repositorio.RepositorioClases;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioClases implements RepositorioClases {
    private static final String ARCHIVO = "clases.bin";
    private List<Clase> lista;

    public AdaptadorRepositorioClases() {
        List<Clase> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Clase clase) {
        if (clase == null) throw new IllegalArgumentException("La clase no puede ser nula");
        lista.add(clase);
        guardar();
    }

    @Override
    public List<Clase> buscarPorGrupoYGrado(String nombreGrupo, String nombreGrado) {
        List<Clase> resultado = lista.stream()
                .filter(c -> c.getGrupo() != null && c.getGrupo().getGrado() != null)
                .filter(c -> c.getGrupo().getNombre().equalsIgnoreCase(nombreGrupo)
                        && c.getGrupo().getGrado().getNombre().equalsIgnoreCase(nombreGrado))
                .sorted(Comparator.comparing(Clase::getDiaSemana).thenComparing(Clase::getHoraInicio))
                .collect(Collectors.toList());
        return resultado;
    }

    @Override
    public boolean existeDocenteEnHorario(String codigoDocente, String dia, String horaInicio, String horaFin) {
        return lista.stream().anyMatch(c -> c.getDocente() != null
                && c.getDocente().getCodigo().equals(codigoDocente)
                && c.getDiaSemana().equals(dia)
                && c.getHoraInicio().equals(horaInicio)
                && c.getHoraFin().equals(horaFin));
    }

    @Override
    public Optional<Clase> buscarClase(String nombreGrado, String nombreGrupo, String dia, String horaInicio, String horaFin) {
        return lista.stream()
                .filter(c -> c.getGrupo() != null && c.getGrupo().getGrado() != null)
                .filter(c -> c.getGrupo().getGrado().getNombre().equalsIgnoreCase(nombreGrado)
                        && c.getGrupo().getNombre().equalsIgnoreCase(nombreGrupo)
                        && c.getDiaSemana().equals(dia)
                        && c.getHoraInicio().equals(horaInicio)
                        && c.getHoraFin().equals(horaFin))
                .findFirst();
    }

    @Override
    public List<Clase> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Clase original, Clase actualizada) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("La clase no existe");
        lista.set(idx, actualizada);
        guardar();
    }

    @Override
    public void eliminar(Clase clase) {
        lista.remove(clase);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}