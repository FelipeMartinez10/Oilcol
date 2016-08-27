package models;

import com.avaje.ebean.Model.*;

import javax.persistence.*;
/**
 * Created by jp.gonzalez14 on 27/08/2016.
 */
public class SensorEntity {

    public static Finder<Long, SensorEntity> FINDER = new Finder<>(SensorEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Pozo")
    private Long id;
    private String tipo;
    private boolean emergencia;

    @ManyToOne
    private PozoEntity pozo;

    //@OneToMany(mappedBy = "sensor")
    //private List<SensorEntity > sensores;

    public SensorEntity()
    {
        this.emergencia = false;
        this.id=null;
        this.tipo ="NO NAME";
    }
    public SensorEntity(Long id) {
        this();
        this.id = id;
    }

    public SensorEntity(boolean emergencia, Long id, String tipo, PozoEntity pozo)
    {
        this.emergencia = emergencia;
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

    public String getEstado() {
        return tipo;
    }

    public void setEstado(String tipo) {
        this.tipo = tipo;
    }

    public boolean getEmergencia() {
        return emergencia;
    }

    public void setEmergencia(boolean emergencia) {
        this.emergencia = emergencia;
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
                "emergencia=" + emergencia +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
