# LOGGING.md — Convención de Logging de WiredAcademy

> Documento maestro de la estrategia de logging del proyecto.
> Define qué se registra, cómo se registra y por qué.

**Versión:** 1.0
**Estado:** Activo
**Última revisión:** 2026-10-05
**Mantenedor:** inici4rsesi0n

---

## 1. Principio Rector

**Logging dirigido, no masivo.** Se registran operaciones críticas de negocio; el resto se añade reactivamente cuando surge un bug y se retira al corregirlo.

| Modo | Zona | Estrategia |
|---|---|---|
| **Proactivo** | Usuarios y gestión académica | Logging permanente de auditoría |
| **Reactivo** | Resto del sistema | Logs temporales para diagnóstico, se eliminan al corregir |

> Un log bien puesto vale más que cien logs ruidosos.

---

## 2. Jerarquía de Loggers

```
WiredAcademy                    ← raíz
├── WiredAcademy.AUDIT          ← auditoría de negocio
├── WiredAcademy.EVENTBUS       ← flujo de eventos
└── WiredAcademy.UI             ← navegación (solo dev)
```

Cada categoría hereda del padre. La separación permite filtrar por archivo.

---

## 3. Niveles por Categoría

| Categoría | Nivel dev | Nivel prod | Uso |
|---|---|---|---|
| `AUDIT` | INFO | INFO | Eventos de negocio |
| `EVENTBUS` | DEBUG | WARN | Emisión y recepción de eventos |
| `UI` | TRACE | OFF | Navegación y refrescos |
| Raíz | INFO | WARN | Sistema general |

---

## 4. Formato Estándar

```
[<CATEGORÍA>] op=<OPERACION> entity=<ENTIDAD> id=<ID> | <detalles>
```

### Operaciones válidas

| Operación | Uso |
|---|---|
| `CREATE` | Creación de entidad |
| `UPDATE` | Modificación de entidad |
| `DELETE` | Eliminación de entidad |
| `LINK` | Vinculación entre entidades |
| `UNLINK` | Desvinculación entre entidades |
| `LOGIN` | Inicio de sesión exitoso |
| `FAILED` | Operación fallida (nivel WARN) |
| `PERSIST` | Error de persistencia (nivel ERROR) |

### Ejemplos

```
[AUDIT] op=CREATE entity=DOCENTE id=U34421561 | especialidad=Mat
[AUDIT] op=UPDATE entity=DOCENTE id=U34421561 | before=[Mat,Fis] after=[Mat]
[AUDIT] op=DELETE entity=GRUPO id=1A | estudiantes=15 tutor=U34421561
[AUDIT] op=LINK entity=DOCENTE id=U34421561 -> GRUPO id=1A | role=TUTOR
[AUDIT] op=LOGIN entity=USER id=ADMIN001 | role=ADMINISTRADOR
[EVENTBUS] event=USUARIOS listeners=3
[UI] action=NAVIGATE from=catalogos to=asignaciones
```

---

## 5. Alcance Proactivo

Las siguientes operaciones **siempre** emiten log `AUDIT`. Todas residen en la capa de aplicación.

### Usuarios
- `GestionUsuarios.agregarUsuario` → `CREATE`
- `GestionUsuarios.actualizarUsuario` → `UPDATE`
- `GestionUsuarios.eliminarUsuario` → `DELETE`

### Docentes
- `GestionDocentes.agregar` → `CREATE`
- `GestionDocentes.actualizar` → `UPDATE`
- `GestionDocentes.eliminar` → `DELETE`
- `GestionDocentes.agregarAsignatura` → `LINK`
- `GestionDocentes.removerAsignatura` → `UNLINK`
- `GestionDocentes.asignarTutoria` → `LINK`

### Grupos
- `GestionGrupos.crearGrupo` → `CREATE`
- `GestionGrupos.actualizarGrupo` → `UPDATE`
- `GestionGrupos.eliminarGrupo` → `DELETE`
- `GestionGrupos.asignarTutor` → `LINK`
- `GestionGrupos.removerTutor` → `UNLINK`

### Coordinadores
- `GestionCoordinadores.agregar` → `CREATE`
- `GestionCoordinadores.actualizar` → `UPDATE`
- `GestionCoordinadores.eliminar` → `DELETE`

### Padres
- `GestionPadres.agregar` → `CREATE`
- `GestionPadres.actualizar` → `UPDATE`
- `GestionPadres.eliminar` → `DELETE`
- `GestionPadres.vincularEstudiante` → `LINK`
- `GestionPadres.desvincularEstudiante` → `UNLINK`

### Estudiantes
- `GestionEstudiantes.agregar` → `CREATE`
- `GestionEstudiantes.actualizar` → `UPDATE`
- `GestionEstudiantes.eliminar` → `DELETE`

### Catálogos académicos
- `GestionAsignaturas.*` → `CREATE` / `UPDATE` / `DELETE`
- `GestionAulas.*` → `CREATE` / `UPDATE` / `DELETE`
- `GestionGrados.*` → `CREATE` / `UPDATE` / `DELETE`
- `GestionPeriodos.*` → `CREATE` / `UPDATE` / `DELETE`
- `GestionTurnos.*` → `CREATE` / `UPDATE` / `DELETE`
- `GestionRecreos.*` → `CREATE` / `UPDATE` / `DELETE`

### Clases y Matrículas
- `GestionClases.crearClase` → `CREATE`
- `GestionClases.actualizarClase` → `UPDATE`
- `GestionClases.eliminarClase` → `DELETE`
- `GestionMatriculas.matricularEstudiante` → `CREATE`
- `GestionMatriculas.actualizar` → `UPDATE`
- `GestionMatriculas.actualizarEstado` → `UPDATE`
- `GestionMatriculas.eliminarMatricula` → `DELETE`

### Administrativos
- `GestionAdministradores.*` → `CREATE` / `UPDATE` / `DELETE`
- `GestionDirectores.*` → `CREATE` / `UPDATE` / `DELETE`
- `GestionSecretarios.*` → `CREATE` / `UPDATE` / `DELETE`

### Autenticación
- `FxLoginController.handleLogin` (éxito) → `LOGIN`

---

## 6. Alcance Reactivo

Las siguientes zonas **no** tienen logging por defecto. Se instrumentan temporalmente ante un bug específico y se limpian al resolver.

- `AutenticarUsuario` (ya tiene WARN por fallo)
- `BootstrapUseCase`
- `GestionHorario` (solo lectura)
- `GestionPermisos`
- Controladores JavaFX (excepto `FxLoginController`)

**Única excepción permanente:** `SistemaEventBus` emite `EVENTBUS` en cada notificación para trazabilidad.

---

## 7. Uso en Código

### Casos de uso

```java
public void actualizarUsuario(Usuario usuario) {
    if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo");
    String antes = usuario.toString();

    switch (usuario.getRol()) {
        case DOCENTE -> repoDoc.actualizar((Docente) usuario, (Docente) usuario);
        // ...
    }

    logger.audit(String.format(
        "op=UPDATE entity=%s id=%s | before=%s after=%s",
        usuario.getRol(), usuario.getCodigo(), antes, usuario));
}
```

### EventBus

```java
public static void notificar(TipoEvento evento) {
    List<Runnable> copia = new ArrayList<>(suscriptores.getOrDefault(evento, List.of()));
    if (copia.isEmpty()) return;
    logger.eventBus(String.format("event=%s listeners=%d", evento, copia.size()));
    Platform.runLater(() -> { /* ... */ });
}
```

### Controladores

No emiten `AUDIT`. Solo notifican al `SistemaEventBus`, que registra automáticamente el flujo.

---

## 8. Reglas de Oro

1. **No loggear en el dominio** — el dominio es puro.
2. **Loggear en la capa de aplicación** — casos de uso.
3. **No loggear lecturas** — solo mutaciones.
4. **No loggear UI** salvo navegación en `TRACE`.
5. **Nunca loggear PII** — ni contraseñas, ni hashes, ni datos sensibles.
6. **Eliminar logs reactivos** una vez corregido el bug.
7. **Un log por operación**, no varios.

---

## 9. Archivos de Salida

**Dev:** consola con todo visible.
**Prod:** archivos rotados diariamente, retención 30 días.

```
logs/
├── wiredacademy.log              → todo (raíz)
├── wiredacademy-audit.log        → solo AUDIT
├── wiredacademy-error.log        → solo ERROR
└── wiredacademy-eventbus.log     → solo EVENTBUS
```

### Consultas útiles

```bash
# Ver todas las mutaciones de un usuario
grep "U34421561" logs/wiredacademy-audit.log

# Ver solo eliminaciones
grep "op=DELETE" logs/wiredacademy-audit.log

# Ver flujo de eventos en vivo
tail -f logs/wiredacademy-eventbus.log

# Ver errores recientes
tail -50 logs/wiredacademy-error.log
```

---

## 10. Anti-patrones

```java
// ❌ Ruido
logger.info("Entrando a método X");

// ❌ Concatenación ineficiente
logger.info("El usuario es " + usuario);

// ❌ Fuga de PII
logger.info("Password: " + password);

// ❌ TRACE disfrazado de DEBUG
logger.debug("Iteración " + i);
```

```java
// ✅ Estructurado y filtrable
logger.audit(String.format(
    "op=UPDATE entity=USER id=%s | before=%s after=%s",
    usuario.getCodigo(), antes, usuario));
```

---

## 11. Evolución del Documento

Este documento es **vivo**. Se actualiza cuando:
- Se añade una nueva zona crítica con logging proactivo.
- Se descubre un patrón útil de consulta de logs.
- Se retira o añade una categoría de logger.

---

*WiredAcademy — Ecosistema Educativo Digital*
