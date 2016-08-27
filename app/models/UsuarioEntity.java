package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by Felipe Martinez on 26/08/2016.
 */
@Entity
@Table(name="usuarioEntity")
public class UsuarioEntity extends Model
{
    public static Finder<Long, UsuarioEntity> FINDER = new Finder<>(UsuarioEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Usuario")
    private Long id;
    private String name;

    @ManyToOne
    private OilColEntity oilCol;

    public UsuarioEntity()
    {
        this.id=null;
        this.name ="NO NAME";
    }
    public UsuarioEntity(Long id) {
        this();
        this.id = id;
    }

    public UsuarioEntity(Long id, String name, OilColEntity oilCol)
    {
        this.oilCol = oilCol;
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

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
