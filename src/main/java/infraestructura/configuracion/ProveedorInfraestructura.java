package infraestructura.configuracion;
import aplicacion.casosdeuso.*;
import dominio.puerto.externo.HashProvider;
import dominio.puerto.repositorio.*;
import infraestructura.persistencia.*;
import infraestructura.seguridad.BCryptHashProvider;
/**
 *
 * @author inici4rsesi0n
 */
public class ProveedorInfraestructura {
    private static RepositorioAdministradores repoAdministradores;
    private static RepositorioAsignaturas repoAsignaturas;
    private static RepositorioAulas repoAulas;
    private static RepositorioClases repoClases;
    private static RepositorioCoordinadores repoCoordinadores;
    private static RepositorioDirectores repoDirectores;
    private static RepositorioDocentes repoDocentes;
    private static RepositorioEstudiantes repoEstudiantes;
    private static RepositorioGrados repoGrados;
    private static RepositorioGrupos repoGrupos;
    private static RepositorioMatriculas repoMatriculas;
    private static RepositorioPadres repoPadres;
    private static RepositorioPeriodos repoPeriodos;
    private static RepositorioRecreos repoRecreos;
    private static RepositorioSecretarios repoSecretarios;
    private static RepositorioTurnos repoTurnos;
    private static HashProvider hashProvider;
    private static GestionAdministradores gestionAdministradores;
    private static GestionAsignaturas gestionAsignaturas;
    private static GestionAulas gestionAulas;
    private static GestionClases gestionClases;
    private static GestionCoordinadores gestionCoordinadores;
    private static GestionDirectores gestionDirectores;
    private static GestionDocentes gestionDocentes;
    private static GestionEstudiantes gestionEstudiantes;
    private static GestionGrados gestionGrados;
    private static GestionGrupos gestionGrupos;
    private static GestionMatriculas gestionMatriculas;
    private static GestionPadres gestionPadres;
    private static GestionPeriodos gestionPeriodos;
    private static GestionRecreos gestionRecreos;
    private static GestionSecretarios gestionSecretarios;
    private static GestionTurnos gestionTurnos;
    private static AutenticarUsuario autenticarUsuario;
    private static GestionPermisos gestionPermisos;
    private static GestionUsuarios gestionUsuarios;
    private static GestionHorario gestionHorario;
    private static BootstrapUseCase bootstrapUseCase;
    static {
        inicializar();
    }
    public static void inicializar() {
        repoAdministradores = new AdaptadorRepositorioAdministradores();
        repoAsignaturas = new AdaptadorRepositorioAsignaturas();
        repoAulas = new AdaptadorRepositorioAulas();
        repoClases = new AdaptadorRepositorioClases();
        repoCoordinadores = new AdaptadorRepositorioCoordinadores();
        repoDirectores = new AdaptadorRepositorioDirectores();
        repoDocentes = new AdaptadorRepositorioDocentes();
        repoEstudiantes = new AdaptadorRepositorioEstudiantes();
        repoGrados = new AdaptadorRepositorioGrados();
        repoGrupos = new AdaptadorRepositorioGrupos();
        repoMatriculas = new AdaptadorRepositorioMatriculas();
        repoPadres = new AdaptadorRepositorioPadres();
        repoPeriodos = new AdaptadorRepositorioPeriodos();
        repoRecreos = new AdaptadorRepositorioRecreos();
        repoSecretarios = new AdaptadorRepositorioSecretarios();
        repoTurnos = new AdaptadorRepositorioTurnos();
        hashProvider = new BCryptHashProvider();
        gestionAdministradores = new GestionAdministradores(repoAdministradores);
        gestionAsignaturas = new GestionAsignaturas(repoAsignaturas);
        gestionAulas = new GestionAulas(repoAulas);
        gestionClases = new GestionClases(repoClases);
        gestionCoordinadores = new GestionCoordinadores(repoCoordinadores);
        gestionDirectores = new GestionDirectores(repoDirectores);
        gestionDocentes = new GestionDocentes(repoDocentes);
        gestionEstudiantes = new GestionEstudiantes(repoEstudiantes);
        gestionGrados = new GestionGrados(repoGrados);
        gestionGrupos = new GestionGrupos(repoGrupos);
        gestionMatriculas = new GestionMatriculas(repoMatriculas);
        gestionPadres = new GestionPadres(repoPadres);
        gestionPeriodos = new GestionPeriodos(repoPeriodos);
        gestionRecreos = new GestionRecreos(repoRecreos);
        gestionSecretarios = new GestionSecretarios(repoSecretarios);
        gestionTurnos = new GestionTurnos(repoTurnos);
        gestionUsuarios = new GestionUsuarios(repoAdministradores, repoDirectores, repoDocentes,
        repoEstudiantes, repoSecretarios, repoCoordinadores, repoPadres);
        gestionPermisos = new GestionPermisos();
        gestionHorario = new GestionHorario(repoClases, repoRecreos);
        bootstrapUseCase = new BootstrapUseCase(hashProvider);
        autenticarUsuario = new AutenticarUsuario(repoAdministradores, repoDirectores, repoDocentes,
                repoEstudiantes, repoSecretarios, repoCoordinadores, repoPadres, hashProvider);
    }
    public static RepositorioAdministradores getRepoAdministradores() { return repoAdministradores; }
    public static RepositorioAsignaturas getRepoAsignaturas() { return repoAsignaturas; }
    public static RepositorioAulas getRepoAulas() { return repoAulas; }
    public static RepositorioClases getRepoClases() { return repoClases; }
    public static RepositorioCoordinadores getRepoCoordinadores() { return repoCoordinadores; }
    public static RepositorioDirectores getRepoDirectores() { return repoDirectores; }
    public static RepositorioDocentes getRepoDocentes() { return repoDocentes; }
    public static RepositorioEstudiantes getRepoEstudiantes() { return repoEstudiantes; }
    public static RepositorioGrados getRepoGrados() { return repoGrados; }
    public static RepositorioGrupos getRepoGrupos() { return repoGrupos; }
    public static RepositorioMatriculas getRepoMatriculas() { return repoMatriculas; }
    public static RepositorioPadres getRepoPadres() { return repoPadres; }
    public static RepositorioPeriodos getRepoPeriodos() { return repoPeriodos; }
    public static RepositorioRecreos getRepoRecreos() { return repoRecreos; }
    public static RepositorioSecretarios getRepoSecretarios() { return repoSecretarios; }
    public static RepositorioTurnos getRepoTurnos() { return repoTurnos; }
    public static HashProvider getHashProvider() { return hashProvider; }
    public static GestionAdministradores getGestionAdministradores() { return gestionAdministradores; }
    public static GestionAsignaturas getGestionAsignaturas() { return gestionAsignaturas; }
    public static GestionAulas getGestionAulas() { return gestionAulas; }
    public static GestionClases getGestionClases() { return gestionClases; }
    public static GestionCoordinadores getGestionCoordinadores() { return gestionCoordinadores; }
    public static GestionDirectores getGestionDirectores() { return gestionDirectores; }
    public static GestionDocentes getGestionDocentes() { return gestionDocentes; }
    public static GestionEstudiantes getGestionEstudiantes() { return gestionEstudiantes; }
    public static GestionGrados getGestionGrados() { return gestionGrados; }
    public static GestionGrupos getGestionGrupos() { return gestionGrupos; }
    public static GestionMatriculas getGestionMatriculas() { return gestionMatriculas; }
    public static GestionPadres getGestionPadres() { return gestionPadres; }
    public static GestionPeriodos getGestionPeriodos() { return gestionPeriodos; }
    public static GestionRecreos getGestionRecreos() { return gestionRecreos; }
    public static GestionSecretarios getGestionSecretarios() { return gestionSecretarios; }
    public static GestionTurnos getGestionTurnos() { return gestionTurnos; }
    public static AutenticarUsuario getAutenticarUsuario() { return autenticarUsuario; }
    public static GestionPermisos getGestionPermisos() { return gestionPermisos;}
    public static GestionUsuarios getGestionUsuarios() {return gestionUsuarios;}
    public static GestionHorario getGestionHorario() {return gestionHorario;}
    public static BootstrapUseCase getBootstrapUseCase() {return bootstrapUseCase;}
}