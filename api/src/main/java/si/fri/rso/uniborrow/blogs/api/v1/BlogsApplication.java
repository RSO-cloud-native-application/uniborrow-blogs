package si.fri.rso.uniborrow.blogs.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Uniborrow Blogs API",
                version = "v1",
                contact = @Contact(email = "mp6079@student.uni-lj.si"),
                license = @License(name = "dev"),
                description = "API for managing blogs for Uniborrow application."
        ),
        servers = @Server(url = "http://35.223.79.242/uniborrow-blogs/")
)
@RegisterService(value = "uniborrow-blogs-service", environment = "dev", version = "1.0.0")
@ApplicationPath("/v1")
public class BlogsApplication extends Application {

}
