package modelo.colaboracion;

import modelo.elementos.SolicitudApertura;
import lombok.Getter;
import lombok.Setter;
import modelo.elementos.Heladera;
import modelo.personas.Colaborador;
import modelo.personas.TipoPersona;
import persistencia.RepositorioSolicitudes;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DistribucionDeViandas extends Colaboracion {
    private Heladera heladeraOrigen;
    private Heladera heladeraDestino;
    private List<Vianda>viandas;
    private MotivoDistribucion motivoDistribucion;
    @Setter private static Double coeficiente = 1.0;
    private int viandasDistribuidas;
    @Getter private SolicitudApertura solicitud;

    // CONSTRUCTOR PRINCIPAL
    public DistribucionDeViandas(TipoPersona persona, Integer cantidadViandas, Heladera heladeraOrigen, Heladera heladeraDestino, MotivoDistribucion motivoDistribucion, LocalDate fechaDistribucion) {
        this.tiposPersonasHabilitadas = Arrays.asList(TipoPersona.PH);
        this.fechaColaboracion = fechaDistribucion;

        this.heladeraOrigen = heladeraOrigen;
        this.heladeraDestino = heladeraDestino;
        this.viandas = new ArrayList<>();
        this.motivoDistribucion = motivoDistribucion;
    }

    // CONSTRUCTOR PARA IMPORTADOR CSV
    public DistribucionDeViandas(LocalDate fechaDistribucion, Integer cantidadViandas)
    {
        this.tiposPersonasHabilitadas = Arrays.asList(TipoPersona.PH);
        this.fechaColaboracion  = fechaDistribucion;
        this.viandas = new ArrayList<>();

        this.heladeraDestino = null;
        this.heladeraOrigen = null;
        this.motivoDistribucion = null;
        this.viandasDistribuidas = 0;

    }

    public void efectuarApertura(Colaborador colaborador){
        if(this.solicitud != null){
            RepositorioSolicitudes repositorioSolicitudes = RepositorioSolicitudes.getInstancia();
            repositorioSolicitudes.cambiarEstadoAFehaciente(this.solicitud);
        }
        else {
            System.out.println("No se ha solicitado la apertura de la heladera");
            return;
        }
    }

    public void solicitarAperturaHeladera(Colaborador colaborador, Heladera heladera){
        if( !heladera.permitirIngreso() ) {
            System.out.println("La heladera ya se encuentra inhabilitada");
            return;
        }
        SolicitudApertura seguimientoApertura = new SolicitudApertura(heladera, colaborador);
        RepositorioSolicitudes repositorioSolicitudes = RepositorioSolicitudes.getInstancia();
        repositorioSolicitudes.agregarSolicitud(seguimientoApertura);
        this.solicitud = seguimientoApertura;

        if( colaborador.getTarjeta() == null ){
            colaborador.solicitarTarjeta();
        }
    }
    @Override
    public void hacerColaboracion(Colaborador colaborador) {
        if(this.solicitud.getAperturaFehaciente() == null){
            System.out.println("No se ha solicitado la apertura de la heladera destino");
            return;
        }else if ( Duration.between(this.solicitud.getFechaSolicitud(), this.solicitud.getAperturaFehaciente()).toHours() >= this.solicitud.getHorasDeApertura()){
            System.out.println("El usuario carece de permisos para realizar dicha acción.");
            return;
        }
        this.efectuarApertura(colaborador);
        String text = validar(colaborador);
        if(text == null){
            colaborador.agregarColaboracion(this);

            switch (motivoDistribucion) {
                case DESPERFECTO:

                    for (Vianda vianda : viandas) {
                        if(!heladeraDestino.hayLugar()){
                            System.out.println("No hay lugar en la heladera destino");
                            break; // salgo del for
                        }
                        heladeraOrigen.retirarVianda(heladeraOrigen.getViandas().indexOf(vianda));
                        heladeraDestino.agregarVianda(vianda);
                        viandasDistribuidas += 1;
                    }
                    break;
                case FALTA_VIANDAS:
                    if(!heladeraDestino.hayLugar()){
                        System.out.println("No hay lugar en la heladera destino");
                        break; // salgo del for
                    }else {
                        for (Vianda vianda : viandas) {
                            heladeraOrigen.retirarVianda(heladeraOrigen.getViandas().indexOf(vianda));
                            heladeraDestino.agregarVianda(vianda);
                            viandasDistribuidas += 1;
                        }
                        break;
                    }
                default:
                    break;
            }

            incrementarPuntos(colaborador);
        }
        else {
            System.out.println("Error!!!");
            System.out.println(text);
        }
    }

    @Override
    public String validar(Colaborador colaborador) {
        if(heladeraOrigen.getHabilitado() && heladeraOrigen.getActiva() && this.tiposPersonasHabilitadas.contains(colaborador.getTipoPersona())){
            return null;
        }
        else if(heladeraOrigen.getHabilitado()){
            return "La heladera no está habilitada";
        }
        else if(heladeraOrigen.getActiva()){
            return "La heladera no está activa";
        } else {
            return "Ese Tipo de Persona no puede realizar este tipo de Colaboración!";
        }
    }

    @Override
    public void incrementarPuntos(Colaborador colaborador) {
        colaborador.incrementarPuntaje(this.viandasDistribuidas * coeficiente);
    }

}