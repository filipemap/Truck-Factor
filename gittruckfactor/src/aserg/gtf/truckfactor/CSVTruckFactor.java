package aserg.gtf.truckfactor;

import aserg.gtf.model.authorship.AuthorshipInfo;
import aserg.gtf.model.authorship.Developer;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.Repository;
import aserg.gtf.truckfactor.TFInfo;
import aserg.gtf.truckfactor.TruckFactor;
import aserg.gtf.truckfactor.csv.GenerateCSVTemplate;
import aserg.gtf.truckfactor.csv.ProjectContributionCSV;
import aserg.gtf.truckfactor.csv.ProjectTruckFactorCSV;
import org.apache.log4j.Logger;

import java.util.*;

public class CSVTruckFactor extends TruckFactor {
    private static final Logger LOGGER = Logger.getLogger(TruckFactor.class);

    private TFInfo tfInfo = new TFInfo();
    private float minPercentage;

    public CSVTruckFactor(float minPercentage) {
        this.minPercentage = minPercentage;
    }

    @Override
    public TFInfo getTruckFactor(Repository repository) {
        Map<Developer, Set<File>> authorsMap = getFilesAuthorMap(repository);

        int repFilesSize = repository.getFiles().size();
        int factor = 0;
        float coverage = 1;
        while (authorsMap.size() > 0) {
            removeTopAuthor(repFilesSize, authorsMap);
            factor++;
        }
        tfInfo.setCoverage(getCoverage(repFilesSize, authorsMap));
        tfInfo.setTf(factor);
        tfInfo.setTotalFiles(repFilesSize);

        // firstCSV is on "PrunedGreedyTruckFactor" class.
        GenerateCSVTemplate secondCSV = new ProjectContributionCSV(repository, tfInfo);
        secondCSV.generateCSV();

        return pruneTF(tfInfo);
    }

    private TFInfo pruneTF(TFInfo tfInfo) {
        return tfInfo;
    }

    private Map<Developer, Set<File>> getFilesAuthorMap(Repository repository) {
        Map<Developer, Set<File>> map = new HashMap<Developer, Set<File>>();
        List<Developer> developers = repository.getDevelopers();
        for (Developer developer : developers) {
            Set<File> devFiles = new HashSet<File>();
            List<AuthorshipInfo> authorships = developer.getAuthorshipInfos();
            for (AuthorshipInfo authorshipInfo : authorships) {
                if (authorshipInfo.isDOAAuthor())
                    devFiles.add(authorshipInfo.getFile());

            }
            if (devFiles.size() > 0)
                map.put(developer, devFiles);
        }
        return map;
    }

    private float getCoverage(int repFilesSize, Map<Developer, Set<File>> authorsMap) {
        Set<File> authorsSet = new HashSet<File>();
        for (Map.Entry<Developer, Set<File>> entry : authorsMap.entrySet()) {
            for (File file : entry.getValue()) {
                authorsSet.add(file);
                if (authorsSet.size() == repFilesSize)
                    return 1f;
            }
        }
        return (float) authorsSet.size() / repFilesSize;
    }

    private void removeTopAuthor(int repFilesSize, Map<Developer, Set<File>> authorsMap) {
        int biggerNumber = 0;
        Developer biggerDev = null;
        for (Map.Entry<Developer, Set<File>> entry : authorsMap.entrySet()) {
            if (entry.getValue().size() > biggerNumber) {
                biggerNumber = entry.getValue().size();
                biggerDev = entry.getKey();
            }
            if (biggerDev != null && entry.getValue().size() == biggerNumber)
                if (entry.getKey().getDevChanges() > biggerDev.getDevChanges())
                    biggerDev = entry.getKey();


        }
        tfInfo.addDeveloper(biggerDev);
        authorsMap.remove(biggerDev);
    }
}
