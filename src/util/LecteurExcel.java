package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mélanie DUBREUIL
 * @author Ophélie EOUZAN
 * 
 */

public class LecteurExcel {
    
    public int retournerNbSemaines(String excelUrl){
        int nbSemaines = 0;
        
        try {
            FileInputStream fis = null;
            File myFile = new File(excelUrl);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(2);
            nbSemaines = mySheet.getRow(1).getPhysicalNumberOfCells() - 1;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nbSemaines;
    }
    
    public void creerArticles(String excelUrl, Nomenclature n){
       try {
            FileInputStream fis = null;
            File myFile = new File(excelUrl);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(1);
            boolean check = false;
            
            Iterator <Row> rowIterator = mySheet.iterator();
            while (rowIterator.hasNext()) {
                Article a = new Article();
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex()){
                        case 0:
                            if(check){
                                a.setCodeArticle(cell.getStringCellValue()); 
                            }
                        break;
                        case 1: 
                            if(check){
                                a.setTypeGestion(cell.getStringCellValue()); 
                            }
                        break;
                        case 2:
                            if(check){
                                a.setQuantitéLancement(cell.getNumericCellValue()); 
                            }
                        break;
                        case 3: 
                            if(check){
                                a.setStockInitial(cell.getNumericCellValue());
                            }
                        break;
                        case 4: 
                            if(check){
                                a.setStockSécurité(cell.getNumericCellValue());
                            }
                        break;
                        case 6: 
                            if(check){
                                a.setDélai(cell.getNumericCellValue());
                            }
                        break;
                        default :
                    }
                }
                n.ajouterArticle(a);
                check = true;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public void creerLiens(String excelUrl, Nomenclature n){
        try {
            FileInputStream fis = null;
            File myFile = new File(excelUrl);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator <Row> rowIterator = mySheet.iterator();
            boolean check = false;
            
            while (rowIterator.hasNext()) {
                Lien l = new Lien();
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex()) {
                        case 0:
                            if (check){
                                l.setComposé(n.rechercheArticleParCode(cell.getStringCellValue()));   
                            }
                        break;
                        case 1:
                            if (check){
                                l.setComposant(n.rechercheArticleParCode(cell.getStringCellValue()));  
                            }
                        break;
                        case 2:
                            if (check){
                               l.setCoefficientLien((double)cell.getNumericCellValue()); 
                            }
                        break;
                        default :
                    }
                }
                n.ajouterLien(l);
                check = true;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public void initialisationBesoinsBruts(String excelUrl, Nomenclature n){
        try {
            FileInputStream fis = null;
            File myFile = new File(excelUrl);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(2);
            Iterator <Row> rowIterator = mySheet.iterator();
            boolean check = false;
            Article articleActuel = new Article();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex()) {
                        case 0:
                            if (check){
                                articleActuel = n.rechercheArticleParCode(cell.getStringCellValue());
                            }
                        break;
                        default :
                            if (check){
                                articleActuel.calculBesoinBrut(cell.getColumnIndex(), cell.getNumericCellValue());
                            }
                    }
                }
                check = true;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public void calculerArrivéesPrévues(String excelUrl, Nomenclature n){
       try {
            FileInputStream fis = null;
            File myFile = new File(excelUrl);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(1);
            boolean check = false;
            Article articleActuel = new Article();
            
            Iterator <Row> rowIterator = mySheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex()){
                        case 0:
                            if (check){
                                articleActuel = n.rechercheArticleParCode(cell.getStringCellValue());
                            }
                        break;
                        case 7: 
                            if(check && (cell.getNumericCellValue() != 0)){
                                articleActuel.getBesoinNet(1).setLivraison(cell.getNumericCellValue());
                            }
                        break;
                        case 8:
                            if(check && (cell.getNumericCellValue() != 0)){
                                articleActuel.getBesoinNet(2).setLivraison(cell.getNumericCellValue());
                            }
                        break;
                        default :
                    }
                }
                check = true;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LecteurExcel.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
}
