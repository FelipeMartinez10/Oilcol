package controllers;

import akka.dispatch.MessageDispatcher;
import dispatchers.AkkaDispatcher;
import models.InformeEntity;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;
/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class InformeController extends Controller {
    public CompletionStage<Result> getInformes() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return InformeEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(informeEntities -> {return ok(toJson(informeEntities));}
                );
    }
}
