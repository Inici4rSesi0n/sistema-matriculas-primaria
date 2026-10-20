package infraestructura.persistencia.mappers;
import dominio.modelo.Padre;
import infraestructura.persistencia.entidades.PadreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
@Mapper(componentModel="spring", unmappedTargetPolicy=ReportingPolicy.IGNORE, uses={EstudianteMapper.class})
public interface PadreMapper{
    @Mapping(target="estudiantes", ignore=true)
    Padre toDomain(PadreEntity entity);
    
    @Mapping(target="id", ignore=true)
    @Mapping(target="estudiantes", ignore=true)
    PadreEntity toEntity(Padre domain);
    
    List<Padre> toDomainList(List<PadreEntity> entities);
}
