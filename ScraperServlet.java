import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/ScrapeServlet")
public class ScrapeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data
        String url = request.getParameter("url");
        String[] options = request.getParameterValues("options");

        // Perform scraping (assume this method is implemented)
        List<Map<String, String>> scrapedData = Scraper.scrapeData(url, options);

        // Store results in session for later download
        request.getSession().setAttribute("scrapedData", scrapedData);

        // Convert to JSON using Gson
        Gson gson = new Gson();
        String json = gson.toJson(scrapedData);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // For downloading CSV
        List<Map<String, String>> scrapedData = (List<Map<String, String>>) request.getSession().getAttribute("scrapedData");
        
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"scraped_data.csv\"");

        PrintWriter out = response.getWriter();

        if (scrapedData != null && !scrapedData.isEmpty()) {
            // Write headers
            Set<String> headers = scrapedData.get(0).keySet();
            out.println(String.join(",", headers));

            // Write rows
            for (Map<String, String> row : scrapedData) {
                List<String> rowValues = new ArrayList<>();
                for (String header : headers) {
                    rowValues.add(row.getOrDefault(header, ""));
                }
                out.println(String.join(",", rowValues));
            }
        } else {
            out.println("No data to download.");
        }
        out.flush();
        out.close();
    }
    
    
    HttpSession session = request.getSession();
    Integer visitCount = (Integer) session.getAttribute("visitCount");

    if (visitCount == null) visitCount = 0;
    visitCount++;
    session.setAttribute("visitCount", visitCount);

    // Display visit count message
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<p>You have visited this page " + visitCount + " times.</p>");
    // Optionally include form again or link to go back
    out.println("</body></html>");

}
