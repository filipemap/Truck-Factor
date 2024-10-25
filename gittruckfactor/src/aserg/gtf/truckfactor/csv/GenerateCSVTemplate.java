package aserg.gtf.truckfactor.csv;

import aserg.gtf.model.authorship.Repository;
import aserg.gtf.truckfactor.TFInfo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class GenerateCSVTemplate {

    protected abstract String[] getHeader();
    protected abstract String getFileName();
    protected abstract void writeContent(FileWriter writer) throws IOException;

    // Template Method Pattern
    public final void generateCSV() {
        try (FileWriter writer = new FileWriter(getFileName())) {
            for (String column : getHeader()) {
                writer.append(column).append(",");
            }
            writer.append("\n");

            // Template Method
            writeContent(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
