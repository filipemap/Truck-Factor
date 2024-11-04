package aserg.gtf.truckfactor.csv;

import aserg.gtf.model.authorship.AuthorshipInfo;
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

public class ProjectFileContributionCSV extends GenerateCSVTemplate {

    private TFInfo tfInfo;
    private Repository repository;

    public ProjectFileContributionCSV(Repository repository, TFInfo tfInfo) {
        this.tfInfo = tfInfo;
        this.repository = repository;
    }

    @Override
    protected String[] getHeader() {
        return new String[]{"project", "git-user", "file", "contribution-on-file (%)", "timestamp"};
    }

    @Override
    protected String getFileName() {
        return "project_file_contribution.csv";
    }

    @Override
    protected void writeContent(FileWriter writer) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (File file : repository.getFiles()) {
            List<AuthorshipInfo> authorships = file.getAuthorshipInfos();
            double totalFileDOA = authorships.stream().mapToDouble(AuthorshipInfo::getDoaMultAuthor).sum();

            for (AuthorshipInfo authorship : authorships) {
                Developer developer = authorship.getDeveloper();
                String gitUser = developer.getName();
                double developerDOA = authorship.getDoaMultAuthor();
                double contribution = (developerDOA / totalFileDOA) * 100;

                writer.append(repository.getFullName()).append(",")
                        .append("\"").append(gitUser).append("\"").append(",")
                        .append(file.getPath()).append(",")
                        .append(String.format(Locale.US, "%.2f", contribution)).append(",")
                        .append(LocalDateTime.now().format(formatter)).append("\n");
            }
        }
    }
}
