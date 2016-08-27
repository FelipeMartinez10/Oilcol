package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe Martinez on 26/08/2016.
 */
@Entity
@Table(name="oilColEntity")
public class OilColEntity extends Model
{
    public static Model.Finder<Long,OilColEntity> FINDER = new Model.Finder<>(OilColEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "OilCol")
    private Long id;

    @OneToMany(mappedBy = "oilCol")
    private List<UsuarioEntity> usuarios;

    @OneToMany(mappedBy = "oilCol")
    private List<RegionEntity> regiones;

    //@OneToMany(mappedBy = "oilCol")
    //private List<InformeEntity> informes;

    public OilColEntity() {
        this.usuarios = new ArrayList<UsuarioEntity>();
        this.regiones = new ArrayList<RegionEntity>();
        this.id=null;
    }
    public OilColEntity(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UsuarioEntity> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioEntity> usuarios) {
        this.usuarios = usuarios;
    }

    public List<RegionEntity> getRegiones() {
        return regiones;
    }

    public void setRegiones(List<RegionEntity> regiones) {
        this.regiones = regiones;
    }

    @Override
    public String toString() {
        return "OilColEntity{" +
                "id=" + id +
                '}';
    }

}
