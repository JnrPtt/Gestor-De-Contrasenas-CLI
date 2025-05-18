package org.example;

import java.util.Scanner;
import java.util.List;
import javax.crypto.SecretKey;

public class CLIManager {
    private final Scanner scanner;
    private final PasswordStore store;

    public CLIManager(PasswordStore store) {
        this.scanner = new Scanner( System.in);
        this.store = store;
    }

    public void start(){
        System.out.println("=== Gestor de Contraseñas CLI ===");

        while (true) {
            System.out.println("\nOpciones:");
            System.out.println("1. Añadir entrada");
            System.out.println("2. Ver todos los servicios");
            System.out.println("3. Ver detalles de un servicio");
            System.out.println("4. Eliminar entrada");
            System.out.println("5. Salir");

            System.out.print("Selecciona una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addEntry();
                case "2" -> viewAllEntries();
                case "3" -> viewEntryDetails();
                case "4" -> removeEntry();
                case "5" -> {
                    System.out.println("Saliendo...");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void addEntry(){
        System.out.println("\n=== Añadir nueva entrada ===");
        String service = prompt("Servicio: ");
        String username = prompt("Nombre de usuario: ");
        String password = prompt("Contraseña a cifrar: ");
        String masterPassword = prompt("Contraseña maestra: ");

        try {
            String salt = CryptoUtils.generateSalt();
            String iv = CryptoUtils.generateIv();
            SecretKey key = CryptoUtils.generateKeyFromPassword(masterPassword, salt);
            String encryptedPassword = CryptoUtils.encrypt(password, key, iv);

            Entry entry = new Entry(service, username, salt, iv, encryptedPassword);
            store.addEntry(entry);
            System.out.println("Entrada guardada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cifrar la contraseña: " + e.getMessage());
        }
    }

    private void removeEntry(){
        System.out.println("\n=== Eliminar entrada ===");
        String service = prompt("Nombre del servicio a eliminar: ");
        boolean deleted = store.removeEntryByService(service);

        if (deleted) {
            System.out.println("Entrada eliminada.");
        } else {
            System.out.println("No se encontró el servicio.");
        }
    }

    private void viewAllEntries(){
        System.out.println("\n=== Servicios Guardados ===");
        List<Entry> entries = store.getAllEntries();
        if (entries.isEmpty()) {
            System.out.println("No hay entradas guardadas.");
            return;
        }

        for (Entry entry : entries) {
            System.out.println("- " + entry.getService());
        }
    }

    private void viewEntryDetails(){
        System.out.println("\n=== Ver detalles ===");
        String service = prompt("Nombre del servicio: ");
        Entry entry = store.findEntryByService(service);

        if (entry == null) {
            System.out.println("No se encontró el servicio.");
            return;
        }

        String masterPassword = prompt("Introduce tu contraseña maestra: ");
        try {
            SecretKey key = CryptoUtils.generateKeyFromPassword(masterPassword, entry.getSalt());
            String decryptedPassword = CryptoUtils.decrypt(entry.getEncryptedPassword(), key, entry.getIv());

            System.out.println("Usuario: " + entry.getUsername());
            System.out.println("Contraseña: " + decryptedPassword);
        } catch (Exception e) {
            System.err.println("No se pudo descifrar la contraseña. ¿Contraseña maestra incorrecta?");
        }
    }

    private String prompt(String message){
        System.out.print(message);
        return scanner.nextLine();
    }
}

