package ankit;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean; // Note: This is a sun.management class

public class SystemInfoExample {
    public static void main(String[] args) {
        // Get the OperatingSystemMXBean instance
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // Retrieve various system information
        System.out.println("Operating System Name: " + osBean.getName());
        System.out.println("Operating System Architecture: " + osBean.getArch());
        System.out.println("Operating System Version: " + osBean.getVersion());
        System.out.println("Available Processors: " + osBean.getAvailableProcessors());

        // Note: The following methods are specific to com.sun.management.OperatingSystemMXBean
        // and may require casting, as shown.
        System.out.println("Total Physical Memory Size (bytes): " + osBean.getTotalPhysicalMemorySize());
        System.out.println("Free Physical Memory Size (bytes): " + osBean.getFreePhysicalMemorySize());
        System.out.println("Total Swap Space Size (bytes): " + osBean.getTotalSwapSpaceSize());
        System.out.println("Free Swap Space Size (bytes): " + osBean.getFreeSwapSpaceSize());
        System.out.println("System CPU Load: " + osBean.getSystemCpuLoad()); // Value in [0.0, 1.0]
        System.out.println("Process CPU Load: " + osBean.getProcessCpuLoad()); // Value in [0.0, 1.0]
        System.out.println("Committed Virtual Memory Size (bytes): " + osBean.getCommittedVirtualMemorySize());
    }
}