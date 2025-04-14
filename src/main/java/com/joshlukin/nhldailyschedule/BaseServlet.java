package com.joshlukin.nhldailyschedule;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@WebServlet(name = "BaseServlet", value = "/games")
public class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the date parameter, default to today if not provided
        String dateParam = request.getParameter("date");
        if (dateParam == null || dateParam.isEmpty()) {
            dateParam = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
        }

        // Set the date as a request attribute to be used in JSP
        request.setAttribute("selectedDate", dateParam);

        getServletContext().getRequestDispatcher("/games.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
