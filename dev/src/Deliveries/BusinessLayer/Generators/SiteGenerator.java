package Deliveries.BusinessLayer.Generators;

import Deliveries.BusinessLayer.Site;
import Deliveries.DataAccessLayer.SiteDAO;

import java.util.ArrayList;
import java.util.List;

public class SiteGenerator {
    private static final String[] SITE_NAMES = {"Tel Aviv", "Rishon Le Zion", "Raanana",  "Haifa",
            "Beer Sheva",  "Ashkelon", "Ashdod", "Eilat"};
    private static final String[] CONTACT_NAMES = {"Peleg", "Tomer", "Tamar", "Gili", "Itay", "Reut", "Lior", "Noa"};
    private List<Site> sitesList;

    public SiteDAO siteDAO;
    public SiteGenerator(){
        siteDAO = new SiteDAO();
        sitesList = siteDAO.loadData();
        if (sitesList.isEmpty()){
            sitesList = generateSites();
        }
    }

    private List<Site> generateSites() {
        List<Site> sites = new ArrayList<>();
        for (int i = 0; i < SITE_NAMES.length; i++) {
            String name = SITE_NAMES[i];
            String address = "Ben Gurion St " + (2*i + 1);
            String contactName = CONTACT_NAMES[i];
            String contactPhone = "054-123456" + i;
            int deliveryZone;
            if (name.equals("Haifa")) {
                deliveryZone = 1;
            } else if (name.equals("Rishon Le Zion") || name.equals("Raanana") || name.equals("Tel Aviv")) {
                deliveryZone = 2;
            } else if (name.equals("Ashkelon") || name.equals("Ashdod") ||
                    name.equals("Eilat") || name.equals("Beer Sheva")) {
                deliveryZone = 3;
            } else {
                deliveryZone = 4;
            }
            Site site = new Site(name, address, contactName, contactPhone, deliveryZone);
            sites.add(site);
            siteDAO.addSite(site);

        }
        return sites;
    }

    public List<Site> getSitesList() {
        return sitesList;
    }
}
