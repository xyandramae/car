/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package rentalcar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author USER
 */
public class Reserve extends javax.swing.JFrame {

    /**
     * Creates new form Reserve
     */
    public Reserve() {
        initComponents();
        Connect();
      
        carNo();
         carBrand();
         carModel() ;
         Load_reservation();
    }
Connection con;
    PreparedStatement pat;
  
    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/crental", "root", "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClientReg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



public String generateRentID() {
    String rentID = "R";
    int lastRentID = 0;  // Initialize lastRentID
    
    try {
        // Query to get the number of reservations made by the current user
        pat = con.prepareStatement("SELECT COUNT(*) FROM reservations WHERE UserID = ?");
        pat.setInt(1, Client.UID);  // Use the current user's ID
        ResultSet rs = pat.executeQuery();
        
        if (rs.next()) {
            // If user has no previous reservations, start from R001
            lastRentID = rs.getInt(1);  // Get the count of reservations for the user
        }
    } catch (SQLException ex) {
        Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // If no reservations, this is the user's first reservation (R001)
    lastRentID++;  // Increment to start from 1 for the first reservation
    
    return "R" + String.format("%03d", lastRentID);  // Return RentID with leading zeros, e.g., R001, R002, etc.
}


    public void carNo() {
        try {
        pat = con.prepareStatement("SELECT DISTINCT carNo FROM cars");
        ResultSet rs = pat.executeQuery();
        carno.removeAllItems();
        while (rs.next()) {
            carno.addItem(rs.getString("carNo"));
        }
    } catch (SQLException ex) {
        Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
      public void carBrand() {
           try {
        pat = con.prepareStatement("SELECT DISTINCT Carbrand FROM cars");
        ResultSet rs = pat.executeQuery();
       txtcarbrand.removeAllItems();
        while (rs.next()) {
            txtcarbrand.addItem(rs.getString("Carbrand"));
        }
    } catch (SQLException ex) {
        Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
        public void carModel() {
           try {
        pat = con.prepareStatement("SELECT DISTINCT Carmodel FROM cars");
        ResultSet rs = pat.executeQuery();
       txtcarmodel.removeAllItems();
        while (rs.next()) {
            txtcarmodel.addItem(rs.getString("Carmodel"));
        }
    } catch (SQLException ex) {
        Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
        public void Load_reservation() {
    try {
        // Use a parameterized query to prevent SQL injection
        pat = con.prepareStatement("SELECT RentID, Name, Address, ContactNo, Carbrand, Carmodel, Rentdate, Returndate, Amount, Status FROM reservations WHERE userID = ?");
        pat.setInt(1, Client.UID); // Use Login.UID to fetch only the current user's reservations
        ResultSet rs = pat.executeQuery();

        // Get table model and clear existing rows
        DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
        d.setRowCount(0);

        while (rs.next()) {
            // Create a new row with values for the current reservation based on the columns provided
            Vector<String> v2 = new Vector<>();
            v2.add(rs.getString("RentID"));      // RentID
            v2.add(rs.getString("Name"));        // Name
            v2.add(rs.getString("Address"));     // Address
            v2.add(rs.getString("ContactNo"));   // ContactNo
            v2.add(rs.getString("Carbrand"));    // Carbrand
            v2.add(rs.getString("Carmodel"));    // Carmodel
            v2.add(rs.getString("Rentdate"));    // Rentdate
            v2.add(rs.getString("Returndate"));  // Returndate
            v2.add(rs.getString("Amount"));      // Amount
            v2.add(rs.getString("Status"));      // Status

            // Add the row to the table model
            d.addRow(v2);
        }
    } catch (SQLException ex) {
        Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error loading reservations: " + ex.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtname = new javax.swing.JTextField();
        txtaddress = new javax.swing.JTextField();
        txtmobile = new javax.swing.JTextField();
        txtcarbrand = new javax.swing.JComboBox<>();
        txtcarmodel = new javax.swing.JComboBox<>();
        txtcheckin = new com.toedter.calendar.JDateChooser();
        txtcheckout = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        carno = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        txtmobile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmobileActionPerformed(evt);
            }
        });

        txtcarbrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        txtcarbrand.setMinimumSize(new java.awt.Dimension(72, 30));
        txtcarbrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcarbrandActionPerformed(evt);
            }
        });

        txtcarmodel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcarmodelActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("Name");

        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setText("Address");

        jLabel4.setForeground(new java.awt.Color(204, 0, 0));
        jLabel4.setText("Contact No");

        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("Car Brand");

        jLabel6.setForeground(new java.awt.Color(204, 0, 0));
        jLabel6.setText("Car Model");

        jLabel7.setForeground(new java.awt.Color(204, 0, 0));
        jLabel7.setText("Return Date");

        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setText("Rent Date");

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Close");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("RENTID");

        carno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carnoActionPerformed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(204, 0, 0));
        jLabel11.setText("CarNo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtcarmodel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtcarbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtmobile, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtcheckout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcheckin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(carno, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(30, 30, 30))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtaddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtmobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcarbrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcarmodel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(carno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtcheckin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtcheckout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton1))
                .addGap(29, 29, 29))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "RentID", "Name", "Address", "Contact No", "Car Brand", "Car Model", "Rent Date", "Retrun Date", "Amount", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 746, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );

        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addGap(29, 29, 29))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtcarbrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcarbrandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcarbrandActionPerformed

    private void txtcarmodelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcarmodelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcarmodelActionPerformed

    private void txtmobileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmobileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmobileActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    String rentID = generateRentID();  // Generate RentID based on logic

    String name = txtname.getText().trim();
    String address = txtaddress.getText().trim();
    String mobile = txtmobile.getText().trim();
    String carbrand = txtcarbrand.getSelectedItem().toString();
    String carmodel = txtcarmodel.getSelectedItem().toString();
    String carNo = carno.getSelectedItem().toString();

    // Validate inputs
    if (txtcheckin.getDate() == null || txtcheckout.getDate() == null || 
        name.isEmpty() || address.isEmpty() || mobile.isEmpty() ||
        carNo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Missing Information", JOptionPane.WARNING_MESSAGE);
        return;
    }

    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    String rentdate = df1.format(txtcheckin.getDate());
    String returndate = df1.format(txtcheckout.getDate());

    // Validate that CheckIn date is today or later
    Date today = new Date();
    if (txtcheckin.getDate().before(today)) {
        JOptionPane.showMessageDialog(this, "Check-in date cannot be before today's date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if checkout date is before check-in date
    if (txtcheckin.getDate().after(txtcheckout.getDate())) {
        JOptionPane.showMessageDialog(this, "Check-out date cannot be before the check-in date", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Calculate the number of nights
        long diffInMillis = txtcheckout.getDate().getTime() - txtcheckin.getDate().getTime();
        long numberOfNights = diffInMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days

        // Fetch the car's rental rate per day
        double carAmountPerDay = 0;
        pat = con.prepareStatement("SELECT amount FROM cars WHERE carNo = ?");
        pat.setString(1, carNo);
        ResultSet rs = pat.executeQuery();

        if (rs.next()) {
            carAmountPerDay = rs.getDouble("amount");
        } else {
            JOptionPane.showMessageDialog(this, "Car rental rate not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ensure amount is greater than 0
        if (carAmountPerDay <= 0) {
            JOptionPane.showMessageDialog(this, "Invalid car rental rate", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate the total rental amount
        double totalAmount = numberOfNights * carAmountPerDay;

        // Check if the car is already reserved for the selected dates
        pat = con.prepareStatement(
            "SELECT * FROM reservations WHERE carNo = ? AND ( " +
            "(Rentdate BETWEEN ? AND ?) OR " + 
            "(Returndate BETWEEN ? AND ?) OR " + 
            "(Rentdate <= ? AND Returndate >= ?)" + 
            ")");

        pat.setString(1, carNo);
        pat.setString(2, rentdate);
        pat.setString(3, returndate);
        pat.setString(4, rentdate);
        pat.setString(5, returndate);
        pat.setString(6, rentdate);
        pat.setString(7, returndate);

        rs = pat.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Car is unavailable for the selected dates.", "Car Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert the reservation into the database
        pat = con.prepareStatement(
            "INSERT INTO reservations (RentID, UserID, Name, Address, ContactNo, Carbrand, Carmodel, Rentdate, Returndate, Amount, Status, carNo) " + 
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
        );

        // Set parameters for the query
        pat.setString(1, rentID);  // Use the generated RentID
        pat.setInt(2, Client.UID);  // Use the logged-in user's ID
        pat.setString(3, name);
        pat.setString(4, address);
        pat.setString(5, mobile);
        pat.setString(6, carbrand);
        pat.setString(7, carmodel);
        pat.setString(8, rentdate);
        pat.setString(9, returndate);
        pat.setDouble(10, totalAmount);
        pat.setString(11, "Pending");
        pat.setString(12, carNo);

        pat.executeUpdate();  // Execute the insert

        JOptionPane.showMessageDialog(this, "Car reservation added successfully.");

        // Refresh the reservation list
        Load_reservation();

        // Clear the fields after successful insertion
        txtname.setText("");
        txtaddress.setText("");
        txtmobile.setText("");
        txtcarbrand.setSelectedIndex(-1);
        txtcarmodel.setSelectedIndex(-1);
        carno.setSelectedIndex(-1);
        txtcheckin.setDate(null);
        txtcheckout.setDate(null);

    } catch (SQLException ex) {
        Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error processing reservation: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void carnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_carnoActionPerformed
 private int previouslySelectedRow = -1;
 private String selectedRentID = "";
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
  int selectedRow = jTable1.getSelectedRow();
if (selectedRow >= 0) {
        selectedRentID = jTable1.getValueAt(selectedRow, 0) != null ? jTable1.getValueAt(selectedRow, 0).toString() : "";
        // Enable the Edit button if a valid row is selected
        jButton2.setEnabled(true);
    } else {
        jButton2.setEnabled(false);  // Disable the Edit button if no row is selected
    }
    // Check if the same row is clicked again
    if (selectedRow == previouslySelectedRow) {
        // Clear all fields if the same row is clicked again
        txtname.setText("");  // Name text field
        txtaddress.setText("");  // Address text field
        txtmobile.setText("");  // Mobile text field
        txtcheckin.setDate(null);  // Rentdate date chooser
        txtcheckout.setDate(null);  // Returndate date chooser
        txtcarbrand.setSelectedIndex(-1);  // Carbrand combo box
        txtcarmodel.setSelectedIndex(-1);  // Carmodel combo box
        carno.setSelectedIndex(-1);  // Car number combo box

        // Enable the Add button
        jButton1.setEnabled(true);

        // Deselect the row
        jTable1.clearSelection();

        // Reset the previously selected row index
        previouslySelectedRow = -1;
    } else {
        // Set the previously selected row index to the current row
        previouslySelectedRow = selectedRow;

        if (selectedRow >= 0) { // Ensure a valid row is selected
            try {
                // Get the RentID (column 0)
                String rentID = jTable1.getValueAt(selectedRow, 0) != null ? jTable1.getValueAt(selectedRow, 0).toString() : "";

                // Set Name (column 1)
                txtname.setText(jTable1.getValueAt(selectedRow, 1) != null ? jTable1.getValueAt(selectedRow, 1).toString() : "");

                // Set Address (column 2)
                txtaddress.setText(jTable1.getValueAt(selectedRow, 2) != null ? jTable1.getValueAt(selectedRow, 2).toString() : "");

                // Set Mobile Number (column 3) (ContactNo)
                txtmobile.setText(jTable1.getValueAt(selectedRow, 3) != null ? jTable1.getValueAt(selectedRow, 3).toString() : "");

                // Set Carbrand (column 4)
                txtcarbrand.setSelectedItem(jTable1.getValueAt(selectedRow, 4) != null ? jTable1.getValueAt(selectedRow, 4).toString() : "");

                // Set Carmodel (column 5)
                txtcarmodel.setSelectedItem(jTable1.getValueAt(selectedRow, 5) != null ? jTable1.getValueAt(selectedRow, 5).toString() : "");

                // Parse and set Rentdate and Returndate (columns 6 and 7)
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (jTable1.getValueAt(selectedRow, 6) != null) {
                    txtcheckin.setDate(df.parse(jTable1.getValueAt(selectedRow, 6).toString()));
                } else {
                    txtcheckin.setDate(null);
                }

                if (jTable1.getValueAt(selectedRow, 7) != null) {
                    txtcheckout.setDate(df.parse(jTable1.getValueAt(selectedRow, 7).toString()));
                } else {
                    txtcheckout.setDate(null);
                }

                // Disable the Add button to avoid accidental additions
                jButton1.setEnabled(false);

                // Store RentID for further actions
               // Store RentID to use in update and delete actions

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading reservation details: " + ex.getMessage());
            }
        }
    }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
     int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        // No row selected, show an error message
        JOptionPane.showMessageDialog(this, "No reservation selected.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Retrieve RentID from the selected row (assuming it's in the first column)
    String rentID = jTable1.getValueAt(selectedRow, 0).toString();

    // Validate the RentID
    if (rentID == null || rentID.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No valid RentID found for this reservation.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Confirm the deletion with the user
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to delete this reservation?", 
        "Confirm Deletion", 
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Prepare the DELETE SQL statement
            pat = con.prepareStatement("DELETE FROM reservations WHERE RentID = ? AND UserID = ?");
            pat.setString(1, rentID);  // Use RentID to delete the reservation
            pat.setInt(2, Client.UID);  // Ensure only the logged-in user can delete their reservation

            // Execute the DELETE operation
            int rowsAffected = pat.executeUpdate();

            if (rowsAffected > 0) {
                // Show success message
                JOptionPane.showMessageDialog(this, "Reservation deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear the form fields
                txtname.setText("");
                txtaddress.setText("");
                txtmobile.setText("");
                txtcheckin.setDate(null);
                txtcheckout.setDate(null);
                txtcarbrand.setSelectedIndex(-1);
                txtcarmodel.setSelectedIndex(-1);
                carno.setSelectedIndex(-1);

                // Refresh the data
                Load_reservation();  // Reload the reservations to reflect changes
                jButton1.setEnabled(true);  // Enable the reservation button again
            } else {
                // Show error if no rows were affected
                JOptionPane.showMessageDialog(this, "No reservation found with the provided RentID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error deleting reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
     // TODO add your handling code here:
     String rentID = selectedRentID;

    // Validate RentID
    if (rentID == null || rentID.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No valid reservation selected.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Fetch the updated values from the fields
    String name = txtname.getText().trim();
    String address = txtaddress.getText().trim();
    String mobile = txtmobile.getText().trim();
    String carbrand = txtcarbrand.getSelectedItem().toString();
    String carmodel = txtcarmodel.getSelectedItem().toString();
    String carNo = carno.getSelectedItem().toString();
    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    String rentdate = df1.format(txtcheckin.getDate());
    String returndate = df1.format(txtcheckout.getDate());

    // Validate inputs
    if (txtcheckin.getDate() == null || txtcheckout.getDate() == null || 
        name.isEmpty() || address.isEmpty() || mobile.isEmpty() ||
        carNo.isEmpty()) {  // Check if carNo is selected
        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Missing Information", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Validate that CheckIn date is today or later
    Date today = new Date();
    if (txtcheckin.getDate().before(today)) {
        JOptionPane.showMessageDialog(this, "Check-in date cannot be before today's date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if checkout date is before check-in date
    if (txtcheckin.getDate().after(txtcheckout.getDate())) {
        JOptionPane.showMessageDialog(this, "Check-out date cannot be before the check-in date", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Calculate the amount (example logic for rental duration and daily rental rate)
        long diffInMillis = txtcheckout.getDate().getTime() - txtcheckin.getDate().getTime();
        long rentalDays = diffInMillis / (1000 * 60 * 60 * 24);  // Convert milliseconds to days

        double dailyRate = 100.00;  // Assume a fixed rate per day (or fetch from the database)
        double totalAmount = rentalDays * dailyRate;

        // Prepare the UPDATE SQL statement
        pat = con.prepareStatement(
            "UPDATE reservations SET Name = ?, Address = ?, ContactNo = ?, Carbrand = ?, Carmodel = ?, Rentdate = ?, Returndate = ?, Amount = ?, Status = ?, carNo = ? " + 
            "WHERE RentID = ? AND UserID = ?"
        );

        pat.setString(1, name);
        pat.setString(2, address);
        pat.setString(3, mobile);
        pat.setString(4, carbrand);
        pat.setString(5, carmodel);
        pat.setString(6, rentdate);
        pat.setString(7, returndate);
        pat.setDouble(8, totalAmount);  // Set the calculated total amount
        pat.setString(9, "Pending");  // You can set the status as "Pending" or based on your logic
        pat.setString(10, carNo);
        pat.setString(11, rentID);  // Use RentID to identify the reservation to update
        pat.setInt(12, Client.UID);  // Ensure only the logged-in user can update their reservation

        // Execute the UPDATE operation
        int rowsAffected = pat.executeUpdate();

        if (rowsAffected > 0) {
            // Show success message
            JOptionPane.showMessageDialog(this, "Reservation updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear the form fields
            txtname.setText("");
            txtaddress.setText("");
            txtmobile.setText("");
            
            // Reset the combo boxes to default (no selection)
            txtcarbrand.setSelectedIndex(-1);
            txtcarmodel.setSelectedIndex(-1);
            carno.setSelectedIndex(-1);
            
            // Clear the date pickers (if applicable)
            txtcheckin.setDate(null);
            txtcheckout.setDate(null);
            
            // Refresh the data
            Load_reservation();  // Reload the reservations
        } else {
            // Show error if no rows were affected
            JOptionPane.showMessageDialog(this, "No reservation found with the provided RentID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error updating reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Client obj = new Client();
        obj.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Reserve.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reserve.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reserve.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reserve.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reserve().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> carno;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtaddress;
    private javax.swing.JComboBox<String> txtcarbrand;
    private javax.swing.JComboBox<String> txtcarmodel;
    private com.toedter.calendar.JDateChooser txtcheckin;
    private com.toedter.calendar.JDateChooser txtcheckout;
    private javax.swing.JTextField txtmobile;
    private javax.swing.JTextField txtname;
    // End of variables declaration//GEN-END:variables
}
