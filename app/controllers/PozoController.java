package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.libs.Json.toJson;

/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class PozoController extends Controller {

    public CompletionStage<Result> getPozos() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return PozoEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(pozoEntities -> {return ok(toJson(pozoEntities));}
                );
    }

    public CompletionStage<Result> getPozo(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return PozoEntity.FINDER.byId(idP); } ,jdbcDispatcher)
                .thenApply(pozoEntities -> {return ok(toJson(pozoEntities));}
                );
    }
    public CompletionStage<Result> createPozo(){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        PozoEntity pozo = Json.fromJson( n , PozoEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    pozo.save();
                    return pozo;
                }
        ).thenApply(
                pozoEntity -> {
                    return ok(Json.toJson(pozoEntity));
                }
        );
    }

    public CompletionStage<Result> createPozoEnCampo(Long idCampo){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        PozoEntity pozo = Json.fromJson( n , PozoEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    CampoEntity campo = CampoEntity.FINDER.byId(idCampo);
                    campo.addPozo(pozo);
                    pozo.setCampo(campo);
                    campo.update();
                    pozo.save();
                    return pozo;
                }
        ).thenApply(
                pozoEntity -> {
                    return ok(Json.toJson(pozoEntity));
                }
        );
    }

    public CompletionStage<Result> deletePozo(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    PozoEntity pozo = PozoEntity.FINDER.byId(idP);
                    pozo.delete();
                    return pozo;
                }
        ).thenApply(
                pozoEntities -> {
                    return ok(Json.toJson(pozoEntities));
                }
        );
    }
    public CompletionStage<Result> updatePozo( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        PozoEntity pozo = Json.fromJson( n , PozoEntity.class ) ;
        PozoEntity antiguo = PozoEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(pozo.getId());
                    antiguo.setCampo(pozo.getCampo());
                    antiguo.setEstado(pozo.getEstado());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                pozoEntities -> {
                    return ok(Json.toJson(pozoEntities));
                }
        );
    }

    public Result pozoHtml(Long idPozo)
    {
        PozoEntity pozo = PozoEntity.FINDER.byId(idPozo);

        List<SensorEntity> sensores = pozo.getSensores();
        List<Long> idsSensores = new ArrayList<Long>();
        for (SensorEntity sensor: sensores)
        {
            idsSensores.add(sensor.getId());
        }
          List<InformeEntity> informes = InformeEntity.FINDER.where().in("sensor_id", idsSensores).findList();

            String datosTemp = "";
            String datosCaudal = "";
            String datosConsumo = "";
            int numEmergencias=0;
            double tempProm=0;
            double caudalProm=0;
            double consumoProm=0;
            int[] cantidades=new int[3];

        for (int i = 0; i < informes.size(); i++)
            {
                if (informes.get(i).getTipo().equals("0"))
                {
                    //Temperatura
                    cantidades[0]++;
                    tempProm+=informes.get(i).getDato();
                    if (i < informes.size() - 1)
                        datosTemp += informes.get(i).getDato() + ",";
                    else
                        datosTemp += informes.get(i).getDato();
                } else if (informes.get(i).getTipo().equals("2"))
                {
                    //Caudal
                    cantidades[1]++;
                    caudalProm+=informes.get(i).getDato();
                    if (i < informes.size() - 1)
                        datosCaudal += informes.get(i).getDato() + ",";
                    else
                        datosCaudal += informes.get(i).getDato();
                } else if (informes.get(i).getTipo().equals("1"))
                {
                    //Consumo
                    cantidades[2]++;
                    consumoProm+=informes.get(i).getDato();
                    if (i < informes.size() - 1)
                        datosConsumo += informes.get(i).getDato() + ",";
                    else
                        datosConsumo += informes.get(i).getDato();
                }

                if(informes.get(i).getEmergencia())
                {
                    numEmergencias++;
                }
            }


        if(!informes.isEmpty())
        {
            tempProm=tempProm/cantidades[0];
            caudalProm=caudalProm/cantidades[1];
            consumoProm=consumoProm/cantidades[2];
        }

            return ok(views.html.pozo.render(pozo, datosTemp, datosCaudal, datosConsumo, numEmergencias, tempProm+"", caudalProm+"", consumoProm+"", sensores));

    }


}
