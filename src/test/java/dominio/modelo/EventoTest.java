package dominio.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author inici4rsesi0n
 */
class EventoTest {

    private FranjaHoraria franja;
    private PeriodoAcademico periodo;
    private Evento evento;

    @BeforeEach
    void setUp() {
        franja = new FranjaHoraria("Lunes", "08:00", "10:00");
        periodo = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
        evento = new EventoConcreto(franja, periodo, "Clase de prueba");
    }

    @Test
    void constructor_debeCrearEventoConDatosCorrectos() {
        assertEquals("Lunes", evento.getDiaSemana());
        assertEquals("08:00", evento.getHoraInicio());
        assertEquals("10:00", evento.getHoraFin());
        assertEquals(periodo, evento.getPeriodo());
        assertEquals(franja, evento.getFranja());
    }

    @Test
    void constructor_debeLanzarExcepcionSiFranjaNula() {
        assertThrows(IllegalArgumentException.class,
                () -> new EventoConcreto(null, periodo, "desc"));
    }

    @Test
    void constructor_debeLanzarExcepcionSiPeriodoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new EventoConcreto(franja, null, "desc"));
    }

    @Test
    void setFranja_debeActualizarFranja() {
        FranjaHoraria nuevaFranja = new FranjaHoraria("Martes", "09:00", "11:00");
        evento.setFranja(nuevaFranja);
        assertEquals(nuevaFranja, evento.getFranja());
        assertEquals("Martes", evento.getDiaSemana());
    }

    @Test
    void setPeriodo_debeActualizarPeriodo() {
        PeriodoAcademico nuevoPeriodo = new PeriodoAcademico("2026-II", "2026-07-01", "2026-12-31", "Activo");
        evento.setPeriodo(nuevoPeriodo);
        assertEquals(nuevoPeriodo, evento.getPeriodo());
    }

    @Test
    void toString_debeIncluirDescripcionYFranja() {
        String texto = evento.toString();
        assertTrue(texto.contains("Clase de prueba"));
        assertTrue(texto.contains("Lunes 08:00-10:00"));
    }

    @Test
    void toString_conDescripcionNulaDebeMostrarSinDescripcion() {
        Evento eventoSinDesc = new EventoConcreto(franja, periodo, null);
        String texto = eventoSinDesc.toString();
        assertTrue(texto.contains("Sin descripción"));
    }

    private static class EventoConcreto extends Evento {
        private String descripcion;

        public EventoConcreto(FranjaHoraria franja, PeriodoAcademico periodo, String descripcion) {
            super(franja, periodo);
            this.descripcion = descripcion;
        }

        @Override
        public String getDescripcion() {
            return descripcion;
        }
    }
}