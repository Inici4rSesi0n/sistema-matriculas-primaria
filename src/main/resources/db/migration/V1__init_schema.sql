-- ==============================================================================
-- V1__init_schema.sql
-- Propósito: Crear el esquema base para WiredAcademy.
-- Convenciones: Tablas en plural, snake_case, PKs con BIGSERIAL, FKs indexadas.
-- ==============================================================================

-- 1. Usuarios (Estrategia SINGLE_TABLE para la jerarquía Usuario)
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    hash_contrasena VARCHAR(100) NOT NULL,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    edad INTEGER NOT NULL CHECK (edad > 0),
    rol VARCHAR(20) NOT NULL,
    
    especialidad VARCHAR(100),
    grupo_tutoria_id BIGINT
);

CREATE INDEX idx_usuarios_rol ON usuarios(rol);

-- 2. Catálogos Básicos
CREATE TABLE turnos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX uq_turnos_nombre_lower ON turnos (LOWER(nombre));

CREATE TABLE grados (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX uq_grados_nombre_nivel ON grados (LOWER(nombre), LOWER(nivel));

CREATE TABLE periodos_academicos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    fecha_inicio VARCHAR(10) NOT NULL,
    fecha_fin VARCHAR(10) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);
CREATE UNIQUE INDEX uq_periodos_nombre_lower ON periodos_academicos (LOWER(nombre));

CREATE TABLE aulas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    capacidad INTEGER NOT NULL CHECK (capacidad > 0),
    ubicacion VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    modalidad VARCHAR(20) NOT NULL CHECK (modalidad IN ('PRESENCIAL', 'VIRTUAL', 'REMOTO'))
);
CREATE UNIQUE INDEX uq_aulas_nombre_lower ON aulas (LOWER(nombre));

CREATE TABLE asignaturas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX uq_asignaturas_nombre_lower ON asignaturas (LOWER(nombre));

-- 3. Grupos y Relaciones Académicas
CREATE TABLE grupos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    grado_id BIGINT NOT NULL REFERENCES grados(id),
    tutor_id BIGINT REFERENCES usuarios(id)
);
CREATE UNIQUE INDEX uq_grupos_nombre_grado ON grupos (LOWER(nombre), grado_id);

ALTER TABLE usuarios ADD CONSTRAINT fk_usuarios_grupo_tutoria FOREIGN KEY (grupo_tutoria_id) REFERENCES grupos(id);

CREATE TABLE docentes_asignaturas (
    docente_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    asignatura_id BIGINT NOT NULL REFERENCES asignaturas(id) ON DELETE CASCADE,
    PRIMARY KEY (docente_id, asignatura_id)
);

CREATE TABLE grupo_estudiantes (
    grupo_id BIGINT NOT NULL REFERENCES grupos(id) ON DELETE CASCADE,
    estudiante_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    PRIMARY KEY (grupo_id, estudiante_id)
);

CREATE TABLE coordinadores_grados (
    coordinador_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    grado_id BIGINT NOT NULL REFERENCES grados(id),
    PRIMARY KEY (coordinador_id, grado_id)
);

CREATE TABLE padres_estudiantes (
    padre_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    estudiante_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    PRIMARY KEY (padre_id, estudiante_id)
);

-- 4. Eventos y Horarios
CREATE TABLE clases (
    id BIGSERIAL PRIMARY KEY,
    dia_semana VARCHAR(15) NOT NULL,
    hora_inicio VARCHAR(5) NOT NULL,
    hora_fin VARCHAR(5) NOT NULL,
    periodo_id BIGINT NOT NULL REFERENCES periodos_academicos(id),
    asignatura_id BIGINT NOT NULL REFERENCES asignaturas(id),
    docente_id BIGINT NOT NULL REFERENCES usuarios(id),
    grupo_id BIGINT NOT NULL REFERENCES grupos(id),
    aula_id BIGINT NOT NULL REFERENCES aulas(id),
    CONSTRAINT ck_clases_horas CHECK (hora_inicio < hora_fin)
);
CREATE INDEX idx_clases_docente ON clases(docente_id);
CREATE INDEX idx_clases_grupo ON clases(grupo_id);
CREATE INDEX idx_clases_aula ON clases(aula_id);

CREATE TABLE recreos (
    id BIGSERIAL PRIMARY KEY,
    dia_semana VARCHAR(15) NOT NULL,
    hora_inicio VARCHAR(5) NOT NULL,
    hora_fin VARCHAR(5) NOT NULL,
    descripcion VARCHAR(100) NOT NULL DEFAULT 'Recreo',
    CONSTRAINT ck_recreos_horas CHECK (hora_inicio < hora_fin)
);

CREATE TABLE recreo_periodos (
    recreo_id BIGINT NOT NULL REFERENCES recreos(id) ON DELETE CASCADE,
    periodo_id BIGINT NOT NULL REFERENCES periodos_academicos(id) ON DELETE CASCADE,
    PRIMARY KEY (recreo_id, periodo_id)
);

-- 5. Matrículas
CREATE TABLE matriculas (
    id BIGSERIAL PRIMARY KEY,
    estudiante_id BIGINT NOT NULL REFERENCES usuarios(id),
    periodo_id BIGINT NOT NULL REFERENCES periodos_academicos(id),
    grupo_id BIGINT NOT NULL REFERENCES grupos(id),
    fecha VARCHAR(10) NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('ACTIVA', 'RETIRADA', 'CULMINADA')),
    CONSTRAINT uq_matriculas_estudiante_periodo UNIQUE (estudiante_id, periodo_id)
);
CREATE INDEX idx_matriculas_grupo ON matriculas(grupo_id);

CREATE TABLE matricula_asignaturas (
    matricula_id BIGINT NOT NULL REFERENCES matriculas(id) ON DELETE CASCADE,
    asignatura_id BIGINT NOT NULL REFERENCES asignaturas(id) ON DELETE CASCADE,
    PRIMARY KEY (matricula_id, asignatura_id)
);

-- 6. Sesiones
CREATE TABLE sesiones (
    id UUID PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    creada_en TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expira_en TIMESTAMPTZ NOT NULL,
    revocada_en TIMESTAMPTZ
);
CREATE INDEX idx_sesiones_usuario ON sesiones(usuario_id);
CREATE INDEX idx_sesiones_token ON sesiones(id) WHERE revocada_en IS NULL;
