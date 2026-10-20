package infraestructura.persistencia.entidades;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
/**
 *
 * @author inici4rsesi0n
 */
@Entity
@DiscriminatorValue("SECRETARIO")
public class SecretarioEntity extends UsuarioEntity{}
