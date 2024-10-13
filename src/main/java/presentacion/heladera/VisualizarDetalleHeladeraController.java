package presentacion.heladera;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VisualizarDetalleHeladeraController implements Handler{
    @Override
    public void handle(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();
        Boolean estaLogueado = context.sessionAttribute("logueado");
        //if( estaLogueado == null ){ estaLogueado = false; }
        model.put("logueado", estaLogueado != null && estaLogueado);
        context.render("templates/visualizarDetalleHeladera.mustache", model);



    }
}