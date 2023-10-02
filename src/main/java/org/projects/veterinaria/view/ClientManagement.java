package org.projects.veterinaria.view;

import org.projects.veterinaria.DB.ClientDB;
import org.projects.veterinaria.entity.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClientManagement extends JFrame {
    private final ClientDB clientDB;
    private final JTable clientTable;

    public ClientManagement() {
        // Configurar la ventana
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 230);

        // Crear una tabla para mostrar los clientes
        clientTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(clientTable);
        add(scrollPane, BorderLayout.CENTER);

        // Crear botones para acciones
        JButton agregarButton = new JButton("Agregar");
        JButton actualizarButton = new JButton("Actualizar");
        JButton eliminarButton = new JButton("Eliminar");
        JButton listarButton = new JButton("Gestionar Mascotas");

        // Configurar el panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(agregarButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(listarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Establecer estilos para los botones
        agregarButton.setBackground(new Color(46, 204, 113));
        agregarButton.setForeground(Color.WHITE);
        agregarButton.setFocusPainted(false);

        actualizarButton.setBackground(new Color(52, 152, 219));
        actualizarButton.setForeground(Color.WHITE);
        actualizarButton.setFocusPainted(false);

        eliminarButton.setBackground(new Color(231, 76, 60));
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.setFocusPainted(false);

        listarButton.setBackground(new Color(184, 82, 160));
        listarButton.setForeground(Color.WHITE);
        listarButton.setFocusPainted(false);

        // Crear el objeto DB para acceder a la base de datos

        clientDB = new ClientDB();

        // Cargar los clientes iniciales en la tabla
        refreshClientTable();

//        // Agregar ActionListener para los botones
        agregarButton.addActionListener(e -> {
            try {
                agregarCliente();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        actualizarButton.addActionListener(e -> actualizarCliente());
//
        eliminarButton.addActionListener(e -> eliminarCliente());

        listarButton.addActionListener(e -> listarMascotas());
    }

    private void refreshClientTable() {
        // Obtener la lista actualizada de clientes desde la base de datos
        try {
            List<Client> clients = clientDB.getAllClients();

            // Crear un modelo de tabla y establecer los datos de los clientes
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Apellido");
            model.addColumn("Direccion");
            model.addColumn("Telf Referencia 1");
            model.addColumn("Telf Referencia 2");

            for (Client client : clients) {
                Object[] rowData = {
                        client.getId(),
                        client.getName(),
                        client.getLastName(),
                        client.getAddress(),
                        client.getRefNumber1(),
                        client.getRefNumber2()
                };
                model.addRow(rowData);
            }

            // Establecer el modelo de tabla actualizado
            clientTable.setModel(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void agregarCliente() throws SQLException {
        // Crear un formulario para agregar un cliente
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField ref1Field = new JTextField();
        JTextField ref2Field = new JTextField();

        Object[] fields = {
                "Nombre:", nombreField,
                "Apellido:", apellidoField,
                "Direccion:", direccionField,
                "Ref 1:", ref1Field,
                "Ref 2:", ref2Field
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Crear un nuevo objeto Client con los datos ingresados
            Client client = new Client();
            client.setId("1");
            client.setName(nombreField.getText());
            client.setLastName(apellidoField.getText());
            client.setAddress(direccionField.getText());
            client.setRefNumber1(Integer.parseInt(ref1Field.getText()));
            client.setRefNumber2(Integer.parseInt(ref2Field.getText()));

            // Guardar el nuevo cliente en la base de datos
            clientDB.createClient(client);

            // Actualizar la tabla con los clientes actualizados
            refreshClientTable();

            JOptionPane.showMessageDialog(this, "Cliente agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarCliente() {
        // Obtener el ID del cliente a actualizar
//        String clienteIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID del cliente a actualizar:", "Actualizar Cliente", JOptionPane.QUESTION_MESSAGE);
        int column = 0;
        int row = clientTable.getSelectedRow();
        if (row >= 0) {
            String clientId = clientTable.getModel().getValueAt(row, column).toString();
            try {
                // Obtener el cliente desde la base de datos
                Client client = clientDB.getClient(clientId);

                if (client != null) {
                    // Crear un formulario con los datos del cliente
                    JTextField nombreField = new JTextField(client.getName());
                    JTextField apellidoField = new JTextField(client.getLastName());
                    JTextField direccionField = new JTextField(client.getAddress());
                    JTextField ref1Field = new JTextField(String.valueOf(client.getRefNumber1()));
                    JTextField ref2Field = new JTextField(String.valueOf(client.getRefNumber2()));

                    Object[] fields = {
                            "Nombre:", nombreField,
                            "Apellido:", apellidoField,
                            "Direccion:", direccionField,
                            "Referencia 1:", ref1Field,
                            "Referencia 2:", ref2Field
                    };

                    int confirmResult = JOptionPane.showConfirmDialog(this, fields, "Actualizar Cliente", JOptionPane.OK_CANCEL_OPTION);
                    if (confirmResult == JOptionPane.OK_OPTION) {
                        // Actualizar los datos del cliente con los valores ingresados en el formulario
                        client.setName(nombreField.getText());
                        client.setLastName(apellidoField.getText());
                        client.setAddress(direccionField.getText());
                        client.setRefNumber1(Integer.parseInt(ref1Field.getText()));
                        client.setRefNumber2(Integer.parseInt(ref2Field.getText()));

                        // Guardar los cambios en la base de datos
                        clientDB.updateClient(client);

                        JOptionPane.showMessageDialog(this, "Se actualizo el cliente", "Exito", JOptionPane.INFORMATION_MESSAGE);

                        // Actualizar la tabla de clientes en la interfaz
                        refreshClientTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el ID especificado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (HeadlessException e) {
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        // Obtener el ID del cliente a eliminar
//        String clienteIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID del cliente a eliminar:", "Eliminar Cliente", JOptionPane.QUESTION_MESSAGE);
        int column = 0;
        int row = clientTable.getSelectedRow();
        if (row >= 0) {
            String clientId = clientTable.getModel().getValueAt(row, column).toString();
            try {
                // Confirmar la eliminación del cliente
                int confirmResult = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    // Eliminar el cliente de la base de datos
                    clientDB.deleteClient(clientId);

                    JOptionPane.showMessageDialog(this, "Se elimino el cliente", "Exito", JOptionPane.INFORMATION_MESSAGE);

                    // Actualizar la tabla de clientes en la interfaz
                    refreshClientTable();
                }
            } catch (HeadlessException e) {
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarMascotas() {
        int column = 0;
        int row = clientTable.getSelectedRow();
        if (row >= 0) {
            String clientId = clientTable.getModel().getValueAt(row, column).toString();
            try {
//                clientDB.getPets(clientId);
                Client client = clientDB.getClient(clientId);
                PetManagement petManagement = new PetManagement(client);
                petManagement.setVisible(true);
                this.setVisible(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}