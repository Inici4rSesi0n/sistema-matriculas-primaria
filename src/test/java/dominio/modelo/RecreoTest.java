package dominio.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author inici4rsesi0n
 */
class RecreoTest {

    private FranjaHoraria franja;
    private PeriodoAcademico periodo;

    @BeforeEach
    void setUp() {
        franja = new FranjaHoraria("Lunes", "10:00", "10:30");
        periodo = new PeriodoAcademico("2026-I", "2026-01-01", "2026-12-31", "Activo");
    }

    @Test
    void constructor_debeCrearRecreoConDescripcion() {
        Recreo recreo = new Recreo(franja, "Recreo Largo", periodo);
        assertEquals("Recreo Largo", recreo.getDescripcion());
        assertEquals("Lunes", recreo.getDiaSemana());
        assertEquals("10:00", recreo.getHoraInicio());
        assertEquals("10:30", recreo.getHoraFin());
        assertEquals(periodo, recreo.getPeriodo());
    }

    @Test
    void constructor_debeAsignarRecreoPorDefectoSiDescripcionNula() {
        Recreo recreo = new Recreo(franja, null, periodo);
        assertEquals("Recreo", recreo.getDescripcion());
    }

    @Test
    void constructor_debeAsignarRecreoPorDefectoSiDescripcionVacia() {
        Recreo recreo = new Recreo(franja, "", periodo);
        assertEquals("Recreo", recreo.getDescripcion());
    }

    @Test
    void constructor_debeAsignarRecreoPorDefectoSiDescripcionBlanco() {
        Recreo recreo = new Recreo(franja, "   ", periodo);
        assertEquals("Recreo", recreo.getDescripcion());
    }

    @Test
    void setDescripcion_debeActualizarDescripcion() {
        Recreo recreo = new Recreo(franja, "Recreo", periodo);
        recreo.setDescripcion("Recreo Modificado");
        assertEquals("Recreo Modificado", recreo.getDescripcion());
    }

    @Test
    void equals_debeSerSimetrico() {
        Recreo r1 = new Recreo(franja, "Recreo", periodo);
        Recreo r2 = new Recreo(franja, "Recreo", periodo);
        assertEquals(r1, r2);
        assertEquals(r2, r1);
    }

    @Test
    void equals_debeRetornarFalsoSiFranjaDiferente() {
        Recreo r1 = new Recreo(franja, "Recreo", periodo);
        FranjaHoraria otraFranja = new FranjaHoraria("Martes", "10:00", "10:30");
        Recreo r2 = new Recreo(otraFranja, "Recreo", periodo);
        assertNotEquals(r1, r2);
    }

    @Test
    void equals_debeRetornarFalsoSiPeriodoDiferente() {
        PeriodoAcademico otroPeriodo = new PeriodoAcademico("2026-II", "2026-07-01", "2026-12-31", "Activo");
        Recreo r1 = new Recreo(franja, "Recreo", periodo);
        Recreo r2 = new Recreo(franja, "Recreo", otroPeriodo);
        assertNotEquals(r1, r2);
    }

    @Test
    void equals_debeRetornarFalsoSiDescripcionDiferente() {
        Recreo r1 = new Recreo(franja, "Recreo", periodo);
        Recreo r2 = new Recreo(franja, "Recreo Largo", periodo);
        assertNotEquals(r1, r2);
    }

    @Test
    void hashCode_debeSerConsistente() {
        Recreo r1 = new Recreo(franja, "Recreo", periodo);
        Recreo r2 = new Recreo(franja, "Recreo", periodo);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}