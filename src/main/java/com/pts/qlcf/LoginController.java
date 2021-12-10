/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.pts.qlcf;

import com.pts.conf.JDBCUtils;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

/**
 * FXML Controller class
 *
 * @author 84344
 */
public class LoginController implements Initializable {
    private static Connection conn ;
    PreparedStatement pst;
    ResultSet rs;
    @FXML private TextField username;    
    @FXML private TextField password;
    
    Alert alert = new Alert(Alert.AlertType.WARNING);

    public String checkUser(TextField usernameT,TextField passwordT) throws SQLException{
        String username = usernameT.getText();        
        String password = passwordT.getText();
        if(username.equals("")||password.equals("")){
            return "Tên đăng nhập hoặc mật khẩu trống";
        }
        else{
            conn = JDBCUtils.getConn();
            pst = conn.prepareStatement("select * from user where username = ?");
            pst.setString(1, username);
            rs = pst.executeQuery();
            if(rs.next()){
                String password_data = rs.getString("password");
                if(conn!=null)conn.close();
                boolean check_pass = BCrypt.checkpw(password, password_data);
                if(check_pass==true){
                    return "true";
                }
                else{
                    return "Tên đăng nhập hoặc mật khẩu không chính xác";
                }
            }
            else{ 
                return "Tên đăng nhập hoặc mật khẩu không chính xác";
            }  
        }
    }
    
    @FXML
    public void Login(ActionEvent event)throws IOException, SQLException{
        String xn = checkUser(username, password);
        if("true".equals(xn)){
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setHeaderText("Thông báo");
            alertInfo.setContentText("Đăng nhập thành công");
            App.setRoot("register");
            alertInfo.showAndWait();
        }
        else{
            alert.setContentText(xn);
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void switchRegister() throws IOException {
        App.setRoot("register");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
