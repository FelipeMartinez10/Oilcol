package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jp.gonzalez14 on 27/08/2016.
 */
@Entity
@Table(name="informeEntity")
public class InformeEntity extends Model {

    public static Finder<Long, InformeEntity> FINDER = new Finder<>(InformeEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Informe")
    private Long id;
    private String tipo;
    private double dato;
    private Long fecha;
    private boolean emergencia;

    @ManyToOne
    @JsonBackReference
    private SensorEntity sensor;

    //@OneToMany(mappedBy = "informe")
    //private List<InformeEntity > informes;

    public InformeEntity()
    {
        this.emergencia = false;
        this.dato = -1;
        this.fecha = null;
        this.id=null;
        this.tipo ="NO NAME";
    }
    public InformeEntity(Long id) {
        this();
        this.id = id;
    }

    public InformeEntity(boolean emergencia, double dato, Long fecha, Long id, String tipo, SensorEntity sen)
    {
        this.emergencia = emergencia;
        this.dato = dato;
        this.fecha = fecha;
        this.tipo = tipo;
        this.id = id;
        this.sensor = sen;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDato() {
        return dato;
    }

    public void setDato(double dato) {
        this.dato = dato;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean getEmergencia() {
        return emergencia;
    }

    public void setEmergencia(boolean emergencia) {
        this.emergencia = emergencia;
    }

    public SensorEntity getSensor() {
        return sensor;
    }

    public void setSensor(SensorEntity sen) {
        this.sensor = sen;
    }
    @Override
    public String toString() {
        return "SensorEntity{" +
                "id=" + id +
                "dato=" + dato +
                "emergencia=" + emergencia +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
