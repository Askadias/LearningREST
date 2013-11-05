package ru.forxy.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * HelloWorldServlet http servlet to learn J2EE basics
 */
public class HelloWorldServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String error = req.getParameter("error");
        if (error != null) {
            if ("NotFound".equals(error)) {
                //resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
                //getServletContext().getRequestDispatcher("/hello/error/error.jsp").forward(req, resp);
                //throw new IllegalStateException("Resource not found");
            }
        } else {
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String param = paramNames.nextElement();
                if ("".equals(req.getParameter(param))) {
                    getServletContext().removeAttribute(param);
                    req.getSession().removeAttribute(param);
                } else {
                    getServletContext().setAttribute(param, req.getParameter(param));
                    req.getSession().setAttribute(param, req.getParameter(param));
                }
            }
            String userName = req.getParameter("user");
            User user = new User(userName != null && !"".equals(userName) ? userName : "World");
            req.setAttribute("user", user);
            getServletContext().getRequestDispatcher("/hello/hello.jsp").forward(req, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        LOGGER.info("Servlet {}: initialized", getServletName());
        Enumeration<String> paramNames = config.getInitParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();
            LOGGER.info("{} = {}", param, config.getInitParameter(param));
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Servlet {}: service", getServletName());
        Enumeration<String> paramNames = req.getParameterNames();
        LOGGER.info("Parameters:", getServletName());
        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();
            LOGGER.info("    {} = {}", param, req.getParameter(param));
        }
        Enumeration<String> headerNames = req.getHeaderNames();
        LOGGER.info("Headers:", getServletName());
        while (headerNames.hasMoreElements()) {
            String param = headerNames.nextElement();
            LOGGER.info("    {} = {}", param, req.getHeader(param));
        }
        int startIdx = req.getRequestURL().lastIndexOf("/") + 1;
        int endIdx = req.getRequestURL().lastIndexOf(".do");
        String action = req.getRequestURL().substring(startIdx, endIdx);
        if (action != null && !"".equals(action)) {
            if ("logout".equals(action)) {
                logout(req, resp);
            } else if ("login".equals(action)) {
                login(req, resp);
            }
        } else {
            super.service(req, resp);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = req.getParameter("user");
        User user = (User) req.getSession(true).getAttribute("user");
        if ((user == null || user.getName() == null || "".equals(user.getName())) && userName != null) {
            user = new User(userName);
        }
        if (user == null) {
            getServletContext().getRequestDispatcher("/hello/login.jsp").forward(req, resp);
        } else {
            req.getSession(true).setAttribute("user", user);
            getServletContext().getRequestDispatcher("/hello/hello.jsp").forward(req, resp);
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        getServletContext().getRequestDispatcher("/hello/login.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("Servlet {}: destroyed", getServletName());
    }
}
