import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class startMain {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/dba?serverTimezone=UTC";
            String user = "root";
            String pwd = "automate_1467980";
            conn = DriverManager.getConnection(url,user,pwd);
            MainForm frame = new MainForm(conn);
            frame.setVisible(true);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
