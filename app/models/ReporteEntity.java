package models;

import com.avaje.ebean.Model;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by jd.torres11 on 27/08/2016.
 */
@Entity
@Table(name="reporteEntity")
public class ReporteEntity extends Model {
    public static Finder<Long, ReporteEntity> FINDER = new Finder<>(ReporteEntity.class);
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "Reporte")
    private Long id;
    private Date date;

    public ReporteEntity()
    {
        this.id = null;
        this.date=null;
    }
    public ReporteEntity(Long id)
    {
        this();
        this.id=id;
    }
    public ReporteEntity(Long id,Date date )
    {
        this.id=id;
        this.date=date;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return date;
    }

    public void setFecha(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SensorEntity{" +
                "id=" + id +
                "fecha=" + date.toString() +
                '}';
    }
}
