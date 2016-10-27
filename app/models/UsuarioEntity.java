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
    private TipoUsuario tipo;

    @ManyToOne
    private OilColEntity oilCol;

    @OneToOne
    private CampoEntity campo;

    public UsuarioEntity()
    {
        this.id=null;
        this.name ="NO NAME";
        this.tipo = null;
    }
    public UsuarioEntity(Long id) {
        this();
        this.id = id;
    }

    public UsuarioEntity(Long id, String name, OilColEntity oilCol, String tipo)
    {
        this.oilCol = oilCol;
        this.id = id;
        this.name = name;
        if(TipoUsuario.JefeDeCampo.toString().equalsIgnoreCase(tipo))
            this.tipo = TipoUsuario.JefeDeCampo;
        else
            this.tipo = TipoUsuario.JefeDeProduccion;

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

    public String getTipo() {
        return tipo.toString();
    }

//    public UsuarioEntity authenticate(String email, String password) {
//        return FINDER.where().eq("email", email)
//                .eq("password", password).findUnique();
//    }

    public void setTipo(String tipo) {
        if(TipoUsuario.JefeDeCampo.toString().equalsIgnoreCase(tipo) )
            this.tipo = TipoUsuario.JefeDeCampo;
        else
            this.tipo = TipoUsuario.JefeDeProduccion;
    }
    public CampoEntity getCampo() {
        return campo;
    }

    public void setCampo(CampoEntity campo) {
        if(this.tipo.equals(TipoUsuario.JefeDeCampo))
            this.campo = campo;
        else this.campo = null;
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tipo='" + tipo.toString() + '\'' +
                '}';
    }
}
