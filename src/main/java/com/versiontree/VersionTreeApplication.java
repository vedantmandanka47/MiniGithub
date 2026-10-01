package com.versiontree;

// SYLLABUS: Servlet - ServletComponentScan for discovering WebServlet annotations
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.versiontree.servlet")
public class VersionTreeApplication {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("   Starting Version Tree (VT) — Mini-GitHub Academic Platform   ");
        System.out.println("   Course: Advanced Java Programming (102045605)                ");
        System.out.println("   Running Web Application Server on Port: 8082                  ");
        System.out.println("=================================================================");

        SpringApplication.run(VersionTreeApplication.class, args);
    }
}
