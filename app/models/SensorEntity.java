package models;

import com.avaje.ebean.Model.*;

import javax.persistence.*;
/**
 * Created by jp.gonzalez14 on 27/08/2016.
 */
public class SensorEntity {

    public static Finder<Long, SensorEntity> FINDER = new Finder<>(SensorEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Sensor")
    private Long id;
    private String tipo;

    @ManyToOne
    private PozoEntity pozo;

    //@OneToMany(mappedBy = "informe")
    //private List<InformeEntity > informes;

    public SensorEntity()
    {
        this.id=null;
        this.tipo ="NO NAME";
    }
    public SensorEntity(Long id) {
        this();
        this.id = id;
    }

    public SensorEntity(Long id, String tipo, PozoEntity pozo)
    {
        this.tipo = tipo;
        this.id = id;
        this.pozo = pozo;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public PozoEntity getCampo() {
        return pozo;
    }

    public void setCampo(PozoEntity pozo) {
        this.pozo = pozo;
    }
    @Override
    public String toString() {
        return "SensorEntity{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
