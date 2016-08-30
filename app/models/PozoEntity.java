package models;


import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jp.gonzalez14 on 27/08/2016.
 */
@Entity
@Table(name="pozoEntity")
public class PozoEntity extends Model {

    public static Finder<Long, PozoEntity> FINDER = new Finder<>(PozoEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Pozo")
    private Long id;
    private EstadoPozo estado;

    @ManyToOne
    private CampoEntity campo;

    @OneToMany(mappedBy = "pozo")
    private List<SensorEntity > sensores;

    public PozoEntity()
    {
        this.id=null;
        this.estado = null;
    }
    public PozoEntity(Long id) {
        this();
        this.id = id;
    }

    public PozoEntity(Long id, String estado, CampoEntity campo)
    {
        this.campo = campo;
        this.id = id;
        this.setEstado(estado);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado.toString();
    }

    public void setEstado(String estado) {
        if(estado.equalsIgnoreCase(EstadoPozo.Abierto.toString()))
            this.estado = EstadoPozo.Abierto;
        else if(estado.equalsIgnoreCase(EstadoPozo.Clausurado.toString()))
            this.estado = EstadoPozo.Clausurado;
        else if(estado.equalsIgnoreCase(EstadoPozo.Parado.toString()))
            this.estado = EstadoPozo.Parado;
        else
            this.estado = EstadoPozo.Produccion;

    }

    public CampoEntity getCampo() {
        return campo;
    }

    public void setCampo(CampoEntity campo) {
        this.campo = campo;
    }
    @Override
    public String toString() {
        return "PozoEntity{" +
                "id=" + id +
                ", estado='" + estado.toString() + '\'' +
                '}';
    }
}
