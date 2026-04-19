package com.cookieservlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String userName = request.getParameter("userName");

        String existingUser = null;
        int visitCount = 0;

        // Read cookies
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    existingUser = c.getValue();
                }
                if (c.getName().equals("count")) {
                    visitCount = Integer.parseInt(c.getValue());
                }
            }
        }

        // First time user (create cookies)
        if (userName != null && !userName.isEmpty()) {
            existingUser = userName;
            visitCount = 0;

            Cookie userCookie = new Cookie("user", userName);
            userCookie.setMaxAge(30); // expires in 30 seconds

            Cookie countCookie = new Cookie("count", "0");
            countCookie.setMaxAge(30);

            response.addCookie(userCookie);
            response.addCookie(countCookie);
        }

        out.println("<html><body>");

        if (existingUser != null) {

            visitCount++;

            // Update count cookie
            Cookie countCookie = new Cookie("count", String.valueOf(visitCount));
            countCookie.setMaxAge(30);
            response.addCookie(countCookie);

            out.println("<h2 style='color:blue'>Welcome back, " + existingUser + "!</h2>");
            out.println("<h3>You have visited this page " + visitCount + " times</h3>");

            out.println("<p style='color:red;'>Cookie expires in 30 seconds!</p>");

            // Display all cookies
            out.println("<h3>Cookies List:</h3>");
            if (cookies != null) {
                out.println("<table border='1'>");
                out.println("<tr><th>Name</th><th>Value</th></tr>");

                for (Cookie c : cookies) {
                    out.println("<tr>");
                    out.println("<td>" + c.getName() + "</td>");
                    out.println("<td>" + c.getValue() + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
            }

            // Logout button (expiry demo)
            out.println("<br><form action='CookieServlet' method='post'>");
            out.println("<input type='submit' value='Delete Cookies'>");
            out.println("</form>");

        } else {
            out.println("<h2 style='color:red;'>New User</h2>");
            out.println("<form action='CookieServlet' method='get'>");
            out.println("Name: <input type='text' name='userName' required>");
            out.println("<input type='submit' value='Submit'>");
            out.println("</form>");
        }

        out.println("</body></html>");
    }

    // Delete cookies (expiry demonstration)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cookie userCookie = new Cookie("user", "");
        userCookie.setMaxAge(0);

        Cookie countCookie = new Cookie("count", "");
        countCookie.setMaxAge(0);

        response.addCookie(userCookie);
        response.addCookie(countCookie);

        response.sendRedirect("CookieServlet");
    }
}