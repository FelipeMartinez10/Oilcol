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
import java.util.Date;
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
                    //region.addCampo(campo);
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
    public CompletionStage<Result> createCampoEnRegionJefe(Long idRegion, Long idUsuario){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode nCamp = request().body().asJson();
        CampoEntity campo = Json.fromJson( nCamp , CampoEntity.class );

        return CompletableFuture.supplyAsync(
                ()->{
                    RegionEntity region = RegionEntity.FINDER.byId(idRegion);
                    UsuarioEntity user = UsuarioEntity.FINDER.byId(idUsuario);
                    campo.setRegion(region);
                    region.update();
                    campo.save();
                    user.setCampo(campo);
                    user.update();
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

    @Security.Authenticated(Secured.class)
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
        String pozoos="";

        List<PozoEntity> pozos = PozoEntity.FINDER.where().in("campo_id", idCampo).findList();
        pozoos += pozos.size();
        for (PozoEntity pozo: pozos)
        {
            //if(pozo.getEstado().equals(EstadoPozo.Produccion))produccion++;
            //if(pozo.getEstado().equals(EstadoPozo.Clausurado))clausurados++;
            //if(pozo.getEstado().equals(EstadoPozo.Abierto))abierto++;
            //if(pozo.getEstado().equals(EstadoPozo.Parado))parado++;

        List<SensorEntity> sensores = pozo.getSensores();
        for (SensorEntity sensor: sensores)
        {
            List<InformeEntity> informes = sensor.getInformes();
            for (InformeEntity informe : informes)
            {
                if (informe.getTipo().equals("0"))
                {
                    cantidades[0]++;
                    tempProm+=informe.getDato();
                    datosTemp+=informe.getDato()+",";
                    dato1 += informe.getDato();

                } else if (informe.getTipo().equals("2"))
                {
                    cantidades[2]++;
                    consumoProm+=informe.getDato();
                    datosConsumo+=informe.getDato()+",";
                    dato2 += informe.getDato();

                } else if (informe.getTipo().equals("1"))
                {
                    cantidades[1]++;
                    caudalProm+=informe.getDato();
                    datosCaudal+=informe.getDato()+",";
                    dato1 += informe.getDato();


                }
                if(informe.getEmergencia())
                {
                    numEmergencias++;
                }
            }

        }
            if(tempProm!=0)
            {
                tempProm=tempProm/cantidades[0];
                caudalProm=caudalProm/cantidades[1];
                consumoProm=consumoProm/cantidades[2];
            }

        }
        String sConsumoP = (consumoProm+"     ").substring(0,(consumoProm+"").indexOf(".")+3);
        String sCaudalP = (caudalProm+"    ").substring(0,(caudalProm+"").indexOf(".")+3);
        String sTempProm =(tempProm+"      ").substring(0,(tempProm+"").indexOf(".")+3);

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fR1=formato.format(new Date(System.currentTimeMillis()-2592000000L));
        String fR2=formato.format(new Date(System.currentTimeMillis()));

       // Boolean bools = false;
       // if(tienePermiso==1) {bools=true;}

        return ok(views.html.campo.render(campo, pozoos, datosTemp, datosCaudal, datosConsumo, pozos, numEmergencias+"", sTempProm, sCaudalP, sConsumoP, dato1+"", dato2+"",fR1,fR2));

    }

}
