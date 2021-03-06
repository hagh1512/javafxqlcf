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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.mindrot.jbcrypt.BCrypt;


/**
 * FXML Controller class
 *
 * @author 84344
 */
public class RegisterController implements Initializable {
    @FXML
    private Button btn_admin;

    @FXML
    private Button btn_customer;
    @FXML
    private RadioButton radio_nam;

    @FXML
    private RadioButton radio_nu;
    @FXML
    private TextField email_cus;
    @FXML
    private RadioButton radio_tv_nam;

    @FXML
    private RadioButton radio_tv_nu;
    @FXML
    private TextField email_mng;
    @FXML
    private ToggleGroup sex;
    @FXML
    private ComboBox cb_role;
    @FXML
    private GridPane gp_admin;
    @FXML
    private GridPane gp_customer;
    @FXML
    private PasswordField pass1_mng;
    @FXML
    private PasswordField pass2_cus;
    @FXML
    private PasswordField pass2_mng;
    @FXML
    private PasswordField pass_cus;
    @FXML
    private PasswordField password_admin;
    @FXML
    private TextField username_admin;
    @FXML
    private TextField username_cus;
    @FXML
    private TextField username_mng;
    @FXML
    private ImageView imgV_custumer;
    @FXML
    private HBox hBox_customer;
    
    
    private static Connection conn ;
    PreparedStatement pst;
    ResultSet rs;
    Alert alert = new Alert(Alert.AlertType.WARNING);

    @FXML
    public void Register_custom(){
        btn_customer.setStyle("-fx-background-color:#47ff50;-fx-text-fill:#353b48;");
//        btn_customer.setStyle("");
        btn_admin.setStyle("-fx-background-color:black;-fx-text-fill:white;");
//        btn_admin.setStyle("");
        hBox_customer.setVisible(true);
        gp_admin.setVisible(false);
    }
    @FXML
    public void Register_admin(){
        btn_admin.setStyle("-fx-background-color:#47ff50;-fx-text-fill:#353b48;");
//        btn_admin.setStyle("");
        btn_customer.setStyle("-fx-background-color:black;-fx-text-fill:white;");
//        btn_customer.setStyle("");
        hBox_customer.setVisible(false);
        gp_admin.setVisible(true);
    }
    boolean flag_sex = true ;//gt=nam  

    
    static boolean emailIsValid(String email) {
        String regex = "^[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
        return email.matches(regex);
    }
    
    public String checkNullInfo(TextField usernameT, PasswordField passwordT,
            PasswordField Password_cf,TextField emailT) throws SQLException
    {
        String username = usernameT.getText();
        String password = passwordT.getText();
        String password_cf = Password_cf.getText();
        String email = emailT.getText();
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(username);
        boolean b = m.find(); 
        
        Pattern num = Pattern.compile("[^0-9]");
        Matcher mnum = num.matcher(password);
        boolean bnum = mnum.find();
        
        Pattern str = Pattern.compile("[^A-Za-z]");
        Matcher mstr = str.matcher(password);
        boolean bstr = mstr.find();
        
        if(username.isEmpty()||password.isEmpty()||password_cf.isEmpty()||email.isEmpty()){
            return "??i???n ????? th??ng tin c??c tr?????ng.";
        }
//        else if(emailIsValid(email)==false){
//            return "Email kh??ng ????ng ?????nh d???ng.";
//        }
        else if(username.length()<4||username.length()>24){
            return "T??n t??i kho???n t??? 4 ?????n 24 k?? t???.";
        }
        else if(b){
            return "T??n ch??? bao g???m: ch??? hoa, ch??? th?????ng, s???.";
        }
        else if(Character.isDigit(username.charAt(0))){
            return "T??n ph???i b???t ?????u b???ng k?? t??? ch??? [a-z][A-Z]";
        }
        else if(password.length()<8||password.length()>24){
            return "M???t kh???u t??? 8 ?????n 24 k?? t???.";
        }
        else if(bnum==false||bstr==false){
            return "M???t kh???u ph???i g???m ??t nh???t c??? ch??? v?? s???.";
        }
        else if(password.equals(password_cf)==false){
            return "M???t kh???u kh??ng kh???p.";
        }
        
        else{
            conn = JDBCUtils.getConn();
            pst = conn.prepareStatement("select * from user where username = ?");
            pst.setString(1, username);
            rs = pst.executeQuery();
            if(rs.next()){
                if(conn!=null) conn.close();
                return "T??n t??i kho???n ???? t???n t???i.";
            }
            if(conn!=null) conn.close();
            return "true";
        }
    }
    
    
    @FXML
    public void Register_Customer(ActionEvent event) throws SQLException{
        String xn =checkNullInfo(username_cus, pass_cus, pass2_cus,email_cus);
        if("true".equals(xn)){
            conn = JDBCUtils.getConn();
            UUID uuid = UUID.randomUUID();
            String userID = uuid.toString();
            String pass = BCrypt.hashpw(pass_cus.getText(), BCrypt.gensalt(12));
            String sql ="INSERT INTO `user` (`id`,`username`, `password`, `role`) VALUES (?,?,?,?);";
            pst = conn.prepareStatement(sql);
            pst.setString(1, userID);            
            pst.setString(2, username_cus.getText());                
            pst.setString(3, pass);
            pst.setString(4, "customer");
            pst.execute();
            alert.setContentText("Th??m th??nh c??ng!");
            alert.showAndWait();
            if(conn!=null) conn.close();
        }
        else{
            alert.setContentText(xn);
            alert.showAndWait();
        }

    }
    
    
    @FXML
    public void Register_Manager(ActionEvent event) throws SQLException{
        String xn = checkNullInfo(username_mng, pass1_mng, pass2_mng,email_mng);
        boolean flag = false; //kiem tra tai khoan quan tri
        if(username_admin.getText().equals("") 
                || password_admin.getText().equals("")){
            alert.setContentText("Nh???p th??ng tin t??i kho???n qu???n tr??? (admin)");
            alert.showAndWait();
        }
        else{
            conn = JDBCUtils.getConn();
            String pass = password_admin.getText();
            pst = conn.prepareStatement("select * from user where username = ?");
            pst.setString(1, username_admin.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                String password_data = rs.getString("password");
                boolean check_pass = BCrypt.checkpw(pass, password_data);
                if(check_pass==true){
                    flag =true;
                }
                else{
                    flag=false;
                    alert.setContentText("Th??ng tin t??i kho???n qu???n tr??? (admin) ch??a ch??nh x??c");
                    alert.showAndWait();
                }
            }
            else{ 
                alert.setContentText("Th??ng tin t??i kho???n qu???n tr??? (admin) ch??a ch??nh x??c");
                alert.showAndWait();
                flag = false;
            }
            conn.close();
        }
        if(flag==true){
            if(cb_role.getValue()==null){
                alert.setContentText("Ch???n vai tr??");
                alert.showAndWait();
            }
            else{
                if("true".equals(xn)){
                    conn = JDBCUtils.getConn();
                    UUID uuid = UUID.randomUUID();
                    String userID = uuid.toString();
                    String pass = BCrypt.hashpw(pass1_mng.getText(),
                            BCrypt.gensalt(12));
                    
                    PreparedStatement pst1;
                    String query ="INSERT INTO `user` (`id`,`username`, `password`, `role`) VALUES (?,?,?,?);";
                    pst1 = conn.prepareStatement(query);
                    pst1.setString(1, userID);
                    pst1.setString(2, username_mng.getText());                
                    pst1.setString(3, pass);
                    pst1.setString(4, cb_role.getValue().toString());
                    pst1.execute();
                    alert.setContentText("????ng k?? th??nh c??ng");
                    alert.showAndWait();
                    if(conn!=null) conn.close();

                }
                else{
                    alert.setContentText(xn);
                    alert.showAndWait();
                }
            }
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cb_role.getItems().addAll("admin","manager");
    }
    
    @FXML
    private void switchLogin() throws IOException {
        App.setRoot("login");
    }    
    
}
