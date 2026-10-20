package infraestructura.persistencia.adaptadores;
import dominio.modelo.Docente;
import dominio.puerto.repositorio.RepositorioDocentes;
import infraestructura.persistencia.entidades.AsignaturaEntity;
import infraestructura.persistencia.entidades.DocenteEntity;
import infraestructura.persistencia.jpa.AsignaturaJpaRepository;
import infraestructura.persistencia.jpa.DocenteJpaRepository;
import infraestructura.persistencia.mappers.DocenteMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author inici4rsesi0n
 */
@Repository
public class AdaptadorRepositorioDocentes implements RepositorioDocentes{
    private final DocenteJpaRepository jpaRepository;
    private final AsignaturaJpaRepository asignaturaJpa;
    private final DocenteMapper mapper;
    
    public AdaptadorRepositorioDocentes(DocenteJpaRepository jpaRepository, AsignaturaJpaRepository asignaturaJpa, DocenteMapper mapper){
        this.jpaRepository=jpaRepository;
        this.asignaturaJpa=asignaturaJpa;
        this.mapper=mapper;
    }
    
    @Override @Transactional
    public void agregar(Docente docente){
        if(docente==null) throw new IllegalArgumentException("El docente no puede ser nulo");
        DocenteEntity entity = mapper.toEntity(docente);
        entity.setAsignaturas(resolverAsignaturas(docente));
        jpaRepository.save(entity);
    }
    @Override
    public Optional<Docente> buscarPorDni(String dni){
        return jpaRepository.findByDni(dni).map(mapper::toDomain);
    }
    @Override
    public Optional<Docente> buscarPorCodigo(String codigo){
        return jpaRepository.findByCodigo(codigo).map(mapper::toDomain);
    }
    @Override
    public List<Docente> listarTodos(){
        return mapper.toDomainList(jpaRepository.findAll());
    }
    @Override @Transactional
    public void actualizar(Docente original, Docente actualizado){
        DocenteEntity entity=jpaRepository.findByCodigo(original.getCodigo())
                .orElseThrow(()->new IllegalArgumentException("El docente no existe"));
        entity.setNombre(actualizado.getNombre());
        entity.setApellido(actualizado.getApellido());
        entity.setEdad(actualizado.getEdad());
        entity.setHashContrasena(actualizado.getHashContrasena());
        entity.setDni(actualizado.getDni());
        entity.setEspecialidad(actualizado.getEspecialidad());
        entity.setAsignaturas(resolverAsignaturas(actualizado));
        jpaRepository.save(entity);
    }
    @Override @Transactional
    public void eliminar(Docente docente){
        jpaRepository.findByCodigo(docente.getCodigo()).ifPresent(jpaRepository::delete);
    }
    
    private List<AsignaturaEntity> resolverAsignaturas(Docente docente){
        if(docente.getAsignaturas() == null || docente.getAsignaturas().isEmpty()) return List.of();
        return docente.getAsignaturas().stream()
                .map(a -> asignaturaJpa.findByNombre(a.getNombre())
                        .orElseThrow(()->new IllegalArgumentException("Asignatura no existe: " + a.getNombre())))
                .toList();
    }
}
