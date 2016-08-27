package models;

import com.avaje.ebean.Model.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jp.gonzalez14 on 27/08/2016.
 */
public class InformeEntity {

    public static Finder<Long, InformeEntity> FINDER = new Finder<>(InformeEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Informe")
    private Long id;
    private String tipo;
    private double dato;
    private Date fecha;
    private boolean emergencia;

    @ManyToOne
    private PozoEntity pozo;

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

    public InformeEntity(boolean emergencia, double dato, Date fecha, Long id, String tipo, PozoEntity pozo)
    {
        this.emergencia = emergencia;
        this.dato = dato;
        this.fecha = fecha;
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

    public double getDato() {
        return dato;
    }

    public void setDato(double id) {
        this.dato = dato;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date id) {
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
                "dato=" + dato +
                "emergencia=" + emergencia +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
