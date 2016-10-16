
package view.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Class CBN_MRP
 * @author Epulapp
 */
public class CalculBesoinNet extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene startScene = new Scene(root, 400, 300); 
        
        primaryStage.setTitle("CBN - MRP");
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0); // Close all process
        });
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
    
    public void readCBNFile(String fileName)
    {
        try {
            if(!(new File(fileName).exists())) {
                System.err.println("Le fichier "+fileName+" n'existe pas.");
            }
            
            FileInputStream fis = new FileInputStream(new File("exemple_calcul_besoin.xlsx"));
            
            // Component HSSF pour fichiers Excel XLS
            // Component XSSF pour fichiers Excel XLSX
            // Component "Common SS" pour fichiers Excel XLS et XLSX
            // Quel component prendre ?
            
            // A Workbook instance that refers to .xls and .xlsx files
            Workbook wb = WorkbookFactory.create(fis);
            
            // A sheet instance which retrieve first sheet
            Sheet nomSheet = wb.getSheetAt(0); // Nomenclature
            Sheet paramSheet = wb.getSheetAt(1); // Paramètres
            Sheet besSheet = wb.getSheetAt(2); // Besoins en Prévisions & Commandes
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CalculBesoinNet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Le chemin de fichier donné n'est pas valide.");
        } catch (IOException ex) {
            Logger.getLogger(CalculBesoinNet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(CalculBesoinNet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(CalculBesoinNet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
