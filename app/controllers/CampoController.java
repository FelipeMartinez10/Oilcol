package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.*;
import play.libs.Json;
import play.mvc.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;

/**
 * Created by jd.torres11 on 27/08/2016.
 */

public class CampoController extends Controller{

    public CompletionStage<Result> getCampos() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return CampoEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(campoEntities -> {return ok(toJson(campoEntities));}
                );
    }
    public CompletionStage<Result> getCampo(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return CampoEntity.FINDER.byId(idP); } ,jdbcDispatcher)
                .thenApply(itemEntities -> {return ok(toJson(itemEntities));}
                );
    }
    public CompletionStage<Result> createCampo(){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode nCamp = request().body().asJson();
        CampoEntity campo = Json.fromJson( nCamp , CampoEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    campo.save();
                    return campo;
                }
        ).thenApply(
                CampoEntity -> {
                    return ok(Json.toJson(CampoEntity));
                }
        );
    }
    public CompletionStage<Result> createCampoEnRegion(Long idRegion){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode nCamp = request().body().asJson();
        CampoEntity campo = Json.fromJson( nCamp , CampoEntity.class );
        return CompletableFuture.supplyAsync(
                ()->{
                    RegionEntity region = RegionEntity.FINDER.byId(idRegion);
                    region.addCampo(campo);
                    campo.setRegion(region);
                    region.update();
                    campo.save();
                    return campo;
                }
        ).thenApply(
                CampoEntity -> {
                    return ok(Json.toJson(CampoEntity));
                }
        );
    }
    public CompletionStage<Result> deleteCampo(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    CampoEntity campo = CampoEntity.FINDER.byId(idP);
                    campo.delete();
                    return campo;
                }
        ).thenApply(
                CampoEntity -> {
                    return ok(Json.toJson(CampoEntity));
                }
        );
    }
    public CompletionStage<Result> updateCampo( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode nCamp = request().body().asJson();
        CampoEntity camp = Json.fromJson( nCamp , CampoEntity.class ) ;
        CampoEntity antiguo = CampoEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(camp.getId());
                    antiguo.setName(camp.getName());
                    antiguo.setRegion(camp.getRegion());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                CampoEntity -> {
                    return ok(Json.toJson(CampoEntity));
                }
        );
    }


    public Result campoHtml(Long idCampo)
    {
        CampoEntity campo = CampoEntity.FINDER.byId(idCampo);
        double total1=0;
        double total2=0;
        String datosTemp = "";
        String datosCaudal = "";
        String datosConsumo = "";
        int numEmergencias = 0;
        double dato1 = 0;
        double dato2 = 0;
        double dato3 = 0;
        int tipo1=0;
        int tipo2=0;
        int tipo3=0;
        double tempProm=0;
        double caudalProm=0;
        double consumoProm=0;
        int[] cantidades=new int[3];
        int produccion=0;
        int clausurados=0;
        int abierto=0;
        int parado=0;

        List<PozoEntity> pozos = PozoEntity.FINDER.where().in("campo_id", idCampo).findList();
        for (PozoEntity pozo: pozos)
        {
            if(pozo.getEstado().equals(EstadoPozo.Produccion))produccion++;
            if(pozo.getEstado().equals(EstadoPozo.Clausurado))clausurados++;
            if(pozo.getEstado().equals(EstadoPozo.Abierto))abierto++;
            if(pozo.getEstado().equals(EstadoPozo.Parado))parado++;

        List<SensorEntity> sensores = SensorEntity.FINDER.where().in("pozo_id", pozo.getId()).findList();
        for (int i = 0; i < sensores.size(); i++){
            List<InformeEntity> informes = InformeEntity.FINDER.where().in("sensor_id", sensores.get(i).getId()).findList();
            for (int j = 0; j < informes.size(); j++)
            {
                if (informes.get(j).getTipo().equals("0"))
                {
                    cantidades[0]++;
                    tempProm+=informes.get(i).getDato();

                    dato1 += informes.get(j).getDato();
                    if (j < informes.size() - 1)
                        dato1= dato1/informes.size();

                } else if (informes.get(j).getTipo().equals("2"))
                {
                    cantidades[1]++;
                    caudalProm+=informes.get(i).getDato();
                    total1+=informes.get(i).getDato();
                    dato2 += informes.get(j).getDato();
                    if (j < informes.size() - 1)
                        dato2= dato2/informes.size();

                } else if (informes.get(j).getTipo().equals("1"))
                {
                    cantidades[2]++;
                    total2+=informes.get(i).getDato();
                    consumoProm+=informes.get(i).getDato();
                    dato3 += informes.get(j).getDato();
                    if (j < informes.size() - 1)
                        dato3= dato3/informes.size();

                }
                if(informes.get(j).getEmergencia())
                {
                    numEmergencias++;
                }
            }
            if (i < informes.size() - 1){
                datosTemp += dato1 + ",";
                datosCaudal += dato2 + ",";
                datosConsumo += dato3 + ",";
            }
            else{
                datosTemp += dato1;
                datosCaudal += dato2;
                datosConsumo += dato3;
            }
            if(!informes.isEmpty())
            {
                tempProm=tempProm/cantidades[0];
                caudalProm=caudalProm/cantidades[1];
                consumoProm=consumoProm/cantidades[2];
            }
            if(sensores.get(i).getTipo().equals(TipoSensor.TemperaturaBomba)){
                tipo1++;
            }
            else if(sensores.get(i).getTipo().equals(TipoSensor.BarrilesCrudo)){
                tipo2++;
            }
            else if(sensores.get(i).getTipo().equals(TipoSensor.ConsumoEnergetico)){
                tipo2++;
            }

        }
           tempProm=tempProm/tipo1;
           caudalProm=caudalProm/tipo2;
           consumoProm=consumoProm/tipo3;
        }


        return ok(views.html.campo.render(campo, datosTemp, datosCaudal, datosConsumo, pozos, numEmergencias, tempProm+"", caudalProm+"", consumoProm+"", total1, total2, produccion, clausurados, abierto, parado));

    }

}
