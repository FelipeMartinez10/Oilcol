package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.InformeEntity;
import models.PozoEntity;
import models.SensorEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;


/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class SensorController extends Controller {

    public CompletionStage<Result> getSensores() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return SensorEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(sensores -> {return ok(toJson(sensores));}
                );
    }

    public CompletionStage<Result> getSensor(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return SensorEntity.FINDER.byId(idP); } ,jdbcDispatcher)
                .thenApply(sensores -> {return ok(toJson(sensores));}
                );
    }
    public CompletionStage<Result> createSensorEnPozo(Long idPozo){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        SensorEntity sensor = Json.fromJson( n , SensorEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    PozoEntity pozo = PozoEntity.FINDER.byId(idPozo);
                    pozo.addSensor(sensor);
                    sensor.setPozo(pozo);
                    pozo.update();
                    sensor.save();
                    return sensor;
                }
        ).thenApply(
                sensores -> {
                    return ok(Json.toJson(sensores));
                }
        );
    }

    public CompletionStage<Result> createSensor(){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        SensorEntity sensor = Json.fromJson( n , SensorEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    sensor.save();
                    return sensor;
                }
        ).thenApply(
                sensores -> {
                    return ok(Json.toJson(sensores));
                }
        );
    }

    public CompletionStage<Result> deleteSensor(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    SensorEntity sensor = SensorEntity.FINDER.byId(idP);
                    sensor.delete();
                    return sensor;
                }
        ).thenApply(
                sensores -> {
                    return ok(Json.toJson(sensores));
                }
        );
    }
    public CompletionStage<Result> updateSensor( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        SensorEntity sensor = Json.fromJson( n , SensorEntity.class ) ;
        SensorEntity antiguo = SensorEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(sensor.getId());
                    antiguo.setPozo(sensor.getPozo());
                    antiguo.setTipo(sensor.getTipo());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                sensores -> {
                    return ok(Json.toJson(sensores));
                }
        );
    }
  public Result sensorHtml(Long idSensor)

  {
      SensorEntity sensor = SensorEntity.FINDER.byId(idSensor);

//       List<InformeEntity> informes = sensor.getInformes();
//        List<Long> idsSensores = new ArrayList<Long>();
//        for (SensorEntity sensor: sensores)
//        {
//            idsSensores.add(sensor.getId());
//        }

      List<InformeEntity> informes = InformeEntity.FINDER.where().in("sensor_id", sensor.getId()).findList();
      String datosTemp = "";
      String datosCaudal = "";
      String datosConsumo = "";
      for (int i = 0; i < informes.size(); i++)
      {
          if (informes.get(i).getTipo().equals("0"))
          {
              if (i < informes.size() - 1)
                  datosTemp += informes.get(i).getDato() + ",";
              else
                  datosTemp += informes.get(i).getDato();
          }
          else if (informes.get(i).getTipo().equals("2"))
          {
              if (i < informes.size() - 1)
                  datosCaudal += informes.get(i).getDato() + ",";
              else
                  datosCaudal += informes.get(i).getDato();
          }
          else if (informes.get(i).getTipo().equals("1"))
          {
              if (i < informes.size() - 1)
                  datosConsumo += informes.get(i).getDato() + ",";
              else
                  datosConsumo += informes.get(i).getDato();
          }
      }

   return ok(views.html.sensor.render(sensor,datosTemp,datosCaudal,datosConsumo));

    }
}
