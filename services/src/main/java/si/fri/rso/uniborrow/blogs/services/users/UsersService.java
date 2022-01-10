package si.fri.rso.uniborrow.blogs.services.users;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import si.fri.rso.uniborrow.blogs.services.beans.BlogBean;

import java.time.temporal.ChronoUnit;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.InternalServerErrorException;

import java.util.logging.Logger;


@ApplicationScoped
public class UsersService {
    private static final Logger LOG = Logger.getLogger(BlogBean.class
            .getSimpleName());

    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "checkUserExistsFallback")
    @Retry(maxRetries = 3)
    @DiscoverService(value = "uniborrow-users-service", environment = "dev", version = "1.0.0")
    public boolean checkUserExists(int userId) {
        LOG.info("Check if user exists.");
        try {
            Client restClient = ClientBuilder.newClient();
            Response r = restClient
                    .target("/v1/users/" + userId)
                    .request(MediaType.APPLICATION_JSON).buildGet().invoke();

            System.out.println(r.getStatus());
            if (r.getStatus() > 400) {
                throw new WebApplicationException();
            }
            return true;
        }
        catch (Exception e) {
            LOG.severe("Users unavailable");
            throw new InternalServerErrorException(e);
        }
    }

    public boolean checkUserExistsFallback(int userId) {
        LOG.info("checkUserExistsFallback");
        return false;
    }


}
