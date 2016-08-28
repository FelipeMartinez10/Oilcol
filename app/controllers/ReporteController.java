package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.ReporteEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.libs.Json.toJson;

/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class ReporteController extends Controller {
    public CompletionStage<Result> getReportes() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return ReporteEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(reportes -> {return ok(toJson(reportes));}
                );
    }

    public CompletionStage<Result> getReporte(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return ReporteEntity.FINDER.byId(idP); } ,jdbcDispatcher)
                .thenApply(reportes -> {return ok(toJson(reportes));}
                );
    }
    public CompletionStage<Result> createReporte(){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        ReporteEntity reporte = Json.fromJson( n , ReporteEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    reporte.save();
                    return reporte;
                }
        ).thenApply(
                reportes -> {
                    return ok(Json.toJson(reportes));
                }
        );
    }
    public CompletionStage<Result> deleteReporte(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    ReporteEntity reporte = ReporteEntity.FINDER.byId(idP);
                    reporte.delete();
                    return reporte;
                }
        ).thenApply(
                reportes -> {
                    return ok(Json.toJson(reportes));
                }
        );
    }
    public CompletionStage<Result> updateReporte( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        ReporteEntity reporte = Json.fromJson( n , ReporteEntity.class ) ;
        ReporteEntity antiguo = ReporteEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(reporte.getId());
                    antiguo.setFecha(reporte.getFecha());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                reportes -> {
                    return ok(Json.toJson(reportes));
                }
        );
    }
}
