package pl.kalksztejn.mateusz.reactivehttptester.logger;

import com.sun.management.OperatingSystemMXBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class PerformanceMeasurementAspect {
    private static final String FILE_EXTENSION = ".csv";
    private static final String CSV_HEADER = "time;mem;cpu";

    private SimpleDateFormat dateFormat;
    private static final Logger logger = LoggerFactory.getLogger(PerformanceMeasurementAspect.class);
    private MemoryMXBean memoryMXBean;
    private OperatingSystemMXBean osBean;
    private final Map<String, PrintWriter> writers = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        memoryMXBean = ManagementFactory.getMemoryMXBean();
        osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @PreDestroy
    public void cleanup() {
        writers.values().forEach(PrintWriter::close);
    }

    @Around("execution(* pl.kalksztejn.mateusz.reactivehttptester.controller.*.*(..))")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            long startTime = System.nanoTime();
            double cpuUsageBefore = osBean.getProcessCpuLoad() * 100;
            long memoryBefore = memoryMXBean.getHeapMemoryUsage().getUsed() / 1000000;

            Object result = joinPoint.proceed();

            long endTime = System.nanoTime();
            double cpuUsageAfter = osBean.getProcessCpuLoad() * 100;
            long duration = endTime - startTime;
            long memoryAfter = memoryMXBean.getHeapMemoryUsage().getUsed() / 1000000;
            double cpuUsageAv = cpuUsageAfter - cpuUsageBefore;

            String methodName = joinPoint.getSignature().getName();
            String logMessage = String.format("%d;%d;%.2f",
                    duration, memoryAfter - memoryBefore, cpuUsageAv);

            PrintWriter writer = getOrCreateWriter(methodName);
            writer.println(logMessage);
            writer.flush();

            return result;
        } catch (OutOfMemoryError e) {
            logger.error("Out of memory error occurred", e);
        }
        return null;
    }

    private synchronized PrintWriter getOrCreateWriter(String methodName) {
        return writers.computeIfAbsent(methodName, key -> {
            String directory = "result";
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) {
                directoryFile.mkdirs();
            }
            String fileName = directory + File.separator + key + FILE_EXTENSION;
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(fileName, true));
                if (writer.checkError()) {
                    throw new IOException("Error creating writer for file: " + fileName);
                }
                if (writer.checkError()) {
                    throw new IOException("Error writing to file: " + fileName);
                }
                if (writer.checkError()) {
                    throw new IOException("Error flushing writer for file: " + fileName);
                }
                if (writer.checkError()) {
                    throw new IOException("Error writing CSV header to file: " + fileName);
                }
                writer.println(CSV_HEADER);
                return writer;
            } catch (IOException e) {
                logger.error("Error creating writer for file: " + fileName, e);
                throw new RuntimeException("Error creating writer for file: " + fileName, e);
            }
        });
    }
}
