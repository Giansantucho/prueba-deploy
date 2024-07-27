package elementos;

import repositorios.RepositorioIncidentes;

public class ReceptorMovimiento {
    private Heladera heladera;

    public void recibirAlerta(){
        heladera.marcarComoInactiva();
        Alerta alerta = new Alerta(TipoAlerta.FRAUDE, heladera);
        RepositorioIncidentes repo = RepositorioIncidentes.getInstancia();
        repo.agregar(alerta);
    }

}