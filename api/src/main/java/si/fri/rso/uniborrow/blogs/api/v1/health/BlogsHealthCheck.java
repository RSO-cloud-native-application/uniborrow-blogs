package si.fri.rso.uniborrow.blogs.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import si.fri.rso.uniborrow.blogs.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class BlogsHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {
        if (restProperties.getBroken()) {
            return HealthCheckResponse.down(BlogsHealthCheck.class.getSimpleName());
        }
        else {
            return HealthCheckResponse.up(BlogsHealthCheck.class.getSimpleName());
        }
    }
}