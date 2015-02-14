package ua.sergiishapoval.carrental.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Сергей on 28.12.2014.
 */
public class EncodingFilter implements Filter {
    private String encoding;
    
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, 
                         FilterChain filterChain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        filterChain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
    }
    
    @Override
    public void destroy() {
    }

}