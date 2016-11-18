package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.EncriptadoEntity;
import models.InformeEntity;
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
public class InformeController extends Controller {
    private static SensorEntity ultimo;
    public CompletionStage<Result> getInformes() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        List<InformeEntity> informes = InformeEntity.FINDER.all();
        EncriptadoEntity encriptado =new EncriptadoEntity(informes.toString());
        return CompletableFuture.
                supplyAsync(() -> { return encriptado; } ,jdbcDispatcher)
                .thenApply(informeEntities -> {return ok(toJson(informeEntities));}
                );
    }

    public CompletionStage<Result> getInforme(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        InformeEntity informe = InformeEntity.FINDER.byId(idP);
        EncriptadoEntity encriptado =new EncriptadoEntity(informe.toString());
        return CompletableFuture.
                supplyAsync(() -> { return encriptado; } ,jdbcDispatcher)
                .thenApply(informes -> {return ok(toJson(informes));}
                );
    }

    public CompletionStage<Result> createInforme(){

        JsonNode n = request().body().asJson();
        EncriptadoEntity encriptado = Json.fromJson( n , EncriptadoEntity.class ) ;

        if(encriptado.validar())
        {
            String mensaje = encriptado.getMensajeDesencriptado();
            JsonNode json = Json.parse(mensaje);
            InformeEntity informe = Json.fromJson(json, InformeEntity.class);

            return CompletableFuture.supplyAsync(
                    () ->
                    {
                        informe.save();
                        return informe;
                    }
            ).thenApply(
                    informes ->
                    {
                        return ok(Json.toJson(informes));
                    }
            );
        }
        else
        {
            return CompletableFuture.supplyAsync(
                    () ->
                    {
                        return "Error con integridad";
                    }
            ).thenApply(
                    informes ->
                    {
                        return ok(Json.toJson(informes));
                    }
            );
        }
    }

    public CompletionStage<Result> createInformeDeSensor(Long idSensor){
        //MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        InformeEntity informe = Json.fromJson( n , InformeEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    //SensorEntity sensor = null;
                    if(ultimo==null||ultimo.getId()!=idSensor)
                    {

                        ultimo = SensorEntity.FINDER.byId(idSensor);
                        ultimo.addInforme(informe);
                    }
                        informe.setSensor(ultimo);
                        ultimo.update();
                        informe.save();
                        return informe;

                }
        ).thenApply(
                informes -> {
                    return ok(Json.toJson(informes));
                }
        );
    }

    public CompletionStage<Result> deleteInforme(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    InformeEntity informe = InformeEntity.FINDER.byId(idP);
                    informe.delete();
                    return informe;
                }
        ).thenApply(
                informes -> {
                    return ok(Json.toJson(informes));
                }
        );
    }
    public CompletionStage<Result> updateInforme( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        InformeEntity informe = Json.fromJson( n , InformeEntity.class ) ;
        InformeEntity antiguo = InformeEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(informe.getId());
                    antiguo.setFecha(informe.getFecha());
                    antiguo.setSensor(informe.getSensor());
                    antiguo.setTipo(informe.getTipo());
                    antiguo.setEmergencia(informe.getEmergencia());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                informes -> {
                    return ok(Json.toJson(informes));
                }
        );
    }
}
