package com.example.DoencaChagas.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioService {

    @Autowired
    private DataSource dataSource;

    public byte[] gerarPDF(
            String nomeRelatorio,
            Map<String, Object> parametros
    ) throws Exception {

        String subreportJrxml =
                getClass()
                        .getResource("/jasper/pacientes_subreport.jrxml")
                        .getPath();

        String subreportJasper =
                getClass()
                        .getResource("/jasper/")
                        .getPath()
                        + "pacientes_subreport.jasper";

        JasperCompileManager.compileReportToFile(
                subreportJrxml,
                subreportJasper
        );

        InputStream inputStream =
                getClass().getResourceAsStream(
                        "/jasper/" + nomeRelatorio + ".jrxml"
                );

        JasperReport jasperReport =
                JasperCompileManager.compileReport(inputStream);

        if (parametros == null) {
            parametros = new HashMap<>();
        }

        parametros.put(
                "SUBREPORT_DIR",
                getClass()
                        .getResource("/jasper/")
                        .getPath()
        );


        Connection connection =
                dataSource.getConnection();

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        jasperReport,
                        parametros,
                        connection
                );

        connection.close();

        return JasperExportManager.exportReportToPdf(
                jasperPrint
        );
    }
}