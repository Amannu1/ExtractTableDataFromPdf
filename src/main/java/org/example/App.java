package org.example;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class App {
    public static void main(String[] args) throws IOException {
        PdfDocument pdf = new PdfDocument("Anexo.pdf");
        StringBuilder builder = new StringBuilder();
        PdfTableExtractor extractor = new PdfTableExtractor(pdf);

        for (int pageIndex = 0; pageIndex < pdf.getPages().getCount(); pageIndex++) {
            PdfTable[] tableLists = extractor.extractTable(pageIndex);

            if (tableLists != null && tableLists.length > 0) {
                for (PdfTable table : tableLists) {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        for (int j = 0; j < table.getColumnCount(); j++) {
                            String text = table.getText(i, j).replace("\n", " ").replace("\r", " ").trim();
                            builder.append("\"").append(text).append("\"");
                            if (j < table.getColumnCount() - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("\r\n");
                    }
                }
            }
        }

        String zipFileName = "ExtractTable.csv";
        FileWriter fw = new FileWriter(zipFileName);
        fw.write(builder.toString());
        fw.flush();
        fw.close();

        FileOutputStream fos = new FileOutputStream("Data.zip");
        ZipOutputStream zs = new ZipOutputStream(fos);

        FileInputStream fis = new FileInputStream(zipFileName);
        ZipEntry entry = new ZipEntry(zipFileName);
        zs.putNextEntry(entry);

        int length;
        byte[] buffer = new byte[1024];

        while((length = fis.read(buffer)) > 0){
            zs.write(buffer, 0, length);
        }
        fis.close();
        zs.closeEntry();
        zs.close();
    }

}
