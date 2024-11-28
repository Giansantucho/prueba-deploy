package modelo.personas;

import java.time.LocalDate;

import modelo.elementos.TarjetaPlastica;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
public class PersonaVulnerable {

    @Id
    @GeneratedValue
    private int id;
    
    @Column
    private LocalDate fechaRegistro;

    @Column
    @Getter private int menoresACargo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tarjeta_id", referencedColumnName = "id")
    private TarjetaPlastica tarjeta;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_alta_id", referencedColumnName = "id")
    private PersonaHumana dioAlta;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_humana_id", referencedColumnName = "id")
    private PersonaHumana persona;


    public PersonaVulnerable(LocalDate fechaRegistro, int menoresACargo, TarjetaPlastica tarjeta, PersonaHumana dioAlta, PersonaHumana persona){
        this.fechaRegistro = fechaRegistro;
        this.menoresACargo = menoresACargo;
        this.tarjeta = tarjeta;
        this.dioAlta = dioAlta;
        this.persona = persona;
    }

    public PersonaVulnerable() {

    }
}
