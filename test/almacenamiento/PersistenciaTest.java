package almacenamiento;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Grado;
import modelo.Salon;
import modelo.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
/**
 *
 * @author inici4rsesi0n
 */
public class PersistenciaTest {

    @TempDir
    Path tempDir;

    private String rutaArchivo;

    @BeforeEach
    void setUp() {
        rutaArchivo = tempDir.resolve("test.bin").toString();
    }

    @Test
    @DisplayName("guardar y cargar: un objeto serializado se recupera correctamente")
    void testGuardarYCargar() {
        ListaSalones original = new ListaSalones();
        original.agregar(new Salon("A", new Grado("1er Grado", "Primaria"), null, 30));
        original.agregar(new Salon("B", new Grado("2do Grado", "Primaria"), null, 25));

        Persistencia.guardar(original, rutaArchivo);
        ListaSalones recuperada = Persistencia.cargar(rutaArchivo);

        assertNotNull(recuperada);
        assertEquals(2, recuperada.getLista().size());
        assertEquals("A", recuperada.obtener(0).getAula());
    }

    @Test
    @DisplayName("cargar: devuelve null si el archivo no existe")
    void testCargarArchivoInexistente() {
        ListaSalones resultado = Persistencia.cargar(rutaArchivo);
        assertNull(resultado);
    }

    @Test
    @DisplayName("cargarListaSalones: crea lista vacía si el archivo no existe")
    void testCargarListaSalonesArchivoInexistente() {
        ListaSalones lista = Persistencia.cargarListaSalones();
        assertNotNull(lista);
        assertTrue(lista.getLista().isEmpty());
    }

    @Test
    @DisplayName("guardar: lanza excepción si la ruta es inválida")
    void testGuardarRutaInvalida() {
        ListaSalones lista = new ListaSalones();
        assertThrows(RuntimeException.class, () -> Persistencia.guardar(lista, "/ruta/invalida/test.bin"));
    }
}