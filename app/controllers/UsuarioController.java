package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.CampoEntity;
import models.UsuarioEntity;
import play.data.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


import static play.data.Form.form;
import static play.libs.Json.toJson;

/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class UsuarioController extends Controller {

    public CompletionStage<Result> getUsuarios() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return UsuarioEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(usuarioEntities -> {return ok(toJson(usuarioEntities));}
                );
    }

    public CompletionStage<Result> getUsuario(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return UsuarioEntity.FINDER.byId(idP); } ,jdbcDispatcher)
                .thenApply(usuarioEntities -> {return ok(toJson(usuarioEntities));}
                );

    }


    public CompletionStage<Result> createUsuario(){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        UsuarioEntity user = Json.fromJson( n , UsuarioEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    user.save();
                    return user;
                }
        ).thenApply(
                userEntity -> {
                    return ok(Json.toJson(userEntity));
                }
        );
    }
    public CompletionStage<Result> deleteUsuario(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    UsuarioEntity user = UsuarioEntity.FINDER.byId(idP);
                    user.delete();
                    return user;
                }
        ).thenApply(
                userEntities -> {
                    return ok(Json.toJson(userEntities));
                }
        );
    }
    public CompletionStage<Result> updateUsuario( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        UsuarioEntity user = Json.fromJson( n , UsuarioEntity.class ) ;
        UsuarioEntity antiguo = UsuarioEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(user.getId());
                    antiguo.setName(user.getName());
                    antiguo.setTipo(user.getTipo());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                userEntities -> {
                    return ok(Json.toJson(userEntities));
                }
        );
    }

    public CompletionStage<Result> asignarCampo( Long idCampo, Long idUser){
        UsuarioEntity antiguo = UsuarioEntity.FINDER.byId(idUser);
        CampoEntity campo = CampoEntity.FINDER.byId(idCampo);
    System.out.println("Entro a asignar");
        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setCampo(campo);
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                userEntities -> {
                    return ok(Json.toJson(userEntities));
                }
        );
    }
    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (!email.equals("a@a.com") && !email.equals("perra") ) {
                    return "Invalid user or password";
            }
            return null;
        }

    }
    public Result login()
    {
        return ok(views.html.login.render(form(Login.class)));
    }
    public Result logout()
    {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.UsuarioController.login()
        );
    }

    public Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(
                    routes.RegionController.regionesHtml()
            );
        }
    }

}
