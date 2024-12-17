/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package rentalcar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class finaladmin extends javax.swing.JFrame {

    /**
     * Creates new form finaladmin
     */
    public finaladmin() {
        initComponents();
         Connect();
          CarAutoID();
        Load_car();      
        Load_client();
        Load_reservations();
        updateReservationCount();
        updateCarCount();
        updateClientCount();
        displayAllIncome() ;
        Load_payments() ;
           jPanel6.removeAll();
      jPanel6.add(jPanel7);
       jPanel6.repaint();
        jPanel6.revalidate();
    }
Connection con;
    PreparedStatement pat;
    DefaultTableModel d;
    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/crental", "root", "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void CarAutoID() {
    try {
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT MAX(carNo) FROM cars");

        if (rs.next()) {
            String maxID = rs.getString(1); // Fetch the max carNo

            if (maxID == null || maxID.isEmpty()) {
                // Start with CAR001 if there are no records
                jLabel6.setText("CAR001");
            } else {
                try {
                    // Extract the numeric part after "CAR" and increment
                    int numberPart = Integer.parseInt(maxID.substring(3)) + 1; // Increment the numeric part
                    jLabel6.setText("CAR" + String.format("%03d", numberPart)); // Format with leading zeros
                } catch (NumberFormatException e) {
                    Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Failed to parse carNo: " + maxID, e);
                    // Fallback to a default ID format
                    jLabel6.setText("CAR001");
                }
            }
        } else {
            // Handle cases with no rows in the database
            jLabel6.setText("CAR001");
        }
    } catch (SQLException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database error: " + ex.getMessage(), ex);
        // Fallback in case of a database error
        jLabel6.setText("CAR001");
    }
}
public void Load_car() {
        int c;
        try {
            pat = con.prepareStatement("SELECT * FROM cars");
            ResultSet rs = pat.executeQuery();
            ResultSetMetaData rsd = rs.getMetaData();
            c = rsd.getColumnCount();
            d = (DefaultTableModel) jTable1.getModel();
            d.setRowCount(0);
            while (rs.next()) {
           Vector<String> v2 = new Vector<>();
                for (int i = 1; i <= c; i++) {
                   
                    v2.add(rs.getString("carNo"));
                    v2.add(rs.getString("CarBrand"));
                    v2.add(rs.getString("CarModel"));
                    v2.add(rs.getString("Amount"));
                }
                d.addRow(v2);
            }

        } catch (SQLException ex) {
            Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void Load_client() {
    try {
        // Use a parameterized query to prevent SQL injection
        pat = con.prepareStatement("SELECT * FROM clients");
      
        ResultSet rs = pat.executeQuery();

        // Get table model and clear existing rows
        d = (DefaultTableModel) jTable2.getModel();
        d.setRowCount(0);

        while (rs.next()) {
            // Create a new row with values for the current reservation
            Vector<String> v2 = new Vector<>();
            v2.add(rs.getString("UserID"));
            v2.add(rs.getString("Username"));
            v2.add(rs.getString("Firstname"));
            v2.add(rs.getString("Lastname"));
            v2.add(rs.getString("ContactNo"));
            v2.add(rs.getString("Password"));
             v2.add(rs.getString("Gender"));

            // Add the row to the table model
            d.addRow(v2);
        }
    } catch (SQLException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error loading reservations: " + ex.getMessage());
    }
}
public void Load_payments() {
    int c;
    try {
        // SQL query to join reservations and payments tables
        pat = con.prepareStatement(
            "SELECT r.RentID, r.UserID, r.Name, " +
            "p.PaymentMethod, p.PaymentAmount, p.PaymentDate " +
            "FROM reservations r " +
            "LEFT JOIN payments p ON r.UserID = p.UserID"
        );
        ResultSet rs = pat.executeQuery();
        ResultSetMetaData rsd = rs.getMetaData();
        c = rsd.getColumnCount();
        d = (DefaultTableModel) jTable4.getModel();
        d.setRowCount(0); // Clear the existing rows in the table

        while (rs.next()) {
            Vector<String> v2 = new Vector<>();
            v2.add(rs.getString("RentID") != null ? rs.getString("RentID") : "");
            v2.add(rs.getString("UserID") != null ? rs.getString("UserID") : "");
            v2.add(rs.getString("Name") != null ? rs.getString("Name") : "");
            v2.add(rs.getString("PaymentMethod") != null ? rs.getString("PaymentMethod") : "No Payment");
            v2.add(rs.getString("PaymentAmount") != null ? rs.getString("PaymentAmount") : "0.00");
            v2.add(rs.getString("PaymentDate") != null ? rs.getString("PaymentDate") : "N/A");

            d.addRow(v2); // Add the populated vector as a new row in the table
        }
    } catch (SQLException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public void Load_reservations() {
    int c;
    try {
        pat = con.prepareStatement("SELECT * FROM reservations");
        ResultSet rs = pat.executeQuery();
        ResultSetMetaData rsd = rs.getMetaData();
        c = rsd.getColumnCount();
        d = (DefaultTableModel) jTable3.getModel();
        d.setRowCount(0); // Clear the existing rows

        while (rs.next()) {
            Vector<String> v2 = new Vector<>();
            v2.add(rs.getString("RentID"));
            v2.add(rs.getString("UserID"));
            v2.add(rs.getString("Name"));
            v2.add(rs.getString("Address")); // Removed extra space
            v2.add(rs.getString("ContactNo"));
            v2.add(rs.getString("Carbrand"));
            v2.add(rs.getString("Carmodel"));
            v2.add(rs.getString("Rentdate"));
            v2.add(rs.getString("Returndate"));
            v2.add(rs.getString("Amount"));
            v2.add(rs.getString("Status"));
            v2.add(rs.getString("carNo"));

            d.addRow(v2); // Add the populated vector as a new row
        }
    } catch (SQLException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
    }
}
public void updateReservationCount() {
    try {
        
        // Step 1: Ensure 'con' is a valid and open connection
        if (con == null || con.isClosed()) {
            Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database connection is not valid!");
            jLabel13.setText("Database connection error!");
            return;
        }

        // Step 2: Execute SQL query
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM reservations");

        // Step 3: Process result
        if (rs.next()) {
            int count = rs.getInt(1); // Fetch the count of reservations
            jLabel13.setText(" " + count); // Update JLabel
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "Reservation count updated: " + count);
        } else {
            jLabel13.setText("Total Reservations: 0"); // Default text if no rows found
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "No reservations found.");
        }
    } catch (SQLException ex) {
        // Step 4: Log and display database error
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database error: " + ex.getMessage(), ex);
        jLabel13.setText("Error fetching data!");
    } catch (Exception ex) {
        // Step 5: Catch unexpected exceptions
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Unexpected error: " + ex.getMessage(), ex);
        jLabel13.setText("Unexpected error occurred!");
    }
}
public void displayAllIncome() {
   try {
        
        // Step 1: Ensure 'con' is a valid and open connection
        if (con == null || con.isClosed()) {
            Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database connection is not valid!");
            jLabel12.setText("Database connection error!");
            return;
        }

          // Step 2: Execute SQL query to calculate the total payment amount
        String query = "SELECT SUM(paymentAmount) AS totalAmount FROM payments";
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(query);

        // Step 3: Process result
        if (rs.next()) {
            int count = rs.getInt(1); // Fetch the count of reservations
            jLabel12.setText(" " + count); // Update JLabel
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "Reservation count updated: " + count);
        } else {
           jLabel12.setText("Total Reservations: 0"); // Default text if no rows found
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "No reservations found.");
        }
    } catch (SQLException ex) {
        // Step 4: Log and display database error
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database error: " + ex.getMessage(), ex);
        jLabel12.setText("Error fetching data!");
    } catch (Exception ex) {
        // Step 5: Catch unexpected exceptions
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Unexpected error: " + ex.getMessage(), ex);
        jLabel12.setText("Unexpected error occurred!");
    }
}
public void updateCarCount() {
    try {
        
        // Step 1: Ensure 'con' is a valid and open connection
        if (con == null || con.isClosed()) {
            Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database connection is not valid!");
            jLabel14.setText("Database connection error!");
            return;
        }

        // Step 2: Execute SQL query
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM cars");

        // Step 3: Process result
        if (rs.next()) {
            int count = rs.getInt(1); // Fetch the count of reservations
            jLabel14.setText(" " + count); // Update JLabel
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "Reservation count updated: " + count);
        } else {
            jLabel14.setText("Total Reservations: 0"); // Default text if no rows found
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "No reservations found.");
        }
    } catch (SQLException ex) {
        // Step 4: Log and display database error
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database error: " + ex.getMessage(), ex);
        jLabel14.setText("Error fetching data!");
    } catch (Exception ex) {
        // Step 5: Catch unexpected exceptions
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Unexpected error: " + ex.getMessage(), ex);
        jLabel14.setText("Unexpected error occurred!");
    }
}
public void updateClientCount() {
    try {
        
        // Step 1: Ensure 'con' is a valid and open connection
        if (con == null || con.isClosed()) {
            Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database connection is not valid!");
            jLabel15.setText("Database connection error!");
            return;
        }

        // Step 2: Execute SQL query
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM clients");

        // Step 3: Process result
        if (rs.next()) {
            int count = rs.getInt(1); // Fetch the count of reservations
            jLabel15.setText(" " + count); // Update JLabel
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "Reservation count updated: " + count);
        } else {
            jLabel15.setText("Total Reservations: 0"); // Default text if no rows found
            Logger.getLogger(finaladmin.class.getName()).log(Level.INFO, "No reservations found.");
        }
    } catch (SQLException ex) {
        // Step 4: Log and display database error
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Database error: " + ex.getMessage(), ex);
        jLabel15.setText("Error fetching data!");
    } catch (Exception ex) {
        // Step 5: Catch unexpected exceptions
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, "Unexpected error: " + ex.getMessage(), ex);
        jLabel15.setText("Unexpected error occurred!");
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        jPanel1 = new javax.swing.JPanel();
        tab2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tab3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tab4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tab1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        tab5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        carbrand = new javax.swing.JComboBox<>();
        carm = new javax.swing.JComboBox<>();
        txtamount = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        tab2.setBackground(new java.awt.Color(204, 204, 204));
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab2MouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Elephant", 1, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("ADD CAR");

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab2Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        tab3.setBackground(new java.awt.Color(204, 204, 204));
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab3MouseClicked(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Elephant", 1, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("VIEW CLIENTS");

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab3Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tab4.setBackground(new java.awt.Color(204, 204, 204));
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab4MouseClicked(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Elephant", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("VIEW RESERVATION");
        jLabel4.setOpaque(true);

        javax.swing.GroupLayout tab4Layout = new javax.swing.GroupLayout(tab4);
        tab4.setLayout(tab4Layout);
        tab4Layout.setHorizontalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(28, 28, 28))
        );
        tab4Layout.setVerticalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab1.setBackground(new java.awt.Color(204, 204, 204));
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(255, 204, 255));
        jLabel1.setFont(new java.awt.Font("Elephant", 1, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("HOME");

        javax.swing.GroupLayout tab1Layout = new javax.swing.GroupLayout(tab1);
        tab1.setLayout(tab1Layout);
        tab1Layout.setHorizontalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(79, 79, 79))
        );
        tab1Layout.setVerticalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(255, 204, 204));
        jPanel12.setOpaque(false);

        jLabel11.setFont(new java.awt.Font("Segoe Script", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("GOOD DAY ADMIN!!");

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 233, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 91, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        tab5.setBackground(new java.awt.Color(204, 204, 204));
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab5MouseClicked(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Elephant", 1, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("PAYMENT HISTORY");

        javax.swing.GroupLayout tab5Layout = new javax.swing.GroupLayout(tab5);
        tab5.setLayout(tab5Layout);
        tab5Layout.setHorizontalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel20)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        tab5Layout.setVerticalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab5Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addContainerGap())
        );

        jButton8.setText("logout");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(114, 114, 114)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tab4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tab3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tab2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(tab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(tab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(tab4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(29, 29, 29)
                .addComponent(tab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jButton8)
                .addGap(173, 173, 173))
        );

        jPanel6.setLayout(new java.awt.CardLayout());

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        jTable2.setBackground(new java.awt.Color(204, 204, 204));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "UserID", "Username", "Firstname", "Lastname", "ContactNo", "Password", "Gender"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton5.setBackground(new java.awt.Color(153, 153, 153));
        jButton5.setText("DELETE");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 952, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(32, 32, 32))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel6.add(jPanel2, "card4");

        jPanel3.setBackground(new java.awt.Color(255, 153, 153));
        jPanel3.setLayout(null);

        jTable3.setBackground(new java.awt.Color(153, 51, 255));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Rent ID", "UserID", "Name", "Address", "Contact No", "Car Brand", "Car Model", "Rent Date", "Return Date", "Amount", "Status", "carNo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel3.add(jScrollPane3);
        jScrollPane3.setBounds(10, 80, 970, 402);

        jPanel10.setBackground(new java.awt.Color(255, 0, 204));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel10);
        jPanel10.setBounds(30, 710, 940, 30);

        jButton6.setBackground(new java.awt.Color(255, 102, 102));
        jButton6.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton6.setText("APROVE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton6);
        jButton6.setBounds(50, 580, 120, 26);

        jButton7.setBackground(new java.awt.Color(255, 51, 102));
        jButton7.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton7.setText("DELETE");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton7);
        jButton7.setBounds(870, 580, 82, 26);

        jPanel14.setBackground(new java.awt.Color(255, 0, 204));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel14);
        jPanel14.setBounds(20, 20, 950, 30);

        jPanel6.add(jPanel3, "card5");

        jPanel7.setBackground(new java.awt.Color(255, 204, 204));

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("?");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("INCOME");

        jLabel23.setBackground(new java.awt.Color(153, 153, 153));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Rents");

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setText("?");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel14.setText("?");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Cars");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setText("?");

        jLabel25.setBackground(new java.awt.Color(153, 255, 255));
        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("Clients");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addGap(229, 229, 229)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addGap(54, 54, 54))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(74, 74, 74)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 643, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rentalcar/Grey_and_Black_Car_Rental_Service_Logo__5_-removebg-preview.png"))); // NOI18N
        jLabel21.setText("jLabel21");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 113, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(168, 168, 168))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(190, 190, 190))
        );

        jPanel6.add(jPanel7, "card5");

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Rent", "UserID", "Name", "PaymentMethod", " PaymentAmount", "PaymentDate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );

        jPanel6.add(jPanel16, "card6");

        jPanel8.setBackground(new java.awt.Color(255, 204, 204));

        jTable1.setBackground(new java.awt.Color(204, 204, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Car No", "Car Brand", "Car Model", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Car No");

        jLabel6.setText("jLabel6");

        jLabel7.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Car Brand");

        jLabel8.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Car Model");

        jLabel9.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Amount");

        carbrand.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        carbrand.setForeground(new java.awt.Color(255, 255, 255));
        carbrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HONDA", "TOYOTA", "NISSAN", "MITSUBISHI", " " }));

        carm.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        carm.setForeground(new java.awt.Color(255, 255, 255));
        carm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HONDA CIVIC", "HONDA CRV", "HONDA CRV v3", "TOYOTA VIOS", "TOYOTA FORTUNER", "TOYOTA WIGO", "NISSAN ALMERA", "NISSAN TIERA", "NISSAN NAVARA", "MITSUBISHI IXPANDER", "MITSUBISHI ADVENTURE", "MITSUBISHI MIRRAGE " }));
        carm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carmActionPerformed(evt);
            }
        });

        txtamount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtamountActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("ADD ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("CLEAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 204, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("DELETE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(204, 204, 204));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setText("EDIT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jLabel5)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(carbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(carm, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(33, 33, 33)
                .addComponent(txtamount, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(284, 284, 284))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(carbrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(carm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(140, 140, 140)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(131, 131, 131))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel8, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        // TODO add your handling code here:
              jPanel6.removeAll();
      jPanel6.add(jPanel7);
       jPanel6.repaint();
        jPanel6.revalidate();
    }//GEN-LAST:event_tab1MouseClicked

    private void tab2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2MouseClicked
        // TODO add your handling code here:
            jPanel6.removeAll();
      jPanel6.add(jPanel8);
       jPanel6.repaint();
        jPanel6.revalidate();
    }//GEN-LAST:event_tab2MouseClicked

    private void tab3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab3MouseClicked
        // TODO add your handling code here:
          jPanel6.removeAll();
      jPanel6.add(jPanel2);
       jPanel6.repaint();
        jPanel6.revalidate();
    }//GEN-LAST:event_tab3MouseClicked

    private void tab4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab4MouseClicked
        // TODO add your handling code here:
         jPanel6.removeAll();
      jPanel6.add(jPanel3);
       jPanel6.repaint();
        jPanel6.revalidate();
    }//GEN-LAST:event_tab4MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         carbrand.setSelectedIndex(-1);
      carm.setSelectedIndex(-1);
        txtamount.setText("");
        CarAutoID();
        Load_car();
        jButton1.setEnabled(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtamountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtamountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtamountActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
  int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "No row selected.");
        return;
    }

    String Carno = jTable1.getValueAt(selectedRow, 0).toString(); 
    String Carbrand = jTable1.getValueAt(selectedRow, 1).toString();  
    String Carmodel = jTable1.getValueAt(selectedRow, 2).toString(); 
    String amount = jTable1.getValueAt(selectedRow, 3).toString(); 

    // Populate the label and other fields
    jLabel6.setText(Carno); // Set the Carno in the label for delete functionality
    carbrand.setSelectedItem(Carbrand);
    carm.setSelectedItem(Carmodel);
    txtamount.setText(amount); 
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // Retrieve data from form elements
        String carno = jLabel6.getText();
    String carBrand = carbrand.getSelectedItem().toString();
    String carModel = carm.getSelectedItem().toString();
    String amount = txtamount.getText();

    try {
        // Prepare the insert query without 'CarID' (if it's auto-increment)
        pat = con.prepareStatement("INSERT INTO cars (CarNo, CarBrand, CarModel, Amount) VALUES (?, ?, ?, ?)");
        // Set the parameters for the query
        pat.setString(1, carno);  // Assuming CarNo is handled elsewhere or needs to be set
        pat.setString(2, carBrand);
        pat.setString(3, carModel);
        pat.setString(4, amount);
        
        // Execute the insert query
        pat.executeUpdate();

        // Show success message
        JOptionPane.showMessageDialog(this, "Car ADDED");

        // Reset the input fields
        carbrand.setSelectedIndex(-1);
        carm.setSelectedIndex(-1);
        txtamount.setText("");
        
        // Call the method to update the CarID and refresh the table
        CarAutoID();  // If this method generates the next CarID
        Load_car();   // Make sure this method refreshes the table from the database

    } catch (SQLException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void carmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_carmActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String carID = jLabel6.getText(); // Assuming carID is the unique identifier for the car

    if (carID == null || carID.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select or provide a valid Car ID");
        return;
    }

    try {
        pat = con.prepareStatement("DELETE FROM cars WHERE CarNo = ?");
        pat.setString(1, carID);
        int rowsAffected = pat.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Car Deleted Successfully");
        } else {
            JOptionPane.showMessageDialog(this, "No Car Found with the Given ID: " + carID);
        }
        
        carbrand.setSelectedIndex(-1);
        carm.setSelectedIndex(-1);
        txtamount.setText("");
        CarAutoID();
        Load_car();
         displayAllIncome();
    } catch (SQLException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error Deleting Car: " + ex.getMessage());
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
     // TODO add your handling code here:
    String carID = jLabel6.getText(); // Assuming carID is the unique identifier for the car

    if (carID == null || carID.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select or provide a valid Car ID");
        return;
    }

    // Get the new values from your input fields (carbrand, carm, txtamount)
    String carBrand = carbrand.getSelectedItem() != null ? carbrand.getSelectedItem().toString() : "";
    String carModel = carm.getSelectedItem() != null ? carm.getSelectedItem().toString() : "";
    String amount = txtamount.getText().trim();

    // Check if the combo boxes and the amount field are empty
    if (carBrand.isEmpty() || carModel.isEmpty() || amount.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please provide all car details");
        return;
    }

    try {
        // Prepare the SQL update statement
        pat = con.prepareStatement("UPDATE cars SET CarBrand = ?, CarModel = ?, Amount = ? WHERE CarNo = ?");
        
        // Set the values for the prepared statement
        pat.setString(1, carBrand);
        pat.setString(2, carModel);
        pat.setString(3, amount);
        pat.setString(4, carID);

        int rowsAffected = pat.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Car Updated Successfully");
        } else {
            JOptionPane.showMessageDialog(this, "No Car Found with the Given ID: " + carID);
        }
        
        // Reset the fields and reload data after update
        carbrand.setSelectedIndex(-1); // Reset combo box
        carm.setSelectedIndex(-1); // Reset combo box
        txtamount.setText(""); // Clear amount field
        jLabel6.setText(""); // Clear car ID
        CarAutoID(); // Generate new Car ID if needed
        Load_car(); // Reload car data

    } catch (SQLException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error Updating Car: " + ex.getMessage());
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "No row selected.");
        return;
    }
    String UserID = jTable2.getValueAt(selectedRow, 0).toString(); 
    String Username = jTable2.getValueAt(selectedRow, 1).toString();  
    String Firstname = jTable2.getValueAt(selectedRow, 2).toString(); 
    String Lastname = jTable2.getValueAt(selectedRow, 3).toString(); 
  String ContactNo = jTable2.getValueAt(selectedRow, 4).toString(); 
  String  Password = jTable2.getValueAt(selectedRow, 5).toString(); 
  String Gender= jTable2.getValueAt(selectedRow, 6).toString();

    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
          int selectedRow = jTable2.getSelectedRow();

        // Check if a row is selected
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No row selected.");
            return;
        }

        // Get the UserID from the selected row (assuming it's in the first column)
        String UserID = jTable2.getValueAt(selectedRow, 0).toString();

        // Confirm deletion from the user
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this user?",
            "Delete Confirmation", JOptionPane.YES_NO_OPTION);

        // If the user confirms, proceed to delete
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Prepare the DELETE statement with the UserID as the parameter
                pat = con.prepareStatement("DELETE FROM clients WHERE UserID = ?");

                // Set the UserID parameter
                pat.setString(1, UserID);

                // Execute the DELETE query
                int rowsAffected = pat.executeUpdate();

                if (rowsAffected > 0) {
                    // Notify the user that the record has been deleted
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");

                    // Reload the table data (or update the table as needed)
                    Load_client();

                    // Optionally, re-enable buttons or reset UI components
                    jButton1.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Error: User could not be deleted.");
                }

            } catch (SQLException ex) {
                // Handle SQL errors
                Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
           if (selectedRentID == null || selectedRentID.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please select a reservation to delete.");
    return;
}

// Confirm deletion
int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this reservation?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
if (confirm != JOptionPane.YES_OPTION) {
    return; // User canceled
}

try {
    // Retrieve userID from the selected row (adjust the column index if needed)
    int selectedRow = jTable3.getSelectedRow();
    String selectedUserID = jTable3.getValueAt(selectedRow, 1).toString(); // Assuming userID is in column 0

    // Prepare the DELETE query with both ReserveID and userID
    pat = con.prepareStatement("DELETE FROM reservations WHERE RentID = ? AND UserID = ?");
    pat.setString(1, selectedRentID);
    pat.setString(2, selectedUserID); // Bind the userID to the query

    // Execute the delete query
    int rowsAffected = pat.executeUpdate();

    if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(this, "Rent deleted successfully.");
        Load_reservations(); // Refresh the table
        displayAllIncome();
    } else {
        JOptionPane.showMessageDialog(this, "Rent could not be deleted.");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(this, "Error deleting rent: " + ex.getMessage());
}
         
    }//GEN-LAST:event_jButton7ActionPerformed
private String selectedRentID;
    private int previouslySelectedRow = -1;
    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
         int selectedRow = jTable3.getSelectedRow();

    // Ensure a row is selected
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "No row selected.");
        return;
    }

    // Get the ReserveID from the selected row
    String rentID = (String) jTable3.getValueAt(selectedRow, 0); // Assuming ReserveID is in column 1
    if (rentID == null || rentID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Selected row does not have a valid ReserveID.");
        return;
    }

    // Store the selected ReserveID for further use
    selectedRentID = rentID;

    // Debugging: Print the selected reservation details for confirmation
    System.out.println("Selected Rent ID: " + rentID);
 
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
         try {
        // Get the selected row index
        int selectedRow = jTable3.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to approve.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the ReserveID, RoomNo, and Check-in/Check-out dates from the selected row
        String selectedRentID = jTable3.getValueAt(selectedRow, 0).toString(); // Assuming ReserveID is in column 1
        String selectedUserID = jTable3.getValueAt(selectedRow, 1).toString();
     String name= jTable3.getValueAt(selectedRow, 2).toString();
       String address= jTable3.getValueAt(selectedRow, 3).toString();
         String contactno = jTable3.getValueAt(selectedRow, 4).toString();
           String carbrand = jTable3.getValueAt(selectedRow, 5).toString();
             String carmodel = jTable3.getValueAt(selectedRow, 6).toString();
        String rentdate = jTable3.getValueAt(selectedRow, 7).toString(); // Assuming Check-In is in column 5
        String returndate = jTable3.getValueAt(selectedRow, 8).toString(); // Assuming Check-Out is in column 6
        String amount = jTable3.getValueAt(selectedRow, 9).toString(); // Assuming userID is in column 0
        String currentStatus = jTable3.getValueAt(selectedRow, 10).toString(); // Assuming Status is in column 7
    String carno = jTable3.getValueAt(selectedRow, 11).toString();
        // Check if the reservation is already approved
        if ("Approved".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, "This reservation is already approved.", "Already Approved", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert dates to the proper format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date checkInDate = sdf.parse(rentdate);
        java.util.Date checkOutDate = sdf.parse(returndate);

        // Prepare SQL query to check if the room is already booked for the selected dates (excluding the current reservation)
        pat = con.prepareStatement(
            "SELECT * FROM reservations WHERE carNo = ? AND Status = 'Pending' AND ( " +
            "(rentdate BETWEEN ? AND ?) OR " + 
            "(returndate BETWEEN ? AND ?) OR " + 
            "(rentdate <= ? AND returndate >= ?) ) AND RentID != ?" // Exclude the current reservation (ReserveID != ?)
        );

        // Set parameters for roomNo and the date ranges
        pat.setString(1, carno);
        pat.setDate(2, new java.sql.Date(checkInDate.getTime()));
        pat.setDate(3, new java.sql.Date(checkOutDate.getTime()));
        pat.setDate(4, new java.sql.Date(checkInDate.getTime()));
        pat.setDate(5, new java.sql.Date(checkOutDate.getTime()));
        pat.setDate(6, new java.sql.Date(checkInDate.getTime()));
        pat.setDate(7, new java.sql.Date(checkOutDate.getTime()));
        pat.setString(8, selectedRentID); // Exclude the current reservation by ReserveID

        ResultSet rs = pat.executeQuery();

        // If a conflicting pending reservation exists, show an error message and return
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Room is already booked for the selected dates.", "Room Conflict", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If no conflict, update the reservation status
        pat = con.prepareStatement("UPDATE reservations SET status = ? WHERE RentID = ? AND UserID = ?");
        pat.setString(1, "Approved");  // Change Pending to "Approved"
        pat.setString(2, selectedRentID);  // Set the selected ReserveID
        pat.setString(3, selectedUserID);  // Set the selected userID
        pat.executeUpdate();

        // Show success message
        JOptionPane.showMessageDialog(this, "Rent approved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Refresh the table or reload reservations
        Load_reservations();

    } catch (SQLException | ParseException ex) {
        Logger.getLogger(finaladmin.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error while updating rent status.", "Error", JOptionPane.ERROR_MESSAGE);
    }  
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        Client obj = new Client();
        obj.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void tab5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab5MouseClicked
        // TODO add your handling code here:
        jPanel6.removeAll();
      jPanel6.add(jPanel16);
       jPanel6.repaint();
        jPanel6.revalidate();
        
    }//GEN-LAST:event_tab5MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(finaladmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(finaladmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(finaladmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(finaladmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new finaladmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> carbrand;
    private javax.swing.JComboBox<String> carm;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JTextField txtamount;
    // End of variables declaration//GEN-END:variables
}
