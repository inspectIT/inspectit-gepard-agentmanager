/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.application.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/**
 * Configuration class for handling static resources in a Spring MVC application. This class
 * implements the {@link WebMvcConfigurer} interface to customize the default resource handling
 * behavior.
 *
 * <p>Our approach is to redirect all requests to unknown sources to the index.html of our frontend.
 */
@Configuration
public class ResourceHandlerConfiguration implements WebMvcConfigurer {

  /**
   * Adds resource handlers for serving static resources. This method is overridden from the {@link
   * WebMvcConfigurer} interface.
   *
   * @param registry the {@link ResourceHandlerRegistry} to add resource handlers to
   */
  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    this.serveDirectory(registry, "/", "classpath:/static/");
  }

  /**
   * Serves static resources from a specified directory.
   *
   * @param registry the {@link ResourceHandlerRegistry} to add resource handlers to
   * @param endpoint the URL pattern to match for serving resources
   * @param location the location of the resources to serve
   */
  private void serveDirectory(
      ResourceHandlerRegistry registry, @NonNull String endpoint, @NonNull String location) {

    // The endpointPatterns array contains the URL patterns to match for serving resources.
    // In case of "/" the array would consist of "/" and "/**".
    String[] endpointPatterns =
        endpoint.endsWith("/")
            ? new String[] {endpoint.substring(0, endpoint.length() - 1), endpoint, endpoint + "**"}
            : new String[] {endpoint, endpoint + "/", endpoint + "/**"};
    registry
        .addResourceHandler(endpointPatterns)
        // we need to add a trailing slash to the location to make sure that the
        // PathResourceResolver can find the index.html file
        .addResourceLocations(location.endsWith("/") ? location : location + "/")
        .resourceChain(false)
        .addResolver(
            new PathResourceResolver() {
              @Override
              public Resource resolveResource(
                  HttpServletRequest request,
                  @NonNull String requestPath,
                  @NonNull List<? extends Resource> locations,
                  @NonNull ResourceResolverChain chain) {
                Resource resource = super.resolveResource(request, requestPath, locations, chain);
                if (Objects.nonNull(resource)) {
                  return resource;
                }
                return super.resolveResource(request, "/index.html", locations, chain);
              }
            });
  }
}
