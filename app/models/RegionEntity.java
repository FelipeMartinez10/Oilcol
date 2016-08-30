package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe Martinez on 26/08/2016.
 */
@Entity
@Table(name="regionEntity")
public class RegionEntity extends Model
{
    public static Finder<Long,RegionEntity> FINDER = new Finder<>(RegionEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Region")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "region")
    private List<CampoEntity> campos;

    @ManyToOne
    private OilColEntity oilCol;

    public RegionEntity() {
        this.campos = new ArrayList<CampoEntity>();
        this.id=null;
        this.name ="NO NAME";
    }
    public RegionEntity(Long id) {
        this();
        this.id = id;
    }

    public RegionEntity(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CampoEntity> getCampos() {
        return campos;
    }

    public void setCampos(List<CampoEntity> campos) {
        this.campos = campos;
    }

    @Override
    public String toString() {
        return "RegionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
