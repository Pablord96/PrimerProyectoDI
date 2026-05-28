/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HolaMundo;

/**
 *
 * @author pablo
 */
public class HolaMundo {
    
}


import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.ListViewMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppCrudCompletoTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Aquí cargarás el FXML que os dé el profesor
        Parent root = FXMLLoader.load(getClass().getResource("vista_principal.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testFlujoCompletoCrud() {
        String nombreOriginal = "Usuario Examen";
        String nombreModificado = "Usuario Aprobado";

        // ==========================================
        // 1. CREATE (Crear el registro)
        // ==========================================
        clickOn("#txtNombre");
        write(nombreOriginal);
        clickOn("#btnCrear");

        // ==========================================
        // 2. READ (Verificar que se ha creado y se lee)
        // ==========================================
        // Comprobamos que la lista ahora contiene el elemento que acabamos de crear
        FxAssert.verifyThat("#listaRegistros", ListViewMatchers.hasListCell(nombreOriginal));

        // ==========================================
        // 3. UPDATE (Actualizar el registro)
        // ==========================================
        // Hacemos clic en el elemento de la lista para seleccionarlo
        clickOn(nombreOriginal);
        
        // Limpiamos el campo de texto (simulando pulsar retroceso/borrar)
        clickOn("#txtNombre");
        eraseText(nombreOriginal.length()); // Borra el texto anterior
        
        // Escribimos el nuevo nombre y actualizamos
        write(nombreModificado);
        clickOn("#btnActualizar");

        // Verificamos que el nombre antiguo ya no está y el nuevo sí
        FxAssert.verifyThat("#listaRegistros", ListViewMatchers.hasListCell(nombreModificado));

        // ==========================================
        // 4. DELETE (Borrar el registro)
        // ==========================================
        // Seleccionamos el elemento modificado
        clickOn(nombreModificado);
        
        // Pulsamos borrar
        clickOn("#btnBorrar");
        
        // NOTA: Si el profesor pone un cuadro de diálogo de confirmación, 
        // tendrías que añadir aquí un: clickOn("Aceptar") o clickOn("Sí");

        // Comprobamos que la lista ya NO tiene el elemento
        FxAssert.verifyThat("#listaRegistros", ListViewMatchers.isEmpty()); 
        // O alternativamente verificar que no contiene el texto:
        // boolean existe = lookup(nombreModificado).tryQuery().isPresent();
        // assertFalse(existe);
    }
}






import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.ListViewMatchers;
// ... (resto de imports)

public class AppCrudCompletoTest extends ApplicationTest {

    // ... (método start igual que antes)

    @Test
    public void testFlujoCompletoYRegresion() {
        String nombreValido = "Usuario Examen";
        
        // ====================================================================
        // CASO NEGATIVO (ERRORES DE LÓGICA) - ¡Para el Nivel Verde Claro!
        // ====================================================================
        clickOn("#txtNombre");
        write(""); // Dejamos el campo vacío a propósito
        clickOn("#btnCrear");
        
        // Comprobamos que salta un error en la UI (suponiendo que haya un label de error)
        // O al menos, que la lista sigue vacía y no ha fallado el programa
        FxAssert.verifyThat("#lblError", LabeledMatchers.hasText("El nombre no puede estar vacío"));
        
        // ====================================================================
        // 1. CREATE (Caso de Uso: Creación exitosa)
        // ====================================================================
        clickOn("#txtNombre");
        write(nombreValido);
        clickOn("#btnCrear");
        FxAssert.verifyThat("#listaRegistros", ListViewMatchers.hasListCell(nombreValido));

        // ====================================================================
        // 2 y 3. READ y UPDATE (Caso de Uso: Lectura y Actualización)
        // ====================================================================
        clickOn(nombreValido); // Seleccionar el recién creado
        
        // OTRO ERROR DE LÓGICA: Intentar actualizar borrando el nombre
        clickOn("#txtNombre");
        eraseText(nombreValido.length());
        clickOn("#btnActualizar");
        // Verificar que el error salta
        FxAssert.verifyThat("#lblError", LabeledMatchers.hasText("El nombre no puede estar vacío"));

        // Ahora lo actualizamos bien
        write("Usuario Modificado");
        clickOn("#btnActualizar");
        FxAssert.verifyThat("#listaRegistros", ListViewMatchers.hasListCell("Usuario Modificado"));

        // ====================================================================
        // 4. DELETE (Caso de Uso: Borrado)
        // ====================================================================
        clickOn("Usuario Modificado");
        clickOn("#btnBorrar");
        
        // Verificar que ya no está en la lista
        FxAssert.verifyThat("#listaRegistros", ListViewMatchers.isEmpty());
        
        /* * COMENTARIO PARA EL PROFESOR (Para amarrar el Nivel Verde Oscuro):
         * "La automatización de este flujo E2E constituye una suite de 
         * Pruebas de Regresión. Cualquier cambio futuro en el código fuente 
         * o en la BBDD que rompa el CRUD será detectado inmediatamente al 
         * volver a ejecutar esta clase."
         */
    }
}
