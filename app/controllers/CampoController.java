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

}
