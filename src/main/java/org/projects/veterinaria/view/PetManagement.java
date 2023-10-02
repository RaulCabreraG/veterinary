package org.projects.veterinaria.view;

import org.projects.veterinaria.DB.ClientDB;
import org.projects.veterinaria.DB.PetDB;
import org.projects.veterinaria.entity.Client;
import org.projects.veterinaria.entity.Pet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PetManagement extends JFrame {
    private final PetDB petDB;
    private final ClientDB clientDB;
    private final JTable petTable;

    private final String[] petGenders = {"Macho", "Hembra"};
    private final String[] petTypes = {"Canino", "Felino", "Ave", "Roedor", "Reptil", "Otro"};

    public PetManagement(Client client) {
        // Configurar la ventana
        setTitle("Gestión de Mascotas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 230);

        // Crear una tabla para mostrar los clientes
        petTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(petTable);
        add(scrollPane, BorderLayout.CENTER);

        // Crear botones para acciones
        JButton agregarButton = new JButton("Agregar");
        JButton actualizarButton = new JButton("Actualizar");
        JButton eliminarButton = new JButton("Eliminar");
        JButton listarButton = new JButton("Ver Clientes");

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
        petDB = new PetDB();

        // Cargar las mascotas del cliente
        refreshPetTable(client);

        // Agregar ActionListener para los botones
        agregarButton.addActionListener(e -> {
            try {
                agregarMascota(client);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        actualizarButton.addActionListener(e -> actualizarMascota(client));

        eliminarButton.addActionListener(e -> eliminarMascota(client));

        listarButton.addActionListener(e -> listarClientes());
    }

    private void refreshPetTable(Client client) {
        // Obtener la lista actualizada de mascotas desde la base de datos
        try {
            String clientId = client.getId();
            List<Pet> pets = clientDB.getPets(clientId);

            // Crear un modelo de tabla y establecer los datos de los clientes
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Dueño");
            model.addColumn("Nombre");
            model.addColumn("Tipo");
            model.addColumn("Raza");
            model.addColumn("Color");
            model.addColumn("Genero");
            model.addColumn("Edad");

            for (Pet pet : pets) {
                Object[] rowData = {
                        pet.getId(),
                        pet.getOwnerId().getName() + " " + pet.getOwnerId().getLastName(),
                        pet.getPetName(),
                        pet.getType(),
                        pet.getRace(),
                        pet.getColor(),
                        pet.getGender(),
                        pet.getAge()
                };
                model.addRow(rowData);
            }

            // Establecer el modelo de tabla actualizado
            petTable.setModel(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void agregarMascota(Client client) throws SQLException {
        // Crear un formulario para agregar un cliente
        JTextField nombreField = new JTextField();
        JComboBox tipoField = new JComboBox(petTypes);
        JTextField razaField = new JTextField();
        JTextField colorField = new JTextField();
        JComboBox generoField = new JComboBox(petGenders);
        JTextField edadField = new JTextField();

        generoField.setSelectedIndex(0);

        Object[] fields = {
                "Nombre:", nombreField,
                "Tipo:", tipoField,
                "Raza:", razaField,
                "Color:", colorField,
                "Genero:", generoField,
                "Edad:", edadField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Mascota", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedGender = (String)generoField.getSelectedItem();
            // Crear un nuevo objeto Pet con los datos ingresados
            Pet pet = new Pet();
            pet.setId("1");
            pet.setOwnerId(client);
            pet.setPetName(nombreField.getText());
            pet.setType((String)tipoField.getSelectedItem());
            pet.setRace(razaField.getText());
            pet.setColor(colorField.getText());
            assert selectedGender != null;
            pet.setGender(selectedGender.charAt(0));
            pet.setAge(Byte.parseByte(edadField.getText()));

            // Guardar el nuevo cliente en la base de datos
            petDB.createPet(pet);

            // Actualizar la tabla con los clientes actualizados
            refreshPetTable(client);

            JOptionPane.showMessageDialog(this, "Mascota agregada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarMascota(Client client) {
        // Obtener el ID del cliente a actualizar
        int column = 0;
        int row = petTable.getSelectedRow();
        if (row >= 0) {
            String petId = petTable.getModel().getValueAt(row, column).toString();
            try {
                // Obtener el cliente desde la base de datos
                Pet pet = petDB.getPet(petId);

                if (pet != null) {
                    String compare = String.valueOf(pet.getGender());
                    // Crear un formulario con los datos del cliente
                    JTextField nombreField = new JTextField(pet.getPetName());
                    JComboBox tipoField = new JComboBox(petTypes);
                    JTextField razaField = new JTextField(pet.getRace());
                    JTextField colorField = new JTextField(pet.getColor());
                    JComboBox generoField = new JComboBox(petGenders);
                    JTextField edadField = new JTextField(String.valueOf(pet.getAge()));
                    generoField.setSelectedIndex(compare.equals("H") ? 1 : 0);
                    tipoField.setSelectedItem(pet.getType());

                    Object[] fields = {
                            "Nombre:", nombreField,
                            "Tipo:", tipoField,
                            "Raza:", razaField,
                            "Color:", colorField,
                            "Genero:", generoField,
                            "Edad:", edadField
                    };

                    int confirmResult = JOptionPane.showConfirmDialog(this, fields, "Actualizar Mascota", JOptionPane.OK_CANCEL_OPTION);
                    if (confirmResult == JOptionPane.OK_OPTION) {
                        String selectedGender = (String)generoField.getSelectedItem();
                        // Actualizar los datos del cliente con los valores ingresados en el formulario
                        pet.setPetName(nombreField.getText());
                        pet.setType((String)tipoField.getSelectedItem());
                        pet.setRace(razaField.getText());
                        pet.setColor(colorField.getText());
                        assert selectedGender != null;
                        pet.setGender(selectedGender.charAt(0));
                        pet.setAge(Byte.parseByte(edadField.getText()));
                        // Guardar los cambios en la base de datos
                        petDB.updatePet(pet);

                        // Actualizar la tabla de clientes en la interfaz
                        refreshPetTable(client);

                        JOptionPane.showMessageDialog(this, "Se actualizo la mascota", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ninguna mascota con el ID especificado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (HeadlessException e) {
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una mascota para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMascota(Client client) {
        // Obtener el ID del cliente a eliminar
        int column = 0;
        int row = petTable.getSelectedRow();
        if (row >= 0) {
            String petId = petTable.getModel().getValueAt(row, column).toString();
            try {
                // Confirmar la eliminación de la mascota
                int confirmResult = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la mascota?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    // Eliminar el cliente de la base de datos
                    petDB.deletePet(petId);

                    JOptionPane.showMessageDialog(this, "Se elimino la mascota", "Exito", JOptionPane.INFORMATION_MESSAGE);

                    // Actualizar la tabla de clientes en la interfaz
                    refreshPetTable(client);
                }
            } catch (HeadlessException e) {
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una mascota para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarClientes() {
        ClientManagement clientManagement = new ClientManagement();
        clientManagement.setVisible(true);
        this.setVisible(false);
    }
}
