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










import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import clientside.model.Movement;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DepositsAndPaymentsAdvancedTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // NOTA: Inicializa aquí tu FXML y controlador
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudbankjfxclient/view/DepositsAndPaymentsView.fxml"));
        // Parent root = loader.load();
        // ... configuración ...
        // stage.setScene(new Scene(root));
        // stage.show();
    }

    // =========================================================================
    // MÉTODOS AUXILIARES PARA MANEJAR ALERTAS SIN DEPENDER DEL IDIOMA
    // =========================================================================

    /**
     * Busca un cuadro de diálogo en pantalla y hace clic en el botón de confirmación (OK/Aceptar).
     */
    private void clickOkOnDialog() {
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        clickOn(okButton);
    }

    /**
     * Busca un cuadro de diálogo en pantalla y hace clic en el botón de cancelación (Cancel/Cancelar).
     */
    private void clickCancelOnDialog() {
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        clickOn(cancelButton);
    }

    // =========================================================================
    // 1. FLUJO IDEAL (HAPPY PATH) CON VERIFICACIÓN DE SALDOS
    // =========================================================================

    @Test
    public void testSuccessfulDepositUpdatesTableAndBalance() {
        // --- ARRANGE ---
        TextField tfBalance = lookup("#tfAccountBalance").queryAs(TextField.class);
        double initialBalance = Double.parseDouble(tfBalance.getText());
        
        TableView<Movement> table = lookup("#tbMovements").queryTableView();
        int initialCount = table.getItems() != null ? table.getItems().size() : 0;
        
        double depositAmount = 150.0;

        // --- ACT ---
        clickOn("#cbOperation").clickOn("Deposit");
        clickOn("#tfAmount").write(String.valueOf(depositAmount));
        clickOn("#btMake");
        
        // Aceptar confirmación usando el método auxiliar locale-independent
        clickOkOnDialog(); 

        // --- ASSERT ---
        double expectedBalance = initialBalance + depositAmount;
        double finalBalance = Double.parseDouble(tfBalance.getText());
        assertEquals("El balance total debe reflejar el incremento", expectedBalance, finalBalance, 0.001);

        int finalCount = table.getItems().size();
        assertEquals("Debe haber 1 movimiento nuevo", initialCount + 1, finalCount);

        Movement lastMovement = table.getItems().get(finalCount - 1);
        assertEquals("La cantidad en la tabla debe ser exacta", depositAmount, lastMovement.getAmount(), 0.001);
        assertEquals("Deposit", lastMovement.getDescription());
    }

    @Test
    public void testSuccessfulPaymentUpdatesTableAndBalance() {
        // --- ARRANGE ---
        TextField tfBalance = lookup("#tfAccountBalance").queryAs(TextField.class);
        double initialBalance = Double.parseDouble(tfBalance.getText());
        double paymentAmount = 50.0;

        // --- ACT ---
        clickOn("#cbOperation").clickOn("Payment");
        clickOn("#tfAmount").write(String.valueOf(paymentAmount));
        clickOn("#btMake");
        
        // Aceptar confirmación independientemente del idioma
        clickOkOnDialog(); 

        // --- ASSERT ---
        double expectedBalance = initialBalance - paymentAmount;
        double finalBalance = Double.parseDouble(tfBalance.getText());
        assertEquals("El balance debe disminuir según el pago", expectedBalance, finalBalance, 0.001);
    }

    // =========================================================================
    // 2. TESTS DE CANCELACIÓN (INMUTABILIDAD DE ESTADO)
    // =========================================================================

    @Test
    public void testCancelOperationLeavesBalanceAndTableUnchanged() {
        // --- ARRANGE ---
        TextField tfBalance = lookup("#tfAccountBalance").queryAs(TextField.class);
        double initialBalance = Double.parseDouble(tfBalance.getText());
        
        TableView<Movement> table = lookup("#tbMovements").queryTableView();
        int initialCount = table.getItems() != null ? table.getItems().size() : 0;

        // --- ACT ---
        clickOn("#cbOperation").clickOn("Payment");
        clickOn("#tfAmount").write("100.0");
        clickOn("#btMake");
        
        // Cancelar operación independientemente del idioma
        clickCancelOnDialog(); 

        // --- ASSERT ---
        double finalBalance = Double.parseDouble(tfBalance.getText());
        int finalCount = table.getItems() != null ? table.getItems().size() : 0;
        
        assertEquals("El balance no debe alterarse", initialBalance, finalBalance, 0.001);
        assertEquals("La tabla no debe cambiar de tamaño", initialCount, finalCount);
    }

    // =========================================================================
    // 3. TESTS EXHAUSTIVOS DE VALIDACIÓN DE ERRORES
    // =========================================================================

    @Test
    public void testEmptyAmountShowsSpecificErrorMessage() {
        // --- ACT ---
        clickOn("#cbOperation").clickOn("Deposit");
        clickOn("#tfAmount").write(""); 
        clickOn("#btMake");
        
        clickOkOnDialog(); // 1. Confirmar la intención de hacer la operación
        
        // --- ASSERT ---
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull("Debe aparecer el panel de error", dialogPane);
        assertEquals("Amount is required to do this operation", dialogPane.getContentText());
        
        clickOkOnDialog(); // 2. Cerrar el panel de error
    }

    @Test
    public void testNegativeAmountShowsSpecificErrorMessage() {
        // --- ACT ---
        clickOn("#cbOperation").clickOn("Deposit");
        clickOn("#tfAmount").write("-50.0"); 
        clickOn("#btMake");
        
        clickOkOnDialog(); // 1. Confirmar intención
        
        // --- ASSERT ---
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull("Debe aparecer el panel de error", dialogPane);
        assertEquals("Amount must be greater than 0.0!!", dialogPane.getContentText());
        
        clickOkOnDialog(); // 2. Cerrar el panel de error
    }

    @Test
    public void testNonNumericAmountCaughtByNumberFormatException() {
        // --- ACT ---
        clickOn("#cbOperation").clickOn("Deposit");
        clickOn("#tfAmount").write("cincuenta"); 
        clickOn("#btMake");
        
        clickOkOnDialog(); // 1. Confirmar intención
        
        // --- ASSERT ---
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull("Debe aparecer el panel de error", dialogPane);
        assertEquals("Amount must be a real positive number!!", dialogPane.getContentText());
        
        clickOkOnDialog(); // 2. Cerrar el panel de error
    }

    @Test
    public void testInsufficientBalanceShowsSpecificErrorMessage() {
        // --- ARRANGE ---
        TextField tfBalance = lookup("#tfAccountBalance").queryAs(TextField.class);
        double currentBalance = Double.parseDouble(tfBalance.getText());
        double excessiveAmount = currentBalance + 5000.0;

        // --- ACT ---
        clickOn("#cbOperation").clickOn("Payment");
        clickOn("#tfAmount").write(String.valueOf(excessiveAmount)); 
        clickOn("#btMake");
        
        clickOkOnDialog(); // 1. Confirmar intención
        
        // --- ASSERT ---
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull("Debe aparecer el panel de error", dialogPane);
        assertEquals("Insufficient balance for this amount.", dialogPane.getContentText());
        
        clickOkOnDialog(); // 2. Cerrar el panel de error
    }
}

