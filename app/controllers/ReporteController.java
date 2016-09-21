package controllers;

import akka.dispatch.MessageDispatcher;
import com.avaje.ebean.*;
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
    public CompletionStage<Result> getReporte(Long idP, String fechas)
    {

        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        UsuarioEntity usuario = UsuarioEntity.FINDER.byId(idP);
        String f1=fechas.split("_")[0];
        String f2=fechas.split("_")[1];
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date fecha1=new Date();
        Date fecha2=new Date();
        try
        {
            fecha1 = formato.parse(f1);
            fecha2 = formato.parse(f2);

        Long l1= fecha1.getTime();
        Long l2= fecha2.getTime();
            System.out.println(fecha1+"   "+fecha2);
            System.out.println(l1+"   "+l2);
            System.out.println(usuario.getTipo()+"  "+TipoUsuario.JefeDeCampo.toString() );
        if(usuario.getTipo().equals(TipoUsuario.JefeDeCampo.toString()))
        {
            System.out.println("Entro a Jefe De Campo");
            List<PozoEntity> pozos = usuario.getCampo().getPozos();
            List<Long> idsPozos = new ArrayList<Long>();
            for (PozoEntity pozo: pozos)
            {
                idsPozos.add(pozo.getId());
            }
            List<SensorEntity> sensores = SensorEntity.FINDER.where().in("pozo_id",idsPozos).findList();
            List<Long> idsSensores = new ArrayList<Long>();
            for (SensorEntity sensor: sensores)
            {
                idsSensores.add(sensor.getId());
            }
            List<InformeEntity> informes = InformeEntity.FINDER.where().between("fecha",l1,l2).in("sensor_id",idsSensores).findList();
            //List<InformeEntity> informes = InformeEntity.FINDER.where().between("fecha",fecha1,fecha2)


            return CompletableFuture.
                    supplyAsync(() -> { return informes; } ,jdbcDispatcher)
                    .thenApply(reportes -> {return ok(toJson(reportes));}
                    );
        }
        else
            {

                List<InformeEntity> informes = InformeEntity.FINDER.where().between("fecha",l1,l2)
                //List<InformeEntity> informes = InformeEntity.FINDER.where().between("fecha",fecha1,fecha2)
                        //.isNotNull("fecha")
                        //.eq("fecha",null)
//                .ilike("name", "%coco%")
//               .orderBy("dueDate asc")
                        //.findPagedList(1, 25)
                        .findList();

                return CompletableFuture.
                        supplyAsync(() -> { return informes; } ,jdbcDispatcher)
                        .thenApply(reportes -> {return ok(toJson(reportes));}
                        );
        }
        }
        catch(Exception e)
        {
            System.out.println("Se putio");
            e.printStackTrace();
        }
        return null;
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
