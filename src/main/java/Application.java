import io.javalin.Javalin;
import persistencia.RepositorioColaboradores;
import persistencia.RepositorioHeladeras;
import persistencia.RepositorioPersonasVulnerables;
import presentacion.*;

public class Application {

    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> {
                    javalinConfig.plugins.enableCors(cors -> {
                        cors.add(it -> it.anyHost());
                    }); // para poder hacer requests de un dominio a otro

                    javalinConfig.staticFiles.add("/"); //recursos estaticos (HTML, CSS, JS, IMG)


                    /*javalinConfig.accessManager((handler, ctx, routeRoles) -> {
                        Roles userRole = getUserRole(ctx);
                        if (routeRoles.contains(userRole)) {
                            handler.handle(ctx);
                        }
                        else {
                            ctx.status(401).result("Unauthorized");
                            //ctx.redirect("/login");
                        }
                    });*/
                }

            )
            .get("/", ctx -> {
                //ctx.sessionAttribute("rolUsuario", Roles.SIN_PERMISOS);  // Set session role attribute
                ctx.redirect("/inicio-sinLog");  // Redirect to a non-logged-in start page
            })
            .start(8080);

        RepositorioColaboradores repoColab = RepositorioColaboradores.getInstancia();
        RepositorioHeladeras repoHeladeras = RepositorioHeladeras.getInstancia();
        RepositorioPersonasVulnerables repoPersonasVulnerable = RepositorioPersonasVulnerables.getInstancia();

        app.get("/login", new ShowLoginController()/*, (RouteRole) Set.of(Roles.SIN_PERMISOS)*/);
        app.post("/login", new ProcessLoginController(repoColab));

        app.get("/inicio-sinLog", new InicioController());
        app.get("/inicio-conLog", new InicioLogeadoController());

        // Routa para probar los Roles
        // app.get("/elegirRegistroCuenta", new ElegirRegistroCuentaController(), (RouteRole) Set.of(Roles.SIN_PERMISOS));
        app.get("/elegirRegistroCuenta", new ElegirRegistroCuentaController());

        app.get("/crearCuentaJuridica", new CrearCuentaJuridicaController());
        app.get("/crearCuentaFisica", new CrearCuentaFisicaController());

        app.post("/crearCuentaFisica", new CuentaFisicaCreadaController(repoColab));
        app.post("/crearCuentaJuridica", new CuentaJuridicaCreadaController(repoColab));
        app.get("/cuentaCreada", new CuentaCreadaController());


        //TODO todos los botones que redireccionen a DONAR tienen que venir a este GET
        // TODO FALTA INSERTAR LAS IMAGENES EN LOS BOTONES DE ELEGIR DONACION (en mustache)
        app.get("/elegirDonacion", new ElegirDonacionController());
        app.get("/elegirDonacionFisica", new ElegirDonacionFisicaController());
        app.get("/elegirDonacionJuridica", new ElegirDonacionJuridicaController());

        app.get("/donarVianda", new DonarViandaController());
        //app.get("/donarVianda/{name}&{adress}&{status}&{date}", new DonarViandaController());
        app.post("/donarVianda", new DonarViandaRealizadaController(repoHeladeras));

        app.get("/donarDinero", new DonarDineroController());
        app.post("/donarDinero", new DonarDineroRealizadaController());

        app.get("/donarDistribuirViandas", new DonarDistribucionViandaController());
        //app.post("/donarDistribuirViandas", new DonarDistribucionViandaRealizadaController());

        app.get("/donarEntregarTarjetas", new DonarEntregarTarjetasController());
        //app.post("/donarEntregarTarjetas", new DonarEntregarTarjetasRealizadaController());

        app.get("/agregarHeladera", new AgregarHeladeraController());
        app.post("/heladeraAgregada", new HeladeraAgregadaController(repoHeladeras));


        app.get("/mapaHeladeras", new MapaHeladerasController(repoHeladeras));

        app.get("/visualizarDetalleHeladera", new VisualizarDetalleHeladeraController());

        app.get("/visualizarAlertas", new VisualizarAlertasController());

        app.get("/visualizarFallasTecnicas", new VisualizarFallasTecnicasController());

        app.get("/registroPersonaVulnerable", new RegistroPersonaVulnerableController());
        app.post("/registroPersonaVulnerable", new RegistroPersonaVulnerableRealizadaController(repoPersonasVulnerable));

        app.get("/registroPersonaVulnerableFinal", new RegistroPersonaVulnerableFinalController());

        // http://localhost:8080/home

        // API REST
        //app.get("/api/mascotas", new GetMascotasHandler());
        //app.get("/api/mascotas/{id}", new GetMascotaIdHandler());
        //app.get("/api/mascotas/{id}/imagen", new GetMascotaImgHandler());
        //app.post("/api/mascotas", new PostMascotaHandler());
        //app.get("/api/mis-datos", new GetPerfilSesionHandler());

        //app.post("/api/login", new LoginHandler());

        // VISTA
        //app.get("/home", new IndexHandler());
        //app.get("/info-mascota", new InfoMascotaHandler());


        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400);
        });

    }

    /*
    private static Roles getUserRole(Context ctx) {
        return Roles.CON_PERMISOS;//ctx.sessionAttribute("rolUsuario");
    }*/
}
