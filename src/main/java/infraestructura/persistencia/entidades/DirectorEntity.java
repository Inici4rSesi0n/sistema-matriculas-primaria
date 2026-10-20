package infraestructura.persistencia.entidades;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
/**
 *
 * @author inici4rsesi0n
 */
@Entity
@DiscriminatorValue("DIRECTOR")
public class DirectorEntity extends UsuarioEntity{}
