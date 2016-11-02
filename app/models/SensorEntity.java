package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jp.gonzalez14 on 27/08/2016.
 */
@Entity
@Table(name="sensorEntity")
public class SensorEntity extends Model {


    public static Finder<Long, SensorEntity> FINDER = new Finder<>(SensorEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator = "Sensor")
    private Long id;
    private TipoSensor tipo;

    @ManyToOne
    @JsonBackReference
    private PozoEntity pozo;

    @OneToMany(mappedBy = "sensor", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<InformeEntity > informes;

    public SensorEntity()
    {
        this.id=null;
        this.tipo = null;
    }
    public SensorEntity(Long id) {
        this();
        this.id = id;
    }

    public SensorEntity(Long id, String tipo, PozoEntity pozo)
    {
        this.setTipo(tipo);
        this.id = id;
        this.pozo = pozo;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo.toString();
    }

    public void setTipo(String tipo) {
        System.out.print(tipo);
        if(TipoSensor.BarrilesCrudo.toString().equalsIgnoreCase(tipo) )
            this.tipo = TipoSensor.BarrilesCrudo;
        else if(TipoSensor.ConsumoEnergetico.toString().equalsIgnoreCase(tipo) )
            this.tipo = TipoSensor.ConsumoEnergetico;
        else
            this.tipo = TipoSensor.TemperaturaBomba;
    }

    public PozoEntity getPozo() {
        return pozo;
    }

    public void setPozo(PozoEntity pozo) {
        this.pozo = pozo;
    }

    public void addInforme(InformeEntity nuevo){
        if(nuevo.getTipo().equals("0")&&tipo.equals(TipoSensor.TemperaturaBomba)){
            informes.add(nuevo);}else if(nuevo.getTipo().equals("1")&&tipo.equals(TipoSensor.ConsumoEnergetico)){informes.add(nuevo);}
            else if(nuevo.getTipo().equals("2")&&tipo.equals(TipoSensor.BarrilesCrudo)){informes.add(nuevo);}
            else{
            try {
                throw  new Exception("debe coincidir el tipo de informe con el tipo de sensor:" +
                    "Asi: /n"+"Informe tipo 0 --> sensor temperaturaBomba 2"+"/n" +
                    "Informe tipo 1 --> sensorEnergetico 1"+"/n" +"" +
                    "Informe tipo 3 --> Barriles de crudo 0");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public String toString() {
        return "SensorEntity{" +
                "id=" + id +
                ", tipo='" + tipo.toString() + '\'' +
                '}';
    }

    public List<InformeEntity> getInformes()
    {
        return  informes;
    }
}
