package presentacion.heladera;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import modelo.elementos.Heladera;
import org.jetbrains.annotations.NotNull;
import persistencia.RepositorioHeladeras;

import java.util.List;

public class MapaHeladerasController implements Handler {

    private RepositorioHeladeras repositorioHeladeras;

    public MapaHeladerasController(RepositorioHeladeras repo) {
        super();
        this.repositorioHeladeras = repo;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // Obtén las heladeras del repositorio
        List<Heladera> heladeras = this.repositorioHeladeras.obtenerHeladeras();

        // Devuelve las heladeras en formato JSON
        context.json(heladeras);
    }


}
