package com.group.practic.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class StaticContentFilter implements Filter {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final List<String> fileExtensions =
            Arrays.asList(
                    "html",
                    "js",
                    "json",
                    "css",
                    "png",
                    "svg",
                    "eot",
                    "ttf",
                    "woff",
                    "appcache",
                    "jpg",
                    "jpeg",
                    "gif",
                    "ico"
            );

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String path = request.getServletPath();

        boolean isApi = path.startsWith("/api");
        boolean isApiDoc = isApiDoc(path);

        boolean isResourceFile = !isApiDoc
                && !isApi
                && fileExtensions.stream().anyMatch(path::contains);

        if (isApi || isApiDoc) {
            chain.doFilter(request, response);
        } else if (isResourceFile) {
            resourceToResponse("static" + path, response);
        } else {
            resourceToResponse("static/index.html", response);
        }
    }

    private void resourceToResponse(
            String resourcePath,
            HttpServletResponse response
    ) throws IOException {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);

        if (inputStream == null) {
            response.sendError(404, "File not found");
            return;
        }

        if (resourcePath.endsWith(".js")) {
            response.setContentType("application/javascript");
        }

        inputStream.transferTo(response.getOutputStream());
    }

    private boolean isApiDoc(String path) {
        return pathMatcher.match("/**/swagger-ui/**", path)
                || pathMatcher.match("/swagger-ui/**", path)
                || pathMatcher.match("/**/api-docs/**", path);
    }
}
