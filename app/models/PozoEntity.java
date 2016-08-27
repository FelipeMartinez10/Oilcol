package models;


import com.avaje.ebean.Model.*;

import javax.persistence.*;
/**
 * Created by jp.gonzalez14 on 27/08/2016.
 */
public class PozoEntity {

    public static Finder<Long, PozoEntity> FINDER = new Finder<>(PozoEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Pozo")
    private Long id;
    private String estado;

    @ManyToOne
    private CampoEntity campo;

    //@OneToMany(mappedBy = "sensor")
    //private List<SensorEntity > sensores;

    public PozoEntity()
    {
        this.id=null;
        this.estado ="NO NAME";
    }
    public PozoEntity(Long id) {
        this();
        this.id = id;
    }

    public PozoEntity(Long id, String estado, CampoEntity campo)
    {
        this.campo = campo;
        this.id = id;
        this.estado = estado;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
                ", estado='" + estado + '\'' +
                '}';
    }
}
