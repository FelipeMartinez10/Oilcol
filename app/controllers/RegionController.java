package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;


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
    @Security.Authenticated(Secured.class)
    public Result regionesHtml()
    {

        List<RegionEntity> regiones = RegionEntity.FINDER.all();
        ArrayList<Integer> numPozos = new ArrayList();
        ArrayList<Double> consumo = new ArrayList();
        ArrayList<Double> temperaturaPromedio = new ArrayList();
        ArrayList<Double> caudal = new ArrayList();

        List<PozoEntity> listaPozo = new ArrayList<>();
        List<CampoEntity> listaCampos = new ArrayList<>();

        for(int i =0; i<regiones.size(); i++)
        {
            RegionEntity region = regiones.get(i);
            List<CampoEntity> campos =region.getCampos();
            int cPozos= 0;
            double cConsumo=0.0;
            double cTemperaturaProm=0.0;
            double cCaudal=0.0;
            int cantidadT=0;
            for(CampoEntity campo : campos)
            {
                listaCampos.add(campo);
                List<PozoEntity> pozos =campo.getPozos();
               cPozos+=pozos.size();
                for(PozoEntity pozo: pozos)
                {
                    listaPozo.add(pozo);
                    List<SensorEntity> sensores = pozo.getSensores();
                    for(SensorEntity sensor: sensores)
                    {
                        List<InformeEntity> informes = sensor.getInformes();
                        for(InformeEntity informe: informes)
                        {
                            if(informe.getTipo().equals("0"))
                            {
                                cTemperaturaProm+=informe.getDato();
                                cantidadT++;
                            }
                            else if(informe.getTipo().equals("2"))
                            {
                                cCaudal+=informe.getDato();
                            }
                            else if(informe.getTipo().equals("1"))
                            {
                                cConsumo+=informe.getDato();
                            }
                        }
                    }
                }
            }
            consumo.add(cConsumo);
            temperaturaPromedio.add((double)(((int)((cTemperaturaProm/cantidadT)*1000))/1000));
            caudal.add(cCaudal);
            numPozos.add(cPozos);

        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fR1=formato.format(new Date(System.currentTimeMillis()-2592000000L));
        String fR2=formato.format(new Date(System.currentTimeMillis()));

        return ok(views.html.regiones.render(regiones, numPozos, consumo, temperaturaPromedio,caudal, listaPozo, listaCampos, fR1, fR2));
    }
}
