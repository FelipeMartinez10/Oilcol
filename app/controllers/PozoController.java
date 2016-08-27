package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.PozoEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
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
                CampoEntity -> {
                    return ok(Json.toJson(CampoEntity));
                }
        );
    }
}
