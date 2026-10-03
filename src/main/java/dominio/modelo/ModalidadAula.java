package dominio.modelo;
/**
 *
 * @author inici4rsesi0n
 */
public enum ModalidadAula {
    PRESENCIAL,
    VIRTUAL,
    REMOTO;
    public static ModalidadAula fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            return PRESENCIAL;
        }
        for (ModalidadAula m : values()) {
            if (m.name().equalsIgnoreCase(texto.trim())) {
                return m;
            }
        }
        return PRESENCIAL;
    }
}