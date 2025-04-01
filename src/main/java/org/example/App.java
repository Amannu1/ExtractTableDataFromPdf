package org.example;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;
import java.io.FileWriter;
import java.io.IOException;

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

        FileWriter fw = new FileWriter("ExtractTable.csv");
        fw.write(builder.toString());
        fw.flush();
        fw.close();
    }
}
