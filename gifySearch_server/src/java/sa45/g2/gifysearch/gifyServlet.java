
package sa45.g2.gifysearch;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Mo Mo
 */

@WebServlet(urlPatterns ="/savegify")
public class gifyServlet extends HttpServlet {

    @Resource(lookup = "jdbc/gifyrecord")
    private DataSource conPool;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        JsonArrayBuilder gifyBuilder = Json.createArrayBuilder();
        
        try(Connection con = conPool.getConnection()){
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM gify");            
            
            while(rs.next())
            {
                JsonObject gify = Json.createObjectBuilder()
                        .add("url", rs.getString("url")).build();
                
                gifyBuilder.add(gify);                
            }
            rs.close();          
        } 
        catch (SQLException ex){
            log(ex.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        
        try(PrintWriter pw = resp.getWriter()) {
            resp.setHeader("Access-Control-Allow-Origin", "*");
            resp.setHeader("Access-Control-Allow-Origin-Methods", "GET, POST, OPTIONS");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(MediaType.APPLICATION_JSON);
            pw.println(gifyBuilder.build().toString());
        }    
    }
    
    
    
    
}
