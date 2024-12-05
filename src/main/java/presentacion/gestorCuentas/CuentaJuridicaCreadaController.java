package presentacion.gestorCuentas;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import modelo.authService.AuthServiceColaborador;
import modelo.authService.AuthServiceUsuario;
import modelo.colaboracion.MotivoDistribucion;
import modelo.excepciones.ExcepcionValidacion;
import modelo.personas.Rubro;
import modelo.personas.TipoJuridico;
import org.jetbrains.annotations.NotNull;
import persistencia.RepositorioColaboradores;

import java.util.HashMap;
import java.util.Map;

public class CuentaJuridicaCreadaController implements Handler {

    private RepositorioColaboradores repoColab;


    public CuentaJuridicaCreadaController(RepositorioColaboradores repoColab) {
        super();
        this.repoColab = repoColab;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();
        context.render("templates/elegirRegistroCuenta.mustache", model);

        String razonSocial = context.formParam("razon-social");
        Integer tipo = Integer.valueOf(context.formParam("tipo"));
        Integer rubro = Integer.valueOf(context.formParam("rubro"));
        String cuit = context.formParam("cuit");
        String telefono = context.formParam("telefono-representante");
        String email = context.formParam("email");
        String username = context.formParam("username");
        String password = context.formParam("password");
        String terminos = context.formParam("terms");

        if (razonSocial.equals("") || tipo.equals("") || rubro.equals("") || cuit.equals("") || email.equals("") || username.equals("")
            || password.equals("") || terminos.equals("")){
            model.put("error", "Debe completar todos los campos");
            //context.status(400);
            context.redirect("/crearCuentaJuridica");
            return;
        }

        if ( !esNumerico(cuit)  ||  !esNumerico(telefono) )  {
            model.put("error", "El número de CUIT o el teléfono no son numéricos");
            //context.status(400);
            context.redirect("/crearCuentaJuridica");
            return;
        }

        if ( !cuit.matches("[0-9]{11}") )  {
            model.put("error", "El número de CUIT debe tener 11 dígitos");
            //context.status(400);
            context.redirect("/crearCuentaJuridica");
            return;
        }

        if ( !telefono.equals("")  &&  !telefono.matches("[0-9]{8,10}") )  {
            model.put("error", "El teléfono debe tener entre 8 y 10 dígitos");
            //context.status(400);
            context.redirect("/crearCuentaJuridica");
            return;
        }

        // hay que validad que no exista el mail en la base de datos
        //todo

        Rubro rubroJuridico;
        switch (rubro) {
            case 1:
                rubroJuridico = Rubro.GASTRONOMIA;
                break;
            case 2:
                rubroJuridico = Rubro.ELECTRONICA;
                break;
            default:
                rubroJuridico = Rubro.ARTICULOS_HOGAR;
                break;
        }

        TipoJuridico tipoJuridico;
        switch (tipo) {
            case 1:
                tipoJuridico = TipoJuridico.GUBERNAMENTAL;
                break;
            case 2:
                tipoJuridico = TipoJuridico.ONG;
                break;
            case 3:
                tipoJuridico = TipoJuridico.EMPRESA;
                break;
            default:
                tipoJuridico = TipoJuridico.INSTITUCION;
                break;
        }

        try {
            AuthServiceUsuario.registrarUsuario(email, username, password);
            AuthServiceColaborador.registrarColaboradorJuridico(razonSocial, tipoJuridico, rubroJuridico, cuit, telefono, email);

        } catch (ExcepcionValidacion e) {
            // TODO ROLLBACK
            model.put("errorJuridico", e.getMessage());
            //context.status(400);
            context.redirect("/crearCuentaJuridica");
            return;
        }

        // crear la cuenta y enviar a
        context.redirect("/cuentaCreada");
    }


    public static boolean esNumerico(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}

