package logic;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Конвертер excel файла в текстовый
 * Created by psameyko on 27.11.15.
 */
public class Converter {

    Logger logger = Logger.getLogger(Converter.class);
    private String sourceFile;
    private File targetFile;
    private Config config;

    public Converter setSource(String file) {
        this.sourceFile = file;
        return this;
    }

    public Converter setTarget(String file) {
        this.targetFile = new File(file);
        return this;
    }

    public Converter setConfig(Config conf) {
        this.config = conf;
        return this;
    }

    /**
     * Выполняет конвертацию
     *
     * @throws IOException
     */
    public void convert() throws Exception {
        List<HSSFSheet> sheets = loadExcelFileSheets();
        List<String> convertedSheet = convertSheet(sheets.get(0));
        printToFile(convertedSheet);
    }

    private void printToFile(List<String> convertedSheet) throws Exception {
        try {
            PrintWriter writer = new PrintWriter((targetFile));
            for (String str : convertedSheet) {
                writer.println(str);
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new Exception("Ошибка сохранения данных в файл");
        }
    }

    private List<String> convertSheet(HSSFSheet rows) throws Exception {
        int rowCount = rows.getPhysicalNumberOfRows();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            StringBuilder builder = new StringBuilder();
            builder.append(getPrefix(i));
            builder.append(",");
            convertRow(rows.getRow(i), builder, i);
            result.add(builder.toString());
        }
        return result;
    }

    private String getPrefix(int i) {
        String zeroes = "";
        int digits = Integer.valueOf(config.getProperty(Config.PREFIX_DIGIT_COUNT));
        int zeroCount = digits - String.valueOf(i).length();
        for (int j = 0; j < zeroCount; j++) {
            zeroes += "0";
        }
        int ss = Integer.parseInt(config.getProperty(Config.START_NUMBER)) + i;
        return "\"" + zeroes + ss + config.getProperty(Config.PREFIX_TEXT) + "\"";
    }

    private List<HSSFSheet> loadExcelFileSheets() throws Exception {
        POIFSFileSystem pfs;
        HSSFWorkbook wb;
        try {
            pfs = new POIFSFileSystem(new File(sourceFile));
            wb = new HSSFWorkbook(pfs);
        } catch (Exception e) {
            logger.error("Ошибка Чтения файла", e);
            throw new Exception("Ошибка чтения  Excel файла");
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        List<HSSFSheet> list = new ArrayList<>();
        list.add(sheet);
        return list;
    }

    private void convertRow(HSSFRow excelRow, StringBuilder builder, int rowNum) throws Exception {
        for (int i = 0; i < excelRow.getPhysicalNumberOfCells(); i++) {
            if (i > 0) {
                builder.append(",");
            }
            try {
                builder.append(convertCellValue(excelRow.getCell(i)));
            } catch (Exception e) {
                logger.error(e);
                throw new Exception("Ошибка парсинга. Строка: " + rowNum + ", Столбец: " + i);
            }
        }
    }

    private String convertCellValue(HSSFCell cell) {
        String cellValue;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                cellValue = castNumericCellValue(cell.getNumericCellValue());
                break;
            default:
                cellValue = "\"" + cell.getStringCellValue() + "\"";
                break;
        }
        return cellValue;
    }

    private String castNumericCellValue(double numericCellValue) {
        String val = String.valueOf(numericCellValue);
        int dotIndex = val.indexOf(".");
        int semIndex = val.indexOf(",");
        if (dotIndex != -1 && semIndex != -1) {
            return val;
        }
        if (dotIndex != -1) {
            String d = val.substring(dotIndex + 1);
            if (Integer.valueOf(d) == 0) {
                return val.substring(0, dotIndex);
            } else {
                return val;
            }
        }
        if (semIndex != -1) {
            String d = val.substring(semIndex + 1);
            if (Integer.valueOf(d) == 0) {
                return val.substring(0, semIndex);
            } else {
                return val.replace(",", ".");
            }
        }
        return null;
    }
}
