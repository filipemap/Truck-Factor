package aserg.gtf.truckfactor.csv;

import java.io.FileWriter;
import java.io.IOException;

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
