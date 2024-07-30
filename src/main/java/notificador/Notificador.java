package notificador;

import notificador.mail.ApacheCommonsEmail;
import notificador.mail.StrategyMAIL;
import personas.Colaborador;
import personas.TipoMedioDeContacto;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public class Notificador {
    @Getter
    private static Map<TipoMedioDeContacto, StrategyNotificacion> estrategias;

    static {
        estrategias = new HashMap<TipoMedioDeContacto, StrategyNotificacion>();

        StrategyMAIL mail = new StrategyMAIL();
        mail.setAdapter(new ApacheCommonsEmail());
        estrategias.put(TipoMedioDeContacto.MAIL, mail);
    }

    public static void agregarEstrategia(TipoMedioDeContacto medio, StrategyNotificacion estrategia){
        estrategias.put(medio, estrategia);
    }

    //TODO
/*
    public void notificarXNuevoUsuario(String mensaje, Colaborador persona) {

        if (persona.getEmail() == null) {
            throw new IllegalArgumentException("El colaborador no tiene un email asignado.");
        }

        if (estrategias.containsKey(TipoMedioDeContacto.MAIL)) {
            estrategias.get(TipoMedioDeContacto.MAIL).enviarNotificacion(mensaje, persona, "Bienvenido");
        } else {
            throw new NoExisteMetodoExcepcion();
        }
    }*/

    public static void notificarXNuevoUsuario(String mensaje, Colaborador persona) {

        if (persona.getEmail() == null) {
            throw new IllegalArgumentException("El colaborador no tiene un email asignado.");
        }

        if (estrategias.containsKey(TipoMedioDeContacto.MAIL)) {
            estrategias.get(TipoMedioDeContacto.MAIL).enviarNotificacion(mensaje, persona, "Bienvenido");
        } else {
            throw new NoExisteMetodoExcepcion();
        }
    }
}

    /*public void notificar(String mensaje, Colaborador persona) {

        TipoMedioContacto tipo = persona;
        String email = persona.getEmail();

        if (estrategias.containsKey(contacto)) {
            estrategias.get(contacto).enviarNotificacion(mensaje, persona);
        } else {
            throw new NoExisteMetodoExcepcion();
        }
    }*/

    /*
        TipoMedioContacto contacto = persona.getContacto().get(0);
        if(estrategias.containsKey(contacto)){
            estrategias.get(contacto).enviarNotificacion(mensaje, persona);
        }else{
            throw new NoExisteMetodoExcepcion();
        }

        // de aca se llama al StrategyNotificacion correspondiente
    }
    */

