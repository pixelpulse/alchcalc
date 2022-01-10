package com.alchcalc;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Float.parseFloat;

public class IngredientHandler {
    List<Ingredient> ingredientList = new ArrayList<>();

    public void mapIngredientFile() throws IOException {
        FileInputStream file = new FileInputStream(new File("src/main/java/com/alchcalc/Ingredients.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Iterator<Row> iterator = workbook.getSheetAt(0).iterator();

        while (iterator.hasNext()){
            Ingredient ingredient = new Ingredient();
            Row currentRow = iterator.next();

            if (currentRow.getRowNum() == 0) {
                continue;
            }

            Iterator<Cell> cellIterator = currentRow.iterator();

            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                CellAddress address = currentCell.getAddress();

                if (0 == address.getColumn()) {
                    // 1st col is "name"
                    ingredient.setName(currentCell.getStringCellValue());
                } else if (1 == address.getColumn()) {
                    // 2nd col is "isEnabled"
                    ingredient.setEnabled(currentCell.getBooleanCellValue());
                }
                else if (2 == address.getColumn()) {
                    // 3rd col is "isAdder"
                    ingredient.setAdder(currentCell.getBooleanCellValue());
                } else if (3 == address.getColumn()) {
                    // 4th col is "DH"
                    try{
                        ingredient.setDH(currentCell.getNumericCellValue());
                    }
                    catch (Exception e){
                        ingredient.setDH(0.0);
                    }
                } else if (4 == address.getColumn()) {
                    // 5th col is "DHM"
                    try{
                        ingredient.setDHM(currentCell.getNumericCellValue());
                    }
                    catch (Exception e){
                        ingredient.setDHM(0.0);
                    }
                } else if (5 == address.getColumn()) {
                    // 6th col is "DP"
                    try{
                        ingredient.setDP(currentCell.getNumericCellValue());
                    }
                    catch (Exception e){
                        ingredient.setDP(0.0);
                    }
                } else if (6 == address.getColumn()) {
                    // 7th col is "DPM"
                    try{
                        ingredient.setDPM(currentCell.getNumericCellValue());
                    }
                    catch (Exception e){
                        ingredient.setDPM(0.0);
                    }
                }
            }
            if(ingredient.getEnabled()){
                ingredientList.add(ingredient);
            }
        }
    }

    public List<Ingredient> getIngredientList() {
        try{
            mapIngredientFile();
        }
        catch(Exception e){
            System.out.println(e);
        }
        return ingredientList;
    }
}
