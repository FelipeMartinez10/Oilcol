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
    private String email;
    private String password;

    @ManyToOne
    private OilColEntity oilCol;

    @OneToOne
    private CampoEntity campo;

    public UsuarioEntity()
    {
        this.id=null;
        this.name ="NO NAME";
        this.tipo = null;
        this.email = "NO_EMAIL";
        this.password="NO_PASSWORD";
    }
    public UsuarioEntity(Long id) {
        this();
        this.id = id;
    }

    public UsuarioEntity(Long id, String name, OilColEntity oilCol, String tipo, String email, String password)
    {
        this.oilCol = oilCol;
        this.id = id;
        this.name = name;
        this.email= email;
        this.password=password;
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

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
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

    public static UsuarioEntity authenticate(String email, String password) {
        return FINDER.where().eq("email", email)
                .eq("password", password).findUnique();
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tipo='" + tipo.toString() + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
