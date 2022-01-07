package si.fri.rso.uniborrow.blogs.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.kumuluz.ee.discovery.annotations.RegisterService;

@RegisterService(value = "uniborrow-blogs-service", environment = "dev", version = "1.0.0")
@ApplicationPath("/v1")
public class BlogsApplication extends Application {

}
