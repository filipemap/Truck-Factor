package aserg.gtf.truckfactor.csv;

import java.io.FileWriter;
import java.io.IOException;

public class ProjectContributionCSV extends GenerateCSVTemplate {
    @Override
    protected String[] getHeader() {
        return new String[]{"project", "git-user", "contribution (%)", "timestamp"};
    }

    @Override
    protected String getFileName() {
        return "project_contribution.csv";
    }

    @Override
    protected void writeContent(FileWriter writer) throws IOException {

    }
}
