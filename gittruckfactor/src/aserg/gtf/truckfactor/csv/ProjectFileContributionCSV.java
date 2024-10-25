package aserg.gtf.truckfactor.csv;

import java.io.FileWriter;
import java.io.IOException;

public class ProjectFileContributionCSV extends GenerateCSVTemplate {
    @Override
    protected String[] getHeader() {
        return new String[]{"project", "file", "git-user", "contribution-on-file (%)", "timestamp"};
    }

    @Override
    protected String getFileName() {
        return "project_file_contribution.csv";
    }

    @Override
    protected void writeContent(FileWriter writer) throws IOException {

    }
}
