package presentacion.eventos;
import javafx.application.Platform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author inici4rsesi0n
 */
public final class SistemaEventBus {

    private static final Map<TipoEvento, List<WeakReference<Runnable>>> suscriptores = new EnumMap<>(TipoEvento.class);

    private SistemaEventBus() {}

    public static void suscribir(TipoEvento evento, Runnable listener) {
        if (evento == null || listener == null) return;
        suscriptores.computeIfAbsent(evento, k -> new ArrayList<>())
                .add(new WeakReference<>(listener));
    }

    public static void desuscribir(TipoEvento evento, Runnable listener) {
        if (evento == null || listener == null) return;
        List<WeakReference<Runnable>> lista = suscriptores.get(evento);
        if (lista != null) {
            lista.removeIf(ref -> ref.get() == null || ref.get().equals(listener));
        }
    }

    public static void notificar(TipoEvento... eventos) {
        if (eventos == null) return;
        for (TipoEvento e : eventos) notificar(e);
    }

    public static void notificar(TipoEvento evento) {
        if (evento == null) return;
        List<WeakReference<Runnable>> lista = suscriptores.get(evento);
        if (lista == null || lista.isEmpty()) return;

        List<Runnable> activos = new ArrayList<>();
        lista.removeIf(ref -> {
            Runnable r = ref.get();
            if (r != null) {
                activos.add(r);
                return false;
            }
            return true;         });

        if (activos.isEmpty()) return;

        Platform.runLater(() -> {
            for (Runnable r : activos) {
                try {
                    r.run();
                } catch (Exception e) {
                    System.err.println("[EventBus] Error en listener " + evento + ": " + e.getMessage());
                }
            }
        });
    }
}
