package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Felipe Martinez on 26/08/2016.
 */
@Entity
@Table(name="campoEntity")
public class CampoEntity extends Model
{
    public static Finder<Long, CampoEntity> FINDER = new Finder<>(CampoEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Campo")
    private Long id;
    private String name;

    @ManyToOne(cascade= CascadeType.ALL)
    @JsonBackReference
    private RegionEntity region;

    @OneToMany(mappedBy = "campo",cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<PozoEntity > pozos;

    public CampoEntity()
    {
        this.id=null;
        this.name ="NO NAME";
    }
    public CampoEntity(Long id) {
        this();
        this.id = id;
    }

    public CampoEntity(Long id, String name, RegionEntity region)
    {
        this.region = region;
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

    public RegionEntity getRegion() {
        return region;
    }

    public void addPozo(PozoEntity nuevo){pozos.add(nuevo);}

    public void setRegion(RegionEntity region) {
        this.region = region;
    }
    public List<PozoEntity> getPozos()
    {
        return pozos;
    }
    public void setSensores(List<PozoEntity> pPozos)
    {
        pozos=pPozos;
    }
    @Override
    public String toString() {
        return "CampoEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
