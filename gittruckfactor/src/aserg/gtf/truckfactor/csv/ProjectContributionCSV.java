package aserg.gtf.truckfactor.csv;

import aserg.gtf.model.authorship.Developer;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.Repository;
import aserg.gtf.truckfactor.TFInfo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ProjectContributionCSV extends GenerateCSVTemplate {

    private TFInfo tfInfo;
    private Repository repository;

    public ProjectContributionCSV(Repository repository, TFInfo tfInfo) {
        this.repository = repository;
        this.tfInfo = tfInfo;
    }

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Developer> developers = tfInfo.getTfDevelopers();

        for (Developer developer : developers) {
            String gitUser = developer.getName();
            int gitUserFilesSize = developer.getAuthorshipFiles().size();
            float contribution = (float) gitUserFilesSize / (float) tfInfo.getTotalFiles() * 100;

            writer.append(repository.getFullName()).append(",")
                    .append("\"").append(gitUser).append("\"").append(",")
                    .append(String.format(Locale.US, "%.2f", contribution)).append(",")
                    .append(LocalDateTime.now().format(formatter)).append("\n");
        }
    }
}
