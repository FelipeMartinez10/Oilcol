package dispatchers;

import akka.dispatch.MessageDispatcher;
import play.libs.Akka;

/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class AkkaDispatcher {
    public static MessageDispatcher jdbcDispatcher =  Akka.system().dispatchers().lookup("contexts.jdbc-dispatcher");

}
