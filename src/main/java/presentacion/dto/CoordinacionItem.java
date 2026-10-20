package presentacion.dto;

import dominio.modelo.CoordinadorAcademico;
import dominio.modelo.Grado;

public class CoordinacionItem {
    private final CoordinadorAcademico coordinador;
    private final Grado grado;

    public CoordinacionItem(CoordinadorAcademico coordinador, Grado grado) {
        this.coordinador = coordinador;
        this.grado = grado;
    }

    public CoordinadorAcademico getCoordinador() { return coordinador; }
    public Grado getGrado() { return grado; }
    public String getNombreCoordinador() { return coordinador.getNombreCompleto(); }
    public String getNombreGrado() { return grado.getNombre(); }
}
