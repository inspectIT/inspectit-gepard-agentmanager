/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.application.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

@Configuration
public class ResourceHandlerConfiguration implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    this.serveDirectory(registry, "/", "classpath:/static/");
  }

  private void serveDirectory(ResourceHandlerRegistry registry, String endpoint, String location) {
    String[] endpointPatterns =
        endpoint.endsWith("/")
            ? new String[] {endpoint.substring(0, endpoint.length() - 1), endpoint, endpoint + "**"}
            : new String[] {endpoint, endpoint + "/", endpoint + "/**"};
    registry
        .addResourceHandler(endpointPatterns)
        .addResourceLocations(location.endsWith("/") ? location : location + "/")
        .resourceChain(false)
        .addResolver(
            new PathResourceResolver() {
              @Override
              public Resource resolveResource(
                  HttpServletRequest request,
                  String requestPath,
                  List<? extends Resource> locations,
                  ResourceResolverChain chain) {
                Resource resource = super.resolveResource(request, requestPath, locations, chain);
                if (Objects.nonNull(resource)) {
                  return resource;
                }
                return super.resolveResource(request, "/index.html", locations, chain);
              }
            });
  }
}
