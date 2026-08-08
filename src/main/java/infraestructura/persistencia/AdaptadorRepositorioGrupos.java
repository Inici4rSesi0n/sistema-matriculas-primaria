package infraestructura.persistencia;

import dominio.modelo.Grupo;
import dominio.puerto.repositorio.RepositorioGrupos;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author inici4rsesi0n
 */
public class AdaptadorRepositorioGrupos implements RepositorioGrupos {
    private static final String ARCHIVO = "grupos.bin";
    private List<Grupo> lista;

    public AdaptadorRepositorioGrupos() {
        List<Grupo> cargada = ManejadorPersistencia.cargar(ARCHIVO);
        this.lista = (cargada != null) ? new ArrayList<>(cargada) : new ArrayList<>();
    }

    @Override
    public void agregar(Grupo grupo) {
        if (grupo == null) throw new IllegalArgumentException("El grupo no puede ser nulo");
        lista.add(grupo);
        guardar();
    }

    @Override
    public Optional<Grupo> buscarPorNombre(String nombre) {
        return lista.stream()
                .filter(g -> g.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Grupo> buscarPorNombreYGrado(String nombreGrupo, String nombreGrado) {
        return lista.stream()
                .filter(g -> g.getGrado() != null)
                .filter(g -> g.getNombre().equalsIgnoreCase(nombreGrupo)
                        && g.getGrado().getNombre().equalsIgnoreCase(nombreGrado))
                .collect(Collectors.toList());
    }

    @Override
    public List<Grupo> listarTodos() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(Grupo original, Grupo actualizado) {
        int idx = lista.indexOf(original);
        if (idx == -1) throw new IllegalArgumentException("El grupo no existe");
        lista.set(idx, actualizado);
        guardar();
    }

    @Override
    public void eliminar(Grupo grupo) {
        lista.remove(grupo);
        guardar();
    }

    private void guardar() {
        ManejadorPersistencia.guardar(new ArrayList<>(lista), ARCHIVO);
    }
}