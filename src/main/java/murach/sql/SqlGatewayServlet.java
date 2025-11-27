package murach.sql;

import java.io.*;
import jakarta.servlet.*; // Đã sửa từ javax
import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.WebServlet; // Giữ lại nếu cần
import java.sql.*;
import murach.data.ConnectionPool; 
import murach.data.SQLUtil; 

public class SqlGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";
        
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.getConnection(); // Lấy kết nối từ Pool
            statement = connection.createStatement();

            String sqlStatementTrimmed = sqlStatement.trim();
            if (sqlStatementTrimmed.length() >= 6) {
                String sqlType = sqlStatementTrimmed.substring(0, 6);

                if (sqlType.equalsIgnoreCase("select")) {
                    // Chạy lệnh SELECT
                    resultSet = statement.executeQuery(sqlStatement);
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                } else {
                    // Chạy lệnh INSERT/UPDATE/DELETE/DDL
                    int i = statement.executeUpdate(sqlStatement);
                    if (i == 0) {
                        sqlResult = "<p>The statement executed successfully.</p>";
                    } else {
                        sqlResult = "<p>The statement executed successfully.<br>"
                                + i + " row(s) affected.</p>";
                    }
                }
            }
        } catch (SQLException e) {
            sqlResult = "Error executing the SQL statement: <br>" + e.getMessage();
        } finally {
            // Đóng tài nguyên và trả kết nối về Pool
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) pool.freeConnection(connection);
            } catch (SQLException e) {
                this.log("Error closing JDBC objects: " + e.getMessage());
            }
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}