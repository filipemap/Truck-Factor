package aserg.gtf.truckfactor.csv;

import aserg.gtf.model.authorship.Developer;
import aserg.gtf.model.authorship.Repository;
import aserg.gtf.truckfactor.TFInfo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProjectTruckFactorCSV extends GenerateCSVTemplate {

    private Repository repository;
    private TFInfo tfInfo;
    private List<Pair> authorWithContributionList;

    public ProjectTruckFactorCSV(Repository repository, TFInfo tfInfo) {
        this.repository = repository;
        this.tfInfo = tfInfo;
        this.authorWithContributionList = new ArrayList<Pair>();
    }

    @Override
    protected String[] getHeader() {
        return new String[]{"project", "truck-factor", "author + total contribution (%)", "timestamp"};
    }

    @Override
    protected String getFileName() {
        return "project_truck-factor.csv";
    }

    @Override
    protected void writeContent(FileWriter writer) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Developer> developers = tfInfo.getTfDevelopers();

        for (Developer developer : developers) {
            int devFiles = developer.getAuthorshipFiles().size();
            float contribution = (float) devFiles / tfInfo.getTotalFiles() * 100;
            String formattedContribution = String.format(Locale.US, "%.2f", contribution);

            Pair pair = new Pair(developer.getName(), formattedContribution);
            authorWithContributionList.add(pair);
        }

        writer.append(repository.getFullName()).append(",")
                .append(String.format("%d", tfInfo.getTf())).append(",")
                .append("\"").append(String.format("%s", authorWithContributionList)).append("\"").append(",")
                .append(LocalDateTime.now().format(formatter)).append("\n");
    }
}
