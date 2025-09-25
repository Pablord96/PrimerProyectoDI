/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.feature5;

import java.util.Scanner;

public class HolaMundo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Cómo te llamas? ");
        String nombre = scanner.nextLine();

        System.out.println("¡Hola, " + nombre + "! Bienvenido al programa.");
        
        scanner.close();
    }
    
    /**
     *Añadido comentario
     */
}
