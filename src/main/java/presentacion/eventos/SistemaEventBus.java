package presentacion.eventos;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Bus de eventos simple para sincronizar controladores JavaFX.
 * Despacha los listeners en el hilo de UI vía Platform.runLater
 * para garantizar el refresco visual en el siguiente pulso.
 *
 * @author inici4rsesi0n
 */
public final class SistemaEventBus {

    private static final Map<Evento, List<Runnable>> suscriptores = new EnumMap<>(Evento.class);

    private SistemaEventBus() {}

    public static void suscribir(Evento evento, Runnable listener) {
        if (evento == null || listener == null) return;
        suscriptores.computeIfAbsent(evento, k -> new ArrayList<>()).add(listener);
    }

    public static void desuscribir(Evento evento, Runnable listener) {
        List<Runnable> lista = suscriptores.get(evento);
        if (lista != null) lista.remove(listener);
    }

    public static void notificar(Evento... eventos) {


        if (eventos == null) return;


        for (Evento e : eventos) notificar(e);


    }



    public static void notificar(Evento evento) {
        if (evento == null) return;
        List<Runnable> lista = suscriptores.get(evento);
        if (lista == null || lista.isEmpty()) return;
        List<Runnable> copia = new ArrayList<>(lista);
        Platform.runLater(() -> {
            for (Runnable r : copia) {
                try {
                    r.run();
                } catch (Exception e) {
                    System.err.println("[EventBus] Error en listener " + evento + ": " + e.getMessage());
                }
            }
        });
    }
}
