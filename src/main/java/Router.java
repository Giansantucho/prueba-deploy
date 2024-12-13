import static io.javalin.apibuilder.ApiBuilder.*;

import accessManagment.AutorizacionMiddleware;
import io.javalin.Javalin;
import persistencia.*;
import presentacion.InicioController;
import presentacion.LogoutController;
import presentacion.MostrarErrorPermisosController;
import presentacion.colaboraciones.*;
import presentacion.gestorCuentas.*;
import presentacion.heladera.AceptarAgregarHeladeraController;
import presentacion.heladera.AgregarHeladeraController;
import presentacion.heladera.HeladeraAgregadaController;
import presentacion.heladera.MapaHeladeraVistaController;
import presentacion.heladera.MapaHeladerasController;
import presentacion.heladera.MapaHeladerasDistribucionDestinoController;
import presentacion.heladera.MapaHeladerasDistribucionOrigenController;
import presentacion.heladera.VisualizarDetalleHeladeraController;
import presentacion.incidentes.AceptarReportarFallaController;
import presentacion.incidentes.ReportarFallaTecnicaController;
import presentacion.incidentes.ReportarFallaTecnicaFinalizadaController;
import presentacion.incidentes.VisualizarAlertasController;
import presentacion.incidentes.VisualizarFallasTecnicasController;
import presentacion.ofertas.*;
import presentacion.suscripciones.SuscribirseController;
import presentacion.vistaAdmin.CargarSCVController;
import presentacion.vistaAdmin.SCVCargadoController;
import presentacion.vistaAdmin.inicioADMINController;
import presentacion.reportes.MisReportesController;
import servicioApiRest.ServicioApiRest;

import javax.persistence.EntityManager;

public class Router {
    private static Javalin app = Application.app();

    public static void init(EntityManager entityManager){
        /* *************************************************************************** */
        RepositorioArchivos repoArchivos = RepositorioArchivos.getInstancia(entityManager);
        RepositorioColaboradores repoColab = RepositorioColaboradores.getInstancia(entityManager);
        RepositorioHeladeras repoHeladeras = RepositorioHeladeras.getInstancia(entityManager);
        RepositorioIncidentes repoIncidentes = RepositorioIncidentes.getInstancia(entityManager);
        RepositorioOfertas repoOfertas = RepositorioOfertas.getInstancia(entityManager);
        RepositorioPersonasVulnerables repoPersonasVulnerable = RepositorioPersonasVulnerables.getInstancia(entityManager);
        RepositorioReportes repoReportes = RepositorioReportes.getInstancia(entityManager);
        RepositorioSolicitudes repoSolicitudes = RepositorioSolicitudes.getInstancia(entityManager);
        RepositoriosTecnicos repoTecnicos = RepositoriosTecnicos.getInstancia(entityManager);
        RepositorioTarjetas repoTarjetas = RepositorioTarjetas.getInstancia(entityManager);
        RepositorioVisitas repoVisitas = RepositorioVisitas.getInstancia(entityManager);
        RepositorioUsuarios repoUsuarios = RepositorioUsuarios.getInstancia(entityManager);

        /* *************************************************************************** */

        // ------------------------------------   RUTAS   ------------------------------------------------
        app.routes(() -> {

            path("/", () -> {
                get(ctx -> ctx.redirect("/inicio"));
            });

            path("/404NotFound", () -> {
                get(new MostrarErrorPermisosController());
            });

            path("/aceptarAgregarHeladera", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado().setDebeSerPJ());
                get(new AceptarAgregarHeladeraController());
            });

            path("/aceptarAgregarProducto", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new AceptarAgregarProductoController());
            });

            path("/aceptarReportarFalla", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new AceptarReportarFallaController());
            });

            path("/agregarHeladera", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado().setDebeSerPJ());
                get(new AgregarHeladeraController());
                post(new HeladeraAgregadaController());
            });

            path("/agregarProductosEmpresa", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new AgregarProductosEmpresaController());
                post(new AgregarProductosEmpresaFinalizadoController());
            });

            path("/api/recomendacion/locaciones", () -> {
                get(new ServicioApiRest());
            });

            path("/canjearPuntos", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new CanjearPuntosController());
                post(new CanjearPuntosFinalizadoController());
            });

            path("/catalogoProductos", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new CatalogoProductosController());
            });

            path("/cargarCSV", () -> {
                get(new CargarSCVController());
                post(new SCVCargadoController());
            });

            path("/configurarPerfil", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new ConfigurarPerfilController());
                post(new ConfigurarPerfilFinalizadoController());
            });

            path("/crearCuentaFisica", () -> {
                //before(new AutorizacionMiddleware()); //TODO no tiene que estar logueado
                get(new CrearCuentaFisicaController());
                post(new CuentaFisicaCreadaController());
            });

            path("/crearCuentaJuridica", () -> {
                //before(new AutorizacionMiddleware());
                get(new CrearCuentaJuridicaController());
                post(new CuentaJuridicaCreadaController());
            });

            path("/cuentaCreada", () -> {
                before(new AutorizacionMiddleware());
                get(new CuentaCreadaController());
            });

            path("/donarDistribuirViandas", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado().setDebeSerPF());
                get(new DonarDistribucionViandaController());
                post(new DonarDistribucionViandaRealizadaController());
            });

            path("/donarDinero", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new DonarDineroController());
                post(new DonarDineroRealizadaController());
            });

            path("/donarVianda", () -> {
                //before(new AutorizacionMiddleware().setDebeSerLogueado().setDebeSerPF());
                get(new DonarViandaController());
                post(new DonarViandaRealizadaController());
            });

            path("/elegirDonacion", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new ElegirDonacionController());
            });

            path("/elegirRegistroCuenta", () -> {
                before(new AutorizacionMiddleware());
                get(new ElegirRegistroCuentaController());
            });

            path("/graciasPorDonar", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new GraciasPorDonarController());
            });

            path("/inicio", () -> {
                get(new InicioController());
            });

            path("/inicioADMIN", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado().setDebeSerAdmin());
                get(new inicioADMINController());
            });

            path("/login", () -> {
                get(new ShowLoginController());
                post(new ProcessLoginController());
            });

            path("/logout", () -> {
                get(new LogoutController());
            });

            path("/mapaHeladeras", () -> {
                get(new MapaHeladeraVistaController());
            });

            path("/mapaHeladerasDistribucionDestino", () -> {
                get(new MapaHeladerasDistribucionDestinoController());
            });

            path("/mapaHeladerasDistribucionOrigen", () -> {
                get(new MapaHeladerasDistribucionOrigenController());
            });

            path("/mapaHeladerasRequest", () -> {
                get(new MapaHeladerasController());
            });

            path("/misCanjes", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new MisCanjesController());
            });

            path("/misColaboraciones", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new MisColaboracionesController());
            });

            path("/misReportes", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new MisReportesController());
            });

            path("/perfil", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new PerfilController());
            });

            path("/registroPersonaVulnerable", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new RegistroPersonaVulnerableController());
                post(new RegistroPersonaVulnerableRealizadaController());
            });

            path("/registroPersonaVulnerableFinal", () -> {
                //(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new RegistroPersonaVulnerableFinalController());
            });

            path("/reportarFallaTecnica", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new ReportarFallaTecnicaController());
                post(new ReportarFallaTecnicaFinalizadaController());
            });

            path("/solicitarTarjeta", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new SolicitudDeTarjetasController());
            });

            path("/suscribirse", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                post(new SuscribirseController());
            });

            path("/visualizarAlertas", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new VisualizarAlertasController());
            });

            path("/visualizarDetalleHeladera", () -> {
                get(new VisualizarDetalleHeladeraController());
            });

            path("/visualizarFallasTecnicas", () -> {
                before(new AutorizacionMiddleware().setDebeSerLogueado());
                get(new VisualizarFallasTecnicasController());
            });

        });

        // EXCEPCION
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.redirect("/404NotFound");
        });
    }
}

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