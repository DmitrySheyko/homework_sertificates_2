package com.example.homework_sertificates_2.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyMetricsCustomBean implements CommandLineRunner {

    @Autowired
    private CertificateServiceImpl service;
    private MultiGauge lowInventoryCounts;

    public MyMetricsCustomBean(MeterRegistry registry) {
        lowInventoryCounts = MultiGauge.builder("certificates")
                .tag("Current date", LocalDate.now().toString())
                .register(registry);
    }

    @Override
    public void run(String... args) {
        updateLowInventoryGauges();
    }

    public void updateLowInventoryGauges() {
        boolean overWrite = true;
        List<CertificateInfo> info = service.getCertificates();

        lowInventoryCounts.register(
                info.stream()
                        .map((CertificateInfo c) -> MultiGauge.Row.of(Tags.of("cert_name", c.getName()), c.getDuration()))
                        .collect(Collectors.toList())
                , overWrite);
    }

}