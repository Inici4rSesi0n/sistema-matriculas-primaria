package infraestructura.persistencia.mappers;
import dominio.modelo.Docente;
import infraestructura.persistencia.entidades.DocenteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
@Mapper(componentModel="spring", unmappedTargetPolicy=ReportingPolicy.IGNORE, uses={AsignaturaMapper.class})
public interface DocenteMapper{
    @Mapping(target="asignaturas", ignore=true)
    @Mapping(target="tutoria", ignore=true)
    Docente toDomain(DocenteEntity entity);
    
    @Mapping(target="id", ignore=true)
    @Mapping(target="asignaturas", ignore=true)
    DocenteEntity toEntity(Docente domain);
    
    List<Docente> toDomainList(List<DocenteEntity> entities);
}
