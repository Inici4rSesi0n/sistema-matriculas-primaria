# 🏫 Sistema de Matrículas - Nivel Primario

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Swing-lightgrey)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![NetBeans](https://img.shields.io/badge/IDE-NetBeans-blue)](https://netbeans.apache.org/)

Sistema de escritorio desarrollado en **Java 17** con **Swing** para la gestión integral de matrículas del nivel primario, construido bajo una arquitectura en capas que sigue el patrón **MVC** y los principios **SOLID**, con persistencia local mediante serialización de objetos. Incluye autenticación segura con hash **SHA-256**, control de acceso por roles (Director, Docente, Estudiante), módulos CRUD para estudiantes, docentes, salones, asignaturas y horarios, asignación masiva de cursos, portal de matrícula con validación de cupo y visualización semanal del horario con recreos automáticos. El proyecto se encuentra en desarrollo activo: la lógica central está implementada y funcional, mientras que algunos detalles lógicos y visuales —incluyendo la decoración de las vistas con los recursos de la carpeta `vista.assets`— se irán refinando progresivamente. Todo converge en una interfaz centralizada mediante un único `JDesktopPane`, ofreciendo una experiencia de usuario cohesiva y profesional.

---

## 📋 Tabla de Contenidos

- [Características Principales](#-características-principales)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Estructura de Paquetes](#-estructura-de-paquetes)
- [Modelo de Dominio](#-modelo-de-dominio)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Credenciales de Prueba](#-credenciales-de-prueba)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Estado del Proyecto](#-estado-del-proyecto)
- [Licencia](#-licencia)

---

## ✨ Características Principales

- **Autenticación segura** con hash SHA-256 y roles de usuario (Director, Docente, Estudiante).
- **CRUD completo** para estudiantes, docentes, salones, asignaturas y horarios.
- **Matrícula de estudiantes** con selección de salón y validación de cupo en tiempo real.
- **Asignación masiva de asignaturas** a docentes mediante interfaz dedicada.
- **Visualización de horarios** en formato semanal, con recreos generados automáticamente.
- **Persistencia local** mediante serialización de objetos en archivos `.bin`.
- **Interfaz centralizada** en un único `JDesktopPane` que contiene todos los `JInternalFrame`.
- **Arquitectura en capas** que separa estrictamente modelo, vista, procesos y controladores.
- **Unicidad global de códigos** de usuario garantizada en todas las entidades.
- **Manejo seguro de contraseñas** con `char[]`, hash SHA-256 y limpieza de arrays sensibles.

---

## 🧱 Arquitectura del Proyecto

El sistema está organizado en cuatro capas principales que siguen el patrón **MVC**, más una capa adicional de persistencia:

### 🗄️ Modelo (`modelo`, `modelo.repositorio`, `modelo.tabla`)
- **Entidades**: Clases serializables que representan el dominio del problema (`Usuario`, `Estudiante`, `Docente`, `Director`, `Grado`, `Salon`, `Asignatura`, `Clase`, `Recreo`, `Evento`). Se aplica herencia donde corresponde (ej. `Estudiante extends Usuario`, `Clase extends Evento`) y composición para el resto de relaciones.
- **Repositorios**: Clases que encapsulan las colecciones de entidades y las operaciones CRUD. Ejemplos: `ListaEstudiantes`, `ListaDocentes`, `ListaSalones`, `ListaAsignaturas`, `ListaClases`, `GestorDirector`, `CatalogoGrados`, `CatalogoRecreo`. Garantizan la persistencia y la unicidad de códigos.
- **Modelos de Tabla**: Clases que extienden `AbstractTableModel` para personalizar la visualización de datos en `JTable`. Ejemplos: `EstudianteTable`, `DocenteTable`, `SalonTable`, `AsignaturaTable`, `EventoTable`, `GestionAsignaturaTable`.

### 🖥️ Vista (`vista`, `vista.assets`)
- **Interfaces de usuario**: Formularios construidos con Swing, organizados como `JInternalFrame` dentro de un `JFrame` principal (`FPrincipal`) que contiene un `JDesktopPane`. Incluye pantallas de login, menú principal, menú de registros, portales de estudiante y docente, y formularios CRUD para cada entidad.
- **Recursos gráficos**: Carpeta `vista.assets` con imágenes decorativas para las vistas.

### ⚙️ Procesos (`procesos`)
- **Lógica de negocio**: Clases con métodos estáticos que validan, transforman y procesan los datos entre la vista y el modelo. Ejemplos: `ProcesosEstudiantes`, `ProcesosDocente`, `ProcesosSalon`, `ProcesosAsignatura`, `ProcesosHorario`, `ProcesosPortalCTP`, `ProcesosLogin`, `ProcesosGestionAsignaturas`.
- **Utilidades**: `Mensajes` (diálogos JOptionPane), `Utilidades` (hash y limpieza de contraseñas).

### 🎮 Controlador (`controlador`)
- **Orquestación de eventos**: Clases que implementan `ActionListener` y gestionan la interacción entre la vista y los procesos. Ejemplos: `ControlLogin`, `ControlPrincipal`, `ControlEstudiantes`, `ControlDocentes`, `ControlSalones`, `ControlAsignaturas`, `ControlHorarios`, `ControlMenuDirector`, `ControlMenuRegistros`, `ControlPortalCTP`, `ControlCTPClass`, `ControlGestionAsignaturas`.

### 💾 Persistencia (`almacenamiento`)
- **Serialización**: Clase `Persistencia` con métodos estáticos para guardar y cargar objetos en archivos `.bin`. Cada repositorio se serializa de forma independiente.

---
## 📁 Estructura de Paquetes
```
JavaApplication33/
└── Source Packages/
    ├── almacenamiento/          # Persistencia (serialización .bin)
    ├── controlador/             # Controladores (ActionListener)
    ├── modelo/                  # Entidades del dominio
    ├── modelo.repositorio/      # Colecciones y operaciones CRUD
    ├── modelo.tabla/            # Modelos de tabla personalizados
    ├── principal/               # Clase Main y tests
    ├── procesos/                # Lógica de negocio y utilidades
    ├── vista/                   # Interfaces gráficas (JInternalFrame)
    └── vista.assets/            # Recursos gráficos (imágenes)
```
---

## 📊 Modelo de Dominio

El siguiente diagrama describe las principales entidades y sus relaciones:

---

## 🚀 Instalación y Ejecución

1. **Clona el repositorio**:
   ```bash
   git clone https://github.com/Inici4rSesi0n/sistema-matriculas-primaria.git
2. **Ejecuta la clase principal.Main**
4. **Inicia sesión con las credenciales de prueba proporcionadas abajo**
5. **Credenciales de Prueba**

| Rol       | Código      | Contraseña |
|-----------|-------------|------------|
| Director  | `C22251227` | `admin123` |

6. **¿Cómo probar otros roles?**  
   - Inicia sesión como **Director** con la opción "Portal CTP" con las credenciales de arriba.
     <p align="center"><img width="643" height="388" alt="image" src="https://github.com/user-attachments/assets/78525b50-e80f-41e5-ae1c-c95b7842aa14"></p>
   - Desde el menú registros, selecciona la opción mantenimiento y verás diversas opciones para realizar registros.
     <p align="center"><img width="646" height="392" alt="image" src="https://github.com/user-attachments/assets/72a2b4b7-eaaa-4170-b4ec-5cc43a7dc430"></p>
   - Selecciona Docente/Estudiante y completa los datos para crear el registro.
     <p align="center"><img width="1416" height="699" alt="image" src="https://github.com/user-attachments/assets/90aeaf52-785c-4e35-8ef4-d170b4b8dbaf"></p>
   - Cierra sesión y prueba con las nuevas credenciales.

> ⚠️ **Importante:** El sistema permite que el Director cree cuentas para Docentes y Estudiantes. No hay cuentas predefinidas para estos roles.
---

## 🛠️ Tecnologías Utilizadas

| Tecnología       | Descripción                                           |
|------------------|-------------------------------------------------------|
| Java 17          | Lenguaje de programación principal                    |
| Swing            | Biblioteca gráfica para la interfaz de usuario        |
| Serialización    | Persistencia de objetos en archivos `.bin`            |
| NetBeans         | Entorno de desarrollo integrado (IDE)                 |
| Git / GitHub     | Control de versiones y repositorio remoto             |

---

## 🤝 Cómo Contribuir

Las contribuciones son bienvenidas. Si deseas colaborar:

1. Haz un **fork** del repositorio.
2. Crea una rama para tu funcionalidad (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza tus cambios y haz commit (`git commit -m 'Agrega nueva funcionalidad'`).
4. Sube la rama (`git push origin feature/nueva-funcionalidad`).
5. Abre un **Pull Request** describiendo tus cambios.

Para reportar errores o sugerir mejoras, por favor abre un **Issue** en el repositorio.

---
## ⚠️ Estado del Proyecto
El proyecto se encuentra en desarrollo activo. La lógica de negocio y 
las funcionalidades principales están completas y operativas. 
Quedan pendientes algunos refinamientos lógicos y visuales, 
así como la integración de los recursos gráficos de vista.assets.
Las contribuciones y sugerencias son bienvenidas.
