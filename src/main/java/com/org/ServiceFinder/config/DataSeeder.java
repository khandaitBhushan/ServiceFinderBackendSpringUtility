package com.org.ServiceFinder.config;

import com.org.ServiceFinder.model.*;
import com.org.ServiceFinder.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ServiceCategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final ServiceProviderRepository providerRepo;
    private final ProviderServiceRepository providerServiceRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(ServiceCategoryRepository categoryRepo, UserRepository userRepo, 
                      ServiceProviderRepository providerRepo, ProviderServiceRepository providerServiceRepo,
                      PasswordEncoder passwordEncoder) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.providerRepo = providerRepo;
        this.providerServiceRepo = providerServiceRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        List<ServiceCategory> categories;

        if (categoryRepo.count() == 0) {
            System.out.println("Seeding default categories...");
            ServiceCategory cat1 = new ServiceCategory(null, "Electrician");
            ServiceCategory cat2 = new ServiceCategory(null, "Plumber");
            ServiceCategory cat3 = new ServiceCategory(null, "Carpenter");
            ServiceCategory cat4 = new ServiceCategory(null, "AC Repair");
            ServiceCategory cat5 = new ServiceCategory(null, "Painter");
            categories = categoryRepo.saveAll(List.of(cat1, cat2, cat3, cat4, cat5));
        } else {
            categories = categoryRepo.findAll();
        }

        if (providerRepo.count() < 7 && categories.size() >= 3) {
            System.out.println("Seeding diverse area providers and slots...");
            String sharedPassword = passwordEncoder.encode("pass@123");

            // Users (Providers) located across Lokmanya, Bardi(Sitabuldi), Jaytala, Mihan, Nandanwan!
            User u1 = new User(null, "Central Electric", "ramesh@test.com", sharedPassword, "9876543210", Role.PROVIDER, "Nagpur Central", 21.1458, 79.0882);
            User u2 = new User(null, "Bardi Plumbing", "suresh@test.com", sharedPassword, "9876543211", Role.PROVIDER, "Sitabuldi (Bardi)", 21.1400, 79.0800);
            User u3 = new User(null, "Dharampeth Carpentry", "prakash@test.com", sharedPassword, "9876543212", Role.PROVIDER, "Dharampeth", 21.1350, 79.0700);
            User u4 = new User(null, "Lokmanya AC", "mangesh@test.com", sharedPassword, "9876543213", Role.PROVIDER, "Lokmanya Nagar", 21.1215, 79.0345);
            User u5 = new User(null, "Jaytala Painters", "kiran@test.com", sharedPassword, "9876543214", Role.PROVIDER, "Jaytala", 21.1070, 79.0180);
            User u6 = new User(null, "Mihan Electric", "vikas@test.com", sharedPassword, "9876543215", Role.PROVIDER, "Mihan Area", 21.0180, 79.0280);
            User u7 = new User(null, "Nandanwan Woodworks", "rohit@test.com", sharedPassword, "9876543216", Role.PROVIDER, "Nandanwan", 21.1265, 79.1170);
            List<User> savedUsers = userRepo.saveAll(List.of(u1, u2, u3, u4, u5, u6, u7));

            // Service Providers
            ServiceProvider sp1 = new ServiceProvider(null, "Central Electronics", categories.stream().filter(c->c.getName().equals("Electrician")).findFirst().orElse(categories.get(0)), "Expert wiring", 5, 4.8, "Nagpur Central", 21.1458, 79.0882, 200.0, 100.0, "", savedUsers.get(0), null);
            ServiceProvider sp2 = new ServiceProvider(null, "Sitabuldi (Bardi) Pipes", categories.stream().filter(c->c.getName().equals("Plumber")).findFirst().orElse(categories.get(1)), "Leakages", 8, 4.5, "Sitabuldi, Nagpur", 21.1400, 79.0800, 300.0, 150.0, "", savedUsers.get(1), null);
            ServiceProvider sp3 = new ServiceProvider(null, "Dharampeth Carpentry", categories.stream().filter(c->c.getName().equals("Carpenter")).findFirst().orElse(categories.get(2)), "Furniture repair", 3, 4.2, "Dharampeth, Nagpur", 21.1350, 79.0700, 250.0, 200.0, "", savedUsers.get(2), null);
            ServiceProvider sp4 = new ServiceProvider(null, "Lokmanya AC Services", categories.stream().filter(c->c.getName().equals("AC Repair")).findFirst().orElse(categories.get(3)), "Cooling fix", 6, 4.9, "Lokmanya Nagar, Nagpur", 21.1215, 79.0345, 400.0, 200.0, "", savedUsers.get(3), null);
            ServiceProvider sp5 = new ServiceProvider(null, "Jaytala Pro Painters", categories.stream().filter(c->c.getName().equals("Painter")).findFirst().orElse(categories.get(4)), "Wall painting", 10, 4.6, "Jaytala, Nagpur", 21.1070, 79.0180, 500.0, 300.0, "", savedUsers.get(4), null);
            ServiceProvider sp6 = new ServiceProvider(null, "Mihan Power Systems", categories.stream().filter(c->c.getName().equals("Electrician")).findFirst().orElse(categories.get(0)), "Industrial wiring", 12, 4.7, "Mihan Area, Nagpur", 21.0180, 79.0280, 450.0, 200.0, "", savedUsers.get(5), null);
            ServiceProvider sp7 = new ServiceProvider(null, "Nandanwan Carpenter Hub", categories.stream().filter(c->c.getName().equals("Carpenter")).findFirst().orElse(categories.get(2)), "Shed making", 4, 4.1, "Nandanwan, Nagpur", 21.1265, 79.1170, 200.0, 100.0, "", savedUsers.get(6), null);
            List<ServiceProvider> savedProviders = providerRepo.saveAll(List.of(sp1, sp2, sp3, sp4, sp5, sp6, sp7));

            // Specific Services/Slots
            ProviderService ps1 = new ProviderService(null, "Switch Fix", "Standard switch", 150.0, 30, true, savedProviders.get(0));
            ProviderService ps2 = new ProviderService(null, "Tap Leak Fix", "Standard tap", 200.0, 45, true, savedProviders.get(1));
            ProviderService ps3 = new ProviderService(null, "Door Check", "Hinges fix", 300.0, 45, true, savedProviders.get(2));
            ProviderService ps4 = new ProviderService(null, "AC Gas Check", "Freon refill", 600.0, 90, true, savedProviders.get(3));
            ProviderService ps5 = new ProviderService(null, "Room Painting", "10x10 Room", 2000.0, 240, true, savedProviders.get(4));
            ProviderService ps6 = new ProviderService(null, "High Volt Check", "Transformer side", 1000.0, 120, true, savedProviders.get(5));
            ProviderService ps7 = new ProviderService(null, "Wooden Bed Polish", "Polish works", 800.0, 180, true, savedProviders.get(6));

            providerServiceRepo.saveAll(List.of(ps1, ps2, ps3, ps4, ps5, ps6, ps7));
            System.out.println("Mock Generation Complete!");
        }
    }
}
