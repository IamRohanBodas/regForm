import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static java.sql.DriverManager.drivers;
import static java.sql.DriverManager.getConnection;

public class regForm extends JDialog {
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPass;
    private JPasswordField txtConPass;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel regPanel;

    public regForm(JFrame parent){
        super(parent);
        setTitle("Create a New Account ");
        setContentPane(regPanel);
        setSize(450,475);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name= txtName.getText();
        String email=txtEmail.getText();
        String password= String.valueOf(txtPass.getPassword());
        String confirmPassword= String.valueOf(txtConPass.getPassword());

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please Enter All Fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
                    return;
        }
        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password Does Not match,Please",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
         user = addUserToDatabase(name, email, password);
        if(user!=null){
            dispose();
        }
    }
    public User user;
    private User addUserToDatabase(String name, String email, String password){
        User user=null;
        final String DB_URL="jdbc:mysql://localhost:3306/regform";
        final String USERNAME="root";
        final String PASSWORD="bestar1234";
        try{
            Connection con= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement st= con.createStatement();
            String sql;
            sql = "INSERT INTO user(name,email,password)"+ "VALUES(?,?,?)";
            PreparedStatement preparedStatement= con.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int addedRows=preparedStatement.executeUpdate();
            if(addedRows>0){
                user=new User();
                user.name=name;
                user.email=email;
                user.password=password;
            }
            st.close();
            con.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        regForm myForm=new regForm(null);
        User user= myForm.user;
        if(user!=null){
            System.out.println("The Registration is successful for : "+ user.name);
        }
        else {
            System.out.println("The Registration is cancelled");
        }

    }
}
