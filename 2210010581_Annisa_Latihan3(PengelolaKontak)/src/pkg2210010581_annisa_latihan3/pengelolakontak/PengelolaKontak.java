/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pkg2210010581_annisa_latihan3.pengelolakontak;

/**
 *
 * @author Annisa
 * 2210010581
 */

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PengelolaKontak extends JFrame {
    //Komponen GUI
    private Connection conn;
    private JPanel mainPanel;
    private JTextField nameField, phoneField;
    private JComboBox<String> categoryCombo;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;

    public PengelolaKontak() {
        //Mengatur Frame
        setTitle("Pengelola Kontak Annisa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); 
        setLocationRelativeTo(null);

        // database connection
        initDatabase();

        // Buat Komponen GUI
        createComponents();

        // Menambah event Listeners
        addListeners();
        
        loadContacts();
    }

    private void initDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:contacts.db");
            createContactsTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createContactsTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS contacts " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, phone TEXT, category TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createComponents() {
        // Set warna background utama pink
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(255, 192, 203)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        // Panel Input dengan GridBagLayout untuk penempatan yang lebih baik
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 182, 193)); // Pink lebih tua
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Styling untuk label
        JLabel nameLabel = createStyledLabel("Name:");
        JLabel phoneLabel = createStyledLabel("Phone:");
        JLabel categoryLabel = createStyledLabel("Category:");

        // Styling untuk text fields
        nameField = createStyledTextField(20);
        phoneField = createStyledTextField(20);
        categoryCombo = createStyledComboBox();

        // Tambahkan komponen 
        addComponent(inputPanel, nameLabel, gbc, 0, 0);
        addComponent(inputPanel, nameField, gbc, 1, 0);
        addComponent(inputPanel, phoneLabel, gbc, 0, 1);
        addComponent(inputPanel, phoneField, gbc, 1, 1);
        addComponent(inputPanel, categoryLabel, gbc, 0, 2);
        addComponent(inputPanel, categoryCombo, gbc, 1, 2);

        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(255, 192, 203));
        
        addButton = createStyledButton("Add");
        editButton = createStyledButton("Edit");
        deleteButton = createStyledButton("Delete");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(255, 192, 203));
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Category"}, 0);
        contactTable = createStyledTable();
        contactTable.setModel(tableModel);
        
        // Styling table header
        JTableHeader header = contactTable.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); 
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    // Style Label
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }
    //Style Textfield
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }
    //Style ComboBox
    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Keluarga", "Teman", "Kerja"});
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBackground(Color.BLACK);
        return combo;
    }
    //Style Button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180)); 
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
    //Style Table
    private JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(new Color(70, 130, 180));
        table.setSelectionBackground(new Color(70, 130, 180, 100));
        table.setSelectionForeground(Color.BLACK);
        return table;
    }

    private void addComponent(JPanel panel, Component comp, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(comp, gbc);
    }

    private void addListeners() {
        addButton.addActionListener(e -> addContact());
        editButton.addActionListener(e -> editContact());
        deleteButton.addActionListener(e -> deleteContact());
        categoryCombo.addItemListener(e -> loadContacts());
    }

    private void loadContacts() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM contacts");

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = new Object[] {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("category")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String category = (String) categoryCombo.getSelectedItem();

        try {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO contacts (name, phone, category) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, category);
            stmt.executeUpdate();
            loadContacts();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText();
            String phone = phoneField.getText();
            String category = (String) categoryCombo.getSelectedItem();

            try {
                PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE contacts SET name=?, phone=?, category=? WHERE id=?");
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, category);
                stmt.setInt(4, id);
                stmt.executeUpdate();
                loadContacts();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to edit");
        }
    }

    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this contact?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM contacts WHERE id=?");
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    loadContacts();
                    clearFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete");
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        categoryCombo.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new PengelolaKontak().setVisible(true));
    }
}
