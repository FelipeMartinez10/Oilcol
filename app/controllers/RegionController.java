package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.RegionEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.libs.Json.toJson;

/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class RegionController extends Controller {
    public CompletionStage<Result> getRegiones() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return RegionEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(regiones -> {return ok(toJson(regiones));}
                );
    }

    public CompletionStage<Result> getRegion(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return RegionEntity.FINDER.byId(idP); } ,jdbcDispatcher)
                .thenApply(regiones -> {return ok(toJson(regiones));}
                );
    }
    public CompletionStage<Result> createRegion(){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        RegionEntity region = Json.fromJson( n , RegionEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    region.save();
                    return region;
                }
        ).thenApply(
                regiones -> {
                    return ok(Json.toJson(regiones));
                }
        );
    }
    public CompletionStage<Result> deleteRegion(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    RegionEntity region = RegionEntity.FINDER.byId(idP);
                    region.delete();
                    return region;
                }
        ).thenApply(
                regiones -> {
                    return ok(Json.toJson(regiones));
                }
        );
    }
    public CompletionStage<Result> updateRegion( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        RegionEntity region = Json.fromJson( n , RegionEntity.class ) ;
        RegionEntity antiguo = RegionEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(region.getId());
                    antiguo.setName(region.getName());
                    antiguo.setCampos(region.getCampos());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                regiones -> {
                    return ok(Json.toJson(regiones));
                }
        );
    }
}
