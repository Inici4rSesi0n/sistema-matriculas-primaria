package infraestructura.persistencia.mappers;
import dominio.modelo.CoordinadorAcademico;
import infraestructura.persistencia.entidades.CoordinadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;
/**
 *
 * @author inici4rsesi0n
 */
@Mapper(componentModel="spring", unmappedTargetPolicy=ReportingPolicy.IGNORE, uses={GradoMapper.class})
public interface CoordinadorMapper{
    @Mapping(target="gradosSupervisados", ignore=true)
    CoordinadorAcademico toDomain(CoordinadorEntity entity);
    
    @Mapping(target="id", ignore=true)
    @Mapping(target="gradosSupervisados", ignore=true)
    CoordinadorEntity toEntity(CoordinadorAcademico domain);
    
    List<CoordinadorAcademico> toDomainList(List<CoordinadorEntity> entities);
}
