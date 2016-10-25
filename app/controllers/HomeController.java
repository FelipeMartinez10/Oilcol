package controllers;

import com.google.inject.Inject;
import models.InformeEntity;
import models.PozoEntity;
import models.UsuarioEntity;
import play.mvc.*;

import views.html.*;

import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {


    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {

        //List<UsuarioEntity> users = UsuarioEntity.FINDER.all();
    UsuarioEntity user = UsuarioEntity.FINDER.byId(1L);
        //PozoEntity pozo = PozoEntity.FINDER.byId(1L);
        //return ok(views.html.pozo.render(pozo));
        return ok(index.render(user));
    }



}
