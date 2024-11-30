package modelo.elementos;

import lombok.Getter;
import persistencia.RepositorioTarjetas;
import pruebas.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarjeta")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Tarjeta {

    @Id
    @GeneratedValue
    @Getter private long id;

    @Column
    @Getter protected String nro_tarjeta;

    @OneToMany
    @JoinColumn(name="uso_tarjeta_id",referencedColumnName = "id")
    @Getter protected List<UsoTarjeta> historialDeUsos;

    @Column
    @Getter protected Boolean recibida;

    public Tarjeta(){
        this.nro_tarjeta = IdGenerator.getInstancia().generateNextId();
        this.historialDeUsos = new ArrayList<UsoTarjeta>();
        this.recibida = false;
    }

    public void registrarUso(Heladera heladera){
        this.historialDeUsos.add(new UsoTarjeta(heladera));
    }

    public void fueRecibida(){
        this.recibida = true;
    }
}
